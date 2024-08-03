import java.io.*;

public class VMTranslator {
    
    public Parser parser;
    public CodeWriter code;
    
    // take a filename, create parser and
    // translate each vm commands into asm
    private void parse(File sourceFile) {
        // construct a parser with the file
        parser = new Parser(sourceFile);
        
        // iterate through each command
        while (parser.hasMoreCommands()) {
            //String command = parser.advance();
            
            String ctype = parser.commandType();
            
            if (ctype.equals("C_ARITHMETIC")) {
                code.writeArithmetic(parser.arg1());
            }
            else if (ctype.equals("C_PUSH") || ctype.equals("C_POP")) {
                code.writePushPop(ctype, parser.arg1(), parser.arg2());
            }
        }
    }
    
    public static void main(String[] args) {
        
        File path = new File(args[0]);
        
        VMTranslator translator = new VMTranslator();
        
        //Takes a file and uses Parser to parse
        //opens a new Codewriter with the outputfile
            translator.code = new CodeWriter(args[0]);
            translator.code.setFileName(path.getPath());
            translator.parse(path);  
            translator.code.close();
        }    
    }
