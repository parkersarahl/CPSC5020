/**Handles the parsing of a single .vm file, and 
 * encapsulates access to the input code. 
 * It reads VM commands, parses them, and
 * provides convenient access to their components
 * 
 **/

import java.io.*;

public class Parser {
    
    public String command;
    public String type;
    public BufferedReader in;
    
    // Opens the file and get ready to parse it
    public Parser(File inFile) {
        try {
            in = new BufferedReader(new FileReader(inFile));
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    
    // are there more commands in the file?
    public boolean hasMoreCommands() {
        try {
            while ((command = in.readLine()) != null) {
                // skip empty lines
                if (command.isEmpty()) {
                    continue;
                }
                type = command.trim().split(" ")[0];
                
                
                // check for one line and multi line comments and skip past them
                command = command.trim().toLowerCase();
                
                // Single line comments
                if (command.charAt(0) == '/' && command.charAt(1) == '/') {
                    continue;
                }
                
                // Multi line comments
                else if (command.charAt(0) == '/' && command.charAt(1) == '*') {
                    while ((command = in.readLine()) != null) {
                        int len = command.length();
                        if (command.charAt(len - 1) == '/' && command.charAt(len - 2) == '*') {
                            break;
                        }
                        continue;
                    }
                    continue;
                }
                
                
                // trim end of line comments away
                if (command.contains("//")) {
                    command = command.split("//")[0].trim();
                }
                return true;
            }
            
            // if there's no more commands in the file
            in.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
        return false;
    }
    
    // raed the next command form input
    // and makes it current command
    // called if hasMoreCommands() return true
    public String advance() {
        return command;
    }
    
    /** return the type of VM command
     * 
     * C_ARITHMETIC, C_PUSH, C_POP, C_LABEL, C_GOTO,
     * C_IF, C_FUNCTION, C_RETURN, C_CALL
     * 
     **/    
    public String commandType() {
        if (type.equals("push")) {
            type = "C_PUSH";
        }
        else if (type.equals("pop")) {
            type = "C_POP";
        }
        else if (type.equals("label")) {
            type = "C_LABEL";
        }
        else if (type.equals("goto")) {
            type = "C_GOTO";
        }
        else if (type.equals("if-goto")) {
            type = "C_IF";
        }
        else if (type.equals("function")) {
            type = "C_FUNCTION";
        }
        else if (type.equals("return")) {
            type = "C_RETURN";
        }
        else if (type.equals("call")) {
            type = "C_CALL";
        }
        else {
            type = "C_ARITHMETIC";
        }
        return type;
    }
    
    /** returns first argument of the command
     * 
     * if command type is C_ARITHMETIC return command itself
     * Should not be called if command is C_RETURN
     * 
     **/
    public String arg1() {
        if (type.equals("C_ARITHMETIC")) {
            return command;
        }
        else {
            return command.split(" ")[1];
        }
    }
    
    /** Returns second argument of the command
     * 
     * Should be called only if current command is
     * C_PUSH, C_POP, C_FUNCTION, C_CALL
     * 
     **/
    public String arg2() {
        return command.split(" ")[2];
    }
}