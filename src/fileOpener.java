
import java.util.Scanner;
import java.io.IOException;

public class fileOpener{
	public static void main(String[] args) throws IOException{
		java.io.File outFile = new java.io.File("afterProcess.c");
		java.io.PrintStream output = new java.io.PrintStream(outFile);

		java.io.File file = new java.io.File(
			"/Users/rayyeon/Documents/IdeaProjects/PPLAssignment3/preproceeFile.c");
		Scanner input = new Scanner(file);

		while(input.hasNext()){
			String line = input.nextLine();
			if(line.length() != 0){

			}
			output.println(line);

		}
	}
}