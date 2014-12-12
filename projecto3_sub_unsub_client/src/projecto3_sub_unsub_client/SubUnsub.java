package projecto3_sub_unsub_client;

import java.util.Scanner;

import artifact.SubscriptionUnsubscription;
import artifact.SubscriptionUnsubscriptionService;

public class SubUnsub {
	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		String task = "";
		while (true) {
			System.out.println("###############################");
			System.out.println("Select the task:");
			System.out.println("(1) Subscribe\n(2) Unsubscribe\n(3) Exit");
			System.out.println("###############################");
			task = sc.nextLine();
			if (task.equalsIgnoreCase("1"))
				subscribe();
			else if (task.equalsIgnoreCase("2"))
				unsubscribe();
			else if (task.equalsIgnoreCase("3"))
				System.exit(0);
			else
				System.out.println("Invalid Option!!");
		}

	}

	private static void unsubscribe() {
		String email = "";

		while (true) {
			System.out.println("Insert your email:");
			email = sc.nextLine();
			if (email.equalsIgnoreCase("") == false)
				break;
		}

		try {
			SubscriptionUnsubscriptionService sus = new SubscriptionUnsubscriptionService();
			SubscriptionUnsubscription su = sus.getSubscriptionUnsubscriptionPort();
			System.out.println(su.unsubscribe(email));
		} catch (Exception e) {
			System.out.println("It wasn't possible to delete your subscription. Try later.");
		}

	}

	private static void subscribe() {
		String email = "";
		int digest = -1;
		String region = "";
		String task = "";
		while (true) {
			System.out.print("Insert email: ");
			task = sc.nextLine();
			if (task.equalsIgnoreCase("") == false) {
				email = task;
				break;
			}
			System.out.println("Please inset email!!");
		}

		while (true) {
			System.out.print("Subscription type:\n(1) digest\n(2) non-digest: ");
			task = sc.nextLine();
			if (task.equalsIgnoreCase("1") == true) {
				digest = 1;
				break;
			} else if (task.equalsIgnoreCase("2")) {
				digest = 0;
				break;
			} else
				System.out.println("Invalid option!!!");
		}

		while (true) {
			System.out
					.print("Select region:\n(1) US \n(2) AFRICA \n(3) ASIA \n(4) EUROPE \n(5) LATIN AMERICA \n(6) MIDDLE EAST: ");
			task = sc.nextLine();
			if (task.equalsIgnoreCase("1") == true) {
				region = "US";
				break;
			} else if (task.equalsIgnoreCase("2")) {
				region = "AFRICA";
				break;
			} else if (task.equalsIgnoreCase("3")) {
				region = "ASIA";
				break;
			} else if (task.equalsIgnoreCase("4")) {
				region = "EUROPE";
				break;
			} else if (task.equalsIgnoreCase("5")) {
				region = "LATIN AMERICA";
				break;
			} else if (task.equalsIgnoreCase("6")) {
				region = "MIDDLEEAST";
				break;
			} else
				System.out.println("Invalid option!!");
		}

		try {
			SubscriptionUnsubscriptionService sus = new SubscriptionUnsubscriptionService();
			SubscriptionUnsubscription su = sus.getSubscriptionUnsubscriptionPort();
			System.out.println(su.subscribe(email, region, 1, digest));
		} catch (Exception e) {
			System.out.println("It wasn't possible to insert your subscription. Try later.");
		}

	}

}
