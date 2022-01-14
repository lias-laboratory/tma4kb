package fr.ensma.lias.tma4kb.cardinalities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.roaringbitmap.RoaringBitmap;

import fr.ensma.lias.tma4kb.query.AbstractQuery;
import fr.ensma.lias.tma4kb.query.Query;
import fr.ensma.lias.tma4kb.query.TriplePattern;

/**
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public class ComputeCardinalitiesConfig {

	Properties properties = new Properties();
	List<String> predicates;
	Map<RoaringBitmap, RoaringBitmap> bitmap;
	String src;

	/**
	 * @param source the file containing cardinalities
	 * @throws IOException
	 */
	public ComputeCardinalitiesConfig(String source) {
		src = source;
	}

	public void importSource() throws IOException {
		long time = System.currentTimeMillis();
		File OUTPUT_FILE = new File("output");
		BufferedReader br = Files.newBufferedReader(Paths.get(src));
		FileWriter res = new FileWriter(OUTPUT_FILE);
		BufferedWriter bw = new BufferedWriter(res);
		String line = null;
		while ((line = br.readLine()) != null) {
			line = removeSyntax(line).replace("(", "").replace(")", "");
			String[] parts = line.split(",");
			String p = "";
			if (parts.length == 2)
				bw.write(parts[0].replace(":", "\\:") + ".domain=" + parts[1]);
			else {
				for (int i = 0; i < parts.length - 2; i++)
					p += parts[i];
				String cardMax = parts[parts.length - 1];
				bw.write(p.replace(":", "\\:") + "=" + cardMax);
			}
			bw.newLine();
		}
		br.close();
		bw.close();
		long end = System.currentTimeMillis();
		System.out.println(end - time);
		InputStream input = new FileInputStream(OUTPUT_FILE);
		properties.load(input);
		input.close();
	}

	/**
	 * Removes the prefix from an URI
	 * 
	 * @param uri the URI to remove the prefix from
	 * @return the URI without its prefix
	 */
	public String removeSyntax(String uri) {
		return uri.replace("<", "").replace(">", "");
	}

	/**
	 * Calculates maximum cardinalities of the predicates of all triple patterns of
	 * a query and fills the cardMax attributes of these triple patterns Uses the
	 * .config file included in the resources
	 * 
	 * @param the query that contains all triple patterns whose predicates we want
	 *            to determine the cardinality of
	 * @throws Exception
	 */
	public void computeMaxCardinalities(Query query) throws Exception {

		List<TriplePattern> triples = ((AbstractQuery) query).getTriplePatterns();
		List<String> predicates = new ArrayList<String>();
		for (TriplePattern t : triples) {
			if (!t.isPredicateVariable())
				predicates.add(t.getPredicate());
		}
		int i = 0;
		for (String p : predicates) {
			Integer cardMax = Integer.parseInt(properties.get(p).toString());
			((AbstractQuery) query).setCardMax(i, cardMax);
			i++;
		}
	}

	public void computeMaxLocalCardinalities(Query query) throws Exception {

		List<TriplePattern> triples = ((AbstractQuery) query).getTriplePatterns();
		List<String> domains = new ArrayList<>();
		for (TriplePattern tp : triples) {
			String p = tp.getPredicate();
			for (TriplePattern t : triples) {
				if (t.getSubject().equals(tp.getSubject())) {
					String domain = t.getDomain();
					int i = 0;
					int j = domain.indexOf(',');
					while (j > -1) {
						if (!domains.contains(domain.substring(i, j))) {
							domains.add(domain.substring(i, j));
						}
						i = j + 1;
						j = domain.indexOf(',', i);
					}
					if (!domains.contains(domain.substring(i))) {
						domains.add(domain.substring(i));
					}
				}
			}

			Integer cardMax = Integer.parseInt(properties.get(p).toString()); // commencer par la
																				// cardinalité
																				// globale
			int k = 0;
			while (cardMax > 1 && k < domains.size()) { // si la cardinalité globale max est 1, la cardinalité locale
														// max est aussi 1
				String classe = domains.get(k);
				if (!classe.equals("http://www.w3.org/2002/07/owl#Thing")) {
					try {
						Integer newCard = Integer.parseInt(properties.get(classe + p).toString());
						if (newCard < cardMax)
							cardMax = newCard;
					} catch (NullPointerException e) {
					} // cas où il n'y a pas de cardinalité enregistré pour cette classe
				}
				k++;
			}
			tp.setCardMax(cardMax);
		}
	}

	public void computeDomains(Query query) throws Exception {

		List<TriplePattern> triples = ((AbstractQuery) query).getTriplePatterns();
		List<String> predicates = new ArrayList<>();
		for (TriplePattern t : triples) {
			predicates.add(t.getPredicate());
		}

		for (TriplePattern t : triples) {
			String predicate = t.getPredicate();
			String domain;
			if (predicate.equals("type") && !t.isObjectVariable()) {
				domain = t.getObject();
				t.setDomain(domain);
			} else {
				domain = properties.getProperty(predicate + ".domain");
				t.setDomain(domain);
				if (domain == null)
					t.setDomain("");
			}
		}
	}

	/**
	 * makeCS stores Characteristic sets using RoaringBitmap.
	 * 
	 * @throws Exception
	 */
	public void makeCS() throws Exception {
		predicates = new ArrayList<>();
		BufferedReader reader;
		reader = new BufferedReader(new FileReader(src));
		String line = reader.readLine();
		while (line != null) {
			line = line.substring(1, line.length() - 1);
			String[] split = line.split(">,");
			String[] pred = split[0].split(">;");
			for (String p : pred)
				if (!predicates.contains(removeSyntax(p)))
					predicates.add(removeSyntax(p));
			line = reader.readLine();
		}
		reader.close();
		bitmap = new HashMap<>();
		reader = new BufferedReader(new FileReader(src));
		line = reader.readLine();
		while (line != null) {
			RoaringBitmap presence = new RoaringBitmap();
			RoaringBitmap card1 = new RoaringBitmap();
			line = line.substring(1, line.length() - 1);
			String[] split = line.split(">,");
			String[] pred = split[0].split(">;");
			String[] cards = split[1].split(";");
			for (int i = 0; i < pred.length; i++) {
				presence.flip(predicates.indexOf(removeSyntax(pred[i])));
				if (cards[i].equals("1"))
					card1.flip(predicates.indexOf(removeSyntax(pred[i])));
			}
			bitmap.put(presence, card1);
			line = reader.readLine();
		}
		reader.close();
	}

	/**
	 * hasCard1 uses characteristic sets to determine if the predicate of t has
	 * cardinality 1 within query q
	 * 
	 * @param t the triple pattern containing the predicate to determine the
	 *          cardinality of
	 * @param q the query being used to find the characteristic sets
	 * @return true if p(t) has cardinality 1, false otherwise
	 */
	public boolean hasCard1(TriplePattern t, Query q) {
		Boolean card1 = true;
		for (RoaringBitmap b : bitmap.keySet()) {
			boolean contains = true;
			for (TriplePattern tp : ((AbstractQuery) q).getTriplePatterns()) {
				if (tp.getSubject().equals(t.getSubject())
						&& !b.contains(predicates.indexOf(removeSyntax(tp.getPredicate())))) {
					contains = false;
				}
			}
			if (contains) {
				card1 = card1 && bitmap.get(b).contains(predicates.indexOf(removeSyntax(t.getPredicate())));
				if (!card1)
					return false;
			}
		}
		return card1;
	}
}
