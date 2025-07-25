package com.app.calingaertextend.maquinavirtual;

public class Pilha{

	private int[] pilha;
	private int tamanho;

	public Pilha(int expoenteDe2){
		this.tamanho = (int)Math.pow(2, expoenteDe2);
		this.pilha = new int[this.tamanho]; // 2^12 = 4096 posicoes de memoria
		for(int i = 0; i < this.pilha.length; i++)
			this.pilha[i] = 0;
	}

	public void setPosicaoPilha (int stackPointer, int valor) throws AcessoIndevidoAMemoriaCheckedException{
		if(stackPointer < 0)
			throw new AcessoIndevidoAMemoriaCheckedException("Tentativa de acessar posicao invalida da pilha, stack pointer menor que 0.");
		else if(stackPointer >= tamanho)
			throw new AcessoIndevidoAMemoriaCheckedException("Stack Overflow.");
		else
			pilha[stackPointer] = valor;
	}

	public int getPosicaoPilha(int stackPointer) throws AcessoIndevidoAMemoriaCheckedException{
		if(stackPointer >= 0 && stackPointer < tamanho)
			return pilha[stackPointer];
		else
			throw new AcessoIndevidoAMemoriaCheckedException("Tentativa de acessar posicao invalida da pilha.");
	}

	public int getTamanho(){
		return this.tamanho;
	}

}