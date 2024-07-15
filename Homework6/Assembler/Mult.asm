// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: CPSC5020/homework4/Sarah_Parker/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
// The algorithm is based on repetitive addition.

// sets RAM[2] to zero
  @R2
  M=0
(LOOP)
  //sets D to R1
  @R1
  D=M
  //goto END if R1 is equal to 0
  @END
  D;JEQ

  //sets D to R0
  @R0
  D=M
  //Adds R0 to R2
  @R2
  M=D+M
  //decrements R1 by 1
  @R1
  M=M-1
  //jumps to beginning of LOOP
  @LOOP
  0;JMP

(END)  
  @END
  0;JMP

  



