import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

public class Assembler {
	private File assemblyCode;
	private BufferedWriter machineCode;
	private Code coder;
	private SymbolTable symbolTable;
	
	public Assembler(File source, File target) throws IOException {
		this.assemblyCode = source;
		
		// Create buffered writer.
		FileWriter fw = new FileWriter(target);
		this.machineCode = new BufferedWriter(fw);
		
		// Initialize assembler components.
		this.coder = new Code();
		this.symbolTable = new SymbolTable();
	}
	
	// Translate assembly file to machine language.
	public void translate() throws IOException {
		this.recordLabelAddress();
		this.parse();
	}
	
	private void recordLabelAddress() throws IOException {
		Parser parser = new Parser(this.assemblyCode);
		while (parser.hasMoreCommands()) {
			parser.advance();
			
			String commandType = parser.commandType();
			
			
			if (commandType.equals(Parser.L_COMMAND)) {
				String symbol = parser.symbol();
				int address = this.symbolTable.getProgramAddress();
				this.symbolTable.addEntry(symbol, address);
			} else {
				this.symbolTable.incrementProgramAddress();
			}
		}
		parser.close();
	}
		
	// Parse source file.
	private void parse() throws IOException {
		Parser parser = new Parser(this.assemblyCode);
		while (parser.hasMoreCommands()) {
			parser.advance();
	
			String commandType = parser.commandType();
			String instruction = null;
			
			if (commandType.equals(Parser.A_COMMAND)) {
				// Format A-Instruction.
				String symbol = parser.symbol();
				Character firstCharacter = symbol.charAt(0);
				boolean isSymbol = (!Character.isDigit(firstCharacter));
				
				String address = null;
				if (isSymbol) {
					boolean symbolExists = this.symbolTable.contains(symbol);
					
					// Record address if symbol does not exist (variable).
					if (!symbolExists) {
						int dataAddress = this.symbolTable.getDataAddress();
						this.symbolTable.addEntry(symbol, dataAddress);
						this.symbolTable.incrementDataAddress();
					}
						
					// Get address  of symbol.
					address = Integer.toString(
							this.symbolTable.getAddress(symbol));
				} else {
					address = symbol;
				}
				
				instruction = this.formatAInstruction(address);
			} else if (commandType.equals(Parser.C_COMMAND)) {
				// Format C-Instruction
				String comp = parser.comp();
				String dest = parser.dest();
				String jump = parser.jump();
				instruction = this.formatCInstruction(comp, dest, jump);
			}
	
			if (!commandType.equals(Parser.L_COMMAND)) {
				// Write binary instruction to file.
				this.machineCode.write(instruction);
				this.machineCode.newLine();
			}
		}
		
		// Release resources.
		parser.close();
		this.machineCode.flush();
		this.machineCode.close();
	}

	// Machine-format an A-Instruction.
	private String formatAInstruction(String address) {
		String formattedNumber = this.coder.createBinary(address);
		return "0" + formattedNumber;
	}
	
	// Machine-format a C-Instruction.
	private String formatCInstruction( String comp, String dest, String jump) {
		StringWriter instruction = new StringWriter();
		instruction.append("111");
		instruction.append(this.coder.comp(comp));
		instruction.append(this.coder.dest(dest));
		instruction.append(this.coder.jump(jump));
		return instruction.toString();
	}
}
