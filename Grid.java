
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/*
 * 
 * A solved Cell is a unique Possible_Answer in either a Row, Column, or Box.
 * 
 * 
 * Step 1. Update all Possible_Answer based on Starting Values
 * Step 2. Find Imaginary Numbers (Look at this later)
 * Step 3. Attempt to Find Unique Answers in rows, Columns, and Boxes.
 * Repeat
 * 
 */


public class Grid 
{
	
	public static void main(String[] args) throws IOException {Grid StartUp = new Grid();}
	
	Cell[] Cell_Map = new Cell[81];
	
	int Row = 0, Column = 0;
	int Cell_Index = 0;
	
	int Solve_Count = 0;
	
	boolean Solved = false;
	
	
	public Grid() 
	{
		//(Check) Stupidity Proof 
		//(Check) 17 rules.
		//(Check) No starting duplicates
		//(Check) Each pass has to solve at least 1 cell or else it stops.
		//(Check) New Format reader one line of 81 chars or 9x9
		//create imaginary logic
		//(Check) Get onto GitHub
		
		
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("Input"));
			String Input_Row = "";
			
			Input_Row = reader.readLine();
			
			if(Input_Row == null)
			{
				System.out.println("Input Format Error");
				System.exit(0);
			}
			
			if(Input_Row.length() != 81)
			{
				for(int i = 0;i<8;i++)
				{
					if(Input_Row.length() != 9)
					{
						System.out.println("Input Format Error");
						System.exit(0);
					}
					Input_Row = reader.readLine();
					if(Input_Row == null)
					{
						System.out.println("Input Format Error");
						System.exit(0);
					}
				}
				
				
			}	
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		//Pulling the Data from the Input Text File.
		try {
			reader = new BufferedReader(new FileReader("Input"));
			String Input_Row = "";
			
			Input_Row = reader.readLine();
			
			//debuging why it isn't storing any answers for the 81x1 method.
			//System.out.println(Input_Row);
			
			
			if(Input_Row.length() == 81)
			{
				for(int Grid_Index = 0; Grid_Index < 81; Grid_Index++)
				{
					Cell_Map[Grid_Index] = new Cell();
					Cell_Map[Grid_Index].Answer = Character.getNumericValue(Input_Row.charAt(Grid_Index));
					Cell_Map[Grid_Index].Row = (int)Math.floor(Grid_Index/9);
					Cell_Map[Grid_Index].Column = Grid_Index % 9;
					Cell_Map[Grid_Index].Box = ((int)Math.floor(Cell_Map[Grid_Index].Row/3)*3)+((int)Math.floor(Cell_Map[Grid_Index].Column/3));
					Cell_Map[Grid_Index].Possible_Answer_Set(Cell_Map[Grid_Index].Answer);
				}
			}
			else
			{
				for(;Row < 9; Row++)
				{
					for(;Column < 9; Column++)
					{
						Cell_Index = (Row*9)+Column;
						Cell_Map[Cell_Index] = new Cell();
						Cell_Map[Cell_Index].Answer = Character.getNumericValue(Input_Row.charAt(Column));
						Cell_Map[Cell_Index].Row = Row;
						Cell_Map[Cell_Index].Column = Column;
						Cell_Map[Cell_Index].Box = ((int)Math.floor(Row/3)*3)+((int)Math.floor(Column/3));
						Cell_Map[Cell_Index].Possible_Answer_Set(Cell_Map[Cell_Index].Answer);
					}
					Column = 0;
					Input_Row = reader.readLine();
				}
			}
			Print_Solution(Cell_Map);
			
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		for(int Grid_Index = 0; Grid_Index < 81; Grid_Index++)
		{
			Duplicate_Checker(Area_Finder(Cell_Map[Grid_Index]), Cell_Map[Grid_Index]);
		}
		
		
		//At this point the Input data must be accurate enough to solve
		//Logic Starting Point/ Begin Solving
		//Determine Possible_Answers
		for(int Grid_Index = 0; Grid_Index < 81; Grid_Index++)
		{
			if(Cell_Map[Grid_Index].Answer > 0)
			{
				Update_Possible_Answers(Cell_Map[Grid_Index]);
				Solve_Count++;
			}
			
		}
		
		if(Solve_Count < 17)
		{
			System.out.println("Not enough starting information to solve");
			System.exit(0);
		}
		
		while(Solved == false)
		{
			Solve_Count = 0;			
			
			for(int Grid_Index = 0; Grid_Index < 81; Grid_Index++)
				if(Cell_Map[Grid_Index].Answer == 0)
					Solve_Cell(Cell_Map[Grid_Index]);
			
			for(int Grid_Index = 0; Grid_Index < 81; Grid_Index++)
				if(Cell_Map[Grid_Index].Answer == 0)
					Linear_Logic(Cell_Map[Grid_Index]);
			
			for(int Grid_Index = 0; Grid_Index < 81; Grid_Index++)
				if(Cell_Map[Grid_Index].Answer == 0)
					Only_Option(Cell_Map[Grid_Index]);
			
			Check_Solved();
			Print_Solution(Cell_Map);
			if(Solve_Count == 0)
			{
				System.out.println("No progress was made and the program was terminated.");
				System.exit(0);
			}
		}
		
		
		
		
		
		// TODO Auto-generated constructor stub
	}

	
	public void Duplicate_Checker(Cell[][] Area, Cell Target)
	{
		boolean[] Check_Array = new boolean[9];
		
		for(int Area_Index = 0; Area_Index < 3; Area_Index++)
		{
			for(int i=0;i<9;i++)
				Check_Array[i] = false;
			for(int Cell_Index = 0; Cell_Index < 9; Cell_Index++)
				if(Area[Area_Index][Cell_Index] != Target)
					if(Target.Answer > 0 & Area[Area_Index][Cell_Index].Answer == Target.Answer)
					{
						System.out.println("A duplicate number was found.");
						System.exit(0);
					}
		}
	}
	
	
	public void Update_Possible_Answers(Cell Target)
	{
		for(int Grid_Index = 0; Grid_Index < 81; Grid_Index++)
		{
			if(Cell_Map[Grid_Index].Row == Target.Row)
				Cell_Map[Grid_Index].Possible_Answers[Target.Answer-1] = false;
			if(Cell_Map[Grid_Index].Column == Target.Column)
				Cell_Map[Grid_Index].Possible_Answers[Target.Answer-1] = false;
			if(Cell_Map[Grid_Index].Box == Target.Box)
				Cell_Map[Grid_Index].Possible_Answers[Target.Answer-1] = false;
		}
	}
	
