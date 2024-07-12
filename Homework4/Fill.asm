// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: CPSC5020/homework4/Sarah_Parker/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, 
// the screen should be cleared.


(LOOP)
  //sets D to address of screen
  @SCREEN
  D=A
  //sets memory in variable pixels to 16384
  @pixels
  M=D          

  //probes the keyboard output to see if a key is pressed
  @KBD    // keyboard address
  D=M
  
  // if keyboard > 0 goto CHANGECOLOR
  @CHANGECOLOR
  D;JGT    
  
  // else sets color to white
  @color
  M=0    

  //jumps to created loop to fill screen 
  @COLOR_SCREEN
  0;JMP    
  
  //set color to black pixels
  (CHANGECOLOR)
    @color
    M=-1    

  (COLOR_SCREEN)
    //sets D register to either 0(white) or -1(black)
    @color
    D=M
    
    //address for screen and colors those pixels with @color
    @pixels
    A=M         
    M=D         
    
    //increments memory by 1 and sets D register to same number
    @pixels
    M=M+1
    D=M

    //checks to see if entire screen is filled, if not jump
    //to begining of COLOR_SCREEN to continue filling    
    @24576
    D=D-A
    @COLOR_SCREEN
    D;JLT

//end with infinite loop
@LOOP
0;JMP 