import java.io.*;

public class VMTranslator {
    
    public Parser parser;
    public CodeWriter code;
    
    // take a filename, create parser and
    // translate each vm commands in file 
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
            else if (ctype.equals("C_LABEL")) {
                code.writeLabel(parser.arg1());
            }
            else if (ctype.equals("C_GOTO")) {
                code.writeGoto(parser.arg1());
            }
            else if (ctype.equals("C_IF")) {
                code.writeIf(parser.arg1());
            }
            else if (ctype.equals("C_CALL")) {
                code.writeCall(parser.arg1(), parser.arg2());
            }
            else if (ctype.equals("C_FUNCTION")) {
                code.writeFunction(parser.arg1(), parser.arg2());
            }
            else if (ctype.equals("C_RETURN")) {
                code.writeReturn();
            }
        }
    }
    
    public static void main(String[] args) {
        File path = new File(args[0]);
        
        VMTranslator translator = new VMTranslator();
        
        // argument could be a file or directory containing multiple files
        // FilenameFilter will only accept files with .vm extension
        File[] files = path.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".vm");
            }
        });
        // if path is a directory
        if (files != null) {
            // create CodeWriter
            String outFileName = args[0];
            if (args[0].contains("\\")) {
                outFileName = args[0].substring(args[0].lastIndexOf("\\")+1,args[0].length());
            }
            translator.code = new CodeWriter(args[0] + "\\" + outFileName);
            
            for (File file : files) {
                // set the file name in CodeWriter
                translator.code.setFileName(file.getPath());
                translator.parse(file);
            }
        }
        // single file
        else {
            // create CodeWriter
            translator.code = new CodeWriter(args[0]);
            translator.code.setFileName(path.getPath());
            translator.parse(path);
        }
        
        translator.code.close();
    }  
    }