	public void Linear_Logic(Cell Target)
	{
		Cell[][] Area = new Cell[3][];
		
		Area = Area_Finder(Target);
		
		//test point
		boolean[] Remaining_Answers = new boolean[9];
		
		for(int i=0;i<9;i++)
			Remaining_Answers[i] = (boolean)Target.Possible_Answers[i];
		
	
		//Row First
		for(int Box_Index=0;Box_Index<9;Box_Index++)
			if(Area[2][Box_Index].Row != Target.Row & Area[2][Box_Index].Answer == 0)
				for(int i=0;i<9;i++)
					if(Area[2][Box_Index].Possible_Answers[i] == Target.Possible_Answers[i])
						Remaining_Answers[i] = false;
		
		for(int i=0;i<9;i++)
			if(Remaining_Answers[i])
				for(int Area_Index=0;Area_Index<9;Area_Index++)
					if(Area[0][Area_Index].Possible_Answers[i] & Area[0][Area_Index].Box != Target.Box)
					{
						Area[0][Area_Index].Possible_Answers[i] = false;
						Solve_Count++;
					}
		
		
		//Column Second
		
		for(int i=0;i<9;i++)
		{
			Remaining_Answers[i] = (boolean)Target.Possible_Answers[i];
		}
		
		for(int Box_Index=0;Box_Index<9;Box_Index++)
			if(Area[2][Box_Index].Column != Target.Column & Area[2][Box_Index].Answer == 0)
				for(int i=0;i<9;i++)
					if(Area[2][Box_Index].Possible_Answers[i] == Target.Possible_Answers[i])
						Remaining_Answers[i] = false;
		
		for(int i=0;i<9;i++)
			if(Remaining_Answers[i])
				for(int Area_Index=0;Area_Index<9;Area_Index++)
					if(Area[1][Area_Index].Possible_Answers[i] & Area[1][Area_Index].Box != Target.Box)
					{
						Area[1][Area_Index].Possible_Answers[i] = false;
						Solve_Count++;
					}
	}

	public void Only_Option(Cell Target)
	{
		int Option_Count = 0;
		for(int i=0;i<9;i++)
			if(Target.Possible_Answers[i])
				Option_Count++;
		
		if(Option_Count == 1)
			for(int i=0;i<9;i++)
				if(Target.Possible_Answers[i])
				{
					Target.Answer = i+1;
					Solve_Count++;
				}
		
		
		if(Option_Count == 0)
		{
			System.out.println("Cell at Row:" + Target.Row + " and Column:" + Target.Column + " has no possible answers.");
			System.exit(0);
		}
	}
	
	
	public void Combined_Logic()
	{
		
	}
	
