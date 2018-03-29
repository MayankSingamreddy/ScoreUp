/**
 *  This program contains the rules of the game ScoreUp and runs the game.
 *	<Describe the game in brief here>
 *
 *	The Prompt, ScoreUpPlayer, DiceGroup, and Dice classes are required.
 *
 *  @author Mayank Singamreddy
 *  @since	9/25/17
 */

public class ScoreUp
{
	private ScoreUpPlayer player1, player2;	// the two players
	private DiceGroup dice;				// the group of dice
	private boolean player1Turn;		// true = player1's turn, false = player2's turn
	private int round;					// round counter
	private TileBoard board;			// the board of tiles

	// Constants
	public final int NUM_DICE = 2;		// total number of dice
	public final int NUM_ROUNDS = 5;	// total number of rounds
	public final int NUM_TILES = 9; 	// total number of tiles in a round

// creates new instance of: DiceGroup, ScoreUpPlayer1, ScoreUpPlayer2, TileBoard
	public ScoreUp()
	{
		dice = new DiceGroup(NUM_DICE);
		player1 = new ScoreUpPlayer(NUM_ROUNDS);
		player2 = new ScoreUpPlayer(NUM_ROUNDS);
		board = new TileBoard(NUM_TILES);
	}

// main method creates new game and calls run method
	public static void main(String[] args)
	{
		ScoreUp game = new ScoreUp();
		game.run();
	}

/*
  Calls introduction method to give user instructions
  Receives player names and prints initial score diagram (empty)
  Plays both player's rounds and prints scores each time
  Calculates the total score and based on whose score is higher, prints result
*/
	public void run()
	{
		printIntroduction();
		getNames();
		printRoundScore();
		for(round = 1; round <= NUM_ROUNDS; round++)
		{
			System.out.println(">>>>>>>>>>>>>><<<<<<<<<<<<<<\n>>> Round " + round + " of " + NUM_ROUNDS + " rounds <<<<\n>>>>>>>>>>>>>><<<<<<<<<<<<<<\n");
			playerTurn();
			playerTurn();
		}
		printRoundScore();

		if(player1.getTotalScore() > player2.getTotalScore())
			System.out.println("CONGRATULATIONS >>> " + player1.getName() + " for being the HIGH SCORER.");
		else if(player1.getTotalScore() < player2.getTotalScore())
			System.out.println("CONGRATULATIONS >>> " + player2.getName() + " for being the HIGH SCORER.");
		else
			System.out.println("CONGRATULATIONS >>> YOU BOTH SCORED THE SAME");
	}

/*
	method for most of the game, begins the process of receiving player names
	calls the: roll dice method, prints dice output, and prints tileboard
	enters a while loop to check if any more rounds are playable (max of 5)
	once the plays are over, prompts the user with the ending of their turn
*/
	public void playerTurn()
	{
		//DO STUFF
		ScoreUpPlayer player;
		if(player1Turn)
			player = player1;
		else
			player = player2;

		Prompt.getString(player.getName() + ", it's your turn to play. Please hit enter to roll the dice");
		dice.rollDice();
		dice.printDice();
		board.printTiles();

		while(playIsPossible())
		{
			String remove = "a";
			while(!isValid(remove))
				remove = Prompt.getString("Enter the tiles to remove. For example, if you'd like to remove tiles 1, 2, and 5, enter 125");
			for(int i = 0; i < remove.length(); i++)
				board.clearTile(Integer.parseInt("" + remove.charAt(i)) - 1);
			dice.rollDice();
			dice.printDice();
			board.printTiles();
		}

		System.out.println("\nUh-oh, looks like there are no valid choices left ...\n");
		player.scoreUpRound(round, board.computeRound());
		printRoundScore();
		Prompt.getString("\n" + player.getName() + ", your turn has ended. Please hit enter to finish your turn");
		System.out.println();

		player1Turn = !player1Turn;
		board.resetTiles();
	}

// @return boolean for if any more plays can be played
	public boolean playIsPossible()
	{
		int target = dice.getTotal();

		//find number of unscored tiles
		int count = 0;
		for(int i = 0; i < 9; i++)
			if(!board.isTileScored(i))
				count++;

		//(re)fills the array with unremoved tiles
		int[] values = new int[count];
		count = 0;
		for(int i = 0; i < 9; i++)
			if(!board.isTileScored(i))
				values[count++] = i + 1;

		//Stack loop to find all possible combinations for plays possible
		int[] stack = new int[values.length];
		for (int i = 0; i < values.length; i++)
		{
			stack[0] = values[i];
			if(foundPlay(target, values, i,  stack, 0))
				return true;
		}
		return false;
	}

// boolean for if play is possible (if it has been found)
	public boolean foundPlay(int target, int[] values, int index,  int[] stack, int currentPtr)
	{
		if (stackSum(stack, currentPtr) == target)
			return true;

		for (int i = index + 1; i < values.length; i++)
		{
			currentPtr++;
			stack[currentPtr] = values[i];
			if (stackSum(stack, currentPtr) == target)
				return true;
			else if(stackSum(stack, currentPtr) < target)
				//index or i?
				if(foundPlay(target, values, i, stack, currentPtr))
					return true;
			currentPtr--;
		}
		return false;
	}

// @return int of total entered value
	public int stackSum(int[] stack, int last)
	{
		int sum = 0;
		for (int a = 0; a <= last; a++)
			sum += stack[a];
		return sum;
	}

// @return boolean for whether or not user input of digits is correct
	public boolean isValid(String choice)
	{
		return validDigits(choice) && validSum(choice) && validTiles(choice);
	}

// @return boolean whether or not the the digits entered are in fact digits
	public boolean validDigits(String choice)
	{
		boolean[] digits = new boolean[10];
		for(int i = 0; i < choice.length(); i++)
		{
			int num = 0;
			try
			{
				num = Integer.parseInt("" + choice.charAt(i));
			}
			catch(NumberFormatException e)
			{
				return false;
			}
			if(num == 0 || digits[num])
				return false;
			digits[num] = true;
		}
		return true;
	}

// @return boolean for if the sum of numbers entered was valid
	public boolean validSum(String choice)
	{
		int sum = 0;
		for(int i = 0; i < choice.length(); i++)
			sum += Integer.parseInt("" + choice.charAt(i));
		return sum == dice.getTotal();
	}

// @return boolean for if the tiles removed would be the correct tiles to be removed
	public boolean validTiles(String choice)
	{
		for(int i = 0; i < choice.length(); i++)
			if(board.isTileScored(Integer.parseInt("" + choice.charAt(i)) - 1))
				return false;
		return true;
	}

// method to prompt players for names and saves them to their object 
	public void getNames()
	{
		int total1 = 0; // player1 score
		int total2 = 0; // player2 score
		String name1 = Prompt.getString("Player 1, please enter your first name");
		player1.setName(name1);
		System.out.println();
		String name2 = Prompt.getString("Player 2, please enter your first name");
		player2.setName(name2);
		System.out.println("\nWelcome " + name1 + " and " + name2 + "!\n");

		do{
			Prompt.getString("Lets see who will go first. " + name1 + ", please hit enter to roll the dice");
			dice.rollDice();
			dice.printDice();
			total1 = dice.getTotal();
			Prompt.getString(name2 + ", its your turn. Please hit enter to roll the dice");
			dice.rollDice();
			dice.printDice();
			total2 = dice.getTotal();

			System.out.println(name1 + ", you rolled a sum of " + total1 + ", and " + name2 + ", you rolled a sum of " + total2 + ".\n");

			if(total1 == total2)
				System.out.println(">>>>>>><<<<<<\n>> DO OVER << Both of you got the same value!\n>>>>>>><<<<<<\n\n");

		}while(total1 == total2);

		player1Turn = total1 > total2;
		if(player1Turn)
			System.out.println("*******************\n* Congratulations * " + player1.getName() + ", you rolled a higher number so you get to go first.\n*******************\n\n");
		else
			System.out.println("*******************\n* Congratulations * " + player2.getName() + ", you rolled a higher number so you get to go first.\n*******************\n\n");
	}


