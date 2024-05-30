package games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Blackjack extends CasinoFloor {
	private List<Card> deck = new ArrayList<>();
	private List<Card> userHand = new ArrayList<>();
	private List<Card> dealerHand = new ArrayList<>();

	public Blackjack(int i) {
		super(i);

	}

	public Blackjack() {
		super();

	}

	private void makeDeck() {
		String[] suits = { "Hearts", "Diamonds", "Clubs", "Spades" };
		String[] rank = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace" };
		int[] value = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11 };

		for (String suit : suits) {
			for (int i = 0; i < rank.length; i++) {
				deck.add(new Card(suit, rank[i], value[i]));
			}
		}
	}

	private void shuffle() {
		Collections.shuffle(deck);
	}

	private Card giveCard() {
		if (deck.isEmpty()) {
			throw new IllegalStateException("Deck is empty");
		}
		return deck.remove(deck.size() - 1);
	}

	private int handValue(List<Card> hand) {
		int value = 0;
		int aceCount = 0;

		for (Card card : hand) {
			value += card.value;
			if (card.rank.equals("Ace")) {
				aceCount++;
			}
		}
		if (value > 21 && aceCount > 0) {
			value -= 10; // Convert one Ace from 11 to 1
		}

		return value;
	}

	private void clear() {
		deck.clear();
		userHand.clear();
		dealerHand.clear();
	}

	public void play(int amount) {
		makeDeck();
		shuffle();
		int user = 0, dealer = 0;
		if (amount <= money) {
			money = money - amount;

			userHand.add(giveCard());
			dealerHand.add(giveCard());
			userHand.add(giveCard());
			dealerHand.add(giveCard());

			System.out.println("User hand: " + userHand + " Value " + handValue(userHand));
			System.out.println();
			System.out.println("Dealer hand: [" + dealerHand.get(0) + " *Hidden Card*]");

			user = handValue(userHand);
			dealer = handValue(dealerHand);

			Scanner scanner = new Scanner(System.in);

			boolean stay = false;

			while (stay == false && handValue(userHand) <= 21) {

				System.out.println();
				System.out.print("Hit or Stay? (h/s): ");
				String answer = scanner.nextLine();
				System.out.println();

				if (answer.equalsIgnoreCase("h")) {
					userHand.add(giveCard());
					stay = false;
					user = handValue(userHand);
					System.out.println("New User Hand: " + userHand + " Value " + handValue(userHand));
				} else if (answer.equalsIgnoreCase("s"))
					stay = true;
				else {
					System.out.println("Try again!");
				}
			}
			// Reveals Dealers Hand
			System.out.println("Dealer hand: " + dealerHand + " Value " + handValue(dealerHand));

			while (handValue(dealerHand) <= 17 && handValue(userHand) <= 21
					&& handValue(userHand) > handValue(dealerHand)) {
				dealerHand.add(giveCard());
				System.out.println("Dealer hand: " + dealerHand + " Value " + handValue(dealerHand));
				dealer = handValue(dealerHand);
			}

			if ((user <= 21 && user > dealer) || (user <= 21 && dealer > 21)) {
				money += amount * 2;
				if (user == 21)
					System.out.println("Blackjack!");
				System.out.println("You Win!");
			} else if (user <= 21 && user == dealer)
				money += amount;
			else
				System.out.println("You Lose :( ");
		} else {
			System.out.println("Not enough money");
			System.out.println();
		}
		clear();
		user = 0;
		dealer = 0;
		System.out.println("Total Money: " + getMoney());
	}

	public static void main(String[] args) {
		Blackjack game = new Blackjack(100); // Initial money for the user
		Scanner scanner = new Scanner(System.in);
		
		boolean playMore = true;

		while (playMore) {
			System.out.print("Enter bet amount: ");
			while (!scanner.hasNextInt()) {
	            System.out.println("Invalid input. Please enter a numeric bet amount.");
	            scanner.next(); // Clear the invalid input
	            System.out.print("Enter bet amount: ");
	        }
			int betAmount = scanner.nextInt();
			game.play(betAmount);

			System.out.print("Do you want to play again? (yes/no): ");
			String playAgain = scanner.next();

			if (playAgain.equalsIgnoreCase("no")) {
				playMore = false;
			} else if (!playAgain.equalsIgnoreCase("yes")) {
				System.out.println("Invalid input");
			} else {
				playMore = true;
			}
		}

		scanner.close();
		System.out.println("Thank you for playing!");
	}
}
