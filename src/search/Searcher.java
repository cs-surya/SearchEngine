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
    
    public static String readFileAsString(String fileName)throws Exception {
        String strData = "";
        String strFileName = "Textfiles/" + fileName;
        strData = new String(Files.readAllBytes(Paths.get(strFileName)));
        return strData;
    }
    
    public static TST<Integer> getTST(String path) {
        File wholeFile = new File("Textfiles/"+path);
        TST<Integer> objTST = new TST<Integer>();
        try {
            if (wholeFile.isFile()) {
                String strFileText = readFileAsString(wholeFile.getName());
                StringTokenizer strTokenizer = new StringTokenizer(strFileText);
                while (strTokenizer.hasMoreTokens()) {
                    String strToken = strTokenizer.nextToken().replaceAll("[|;:.,='<>()%#@*^/&\"]", " ");
                    if (objTST.contains(String.valueOf(strToken))){
                        objTST.put(strToken.toLowerCase(),objTST.get(strToken)+1);
                    }
                    else{
                        objTST.put(strToken,1);
                    }
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        
        return objTST;
    }

    public static HashMap<String, Integer>getFrequency(String word){
        ArrayList<String> textList = new ArrayList<>();
        HashMap<String,Integer > freqList = new HashMap<String, Integer>();
        File folder = new File("Textfiles");
        File[] files = folder.listFiles();

        for (File file:files){
            textList.add(file.getName());
            TST<Integer> tst = new TST<Integer>();
            tst = Searcher.getTST(file.getName());
            int counter = 0;

            if (tst.contains(word)){
                int count = tst.get(word);
                counter = counter + count;
            }

            freqList.put(file.getName(), counter);
        }
        
        Integer valueToBeRemoved = 0;
        Iterator<Map.Entry<String, Integer>> iterator = freqList.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            if (valueToBeRemoved.equals(entry.getValue())) {
                iterator.remove();
            }
        }
            return freqList;
    }

    public static HashMap<String, Integer> sortingHashMap(HashMap<String,Integer> freqList){
        List<Map.Entry<String, Integer> > list = new LinkedList<Map.Entry<String, Integer> >(freqList.entrySet());
        Collections.sort(list, (i1,i2) -> i2.getValue().compareTo(i1.getValue()));
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
    
    public static void start() throws IOException {
    	try {
    		while(true) {
        		Scanner input = new Scanner(System.in);
         		System.out.println("Enter your search word:");
         		String word = input.nextLine();
         		
         		ArrayList<String> autoSuglist = AutoSuggestion.startSuggestion(word);
                if(autoSuglist.contains(word)) {
                	autoSuglist.remove(word);
                }
                
                HashMap<String, Integer> hm = sortingHashMap(getFrequency(word));
                List<Map.Entry<String, Integer> > list = new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());
                if(list.size() > 0) {
                	System.out.println("Total Results Found: "+list.size());
                    for (int i = 0; i < list.size(); i++) {
                    	System.out.println((i+1)+". "+list.get(i) + System.lineSeparator());
                    }
                    System.out.println("You can also search for: \n"+ autoSuglist.toString() );
                } else {
                    String correction = "";
                    boolean wordExist;
                    ArrayList<String> spellList = SpellCheck.correction(word);
                    for(int i=0; i<spellList.size();i++) {
                        if(i==spellList.size()-1) {
                            correction += spellList.get(i);
                        }else {
                            correction += spellList.get(i)+ " or ";
                        }
                    }
                    wordExist = SpellCheck.check(word);
                    if(spellList.isEmpty()&&!wordExist) {
                    	System.out.println("This does not appear to be a word");
                    }else if(spellList.isEmpty()&& wordExist) {
                    	System.out.println("Cannot find this word in the files");
                    }
                    else {
                    	System.out.println("Did you mean: "+ correction +" ?");
                    }
                    System.out.println("Results not Found for : " + word);
                    if(list.isEmpty()&&!spellList.isEmpty()) {
                    	System.out.println("No suggestion Found");
                    }
                    else if(list.isEmpty()&&spellList.isEmpty()) {
                    	System.out.println("No suggestion Found");
                    }else {
                    	System.out.println("You can also search for: "+ spellList.toString() );
                    }
                }
        	}
    	}catch (Exception e) {
    		e.printStackTrace();
		}
    }   
}