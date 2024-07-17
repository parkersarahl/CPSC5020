import java.util.Hashtable;

public class Code {
	private Hashtable<String, String> destinationInstruction;
	private Hashtable<String, String> compInstruction;
	private Hashtable<String, String> jumpInstruction;
	
	public Code() {
		this.jumpInstruction = new Hashtable<String, String>();
		this.createJump();
		this.compInstruction = new Hashtable<String, String>();
		this.createComp();
		this.destinationInstruction = new Hashtable<String, String>();
		this.createDest();
	}
	
	private void createJump() {
		this.jumpInstruction.put("NULL", "000");
		this.jumpInstruction.put("JGT", "001");
		this.jumpInstruction.put("JEQ", "010");
		this.jumpInstruction.put("JGE", "011");
		this.jumpInstruction.put("JLT", "100");
		this.jumpInstruction.put("JNE", "101");
		this.jumpInstruction.put("JLE", "110");
		this.jumpInstruction.put("JMP", "111");
	}
	
	private void createComp() {
		this.compInstruction.put("0", "0101010");
		this.compInstruction.put("1", "0111111");
		this.compInstruction.put("-1", "0111010");
		this.compInstruction.put("D", "0001100");
		this.compInstruction.put("A", "0110000");
		this.compInstruction.put("M", "1110000");
		this.compInstruction.put("!D", "0001101");
		this.compInstruction.put("!A", "0110001");
		this.compInstruction.put("!M", "1110001");
		this.compInstruction.put("-D", "0001111");
		this.compInstruction.put("-A", "0110011");
		this.compInstruction.put("-M", "1110011");
		this.compInstruction.put("D+1", "0011111");
		this.compInstruction.put("A+1", "0110111");
		this.compInstruction.put("M+1", "1110111");
		this.compInstruction.put("D-1", "0001110");
		this.compInstruction.put("A-1", "0110010");
		this.compInstruction.put("M-1", "1110010");
		this.compInstruction.put("D+A", "0000010");
		this.compInstruction.put("D+M", "1000010");
		this.compInstruction.put("D-A", "0010011");
		this.compInstruction.put("D-M", "1010011");
		this.compInstruction.put("A-D", "0000111");
		this.compInstruction.put("M-D", "1000111");
		this.compInstruction.put("D&A", "0000000");
		this.compInstruction.put("D&M", "1000000");
		this.compInstruction.put("D|A", "0010101");
		this.compInstruction.put("D|M", "1010101");
	}
	
	private void createDest() {
		this.destinationInstruction.put("NULL", "000");
		this.destinationInstruction.put("M", "001");
		this.destinationInstruction.put("D", "010");
		this.destinationInstruction.put("MD", "011");
		this.destinationInstruction.put("A", "100");
		this.destinationInstruction.put("AM", "101");
		this.destinationInstruction.put("AD", "110");
		this.destinationInstruction.put("AMD", "111");
	}
	
	//  Returns binary code of the dest instruction.
	public String dest(String instruction) {
		if (instruction == null || instruction.isEmpty()) {
			instruction = "NULL";
		}
		
		return this.destinationInstruction.get(instruction);
	}
	
	//  Returns the binary code of the comp instruction.
	public String comp(String instruction) {
		return this.compInstruction.get(instruction);
	}
	
	//  Returns the binary code of the jump instruction.
	public String jump(String instruction) {
		if (instruction == null || instruction.isEmpty()) {
			instruction = "NULL";
		}
		
		return this.jumpInstruction.get(instruction);
	}
	
	// Format a number as a 15-bit, 0-padded binary number.
	public String createBinary(String number) {
		int value = Integer.parseInt(number);
		String binaryNumber = Integer.toBinaryString(value);
		String formattedBinaryNumber =
				String.format("%15s", binaryNumber).replace(' ', '0');
		return formattedBinaryNumber;
	}
}
