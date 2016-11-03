import java.util.*;
import java.io.*;

public class utility{

	public static Set<String> read_string_set (String file) throws IOException {
		Set<String> set = new HashSet<String> ();
		BufferedReader r = new BufferedReader (new FileReader (file));
		String line;
		while ((line = r.readLine()) != null) {
			line = line.trim();
			set.add(line);
		}
		r.close();

		return set;
	}

	public static void write_string_set_map (Map<String, Set<String>> map, String file) throws IOException {
		BufferedWriter w = new BufferedWriter (new FileWriter (file));
		for (String key : map.keySet()) {
			Set<String> valSet = map.get(key);
			for (String val : valSet) {
				w.write(key + "|" + val + "\n");
				w.flush();
			}
		}
		w.close();
	}
	
	public static void write_string_set(Set<String> set, String file) throws IOException {
		BufferedWriter w = new BufferedWriter (new FileWriter (file));
		for (String item : set) {
			w.write(item + "\n");
			w.flush();
		}

		w.close();
	}
}