	public void Solve_Cell(Cell Target)
	{
		
		Cell[][] Temp_Return_Area = new Cell[3][0];
		
		Temp_Return_Area = Area_Finder(Target);
		
		Cell[] Solve_Row = Temp_Return_Area[0];
		Cell[] Solve_Column = Temp_Return_Area[1];
		Cell[] Solve_Box = Temp_Return_Area[2];
		
		for(int Answer_Index = 0; Answer_Index < 9; Answer_Index++)
		{
			if(Target.Possible_Answers[Answer_Index] == true)
			{
				if(Check_Answer(Solve_Row, Answer_Index, Target) & Answer_Index < 9)
				{
					Target.Answer = Answer_Index + 1;
					Answer_Index = 9;
					Target.Possible_Answer_Set(Target.Answer);
					Update_Possible_Answers(Target);
					Solve_Count++;
				}
				if(Check_Answer(Solve_Column, Answer_Index, Target) & Answer_Index < 9)
				{
					Target.Answer = Answer_Index + 1;
					Answer_Index = 9;
					Target.Possible_Answer_Set(Target.Answer);
					Update_Possible_Answers(Target);
					Solve_Count++;
				}
				if(Check_Answer(Solve_Box, Answer_Index, Target) & Answer_Index < 9)
				{
					Target.Answer = Answer_Index + 1;
					Answer_Index = 9;
					Target.Possible_Answer_Set(Target.Answer);
					Update_Possible_Answers(Target);
					Solve_Count++;
				}
					
			}
		}
		
	}
	
	
	public Cell[][] Area_Finder(Cell Target) 
	{
		
		Cell[][] Return_Area = new Cell[3][0];
		
		Cell[] Return_Row = new Cell[9];
		int R_Ind = 0;
		Cell[] Return_Column = new Cell[9];
		int C_Ind = 0;
		Cell[] Return_Box = new Cell[9];
		int B_Ind = 0;
		
		for(int Grid_Index = 0; Grid_Index < 81; Grid_Index++)
		{
			if(Cell_Map[Grid_Index].Row == Target.Row)
			{
				Return_Row[R_Ind] = Cell_Map[Grid_Index];
				R_Ind++;
			}
			if(Cell_Map[Grid_Index].Column == Target.Column)
			{
				Return_Column[C_Ind] = Cell_Map[Grid_Index];
				C_Ind++;
			}
			if(Cell_Map[Grid_Index].Box == Target.Box)
			{
				Return_Box[B_Ind] = Cell_Map[Grid_Index];
				B_Ind++;
			}
		}
		
		Return_Area[0] = Return_Row;
		Return_Area[1] = Return_Column;
		Return_Area[2] = Return_Box;
		
		return Return_Area;
	}
		
	
	//Answer is already passed in as Array format 0-8 is 1-9.
	public boolean Check_Answer(Cell[] Area, int Answer, Cell Target)
	{
		boolean Answer_Found = true;
		
		//This if/statement is to catch if the 'Solve_Cell' for/loop has been prompted to stop
		//this is done by setting the 'Answer_Index' to 9
		if(Answer < 9)
		{
			for(int i = 0; i < 9; i++)
			{
				if(Area[i] != Target)
					if(Area[i].Possible_Answers[Answer])
					{
						Answer_Found = false;
						i = 9;
					}
			}
				
		}		
		return Answer_Found;
		
	}
	
	
	public void Check_Solved()
	{
		Solved = true;
		for(int Grid_Index = 0; Grid_Index < 81; Grid_Index++)
			if(Cell_Map[Grid_Index].Answer == 0)
			{
				Solved = false;
				Grid_Index = 81;
			}
	}
	
	public void Print_Solution(Cell[] Solved_Grid)
	{
		String Output_Row = "";
		Row = 0;
		Column = 0;
		for(;Row < 9; Row++)
		{
			for(;Column < 9; Column++)
			{
				Cell_Index = (Row*9)+Column;
				Output_Row = Output_Row + " " + Cell_Map[Cell_Index].Answer;
			}
			System.out.println(Output_Row);
			Column = 0;
			Output_Row = "";
		}
		System.out.println("____________________________");
	}
	
}
