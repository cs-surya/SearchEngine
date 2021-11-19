package search;

import java.io.File;
import java.io.FileWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTMLToText {

	public static void writeHTMLDocument(Document doc) {
		String name = doc.title().replace('/', '-');
		try {
			FileWriter htmlfile = new FileWriter("HTMLPages/" + name + ".html"); // Creating HTML files.
			htmlfile.write(doc.toString()); // writing the file with document parsed using jsoup
			htmlfile.close();
		} catch (Exception e) {
			System.out.println("Error Occured while saving the html file" + name);
		}
	}

	private static void convertToText(File file) {
		org.jsoup.nodes.Document doc = null; // initializing jsoup document.
		try {
			doc = Jsoup.parse(file, "UTF-8"); // parsing file
			String name = file.getName().substring(0, file.getName().lastIndexOf('.')); // getting name of the file
			FileWriter textfile = new FileWriter("Textfiles/" + name + ".txt"); // Creating new text file.
			textfile.write(doc.text()); // writing the file with document parsed using jsoup
			textfile.close();
		} catch (Exception e) {

		}
	}

	public static void convert(File[] listFiles) {
		int len = listFiles.length;
		for (int i = 0; i < len; i++) {
			convertToText(listFiles[i]);
		}
	}
}
