package fr.ensma.lias.tma4kb.cardinalities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import fr.ensma.lias.tma4kb.query.AbstractQuery;
import fr.ensma.lias.tma4kb.query.Query;
import fr.ensma.lias.tma4kb.query.TriplePattern;

/**
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public class ComputeCardinalitiesConfig {

	Properties properties = new Properties();

	/**
	 * @param source the file containing cardinalities
	 * @throws IOException
	 */
	public ComputeCardinalitiesConfig(String source) throws IOException {
		InputStream input = new FileInputStream(new File(source));
		properties.load(input);
		input.close();
	}

	/**
	 * Removes the prefix from an URI
	 * 
	 * @param uri the URI to remove the prefix from
	 * @return the URI without its prefix
	 */
	public String getNiceName(String uri) {
		int indexOfSeparator = uri.indexOf('#');

		if (indexOfSeparator != -1) {
			return uri.substring(indexOfSeparator + 1, uri.length());
		} else {
			int indexOfSlash = uri.lastIndexOf("/");
			return uri.substring(indexOfSlash + 1, uri.length());
		}
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
			Integer cardMax = Integer.parseInt(properties.get(getNiceName(p) + ".max").toString());
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

			Integer cardMax = Integer.parseInt(properties.get(getNiceName(p) + ".max").toString()); // commencer par la
																									// cardinalité
																									// globale
			int k = 0;
			while (cardMax > 1 && k < domains.size()) { // si la cardinalité globale max est 1, la cardinalité locale
														// max est aussi 1
				String classe = domains.get(k);
				if (!classe.equals("thing")) {
					Integer newCard = Integer.parseInt(properties.get(classe + getNiceName(p) + ".max").toString());
					if (newCard < cardMax)
						cardMax = newCard;
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
				domain = properties.getProperty(getNiceName(predicate) + ".domain");
				t.setDomain(domain);
				if (domain == null)
					t.setDomain("");
			}
		}
	}
}
