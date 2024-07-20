import java.io.File;
import java.io.FileNotFoundException;

public class VMTranslator {
    public static void main(String[] args) {
        if(args.length > 0) {
            try {
                
                File sourceFile = new File(args[0]);

                //Check to see if file is valid
                if (sourceFile.isFile()) {
                    String fileName = sourceFile.getName();
                    int fileNameExtensionIndex = fileName.lastIndexOf(".");
                    
                    //Write output file
                    String fileNameNoExtension = fileName.substring(0, fileNameExtensionIndex);
                    String outputFilePath =  fileNameNoExtension + ".asm";
                    File outputFile = new File(outputFilePath);
                    

                    //Create CodeWriter Object and initialize
                    CodeWriter cw = new CodeWriter(outputFile); 
                    String name = sourceFile.getName();
                    name = name.substring(0, name.indexOf('.'));
                    cw.setFileName(name);

                    Parser p = new Parser(sourceFile);
                    while(true) {
                        if(p.commandType() == 0) {
                            System.out.println(sourceFile + " contains an invalid instruction.");
                            return;
                            }

                        if(p.commandType() == Parser.C_ARITHMETIC) {
                            cw.writeArithmetic(p.arg1());
                        } else if(p.commandType() == Parser.C_PUSH || p.commandType() == Parser.C_POP) {
                            cw.writePushPop(p.commandType(), p.arg1(), p.arg2());
                        } else if(p.commandType() == Parser.C_LABEL) {
                            cw.writeLabel(p.arg1());
                        } else if(p.commandType() == Parser.C_GOTO) {                                
                            cw.writeGoto(p.arg1());
                        } else if(p.commandType() == Parser.C_IF) {
                            cw.writeIf(p.arg1());
                        } else if(p.commandType() == Parser.C_FUNCTION) {
                            cw.writeFunction(p.arg1(), p.arg2());
                        } else if(p.commandType() == Parser.C_CALL) {
                            cw.writeCall(p.arg1(), p.arg2());
                        } else if(p.commandType() == Parser.C_RETURN) {
                            cw.writeReturn();
                        }
                        if(p.hasMoreCommands()) {
                            p.advance();
                            } else break;
                        }
                     cw.close();
                    }
                  {
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("No source entered.");
        }
    }
}
    


    