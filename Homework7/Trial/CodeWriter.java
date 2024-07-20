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

    // write the assembly code for given
    // arithmetic command
    public void writeArithmetic (String arth) {
        try {
            // adds comment to outputfile
            writeComment(arth);
            
            // used for every arithmetic command
            output.write("@SP");
            output.newLine();
            output.write("M=M-1");
            output.newLine();
            output.write("A=M");
            output.newLine();
            output.write("D=M");
            output.newLine();

            //pulls two numbers from stack
            if (!(arth.equals("not") || arth.equals("neg"))) {
                output.write("@SP");
                output.newLine();
                output.write("M=M-1");
                output.newLine();
                output.write("A=M");
                output.newLine();
            }


            // different arth specific function commands
            if (arth.equals("add")) {
                output.write("M=D+M");
                output.newLine();
            }
            else if (arth.equals("sub")) {
                output.write("M=M-D");
                output.newLine();
            }
            else if (arth.equals("and")) {
                output.write("M=D&M");
                output.newLine();
            }
            else if (arth.equals("or")) {
                output.write("M=D|M");
                output.newLine();
            }
            else if (arth.equals("neg")) {
                output.write("M=-D");
                output.newLine();
            }
            else if (arth.equals("not")) {
                output.write("M=!D");
                output.newLine();
            }
            // these operations involve labels for if conditionals
            else if (arth.equals("eq") || arth.equals("gt") || arth.equals("lt")) {
                // D = y - x
                output.write("D=D-M");
                output.newLine();
                // A-instruction referencing a label
                // i used to create a new Label with every new if conditional
                output.write("@IF" + Integer.toString(i));
                output.newLine();

                // C-instruction with different jump statements
                // if 'D == 0' goto IF_i
                if (arth.equals("eq")) {
                    output.write("D;JEQ");
                    output.newLine();
                }
                else if (arth.equals("gt")) {
                    output.write("D;JLT");
                    output.newLine();
                }
                else if (arth.equals("lt")) {
                    output.write("D;JGT");
                    output.newLine();
                }

                // similar part of the assembly code for the commands
                // 'else false' part
                output.write("@SP");
                output.newLine();
                output.write("A=M");
                output.newLine();
                output.write("M=0");
                output.newLine();

                // jump to the end of conditional after executing
                // one part of the conditional
                output.write("@END" + Integer.toString(i));
                output.newLine();
                output.write("0;JMP");
                output.newLine();

                // 'true' part
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

            // SP++, similar for all commands
            output.write("@SP");
            output.newLine();
            output.write("M=M+1");
            output.newLine();

            // Increment i for the different conditional statements
            i++;

        }
        catch (IOException e) {
            System.out.println(e);
        }

    }

    // write the translation of push/pop command
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
        
        
        String seg = "";

        // store the appropriate segment
        // @segment
        if (arg1.equals("argument")) {
            seg = "ARG";
        }
        else if (arg1.equals("local")) {
            seg = "LCL";
        }
        else if (arg1.equals("this")) {
            seg = "THIS";
        }
        else if (arg1.equals("that")) {
            seg = "THAT";
        }
        else if (arg1.equals("temp")) {
            seg = "5";
        }

        try {
            // create assembly code for pseudocode
            // addr = LCL + i
            // !!! LCL and like invoked in what way???
            if (!(arg1.equals("constant") || arg1.equals("static") || arg1.equals("pointer"))) {

                // @LCL
                output.write("@" + seg);
                output.newLine();

                if (arg1.equals("temp")) {
                    // D = A
                    output.write("D=A");
                    output.newLine();
                }
                else {
                    // D = M
                    output.write("D=M");
                    output.newLine();
                }
                // @i
                output.write("@" + index);
                output.newLine();
                // D = D + A
                output.write("D=D+A");
                output.newLine();
                // @addr
                output.write("@addr");
                output.newLine();
                // M = D
                output.write("M=D");
                output.newLine();
            }

            // Pop command
            // SP--
            // *addr = *SP
            if (type.equals("C_POP")) {
                //@ SP--
                output.write("@SP");
                output.newLine();
                output.write("M=M-1");
                output.newLine();

                // *addr = *SP
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
            // *SP = *addr
            // SP++
            else {
                //eg: A = M or @Foo.5 or @THIS
                if (arg1.equals("pointer")) {
                    if (index.equals("0")) {
                        output.write("@THIS");
                    }
                    else {
                        output.write("@THAT");
                    }
                    output.newLine();

                    // D = M
                    output.write("D=M");
                    output.newLine();
                }
                else if (arg1.equals("static")) {
                    output.write("@" + file + "." + index);
                    output.newLine();

                    // D = M
                    output.write("D=M");
                    output.newLine();
                }
                else if (arg1.equals("constant")) {
                    output.write("@" + index);
                    output.newLine();

                    // D = A
                    output.write("D=A");
                    output.newLine();
                }
                else {
                    output.write("A=M");
                    output.newLine();

                    // D = M
                    output.write("D=M");
                    output.newLine();
                }

                // @SP
                output.write("@SP");
                output.newLine();

                // A = M
                output.write("A=M");
                output.newLine();

                // M = D
                output.write("M=D");
                output.newLine();

                // SP++
                // @SP
                output.write("@SP");
                output.newLine();

                // M = M + 1
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
    // Write Assembly code for the Label command
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

    // Write Assembly code for the goto command
    public void writeGoto(String label) {
        try {
            writeComment("goto " + label);
            
            // goto LABEL
            output.write("@" + inFunction + "$" + label.toUpperCase());
            output.newLine();
            output.write("0;JMP");
            output.newLine();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    // Write Assembly code for the if-goto command
    public void writeIf(String label) {
        try {
            writeComment("if-goto " + label);
            
            // pop top-most value from stack
            output.write("@SP");
            output.newLine();
            output.write("M=M-1");
            output.newLine();
            output.write("A=M");
            output.newLine();
            output.write("D=M");
            output.newLine();

            // jump to the label, if the value is not equal to 0
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
            
            
            // goto funcName
            output.write("@" + funcName.toUpperCase());
            output.newLine();
            output.write("0;JMP");
            output.newLine();
            
            
            // (returnAddress)
            output.write("(returnAddress" + i + ")");
            output.newLine();
            
            // increment i for non-duplicate labels
            i++;
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    
    // Write assembly code for pushing value in D-register to Stack
    private void pushToStack() {
        try {
            // push on Stack
            output.write("@SP");
            output.newLine();
            output.write("A=M");
            output.newLine();
            output.write("M=D");
            output.newLine();
            // SP++
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
            
            // endFrame = LCL
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
    private void endFrameMinus(String seg, String n) {
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
            output.write("@" + seg);
            output.newLine();
            output.write("M=D");
            output.newLine();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    // Write Assembly code for the function command
    public void writeFunction(String funcName, String nLocals) {
        // set inFunction to funcName
        inFunction = funcName.toUpperCase();
        
        try {
            writeComment("function " + funcName + " " + nLocals);
            
            // (funcName)
            output.write("(" + funcName.toUpperCase() + ")");
            output.newLine();
            
            
            // repeat nLocals times : push 0
            // n = nLocals
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
            
            // push 0
            writePushPop("C_PUSH", "constant", "0");
            
            // n--
            output.write("@n");
            output.newLine();
            output.write("M=M-1");
            output.newLine();
            
            // goto LOOP
            writeGoto("LOOP");
            
            // (END_LOOPi)
            writeLabel("END_LOOP");
            
            i++;
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    
    // Write one line comments with string provided
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