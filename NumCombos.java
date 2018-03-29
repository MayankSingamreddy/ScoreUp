/**
 *	NumCombos.java
 *	Finding the combinations of numbers 1 through LARGEST_NUMBER
 *	with no repeat numbers that could sum to a given number from
 *	1 to (LARGEST_NUMBER * 2).
 *
 *	Requires the Prompt class.
 *
 *	The algorithm uses an array as a stack. Index 0 is the top
 *	of the stack.
 *
 *	This algorithm is similar to the "is play possible" solution in
 *	the ScoreUp game. Both iterative and recursive algorithms are
 *	presented.
 *
 *	@author	Mr Greenstein
 *	@since	September 20, 2017
 */
public class NumCombos {
	
	private final int LARGEST_NUMBER = 16;	// largest number to find combination
											// try 1 up to this number
	
	public static void main(String[] args) {
		NumCombos nc = new NumCombos();
		nc.run();
	}
	
	/**
	 *	Prompts the user for an input number and then runs both
	 *	iterative and recursive algorithms to print all combinations
	 *	of numbers that create its sum.
	 */
	public void run() {
		System.out.println("\nFind all sum combinations using 1 through 9\n");
		int value = 0;
		String answer = "";
		do {
			value = Prompt.getInt("Input a number", 1, (LARGEST_NUMBER * 2));
			
			// iterative version
			System.out.println("\nPrinting iterative solution:");
			printCombinations(value);
			
			// recursive version
			System.out.println("\nPrinting recursive solution:");
			printCombinationsRecurse(value);
			
			answer = Prompt.getString("\nInput another number? y or n");
		} while (answer.toLowerCase().equals("y"));
		System.out.println("\nThanks for playing!\n");
	}
	
	/**
	 *	Print the combinations of numbers 1 to LARGEST_NUMBER
	 *	for the value passed into the method with iterative loop.
	 *	Example:	value = 5, prints 1 4, 2 3, & 5
	 *				value = 8, prints 1 2 5, 1 3 4, 1 7, 2 6, 3 5, & 8
	 *	@param value	the value to print combinations
	 */
	public void printCombinations(int value) {
		// Create a stack array and put first value on the stack
		int[] stack = new int[LARGEST_NUMBER];
		int stackPtr = 0;
		// Start with 1 as the first value
		stack[stackPtr] = 1;
		
		// try all sums of numbers from 1 to LARGEST_NUMBER
		while (stackPtr > -1) {
			// if sum of stack values equals target value
			if (stackSum(stack, stackPtr) == value) {
				// print the stack of numbers
				printStack(stack, stackPtr);
				// pop stack because we cannot add anymore values
				// nor increase the value of the top of the stack
				stackPtr--;
				// if stack not empty then increment top value
				if (stackPtr > -1)
					stack[stackPtr]++;
			}
			// if sum of stack values is less than target value 
			else if (stackSum(stack, stackPtr) < value) {
				// push a number of 1 greater value than the top of stack
				stackPtr++;
				stack[stackPtr] = stack[stackPtr-1] + 1;
			}
			// if sum of stack values is greater than target value
			else {
				// pop stack
				stackPtr--;
				// if top of stack is less than largest possible value
				if (stackPtr > -1 && stack[stackPtr] < LARGEST_NUMBER)
					// increment top stack value
					stack[stackPtr]++;
				// if top of stack is equal to largest possible value
				else {
					// pop stack and increment the top of the stack
					stackPtr--;
					if (stackPtr > -1) stack[stackPtr]++;
				}
			}
		}
	}
	
	/**
	 *	Starts recursive version of printCombinations
	 *	@param value	the value to print combinations
	 */
	public void printCombinationsRecurse(int value) {
		int stackPtr = 0;
		int[] stack = new int[LARGEST_NUMBER];
		// push each starting number from 1 to LARGEST_NUMBER
		for (int a = 1; a <= LARGEST_NUMBER; a++) {
			stack[stackPtr] = a;
			printRecursive(value, stack, stackPtr);
		}
	}
	
	/**
	 *	Recursive method for printCombinationsRecurse
	 *	@param value		the value to find combinations
	 *	@param stack		the stack of values thus far
	 *	@param currentPtr	the current pointer to last value in stack
	 */
	public void printRecursive(int value, int[] stack, int currentPtr) {
		// base case: stack sum equals value then print stack
		if (stackSum(stack, currentPtr) == value)
			printStack(stack, currentPtr);
		else {
			for (int a = stack[currentPtr] + 1; a <= LARGEST_NUMBER; a++) {
				// push the loop value onto the stack
				currentPtr++;
				stack[currentPtr] = a;
				// base case: stack sum equals value then print stack
				if (stackSum(stack, currentPtr) == value)
					printStack(stack, currentPtr);
				// if stack sum is less than value then recurse and add another value to the stack
				else if (stackSum(stack, currentPtr) < value)
					printRecursive(value, stack, currentPtr);
				// base case: value is less than stack sum, then end recursing
				// pop loop value from stack
				currentPtr--;
			}
		}		
	}
	
	/**
	 *	Print the combination of values that sum to input value.
	 *	@param value			the target value
	 *	@param currentValue		the current value being tried
	 */
	
	public int stackSum(int[] stack, int last) {
		int sum = 0;
		for (int a = 0; a <= last; a++)
			sum += stack[a];
		return sum;
	}
	
	public void printStack(int[] stack, int last) {
		for (int a = 0; a <= last; a++)
			System.out.print(stack[a] + " ");
		System.out.println();
	}
}