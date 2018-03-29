/**
 *  This program defines the Dice class. A Dice is 6 sides by default, but can
 *  be overridden as n-sided. It keeps track of the number of rolls and
 *  the roll value.
 *
 *  @author Mr Greenstein
 *  @since	September 12, 2017
*/

public class Dice {
	// Die's number of sides, roll count, and current value
	private int sides, rollcount, value;
	
	/*	The default constructor. Die has 6 sides. */
	public Dice ( ) {
		sides = 6;
		rollcount = value = 0;
	}
	
	/*	Constructor
	 *	@param sides	number of sides for die
	 */
	public Dice (int s) {
		sides = s;
		rollcount = value = 0;
	}
	
	/**
	 *	Roll the die and show a random value based on the number of sides.
	 *	@return		the value showing on the die
	 */
	public int roll ( ) {
		rollcount++;
		value = (int)(Math.random()*sides) + 1;
		return value;
	}

	/*
	 *	@return		the total number of rolls this die has taken
	 */
	public int numRolls ( ) {
		return rollcount;
	}
	
	/*
	 *	@return		the die value showing
	 */
	public int getValue ( ) {
		return value;
	}
}






