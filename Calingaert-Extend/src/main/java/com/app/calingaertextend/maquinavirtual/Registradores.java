package com.app.calingaertextend.maquinavirtual;

public class Registradores {
	
	private int PC; // program counter
	private int SP; // stack pointer
	private int ACC; // acumulador
	private int MOP; // modo de operacao
	private int RI; // registrador de instrucao
	private int RE; // registrador de endereco de memoria
	private int R0; // registrador de proposito geral 1
	private int R1; // registrador de proposito geral 2
	
	public Registradores(){
		PC = 0;
		SP = 0;
		ACC = 0;
		MOP = 0;
		RI = 0;
		RE = 0;
		R0 = 0;
		R1 = 0;
	}

	public int getPC() {
		return PC;
	}

	public void setPC(int pC){
		PC = pC;
	}

	public int getSP(){
		return SP;
	}

	public void setSP(int sP){
		SP = sP;
	}

	public int getACC(){
		return ACC;
	}

	public void setACC(int aCC){
		ACC = aCC;
	}

	public int getMOP(){
		return MOP;
	}

	public void setMOP(int mOP){
		MOP = mOP;
	}

	public int getRI(){
		return RI;
	}

	public void setRI(int rI){
		RI = rI;
	}

	public int getRE(){
		return RE;
	}

	public void setRE(int rE){
		RE = rE;
	}

	public int getR0() {
		return R0;
	}

	public void setR0(int r0){
		R0 = r0;
	}

	public int getR1() {
		return R1;
	}

	public void setR1(int r1){
		R1 = r1;
	}
}