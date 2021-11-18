package search;

import javax.swing.*;

import Utils.TST;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Indexer {
    Map<Integer, String> sources;
    HashMap<String, HashSet<Integer>> index;
    static JDialog searchBox ;
    static Image icon = Toolkit.getDefaultToolkit().getImage("src\\browser.png");
    
    public static String readFileAsString(String fileName)throws Exception
    {
        String strData = "";
        String strFileName = "ConvertedText\\" + fileName;
        strData = new String(Files.readAllBytes(Paths.get(strFileName)));
        return strData;
    }
    public static TST<Integer> getTST(String path) {

        File wholeFile = new File("ConvertedText/"+path);
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
        int abc=0;
        File folder = new File("ConvertedText");
        File[] files = folder.listFiles();
        for (File file:files){
            textList.add(file.getName());
            TST<Integer> tst = new TST<Integer>();
            tst = Indexer.getTST(file.getName());
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

                // Remove this entry from HashMap
                iterator.remove();
            }
        }
            return freqList;
    }

    public static HashMap<String, Integer> sortingHashMap(HashMap<String,Integer> freqList)
    {
        List<Map.Entry<String, Integer> > list = new LinkedList<Map.Entry<String, Integer> >(freqList.entrySet());
        Collections.sort(list, (i1,i2) -> i2.getValue().compareTo(i1.getValue()));
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
    
    public static void main(String args[]) throws IOException {
    	
    	Scanner input = new Scanner(System.in);
 		System.out.print("Enter your search phrase: ");
 		String searchKeyWord = input.nextLine();

            	Thread searchingThread = new Thread() {
            		public void run() {
            			String word = searchKeyWord;
            			String[] al = word.split(" ");
            			if(al.length>1) 
            				System.out.print("You cannot search more than one word at once.");
                            try {
                                if(word.length()==0) {
                                	System.out.print("Please Enter a word");
                                    searchBox.dispose();
                                }else {
                                    ArrayList<String> lis = AutoSuggestion.startSuggestion(word);         //Auto Suggestion
                                    if(lis.contains(word)) {
                                    	lis.remove(word);
                                    }
                                    HashMap<String, Integer> hm = sortingHashMap(getFrequency(word));               //Indexing
                                    List<Map.Entry<String, Integer> > list = new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());
                                    if(list.size() > 0)
                                    {
                                    	System.out.print("Total Results Found: "+list.size());
                                        if(lis.isEmpty()) {
                                        	System.out.print("No suggestion Found");
                                        }else {
                                        	System.out.print("You can also search for: "+ lis );
                                        }
                                        for (int i = 0; i < list.size(); i++) {
                                        	System.out.print((i+1)+". "+list.get(i) + System.lineSeparator());
                                        }
                                    }
                                    else {
                                        String correction = "";
                                        boolean wordExist;
                                        ArrayList<String> spelllist = SpellCheck.correction(word);            //Spell check
                                        for(int i=0; i<spelllist.size();i++) {
                                            if(i==spelllist.size()-1) {
                                                correction += spelllist.get(i);
                                            }else {
                                                correction += spelllist.get(i)+ " or ";
                                            }
                                        }
                                        wordExist = SpellCheck.check(word);
                                        if(spelllist.isEmpty()&&wordExist==false) {
                                        	System.out.print("Ummmmm, This doesn't seem like a word!");
                                        }else if(spelllist.isEmpty()&&wordExist==true) {
                                        	System.out.print("Word not present");
                                        }
                                        else {
                                        	System.out.print("Did you mean: "+ correction +" ?");
                                        }
                                        System.out.print("Results not Found for : " + word);
                                        if(lis.isEmpty()&&!spelllist.isEmpty()) {
                                        	System.out.print("No suggestion Found");
                                        }
                                        else if(lis.isEmpty()&&spelllist.isEmpty()) {
                                        	System.out.print("No suggestion Found");
                                        }else {
                                        	System.out.print("You can also search for: "+ lis );
                                        }
                                    }
                                    searchBox.dispose();
                                }
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
            			}
            	};
            	searchingThread.start();

    }
    
}
