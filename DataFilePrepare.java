import java.util.*;
import java.io.*;

public class DataFilePrepare{
	static String frompath = "./OriginalData/";
	static String topath = "./ProgramData/";

	public static void main(String[] args) throws IOException {
		// gbm_clinical_trial_drugs("glioblastoma");
		mouse_phenotype_root ();
	}


	private static void mouse_phenotype_root () throws IOException {
		Map<String, Set<String>> map = new HashMap<String, Set<String>> (); // parent to child set
		Set<String> rootSet = new HashSet<String> (); // all nodes have never been child nodes are roots
		Set<String> allChildSet = new HashSet<String> ();

		String file = frompath + "mphen_isa.txt";
		BufferedReader r = new BufferedReader (new FileReader (file));
		String line;
		while ((line = r.readLine()) != null) {
			String[] parts = line.split("\\|");
			String parent = parts[1];
			String child = parts[0];
			if (parent.compareToIgnoreCase("mammalian phenotype") == 0)
				continue;
			push_item_set_map(map, parent, child);

			rootSet.add(parent); // if it is a child, it cannot be a root 
			allChildSet.add(child);
		}
		r.close();
		rootSet.removeAll(allChildSet);
		System.out.println("Number of roots: " + rootSet.size());
		System.out.println(rootSet);

		Map<String, Set<String>> node2root = new HashMap<String, Set<String>> (); 
		int cnt =  0;
		for (String root : rootSet){
			Set<String> childSet = map.get(root); 

			while (!childSet.isEmpty()) {
				Set<String> nextChildset = new HashSet<String> ();

				for (String child : childSet) {
					push_item_set_map(node2root, child, root);
					if (map.containsKey(child)) {
						nextChildset.addAll(map.get(child));
					}
				}

				childSet.clear();
				childSet.addAll(nextChildset);

			}

			cnt++;
			System.out.println(cnt + "th root is processing");
		}
		utility.write_string_set_map(node2root, topath + "mphen_root.txt");

	}

	private static void push_item_set_map (Map<String, Set<String>> map, String key, String val){
		if (map.containsKey(key)) {
			Set<String> set = map.get(key);
			set.add(val);
			map.put(key, set);
		} else {
			Set<String> set = new HashSet<String> ();
			set.add(val);
			map.put(key, set);
		}
	}

	private static void gbm_clinical_trial_drugs (String key) throws IOException {
		Set<String> drugSet = new HashSet<String> ();
		String file =  frompath + "intervention_condition_drugmapped_diseasecleand.txt";
		BufferedReader r = new BufferedReader (new FileReader (file));
		String line;
		while ((line = r.readLine()) != null) {
			String[] parts = line.split("\\|");
			String drug = parts[0].toLowerCase();
			String disease = parts[1].toLowerCase();
			double score = Double.parseDouble(parts[2]);
			if (disease.contains(key) && score > 1) {
				drugSet.add(drug);
			}

		}
		r.close();
		utility.write_string_set(drugSet, topath + "GBM_drug_clinicaltrail.txt");
	}


	
}

