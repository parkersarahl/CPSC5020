/**output assembly code for VMTranslator.java
 *
 * take the file name to output to and get ready to write to it,
 * use writeArithmetic() and writePushPop() to translate,
 * arithmetic and push/pop commands from the VM file.
 **/

import java.io.*;

public class CodeWriter {

    public BufferedWriter out;
    public String file;
    private int i = 1;
    // for unique labels and goto statements inside a function
    private String inFunction = "";

    /** Open the output file, and
     * get ready to write
     **/
    public CodeWriter (String outFile) {
        if (outFile.contains(".vm")) {
            outFile = outFile.split(".vm")[0];
        }
        // sets file name
        setFileName(outFile);

        outFile = outFile + ".asm";

        try {
            out = new BufferedWriter(new FileWriter(new File(outFile)));

            }
        
        catch (IOException e) {
            System.out.println(e);
        }
    }

    // Informs the codeWriter that
    // the translation of a new file has started
    public void setFileName (String fileName) {
        // if a path
        if (fileName.contains("\\")) {
            fileName = fileName.substring(fileName.lastIndexOf("\\")+1,fileName.length());
        }
        // if cotains .vm at the end
        if (fileName.contains(".vm")) {
            fileName = fileName.split(".vm")[0];
        }
        
        file = fileName;
    }

    // write the assembly code for given
    // arithmetic command
    public void writeArithmetic (String arth) {
        try {
            // add the command as a comment
            writeComment(arth);
            
            // same for every arithmetic command,
            // y
            // SP--
            out.write("@SP");
            out.newLine();
            out.write("M=M-1");
            out.newLine();
            // *SP
            out.write("A=M");
            out.newLine();
            out.write("D=M");
            out.newLine();

            // uses two variables from stack to perform the arithmetic
            // x
            if (!(arth.equals("not") || arth.equals("neg"))) {
                // SP--
                out.write("@SP");
                out.newLine();
                out.write("M=M-1");
                out.newLine();
                // *SP
                out.write("A=M");
                out.newLine();
            }


            // different arth specific function commands
            if (arth.equals("add")) {
                out.write("M=D+M");
                out.newLine();
            }
            else if (arth.equals("sub")) {
                out.write("M=M-D");
                out.newLine();
            }
            else if (arth.equals("and")) {
                out.write("M=D&M");
                out.newLine();
            }
            else if (arth.equals("or")) {
                out.write("M=D|M");
                out.newLine();
            }
            else if (arth.equals("neg")) {
                out.write("M=-D");
                out.newLine();
            }
            else if (arth.equals("not")) {
                out.write("M=!D");
                out.newLine();
            }
            // these operations involve labels for if conditionals
            else if (arth.equals("eq") || arth.equals("gt") || arth.equals("lt")) {
                // D = y - x
                out.write("D=D-M");
                out.newLine();
                // A-instruction referencing a label
                // i used to create a new Label with every new if conditional
                out.write("@IF" + Integer.toString(i));
                out.newLine();

                // C-instruction with different jump statements
                // if 'D == 0' goto IF_i
                if (arth.equals("eq")) {
                    out.write("D;JEQ");
                    out.newLine();
                }
                else if (arth.equals("gt")) {
                    out.write("D;JLT");
                    out.newLine();
                }
                else if (arth.equals("lt")) {
                    out.write("D;JGT");
                    out.newLine();
                }

                // similar part of the assembly code for the commands
                // 'else false' part
                out.write("@SP");
                out.newLine();
                out.write("A=M");
                out.newLine();
                out.write("M=0");
                out.newLine();

                // jump to the end of conditional after executing
                // one part of the conditional
                out.write("@END" + Integer.toString(i));
                out.newLine();
                out.write("0;JMP");
                out.newLine();

                // 'true' part
                out.write("(IF" + Integer.toString(i) + ")");
                out.newLine();
                out.write("@SP");
                out.newLine();
                out.write("A=M");
                out.newLine();
                out.write("M=-1");
                out.newLine();
                out.write("(END" + Integer.toString(i) + ")");
                out.newLine();
            }

            // SP++, similar for all commands
            out.write("@SP");
            out.newLine();
            out.write("M=M+1");
            out.newLine();

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
                out.write("@" + seg);
                out.newLine();

                if (arg1.equals("temp")) {
                    // D = A
                    out.write("D=A");
                    out.newLine();
                }
                else {
                    // D = M
                    out.write("D=M");
                    out.newLine();
                }
                // @i
                out.write("@" + index);
                out.newLine();
                // D = D + A
                out.write("D=D+A");
                out.newLine();
                // @addr
                out.write("@addr");
                out.newLine();
                // M = D
                out.write("M=D");
                out.newLine();
            }

            // Pop command
            // SP--
            // *addr = *SP
            if (type.equals("C_POP")) {
                //@ SP--
                out.write("@SP");
                out.newLine();
                out.write("M=M-1");
                out.newLine();

                // *addr = *SP
                out.write("A=M");
                out.newLine();
                out.write("D=M");
                out.newLine();

                if (arg1.equals("pointer")) {
                    if (index.equals("0")) {
                        out.write("@THIS");
                    }
                    else {
                        out.write("@THAT");
                    }
                    out.newLine();
                }
                else if (arg1.equals("static")) {
                    out.write("@" + file + "." + index);
                    out.newLine();
                }
                else {
                    out.write("@addr");
                    out.newLine();
                    out.write("A=M");
                    out.newLine();
                }

                out.write("M=D");
                out.newLine();
            }


            // Push command
            // *SP = *addr
            // SP++
            else {
                //eg: A = M or @Foo.5 or @THIS
                if (arg1.equals("pointer")) {
                    if (index.equals("0")) {
                        out.write("@THIS");
                    }
                    else {
                        out.write("@THAT");
                    }
                    out.newLine();

                    // D = M
                    out.write("D=M");
                    out.newLine();
                }
                else if (arg1.equals("static")) {
                    out.write("@" + file + "." + index);
                    out.newLine();

                    // D = M
                    out.write("D=M");
                    out.newLine();
                }
                else if (arg1.equals("constant")) {
                    out.write("@" + index);
                    out.newLine();

                    // D = A
                    out.write("D=A");
                    out.newLine();
                }
                else {
                    out.write("A=M");
                    out.newLine();

                    // D = M
                    out.write("D=M");
                    out.newLine();
                }

                // @SP
                out.write("@SP");
                out.newLine();

                // A = M
                out.write("A=M");
                out.newLine();

                // M = D
                out.write("M=D");
                out.newLine();

                // SP++
                // @SP
                out.write("@SP");
                out.newLine();

                // M = M + 1
                out.write("M=M+1");
                out.newLine();
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }

    }

