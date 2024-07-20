import java.io.*;

public class CodeWriter {

    public BufferedWriter output;
    public String file;
    private int i = 1;
    private String inFunction = "";

    //Constructor for Codewriter
    public CodeWriter (String outFile) {
        if (outFile.contains(".vm")) {
            outFile = outFile.split(".vm")[0];
        }
        setFileName(outFile);

        outFile = outFile + ".asm";

        try {
            output = new BufferedWriter(new FileWriter(new File(outFile)));
            }
        
        catch (IOException e) {
            System.out.println(e);
        }
    }

    //used in constructor to set output filename
    public void setFileName (String fileName) {
        if (fileName.contains("\\")) {
            fileName = fileName.substring(fileName.lastIndexOf("\\")+1,fileName.length());
        }
        if (fileName.contains(".vm")) {
            fileName = fileName.split(".vm")[0];
        }
        file = fileName;
    }

    // Writes translation for specified arithmetic command
    public void writeArithmetic (String arithmetic) {
        try {
            // adds comment to outputfile
            writeComment(arithmetic);
            
            // used for every arithmetic command
            output.write("@SP");
            output.newLine();
            output.write("M=M-1");
            output.newLine();
            output.write("A=M");
            output.newLine();
            output.write("D=M");
            output.newLine();

            if (!(arithmetic.equals("not") || arithmetic.equals("neg"))) {
                output.write("@SP");
                output.newLine();
                output.write("M=M-1");
                output.newLine();
                output.write("A=M");
                output.newLine();
            }
            
            if (arithmetic.equals("add")) {
                output.write("M=D+M");
                output.newLine();
            }
            else if (arithmetic.equals("sub")) {
                output.write("M=M-D");
                output.newLine();
            }
            else if (arithmetic.equals("and")) {
                output.write("M=D&M");
                output.newLine();
            }
            else if (arithmetic.equals("or")) {
                output.write("M=D|M");
                output.newLine();
            }
            else if (arithmetic.equals("neg")) {
                output.write("M=-D");
                output.newLine();
            }
            else if (arithmetic.equals("not")) {
                output.write("M=!D");
                output.newLine();
            }

            // Conditionals
            else if (arithmetic.equals("eq") || arithmetic.equals("gt") || arithmetic.equals("lt")) {
                output.write("D=D-M");
                output.newLine();
                output.write("@IF" + Integer.toString(i));
                output.newLine();

                // Jump statements
                if (arithmetic.equals("eq")) {
                    output.write("D;JEQ");
                    output.newLine();
                }
                else if (arithmetic.equals("gt")) {
                    output.write("D;JLT");
                    output.newLine();
                }
                else if (arithmetic.equals("lt")) {
                    output.write("D;JGT");
                    output.newLine();
                }

                //"else == false"
                output.write("@SP");
                output.newLine();
                output.write("A=M");
                output.newLine();
                output.write("M=0");
                output.newLine();

                // jump after executing part of the conditional
                output.write("@END" + Integer.toString(i));
                output.newLine();
                output.write("0;JMP");
                output.newLine();

                // "true"
                output.write("(IF" + Integer.toString(i) + ")");
                output.newLine();
                output.write("@SP");
                output.newLine();
                output.write("A=M");
                output.newLine();
                output.write("M=-1");
                output.newLine();
                output.write("(END" + Integer.toString(i) + ")");
                output.newLine();
            }

            // SP++
            output.write("@SP");
            output.newLine();
            output.write("M=M+1");
            output.newLine();
            i++;

        }
        catch (IOException e) {
            System.out.println(e);
        }

    }

