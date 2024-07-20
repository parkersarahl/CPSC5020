// 2constant10
@10
D=A
@SP
A=M
M=D
@SP
M=M+1
// 3local0
@SP
AM=M-1
D=M
@LCL
A=M
M=D
// 2constant21
@21
D=A
@SP
A=M
M=D
@SP
M=M+1
// 2constant22
@22
D=A
@SP
A=M
M=D
@SP
M=M+1
// 3argument2
@ARG
D=M
@2
D=D+A
@ARG
D=D+M
@R13
M=D
@SP
AM=M-1
D=M
@R13
A=M
M=D
// 3argument1
@ARG
D=M
@1
D=D+A
@ARG
D=D+M
@R13
M=D
@SP
AM=M-1
D=M
@R13
A=M
M=D
// 2constant36
@36
D=A
@SP
A=M
M=D
@SP
M=M+1
// 3this6
@ARG
D=M
@6
D=D+A
@THIS
D=D+M
@R13
M=D
@SP
AM=M-1
D=M
@R13
A=M
M=D
// 2constant42
@42
D=A
@SP
A=M
M=D
@SP
M=M+1
// 2constant45
@45
D=A
@SP
A=M
M=D
@SP
M=M+1
// 3that5
@ARG
D=M
@5
D=D+A
@THAT
D=D+M
@R13
M=D
@SP
AM=M-1
D=M
@R13
A=M
M=D
// 3that2
@ARG
D=M
@2
D=D+A
@THAT
D=D+M
@R13
M=D
@SP
AM=M-1
D=M
@R13
A=M
M=D
// 2constant510
@510
D=A
@SP
A=M
M=D
@SP
M=M+1
// 3temp6
@ARG
D=M
@6
D=D+A
@R5
D=D+A
@R13
M=D
@SP
AM=M-1
D=M
@R13
M=D
// 2local0
@LCL
A=M
D=M
@SP
A=M
M=D
@SP
M=M+1
// 2that5
@5
D=A
@THAT
A=D+M
D=M
@SP
A=M
M=D
@SP
M=M+1
// add
@SP 
AM=M-1 
D=M 
A=A-1
M=D+M
// 2argument1
@1
D=A
@ARG
A=D+M
D=M
@SP
A=M
M=D
@SP
M=M+1
// sub
@SP 
AM=M-1 
D=M 
A=A-1
M=M-D
// 2this6
@6
D=A
@THIS
A=D+M
D=M
@SP
A=M
M=D
@SP
M=M+1
// 2this6
@6
D=A
@THIS
A=D+M
D=M
@SP
A=M
M=D
@SP
M=M+1
// add
@SP 
AM=M-1 
D=M 
A=A-1
M=D+M
// sub
@SP 
AM=M-1 
D=M 
A=A-1
M=M-D
// 2temp6
@6
D=A
@R5
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1
// add
@SP 
AM=M-1 
D=M 
A=A-1
M=D+M
