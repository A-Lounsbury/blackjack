package blackjack;

import java.util.Scanner;
import java.util.InputMismatchException;

/*
This program lets the user play Blackjack.  The computer
acts as the dealer.  
The user has a stake of $100, and makes a bet on each game. 
The user can leave at any time, or will be kicked out when he loses all the money.
House rules:  The dealer hits on a total of 16 or less
and stands on a total of 17 or more.  
				Dealer wins ties.
				A new deck of cards is used for each game.
*/

public class Blackjack 
{
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		boolean userWins;
		int money = 100;
		int bet = 0;
		
		System.out.println("Welcome to the game of blackjack.");
		
		while (money > 0)
		{
			do 
			{
				boolean done = false;
				while (!done)
				{
					try
					{
						System.out.println("\nYou have " + money + " dollars.");
						System.out.print("How many dollars do you want to bet? (Enter 0 to end.)  ");
						bet = scanner.nextInt();
						
						done = true;
					}
					catch (InputMismatchException e)
					{
						scanner.nextLine();
						System.out.println("Invalid input: You must enter an integer.");
					}
				}
				
			} while (bet < 0 || bet > money);
				
			if (bet == 0)
				break;
			
			userWins = playBlackjack();
			
			if (userWins)
				money += bet;
			else
				money -= bet;
		}
		System.out.println("You leave with $" + money + ".");
	}
	
	
	private static boolean playBlackjack()
	{
		Scanner scanner = new Scanner(System.in);

		Deck deck = new Deck();
		deck.shuffle();
		
		BlackjackHand user = new BlackjackHand();
		BlackjackHand dealer = new BlackjackHand();
	   
		user.addCard(deck.dealCard());
		dealer.addCard(deck.dealCard());
		user.addCard(deck.dealCard());
		dealer.addCard(deck.dealCard());
		
		System.out.println("\n\nYour cards are:");
		for (int i = 0; i < user.getCardCount(); i++)
		   System.out.println("\t" + user.getCard(i));
		System.out.println("Your total is " + user.getBlackjackValue());
		
		// checking for winner; both have changed
		if (dealer.getBlackjackValue() == 21) // dealer always wins if 21
		{
			if (user.getBlackjackValue() != 21)
			{
				System.out.println("Dealer has 21. You lose.");
				return false;
			}
			else // tie at 21
			{
				System.out.println("Dealer wins in a tie. You lose. ");
				return false;
			}
		}
		else if (dealer.getBlackjackValue() < 21)
		{
			if (user.getBlackjackValue() > 21)
			{
				System.out.println("You busted by going over 21. You lose.");
				return false;
			}
			else if (user.getBlackjackValue() == 21)
			{
				System.out.println("You have 21. You win.");
				return true;
			}
		}
		else // dealer > 21
		{
			if (user.getBlackjackValue() <= 21)
			{
				System.out.println("Dealer busted by going over 21. You win.");
				return true;
			}
			else // user also > 21
			{
				if (dealer.getBlackjackValue() < user.getBlackjackValue())
				{
					System.out.println("Dealer is closer to 21. You lose. ");
					return false;
				}
				else if (dealer.getBlackjackValue() == user.getBlackjackValue()) // tie over 21
				{
					System.out.println("Dealer wins in a tie. You lose. ");
					return false;
				}
				else // dealer > user
				{
					System.out.println("You are closer to 21. You win. ");
					return true;
				}
			}
		}
		System.out.println("\nDealer is showing the " + dealer.getCard(0));
		
		char choice = 'H';
		while (choice == 'H')
		{
			System.out.print("\nHit (H) or Stand (S)?  ");
			choice = scanner.next().charAt(0);
			choice = Character.toUpperCase(choice);
			
			if (choice == 'H')
			{
				user.addCard(deck.dealCard());
				System.out.println("\nUser hits.");
				System.out.print("Your card is the " + user.getCard(user.getCardCount() - 1));
				
				System.out.println("\n\nYour cards are:");
				for (int i = 0; i < user.getCardCount(); i++)
				   System.out.println("\t" + user.getCard(i));
				System.out.println("Your total is " + user.getBlackjackValue());
				
				// checking for winner; dealer hasn't changed
				if (user.getBlackjackValue() == 21)
				{
					System.out.println("You have 21. You win.");
					return true;
				}
				else if (user.getBlackjackValue() > 21)
				{
					System.out.println("You busted by going over 21. You lose.");
					return false;
				}
			}
			else if (choice == 'S')
				System.out.println("\nUser stands.");
			else // user didn't enter H or S
			{
				System.out.println("You must enter, H or S.");
				choice = 'H'; // continues the loop
			}
		}
		
		System.out.println("Dealer's cards are");
		for (int i = 0; i < dealer.getCardCount(); i++)
			System.out.println("\t" + dealer.getCard(i));
		System.out.println("Dealer total: " + dealer.getBlackjackValue());
		
		while(dealer.getBlackjackValue() <= 16)
		{			
			dealer.addCard(deck.dealCard());
			
			System.out.println("\nDealer hits and gets the " + dealer.getCard(dealer.getCardCount() - 1));
			System.out.println("Dealer total: " + dealer.getBlackjackValue());
			
			// checking for winner; user hasn't change
			if (dealer.getBlackjackValue() == 21)
			{
				System.out.println("\nDealer has 21. You lose.");
				return false;
			}
			else if (dealer.getBlackjackValue() > 21)
			{
				System.out.println("\nDealer busted by going over 21. You win.");
				return true;
			}
			else // dealer has < 21
			{
				// user also has < 21 since previous == and > cases would have already ended game
				if (dealer.getBlackjackValue() > user.getBlackjackValue())
				{
					System.out.println("\nDealer is closer to 21. You lose. ");
					return false;
				}
				else if (dealer.getBlackjackValue() == user.getBlackjackValue()) // tie over 21
				{
					System.out.println("\nDealer wins in a tie. You lose. ");
					return false;
				}
				else // dealer < user
				{
					System.out.println("\nYou are closer to 21. You win. ");
					return true;
				}
			}
		}
		// Checking at the end
		// dealer and user both have < 21 since previous == and > cases would have already ended game
		if (dealer.getBlackjackValue() > user.getBlackjackValue())
		{
			System.out.println("\nDealer is closer to 21. You lose. ");
			return false;
		}
		else if (dealer.getBlackjackValue() == user.getBlackjackValue()) // tie under 21
		{
			System.out.println("\nDealer wins in a tie. You lose. ");
			return false;
		}
		else // dealer < user
		{
			System.out.println("\nYou are closer to 21. You win. ");
			return true;
		}
	}  // end playBlackjack()
} // end class Blackjack