	/**
	 *	Prints the introduction screen
	 */
	public void printIntroduction() {
		System.out.println("\n");
		System.out.println("+------------------------------------------------------------------------------------+");
		System.out.println("|           ______   ______   ______   ______   ______   __  __   ______             |");
		System.out.println("|          /\\  ___\\ /\\  ___\\ /\\  __ \\ /\\  == \\ /\\  ___\\ /\\ \\/\\ \\ /\\  == \\            |");
		System.out.println("|          \\ \\___  \\\\ \\ \\____\\ \\ \\/\\ \\\\ \\  __< \\ \\  __\\ \\ \\ \\_\\ \\\\ \\  _-/            |");
		System.out.println("|           \\/\\_____\\\\ \\_____\\\\ \\_____\\\\ \\_\\ \\_\\\\ \\_____\\\\ \\_____\\\\ \\_\\              |");
		System.out.println("|            \\/_____/ \\/_____/ \\/_____/ \\/_/ /_/ \\/_____/ \\/_____/ \\/_/              |");
		System.out.println("|                                                                                    |");
		System.out.println("| WELCOME TO MONTA VISTA SCOREUP!                                                    |");
		System.out.println("|                                                                                    |");
		System.out.println("| ScoreUp is a dice game played between two players.  There are "
							+ NUM_ROUNDS + " rounds in a game   |");
		System.out.println("| of ScoreUp, and the players alternate turns.  In each turn, a player starts with   |");
		System.out.println("| the tiles 1, 2, 3, 4, 5, 6, 7, 8, and 9 showing.  The player then rolls a pair of  |");
		System.out.println("| dice.  After rolling the dice, the player adds up the dots on the dice, and then   |");
		System.out.println("| \"Scores Up\" any combination of numbers that equals the total number of dots        |");
		System.out.println("| showing on the dice. For example, if the total number of dots is 8, the player may |");
		System.out.println("| choose any of the following sets of numbers (as long as all of the numbers in the  |");
		System.out.println("| set have not yet been removed):                                                    |");
		System.out.println("|          8 or 7 & 1 or 6 & 2 or 5 & 3 or 5 & 2 & 1 or 4 & 3 & 1.                   |");
		System.out.println("|                                                                                    |");
		System.out.println("| The player then rolls the dice again, aiming to remove more numbers. The player    |");
		System.out.println("| continues throwing the dice and removing numbers until reaching a point at which,  |");
		System.out.println("| given the results produced by the dice, the player cannot remove any more numbers. |");
		System.out.println("| At that point, the player scores the sum of the numbers that have been removed.    |");
		System.out.println("| For example, if the numbers 2, 3, and 5 remain when the player rolls 6 & 3, the    |");
		System.out.println("| player's score is 35 (1 + 4 + 6 + 7 + 8 + 9 = 35). Play then passes to the next    |");
		System.out.println("| player.  After five rounds, the winner is the player with the highest total.       |");
		System.out.println("|                                                                                    |");
		System.out.println("| LET'S PLAY SOME SCOREUP!                                                           |");
		System.out.println("+------------------------------------------------------------------------------------+");
		System.out.println("\n");
	}

	/**
	 *	Prints the Round Scoreboard
	 */
	public void printRoundScore() {
		int num = 0;
		System.out.println("\n  NAME           Round 1    Round 2    Round 3    Round 4    Round 5     Total");
		System.out.println("+---------------------------------------------------------------------------------+");
		System.out.printf("| %-12s |", player1.getName());
		for (int i = 0; i < NUM_ROUNDS; i++) {
			num = player1.getRoundScore(i);
			if (num == 0) System.out.printf("          |", num);
			else System.out.printf("   %3d    |", num);
		}
		System.out.printf("   %4d    |\n", player1.getTotalScore());
		System.out.println("+---------------------------------------------------------------------------------+");
		System.out.printf("| %-12s |", player2.getName());
		for (int i = 0; i < NUM_ROUNDS; i++) {
			num = player2.getRoundScore(i);
			if (num == 0) System.out.printf("          |", num);
			else System.out.printf("   %3d    |", num);
		}
		System.out.printf("   %4d    |\n", player2.getTotalScore());
		System.out.println("+---------------------------------------------------------------------------------+\n");
	}
}