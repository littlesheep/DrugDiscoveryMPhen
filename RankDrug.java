import java.util.*;
import java.io.*;

public class RankDrug {
	static String geneMPhenFile = "./ProgramData/gene_mphen.txt";
	static String phenRootFile = "./ProgramData/mphen_root.txt";

	public static void main (String[] args) throws IOException {
		Map<String, Integer> mphen2cnt = disease_mphen("./ProgramData/GBM_TCGA_genes.txt");
		Map<String, Integer> root2cnt = check_mphen_root(mphen2cnt);
		

		for (String root : root2cnt.keySet()) {
			System.out.println(root + "\t" +root2cnt.get(root));
		}
	}

	

	public static Map<String, Integer> check_mphen_root (Map<String, Integer> mphen2cnt) throws IOException {
		Map<String, Integer> root2cnt = new HashMap<String, Integer>();
		BufferedReader r = new BufferedReader (new FileReader (phenRootFile));
		String line;
		while ((line = r.readLine()) != null) {
			String[] parts = line.split("\\|");
			String mphen = parts[0];
			String root = parts[1];
			if (mphen2cnt.containsKey(mphen)) {
				int mphenCnt = mphen2cnt.get(mphen);
				push_item_score_map(root2cnt, root, mphenCnt);
			}
		}
		r.close();

		Map<String, Integer> root2sortedCnt = sortByValue(root2cnt);

		return root2sortedCnt;
	}


	public static Map<String, Integer> disease_mphen (String disGeneFile) throws IOException {
		Set<String> gSet = utility.read_string_set(disGeneFile);
		Map<String, Integer> mphen2cnt = new HashMap<String, Integer> ();
		BufferedReader r = new BufferedReader (new FileReader (geneMPhenFile));
		String line;
		while ((line = r.readLine()) != null) {
			String[] parts = line.split("\\|");
			String gene = parts[0].trim();
			String mphen = parts[1].trim().toLowerCase();
			if (gSet.contains(gene)) {
				push_item_score_map(mphen2cnt, mphen, 1);
			}
		}
		r.close();

		return mphen2cnt;
	}

	private static void push_item_score_map (Map<String, Integer> map, String key, int score) {
		if (map.containsKey(key)) {
			int cnt = map.get(key);
			cnt += score;
			map.put(key, cnt);
		} else {
			map.put(key, score);
		}
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {

				return (o2.getValue()).compareTo(o1.getValue());

			}
		}

		);

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}