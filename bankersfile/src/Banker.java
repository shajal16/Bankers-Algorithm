
import java.io.*;
import java.util.StringTokenizer;
import java.util.Scanner;


public class Banker
{
	public static void main(String[]args)throws IOException
	{
		
		int n = 0;							// number of processes
		int m = 0;							//  number of resources
		int count = 0;						// Counter number of lines in the file
		int lineCount = 0;				        // Counter  number of lines in each matrix
		int [] sumColumn;				       //  sum of each column.				
		int [] sumRow;					       //  sum of each row.
		int [] resourceVector;			       //  resource vector
		int [] availableVector;			//  available vector
		int [] work;						//  currently available vector
		int [] processSequence;			// sequence of processes to run to completion
		int index = 0;						//  index value of the process sequence
		boolean finish[];					// process has finished flag
		int [][] claimMatrix;			//  claim matrix
		int [][] allocationMatrix;		// allocation matrix
		int [][] needMatrix;				//  need matrix ( C-A );
		boolean isSafe = false;			//  safe or unsafe state flag
		
  		 
		String line; //  line from a file and tokenize   
		String fileIn; 
               StringTokenizer tokens;
		
		
		Scanner keyboard = new Scanner(System.in);
		
		System.out.println("Please enter in the name of the file : ");
		fileIn = keyboard.nextLine();
      
		// build input stream
  		FileReader fr = new FileReader(fileIn);    
	   
		// Use Buffered reader to read one line at a time
		BufferedReader bufRead = new BufferedReader(fr);
		
		// Read first line
		line = bufRead.readLine();
		n = Integer.parseInt(line);
		count++;
		
		// Read second line
		line = bufRead.readLine();
		m = Integer.parseInt(line);
		count++;
		
		// Create each Matrix(n x m) and Vector( m )
		claimMatrix = new int[n][m];
		allocationMatrix = new int[n][m];
		needMatrix = new int [n][m];
		resourceVector = new int[m];
		availableVector = new int[m];
		work = new int[m];
		finish = new boolean[n];
		processSequence = new int[n];
		sumColumn = new int[m];
		sumRow = new int[n];
		
		
		line = bufRead.readLine();
		count++;
		
		// Read through file and set Claim Matrix
		while(line != null && lineCount < n)
		{
			tokens = new StringTokenizer(line);
		
			if(tokens.hasMoreTokens())
			{
				for(int j = 0; j < m; j++)
				{
					claimMatrix[lineCount][j] = Integer.parseInt(tokens.nextToken());
					
				}
				
			}
			
			
			line = bufRead.readLine();
			lineCount++;
			count++;
		}
		
		lineCount = 0;
		
		// Read through file and set Allocation Matrix
		while(line != null && lineCount < n)
		{
			tokens = new StringTokenizer(line);
			
			if(tokens.hasMoreTokens())
			{
				for(int j = 0; j < m; j++)
				{
					allocationMatrix[lineCount][j] = Integer.parseInt(tokens.nextToken());
				}
			}
			
			line = bufRead.readLine();
			lineCount++;
			count++;
		}
		
		
		// Read the last line and set Resource Vector
		
		tokens = new StringTokenizer(line);
		while(tokens.hasMoreTokens()) 
		{
			for(int i = 0; i < m; i++)
			{
				resourceVector[i] = Integer.parseInt(tokens.nextToken()); 
			}
		}
		
		
		// Close the bufferreader and file
		bufRead.close();
		fr.close();
		
		// Determine the initial need matrix
		
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < m; j++)
			{
				needMatrix[i][j] = claimMatrix[i][j] - allocationMatrix[i][j];
			}
		}
		
				
		// Determine the initial available vector
		
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < m; j++)
			{
				sumColumn[j] += allocationMatrix[i][j];
				sumRow[i] += needMatrix[i][j];	
			}	
			
		}
		
		for(int j = 0; j < m; j++)
		{
			availableVector[j] = resourceVector[j] - sumColumn[j];
		}
		
				
		// Initialize Work and Finish
		
		for(int j = 0; j < m; j++)
		{
			work[j]=availableVector[j];
		}
		
		for(int i = 0; i < n; i++)
		{
			finish[i] = false;
			
		}
		
		// Safety Algorithm (checks if the system is in a safe or unsafe state)
		
		boolean found = false;
		
		do
		{
			found = false;				// Process found flag
			int i = 0;
			
			for(; i < n; i++)
			{
				if ((!finish[i]))
        		{
          		boolean good = true;                    
          		
					for (int j = 0; j < m; j++)
					{ 													   
      				// If the need is greater than the available, then the process is not able to run to completion
						// So it is not good     		
						if(needMatrix[i][j] > work[j])
						{
							good = false;
             			break;
           			}
					}
          		
					if (!good) continue;			// Try another process                   
          		found = true;
          		break;		
      		}
			}
			
			// Process is found that can run to completion, simulate execution
			if(found)                                  
      	{
				finish[i] = true; 
				for (int j = 0; j < m; j++)
				{      
          		work[j] += claimMatrix[i][j];
		      }
				
				processSequence[index++] = i;
			}
		
		}while (found);

    	// Check if all processes have finished
    
	 	for(int i = 0; i < n; i++)
	 	{
	 		if(!finish[i])
			{
				isSafe = false;
			}
			else
			{
				isSafe = true;
			}
	 	}
			
		
		// Display output 
		System.out.println("Number of Processes : " + n);
		System.out.println("Number of Resources : " + m + "\n");
		
		System.out.println("Claim Matrix : ");
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < m; j++)
			{
				System.out.print(claimMatrix[i][j] + " ");
			}
			
			System.out.println();
		}
		
		System.out.println("\nAllocation Matrix : ");
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < m; j++)
			{
				System.out.print(allocationMatrix[i][j] + " ");
			}
			
			System.out.println();
			
		}
		
		System.out.println("\nResource Vector : ");
		for(int i = 0; i < m; i++)
		{
			System.out.print(resourceVector[i] + " " );
			
		}
		
		System.out.println();
		System.out.println("\nNeed Matrix : ");
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < m; j++)
			{
				System.out.print(needMatrix[i][j] + " ");
			}
			
			System.out.println();
		}
		
		System.out.println();
		System.out.println("Initial Available Vector : ");
		for(int j = 0; j < m; j++)
		{	
			System.out.print(availableVector[j] + " ");
			
		}
		
		System.out.println("\n");

		if(isSafe)
		{
			System.out.print("Process Sequence : ");
			for(int i = 0; i < processSequence.length; i++)
			{
				System.out.print((processSequence[i]+1) + " ");
			}
			
			System.out.println();

			System.out.println("This system is in a safe state!!!");
		}
		else
		{
			System.out.println("This system is not in a safe state!!!");
		}
		
	
	}
	
}

