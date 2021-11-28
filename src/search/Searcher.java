package search;

import Utils.TST;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Searcher {
	
	Map<Integer, String> sources;
	HashMap<String, HashSet<Integer>> index;

	public static String readFileAsString(String fileName) throws Exception {
		String strData = "";
		String strFileName = "Textfiles/" + fileName;
		strData = new String(Files.readAllBytes(Paths.get(strFileName)));
		return strData;
	}
	
	/*Extract words in the text file using StringTokenizer.
	Insert extracted words into Ternary Search Trie (TST) using TST.java from 'Utils' */
	public static TST<Integer> getTST(String path) {
		File wholeFile = new File("Textfiles/" + path);
		TST<Integer> objTST = new TST<Integer>();
		try {
			if (wholeFile.isFile()) {
				String strFileText = readFileAsString(wholeFile.getName());
				StringTokenizer strTokenizer = new StringTokenizer(strFileText);
				
				while (strTokenizer.hasMoreTokens()) {
					String strToken = strTokenizer.nextToken().replaceAll("[|;:.,='<>()%#@*^/&\"]", " "); //remove special characters in the word
					if (objTST.contains(String.valueOf(strToken))) {
						objTST.put(strToken.toLowerCase(), objTST.get(strToken) + 1);
					} else {
						objTST.put(strToken, 1);
					}
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return objTST;
	}
	
	/*Count the frequency of the word in the TST for each textfile. 
	Return a HashMap with filename and frquency count */
	public static HashMap<String, Integer> getFrequency(String word) {
		HashMap<String, Integer> freqList = new HashMap<String, Integer>();
		File folder = new File("Textfiles");
		File[] files = folder.listFiles();

		for (File file : files) {
			String fileName = file.getName();
			TST<Integer> tst = new TST<Integer>();
			tst = Searcher.getTST(fileName);
			int counter = 0;

			if (tst.contains(word)) {
				int count = tst.get(word);
				counter += count;
			}

			freqList.put(fileName, counter);
		}

		Integer valueToBeRemoved = 0;
		Iterator<Map.Entry<String, Integer>> iterator = freqList.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, Integer> entry = iterator.next();
			if (valueToBeRemoved.equals(entry.getValue())) {
				iterator.remove(); // remove entries with frequency count = 0
			}
		}
		return freqList;
	}
	
	/*
	 * To sort HashMap by values, use the Collections.sort(List) method by passing customized comparator. 
	 * create a new LinkedHashMap and copy the sorted elements into that. 
	 * LinkedHashMap guarantees the insertion order of mappings and we get a sorted HashMap .
	 */
	public static HashMap<String, Integer> sortingHashMap(HashMap<String, Integer> freqList) {
		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(freqList.entrySet());
		Collections.sort(list, (i1, i2) -> i2.getValue().compareTo(i1.getValue()));	
		HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
			
		}
		
		return temp;
	}

	public static void start() throws IOException {
		try {
			Scanner input = new Scanner(System.in);
			while (true) {
				System.out.println("Enter your search word:");
				String word = input.nextLine();
				String[] al = word.split(" ");
				if (al.length>1)
				{
					System.out.println("\nYou cannot search more than one word at once!\n" );
					continue;
				}
				else if (word.isEmpty())
				{	
					System.out.println("\nSearch cannot be empty!\n");
					continue;
				}
				
				//Auto suggestion
				ArrayList<String> autoSuglist = AutoSuggestion.startSuggestion(word);
				if (autoSuglist.contains(word)) {
					autoSuglist.remove(word);
				}
				
				long startTime = System.currentTimeMillis();
				
				HashMap<String, Integer> hm = sortingHashMap(getFrequency(word)); //search and sort
				List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());
				
				long endTime = System.currentTimeMillis();
				
				if (list.size() > 0) {
					System.out.println("----------------------------------------");
					System.out.println(list.size() + " result(s) found in " + (endTime - startTime) +" ms");
					System.out.println("----------------------------------------");
					
					for (int i = 0; i < list.size(); i++) {
						String[] temp = list.get(i).toString().split("=");
						System.out.println((i + 1) + ". " + temp[0] + " - " + temp[1] + " occurences" + System.lineSeparator());
					}
					
					//Auto Suggestion
					if (!autoSuglist.isEmpty()) {
					System.out.println("You can also search for: \n" + autoSuglist.toString());
					}
				} else {
					String correction = "";
					boolean wordExist;
					//Spell Checker
					ArrayList<String> spellList = SpellCheck.correction(word);
					for (int i = 0; i < spellList.size(); i++) {
						if (i == spellList.size() - 1) {
							correction += spellList.get(i);
						} else {
							correction += spellList.get(i) + " or ";
						}
					}
					
					wordExist = SpellCheck.check(word); 
					if (spellList.isEmpty() && !wordExist) {
						System.out.println("\nNot a valid word\n");
					} else if (spellList.isEmpty() && wordExist) {
						System.out.println("\nCannot find this word in the files\n");
					} else {
						System.out.println("\nDid you mean: " + correction + " ?\n");
					}
					System.out.println("\nResults not found for : " + word+"\n");
					
					if (list.isEmpty() && !spellList.isEmpty()) {
						System.out.println("\nNo suggestions found\n");
					} else if (list.isEmpty() && spellList.isEmpty()) {
						System.out.println("\nNo suggestions found\n");
					} else {
						System.out.println("You can also search for: " + spellList.toString());
					}
				}
				
				System.out.println("\nEnter Y to continue or enter N to exit");
				String response = input.nextLine();
				if(response == null || response.equalsIgnoreCase("N")) {
					break;
				}	    	
			}
			input.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}