    // Writes the translation of push/pop command
    public void writePushPop (String type, String arg1, String index) {
        // add command as a comment
        String com;
        if (type.equals("C_POP")) {
            com = "pop";
        }
        else {
            com = "push";
        }
        writeComment(com + " " + arg1 + " " + index);
        
        
        String segment = "";

        // sets segment
        if (arg1.equals("argument")) {
            segment = "ARG";
        }
        else if (arg1.equals("local")) {
            segment = "LCL";
        }
        else if (arg1.equals("this")) {
            segment = "THIS";
        }
        else if (arg1.equals("that")) {
            segment = "THAT";
        }
        else if (arg1.equals("temp")) {
            segment = "5";
        }

        try {
            // Translate for addresses
            // addr = LCL + i
            if (!(arg1.equals("constant") || arg1.equals("static") || arg1.equals("pointer"))) {

                // @LCL
                output.write("@" + segment);
                output.newLine();

                if (arg1.equals("temp")) {
                    output.write("D=A");
                    output.newLine();
                }
                else {
                    output.write("D=M");
                    output.newLine();
                }
                
                output.write("@" + index);
                output.newLine();
                output.write("D=D+A");
                output.newLine();
                output.write("@addr");
                output.newLine();
                output.write("M=D");
                output.newLine();
            }

            // Pop Command
            if (type.equals("C_POP")) {
            
                output.write("@SP");
                output.newLine();
                output.write("M=M-1");
                output.newLine();
                output.write("A=M");
                output.newLine();
                output.write("D=M");
                output.newLine();

                if (arg1.equals("pointer")) {
                    if (index.equals("0")) {
                        output.write("@THIS");
                    }
                    else {
                        output.write("@THAT");
                    }
                    output.newLine();
                }
                else if (arg1.equals("static")) {
                    output.write("@" + file + "." + index);
                    output.newLine();
                }
                else {
                    output.write("@addr");
                    output.newLine();
                    output.write("A=M");
                    output.newLine();
                }

                output.write("M=D");
                output.newLine();
            }

            // Push command
            else {
                if (arg1.equals("pointer")) {
                    if (index.equals("0")) {
                        output.write("@THIS");
                    }
                    else {
                        output.write("@THAT");
                    }
                    output.newLine();
                    output.write("D=M");
                    output.newLine();
                }
                else if (arg1.equals("static")) {
                    output.write("@" + file + "." + index);
                    output.newLine();
                    output.write("D=M");
                    output.newLine();
                }
                else if (arg1.equals("constant")) {
                    output.write("@" + index);
                    output.newLine();
                    output.write("D=A");
                    output.newLine();
                }
                else {
                    output.write("A=M");
                    output.newLine();
                    output.write("D=M");
                    output.newLine();
                }

                output.write("@SP");
                output.newLine();
                output.write("A=M");
                output.newLine();
                output.write("M=D");
                output.newLine();
                output.write("@SP");
                output.newLine();
                output.write("M=M+1");
                output.newLine();
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    // close the output file
    public void close () {
        try {
            output.close();
        }
        catch (IOException e){
            System.out.println(e);
        }
    }
    
    // Writes the translation for label command
    public void writeLabel(String label) {
        try {
            writeComment("label " + label);
            
            // Label LABEL
            output.write("(" + inFunction + "$" + label.toUpperCase() + ")");
            output.newLine();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    // Writes the translation for GoTo
    public void writeGoto(String label) {
        try {
            writeComment("goto " + label);
            
            output.write("@" + inFunction + "$" + label.toUpperCase());
            output.newLine();
            output.write("0;JMP");
            output.newLine();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    // Writes the translation for If-GoTo command
    public void writeIf(String label) {
        try {
            writeComment("if-goto " + label);
            
            output.write("@SP");
            output.newLine();
            output.write("M=M-1");
            output.newLine();
            output.write("A=M");
            output.newLine();
            output.write("D=M");
            output.newLine();

            output.write("@" + inFunction + "$" + label.toUpperCase());
            output.newLine();
            output.write("D;JNE");
            output.newLine();

        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    // Write Assembly code for the call command
    /**Saves the caller's frame
     * Set ARG
     * Jumps to execute the function
     **/
    public void writeCall(String funcName, String nArgs) {
        try {
            writeComment("call " + funcName + " " + nArgs);
            
            // Save caller's frame
            // push return address
            output.write("@returnAddress" + i);
            output.newLine();
            output.write("D=A");
            output.newLine();
            // push on Stack
            pushToStack();
            
            // push LCL
            output.write("@LCL");
            output.newLine();
            output.write("D=M");
            output.newLine();
            pushToStack();
            
            // push ARG
            output.write("@ARG");
            output.newLine();
            output.write("D=M");
            output.newLine();
            pushToStack();
            
            // push THIS
            output.write("@THIS");
            output.newLine();
            output.write("D=M");
            output.newLine();
            pushToStack();
            
            // push THAT
            output.write("@THAT");
            output.newLine();
            output.write("D=M");
            output.newLine();
            pushToStack();
            
            
            // Update value of ARG and LCL
            // ARG = SP - 5 - nArgs
            output.write("@SP");
            output.newLine();
            output.write("D=M");
            output.newLine();
            output.write("@5");
            output.newLine();
            output.write("D=D-A");
            output.newLine();
            output.write("@" + nArgs);
            output.newLine();
            output.write("D=D-A");
            output.newLine();
            output.write("@ARG");
            output.newLine();
            output.write("M=D");
            output.newLine();
            
            // LCL = SP
            output.write("@SP");
            output.newLine();
            output.write("D=M");
            output.newLine();
            output.write("@LCL");
            output.newLine();
            output.write("M=D");
            output.newLine();
            
            output.write("@" + funcName.toUpperCase());
            output.newLine();
            output.write("0;JMP");
            output.newLine();

            output.write("(returnAddress" + i + ")");
            output.newLine();
            
            i++;
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    
    // Writes code for pushing value in D-register to Stack
    private void pushToStack() {
        try {
            output.write("@SP");
            output.newLine();
            output.write("A=M");
            output.newLine();
            output.write("M=D");
            output.newLine();
            output.write("@SP");
            output.newLine();
            output.write("M=M+1");
            output.newLine();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    // Write Assembly code for the return command
    public void writeReturn() {
        try {
            writeComment("return");
            
            output.write("@LCL");
            output.newLine();
            output.write("D=M");
            output.newLine();
            output.write("@endFrame");
            output.newLine();
            output.write("M=D");
            output.newLine();
            
            // retAddr = *(endFrame - 5)
            // D-register still have endFrame value
            endFrameMinus("retAddr", "5");
            
            // *ARG = pop()
            writePushPop("C_POP", "argument", "0");
            
            // SP = ARG + 1
            output.write("@ARG");
            output.newLine();
            output.write("D=M");
            output.newLine();
            output.write("D=D+1");
            output.newLine();
            output.write("@SP");
            output.newLine();
            output.write("M=D");
            output.newLine();
            
            // THAT = *(endFrame - 1)
            endFrameMinus("THAT", "1");
            
            // THIS = *(endFrame - 2)
            endFrameMinus("THIS", "2");
            
            // ARG = *(endFrame - 3)
            endFrameMinus("ARG", "3");
            
            // LCL = *(endFrame - 4)
            endFrameMinus("LCL", "4");
            
            // goto retAddr
            output.write("@retAddr");
            output.newLine();
            output.write("A=M");
            output.newLine();
            output.write("0;JMP");
            output.newLine();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    
    // write assembly code for seg = *(endFrame - n)
    private void endFrameMinus(String segment, String n) {
        try {
            output.write("@endFrame");
            output.newLine();
            output.write("D=M");
            output.newLine();
            output.write("@" + n);
            output.newLine();
            output.write("D=D-A");
            output.newLine();
            output.write("A=D");
            output.newLine();
            output.write("D=M");
            output.newLine();
            output.write("@" + segment);
            output.newLine();
            output.write("M=D");
            output.newLine();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    // Translates code for the function command
    public void writeFunction(String funcName, String nLocals) {
        
        inFunction = funcName.toUpperCase();
        
        try {
            writeComment("function " + funcName + " " + nLocals);
            
            // (funcName)
            output.write("(" + funcName.toUpperCase() + ")");
            output.newLine();
            
            
            // repeat nLocals times : push 0
            output.write("@" + nLocals);
            output.newLine();
            output.write("D=A");
            output.newLine();
            output.write("@n");
            output.newLine();
            output.write("M=D");
            output.newLine();
            
            // while(n > 0)
            writeLabel("LOOP");
            output.write("@n");
            output.newLine();
            output.write("D=M");
            output.newLine();
            output.write("@" + funcName.toUpperCase() + "$END_LOOP");
            output.newLine();
            output.write("D;JLE");
            output.newLine();
            
            writePushPop("C_PUSH", "constant", "0");
            
            output.write("@n");
            output.newLine();
            output.write("M=M-1");
            output.newLine();
            
            writeGoto("LOOP");
            
            writeLabel("END_LOOP");
            
            i++;
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    
    // Writes comment
    private void writeComment(String line) {
        try {
            output.write("// " + line);
            output.newLine();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}