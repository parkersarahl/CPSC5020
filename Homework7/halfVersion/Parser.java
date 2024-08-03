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
    

    // called if hasMoreCommands() returns true
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
        else {
            type = "C_ARITHMETIC";
        }
        return type;
    }
    
    // returns first argument of the command
    public String arg1() {
        if (type.equals("C_ARITHMETIC")) {
            return command;
        }
        else {
            return command.split(" ")[1];
        }
    }
    
    // Returns second argument of the command
    public String arg2() {
        return command.split(" ")[2];
    }
}