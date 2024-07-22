import java.io.*;

public class Parser {
    
    public String command;
    public String type;
    public BufferedReader in;
    
    // Opens File
    public Parser(File sourceFile) {
        try {
            in = new BufferedReader(new FileReader(sourceFile));
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public boolean hasMoreCommands() {
        try {
            while ((command = in.readLine()) != null) {
                // skip empty lines
                if (command.isEmpty()) {
                    continue;
                }
                type = command.trim().split(" ")[0];
                
                
                // skips comments
                command = command.trim().toLowerCase();
                
                if (command.charAt(0) == '/' && command.charAt(1) == '/') {
                    continue;
                }
                if (command.contains("//")) {
                    command = command.split("//")[0].trim();
                }
                return true;
            }
            in.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
        return false;
    }
    
    // called if hasMoreCommands() return true
    public String advance() {
        return command;
    }
    
    // returns Command Type  
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