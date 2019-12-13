


/*
 * 		This is the Class for each individual 'Cell'
 * 		A Cell has a location (Row, Column, Box)
 * 		'Box' is a 3x3 set of Cells, there are 9 Boxes arranged in a 3x3 grid.
 * 		Cells also hold 1 solution from 1-9.
 * 		Cells also hold the remaining possible solutions from 1-9, any that apply remain.
 */

public class Cell 
{
	//The Solution for a Cell
	int Answer = 0;
	
	//All remaining answers a Cell can be.
	boolean[] Possible_Answers = new boolean[9];
	
	//Cell's location in the Grid
	int Row, Column, Box;
	
	//The Class Constructor. All Values for a Cell are initiated in the File Input in the Class 'Grid'.
	public Cell(){}
	
	/*
	 * This method within the Cell Class is used to set only the Answer as the only true value in the 'Possible_Answer' boolean array to 'true'
	 * It is used at the start to set all the preset Cells with answers to be correct, also sets all none answered cells to have all values set to 'true'
	 * It is used at the point when a Cell is SOLVED to remove any other possible answers other than the Solved Answer.
	 */
	public void Possible_Answer_Set(int Starting_Value)
	{
		/*
		 * Starts by checking if the Cell has an Answer or not
		 * If it does have an answer all other values in the Possible_Answer array are set to false and only the Answer is set to true.
		 * If it does not have an answer all values are set to true, this would only accure at the beginning of the program, 
		 * there is nothing that sets Possible_Answer values to 'true' after this point.
		 */
		if(Starting_Value == 0)
			for(int i = 0; i < 9; i++)
					Possible_Answers[i] = true;
		else
			for(int i = 0; i < 9; i++)
				if(Starting_Value-1==i)
					Possible_Answers[i] = true;
				else
					Possible_Answers[i] = false;
	}

}