    // close the output file
    public void close () {
        try {
            out.close();
        }
        catch (IOException e){
            System.out.println(e);
        }
    }

    // Write assembly code that affects the VM initialization,
    // also called bootstrap code, must be placed at the begining
    // public void writeInit() {
    //     try {
    //         writeComment("BootStrap code");
            
    //         // SP = 256
    //         out.write("@256");
    //         out.newLine();
    //         out.write("D = A");
    //         out.newLine();
    //         out.write("@SP");
    //         out.newLine();
    //         out.write("M = D");
    //         out.newLine();

    //         // call Sys.init
    //         writeCall("Sys.init", "0");
    //     }
    //     catch (IOException e) {
    //         System.out.println(e);
    //     }
    // }

    // Write Assembly code for the Label command
    public void writeLabel(String label) {
        try {
            writeComment("label " + label);
            
            // Label LABEL
            out.write("(" + inFunction + "$" + label.toUpperCase() + ")");
            out.newLine();
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
            out.write("@" + inFunction + "$" + label.toUpperCase());
            out.newLine();
            out.write("0;JMP");
            out.newLine();
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
            out.write("@SP");
            out.newLine();
            out.write("M=M-1");
            out.newLine();
            out.write("A=M");
            out.newLine();
            out.write("D=M");
            out.newLine();

            // jump to the label, if the value is not equal to 0
            out.write("@" + inFunction + "$" + label.toUpperCase());
            out.newLine();
            out.write("D;JNE");
            out.newLine();

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
            out.write("@returnAddress" + i);
            out.newLine();
            out.write("D=A");
            out.newLine();
            // push on Stack
            pushToStack();
            
            // push LCL
            out.write("@LCL");
            out.newLine();
            out.write("D=M");
            out.newLine();
            pushToStack();
            
            // push ARG
            out.write("@ARG");
            out.newLine();
            out.write("D=M");
            out.newLine();
            pushToStack();
            
            // push THIS
            out.write("@THIS");
            out.newLine();
            out.write("D=M");
            out.newLine();
            pushToStack();
            
            // push THAT
            out.write("@THAT");
            out.newLine();
            out.write("D=M");
            out.newLine();
            pushToStack();
            
            
            // Update value of ARG and LCL
            // ARG = SP - 5 - nArgs
            out.write("@SP");
            out.newLine();
            out.write("D=M");
            out.newLine();
            out.write("@5");
            out.newLine();
            out.write("D=D-A");
            out.newLine();
            out.write("@" + nArgs);
            out.newLine();
            out.write("D=D-A");
            out.newLine();
            out.write("@ARG");
            out.newLine();
            out.write("M=D");
            out.newLine();
            
            // LCL = SP
            out.write("@SP");
            out.newLine();
            out.write("D=M");
            out.newLine();
            out.write("@LCL");
            out.newLine();
            out.write("M=D");
            out.newLine();
            
            
            // goto funcName
            out.write("@" + funcName.toUpperCase());
            out.newLine();
            out.write("0;JMP");
            out.newLine();
            
            
            // (returnAddress)
            out.write("(returnAddress" + i + ")");
            out.newLine();
            
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
            out.write("@SP");
            out.newLine();
            out.write("A=M");
            out.newLine();
            out.write("M=D");
            out.newLine();
            // SP++
            out.write("@SP");
            out.newLine();
            out.write("M=M+1");
            out.newLine();
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
            out.write("@LCL");
            out.newLine();
            out.write("D=M");
            out.newLine();
            out.write("@endFrame");
            out.newLine();
            out.write("M=D");
            out.newLine();
            
            // retAddr = *(endFrame - 5)
            // D-register still have endFrame value
            endFrameMinus("retAddr", "5");
            
            // *ARG = pop()
            writePushPop("C_POP", "argument", "0");
            
            // SP = ARG + 1
            out.write("@ARG");
            out.newLine();
            out.write("D=M");
            out.newLine();
            out.write("D=D+1");
            out.newLine();
            out.write("@SP");
            out.newLine();
            out.write("M=D");
            out.newLine();
            
            // THAT = *(endFrame - 1)
            endFrameMinus("THAT", "1");
            
            // THIS = *(endFrame - 2)
            endFrameMinus("THIS", "2");
            
            // ARG = *(endFrame - 3)
            endFrameMinus("ARG", "3");
            
            // LCL = *(endFrame - 4)
            endFrameMinus("LCL", "4");
            
            // goto retAddr
            out.write("@retAddr");
            out.newLine();
            out.write("A=M");
            out.newLine();
            out.write("0;JMP");
            out.newLine();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    
    // write assembly code for seg = *(endFrame - n)
    private void endFrameMinus(String seg, String n) {
        try {
            out.write("@endFrame");
            out.newLine();
            out.write("D=M");
            out.newLine();
            out.write("@" + n);
            out.newLine();
            out.write("D=D-A");
            out.newLine();
            out.write("A=D");
            out.newLine();
            out.write("D=M");
            out.newLine();
            out.write("@" + seg);
            out.newLine();
            out.write("M=D");
            out.newLine();
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
            out.write("(" + funcName.toUpperCase() + ")");
            out.newLine();
            
            
            // repeat nLocals times : push 0
            // n = nLocals
            out.write("@" + nLocals);
            out.newLine();
            out.write("D=A");
            out.newLine();
            out.write("@n");
            out.newLine();
            out.write("M=D");
            out.newLine();
            
            // while(n > 0)
            writeLabel("LOOP");
            out.write("@n");
            out.newLine();
            out.write("D=M");
            out.newLine();
            out.write("@" + funcName.toUpperCase() + "$END_LOOP");
            out.newLine();
            out.write("D;JLE");
            out.newLine();
            
            // push 0
            writePushPop("C_PUSH", "constant", "0");
            
            // n--
            out.write("@n");
            out.newLine();
            out.write("M=M-1");
            out.newLine();
            
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
            out.write("// " + line);
            out.newLine();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}