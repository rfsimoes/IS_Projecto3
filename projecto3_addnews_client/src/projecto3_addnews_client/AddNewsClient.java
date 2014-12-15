package projecto3_addnews_client;

import java.util.Scanner;

import crawler.WebCrawler;

public class AddNewsClient {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String task = "";
		WebCrawler wc = new WebCrawler();
		while (true) {
			System.out.println("Do u want to download a new set of news?\n(y/n)");
			task = sc.nextLine();
			if (task.equalsIgnoreCase("y")) {
				wc.collectNews();
				break;
			} else if (task.equalsIgnoreCase("n"))
				System.exit(0);
			else
				System.out.println("Invalid choice!!");
		}
		
		while(true)
		{
			System.out.println("Select an option:\n(1) Save as XML file\n(2) Send through an HTTP request");
			task = sc.nextLine();
			if(task.equals("1") || task.equals("2"))
			{	
				try {
					wc.javaToXML(Integer.parseInt(task));
				} catch (NumberFormatException | InterruptedException e) {
					System.out.println("Failed to marshal data.");
				}
				break;
			}
			else
				System.out.println("Invalid choice!");
			
		}
		sc.close();

	}
}
