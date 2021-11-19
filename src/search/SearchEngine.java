package search;

import java.util.Scanner;

public class SearchEngine {
	public static void main(String[] args) {
		try {
			System.out.println("*******************************************");
			System.out.println("\tWelcome to the search engine");
			System.out.println("*******************************************");

			System.out.println("\nEnter the URL to crawl. \nIf you like to continue with our default URL ("
					+ Constants.CRAWLER_DEFAULT_URL + "), press 'ENTER'");

			Scanner sc = new Scanner(System.in);

			String url = sc.nextLine();

			if (url == null || url.isEmpty()) {
				url = Constants.CRAWLER_DEFAULT_URL;
			}

			System.out.println("Crawling the url. Please wait...");
			Crawler.crawl(url);
			System.out.println("Crawling done");

			Searcher.start();

			System.out.println("*******************************************");
			System.out.println("\tBye");
			System.out.println("*******************************************");

			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
