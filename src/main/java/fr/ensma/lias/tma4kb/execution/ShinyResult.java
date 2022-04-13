package fr.ensma.lias.tma4kb.execution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.ensma.lias.tma4kb.execution.AlgorithmExec.AlgoChoice;
import fr.ensma.lias.tma4kb.query.Query;

public class ShinyResult {
	private AlgoChoice algo;
	private int limit;
	private Set<Query> xss = new HashSet<Query>();
	private Set<Query> mfis = new HashSet<Query>();
	
	private Integer nbExec;
	private List<Integer[]> times = new ArrayList<Integer[]>();
	
	public ShinyResult(AlgoChoice algorithm, Set<Query> m, Set<Query> x, Integer nb, int K) {
		algo=algorithm;
		mfis=m;
		xss=x;
		nbExec=nb;
		limit=K+1;
	}
	
	public void addTimes(Integer[] t) {
		times.add(t);
	}
	
	public Set<Query> getXSS(){
		return xss;
	}
	
	public Set<Query> getMFIS(){
		return mfis;
	}

	public List<Integer[]> getTimes(){
		return times;
	}
	
	public Integer getNb() {
		return nbExec;
	}
	
	public void save(String file) {
		BufferedWriter fichier;
		String sep = "  ";
		try {
			fichier = new BufferedWriter(new FileWriter(file,true));
			fichier.write(sep+sep+"- algo: "+algo+"\n"+sep+sep +sep+"xss:");
			if (xss.isEmpty()) {
				fichier.write(" []");
			}
			fichier.write("\n");
			for (Query q : xss) {
				fichier.write(sep+sep+sep+sep+"- " +q+"\n");
			}
			fichier.write(sep+sep+sep+"mfis:");
			if (mfis.isEmpty()) {
				fichier.write(" []");
			}
			fichier.write("\n");
			for (Query q : mfis) {
				fichier.write(sep+sep+sep+sep+"- " +q+"\n");
			}
			fichier.write(sep+sep+sep+"nbr: "+nbExec+"\n"+sep+sep+sep+"stats:\n");
			for (Integer i=0;i<times.size();i++) {
				fichier.write(sep+sep+sep+sep+"- iteration: "+ i+"\n");
				fichier.write(sep+sep+sep+sep+sep+"exec_time_lattice: "+ times.get(i)[0]+"\n");
				fichier.write(sep+sep+sep+sep+sep+"exec_time_pfis: "+ times.get(i)[1]+"\n");
				fichier.write(sep+sep+sep+sep+sep+"exec_time_queries: "+ times.get(i)[2]+"\n");
				fichier.write(sep+sep+sep+sep+sep+"exec_time_properties: "+ times.get(i)[3]+"\n");
			}
			fichier.close();
		} catch (IOException e) {
			System.err.println("Unable to create the file with the experiment results.");
			e.printStackTrace();
		}
	}
}
