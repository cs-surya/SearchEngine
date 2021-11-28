package search;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Crawler {

	private static Set<String> visitedURL = new HashSet<String>();

	private static Document requestPage(String url) {
		try {
			Connection con = Jsoup.connect(url);
			Document doc = con.get();
			if (con.response().statusCode() == 200) {
				visitedURL.add(url);
				return doc;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	private static boolean filterURL(String link) {
		if (link.contains(".svg") || link.contains("#") || link.contains(".png") || link.contains(".jpeg")
				|| link.contains(".pdf") || link.contains(".gif") || link.contains(".jpg") || link.contains(".swf")
				|| link.contains("?") || link.contains("mailto:") || link.contains("javascript:"))
			return false;
		return true;
	}

	private static void crawl(int level, String url) {
		if (level <= Constants.CRAWLING_DEPTH) {  //layers of web pages crawled
			Document doc = requestPage(url);
			if (doc != null) {
				HTMLToText.writeHTMLDocument(doc); // Save HTML Pages
				for (Element link : doc.select("a[href]")) {
					String next_link = link.absUrl("href");
					if (!visitedURL.contains(next_link) && filterURL(next_link)) {
						File folder = new File("HTMLPages/");
						File[] files = folder.listFiles();
						if (files.length > 120) {
							break; // break
						} else {
							crawl(level++, next_link);
						}
					}
				}
			}
		}
	}

	public static void crawl(String URL) {
		crawl(1, URL);
		File folder = new File("HTMLPages/");
		final File[] files = folder.listFiles();
		HTMLToText.convert(files); // Convert HTML To Textfiles
	}
}
