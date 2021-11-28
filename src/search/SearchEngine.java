package search;

import java.util.Scanner;

public class SearchEngine {
	
	public static void main(String[] args) {
		try {
			System.out.println("*******************************************");
			System.out.println("\tWelcome to the Search Engine");
			System.out.println("*******************************************");
			
			System.out.println("\nPlease enter the URL to crawl: \n\n(To continue with our default URL "+Constants.CRAWLER_DEFAULT_URL+", press 'ENTER')");
			
			Scanner sc = new Scanner(System.in);
		
			String url = sc.nextLine();
			
			if(url == null || url.isEmpty()) {
				url = Constants.CRAWLER_DEFAULT_URL;
			}				
			
			System.out.println("\nCrawling the url. Please wait...");
			Crawler.crawl(url);
			System.out.println("\n" + "Crawling done\n" + "");
			
			Searcher.start();
			
			System.out.println("***********************************************************");
			System.out.println("\tYou have quit the search engine. Thank You!");
			System.out.println("***********************************************************");
			
			sc.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
