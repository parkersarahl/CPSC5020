
import java.io.File;
import java.io.IOException;

public class Program {
	public static void main(String[] args) {
	
		File sourceFile = new File(args[0].trim());
	
		// Get output file.
		//String sourceAbsolutePath = sourceFile.getAbsolutePath();
		String fileName = sourceFile.getName();
		int fileNameExtensionIndex = fileName.lastIndexOf(".");
		
		String fileNameNoExtension = fileName.substring(0, fileNameExtensionIndex);
		
		String outputFilePath =  fileNameNoExtension + ".hack";
		
		File outputFile = new File(outputFilePath);
		
		try
		{
			outputFile.createNewFile();

			// Translate source file.
			Assembler assembler = new Assembler(sourceFile, outputFile);
			assembler.translate();
		
				
		} catch (IOException e) 
		{
			// Exit with error if I/O error occurs.
			System.err.println("An unknown I/O error occurred.");
			System.exit(3);
		}
	}
}

