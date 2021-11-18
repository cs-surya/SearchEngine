package search;

import java.util.Scanner;

public class SearchEngine {
	public static void main(String[] args) {
		System.out.println("*******************************************");
		System.out.println("\tWelcome to the search engine");
		
		System.out.println("\nEnter the URL to crawl. \nIf you like to continue with our default URL ("+Constants.CRAWLER_DEFAULT_URL+"), press 'ENTER'");
		
		Scanner sc = new Scanner(System.in);
	
		String url = sc.nextLine();
		
		if(url == null || url.isEmpty()) {
			url = Constants.CRAWLER_DEFAULT_URL;
		}
		
		sc.close();
	}
}
