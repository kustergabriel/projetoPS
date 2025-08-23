package com.app.calingaertextend.maquinavirtual;

public class Memoria{

	private int[] memoria;
	private int tamanho;

	public Memoria(int expoenteDe2){
		this.tamanho = (int)Math.pow(2, expoenteDe2); // 2^12 = 4096 
		this.memoria = new int[this.tamanho]; // 4096 posicoes de memoria
		for(int i = 0; i < this.memoria.length; i++)
			this.memoria[i] = 0;
	}

	public void setPosicaoMemoria (int posicao, int valor) throws AcessoIndevidoAMemoriaCheckedException{
		int index = posicao + 2;
		if(posicao >= 0 && posicao < tamanho)
			memoria[index] = valor;
		else
			throw new AcessoIndevidoAMemoriaCheckedException("Tentativa de acesso a memoria invalida.");
	}

	public int getPosicaoMemoria (int posicao) throws AcessoIndevidoAMemoriaCheckedException{
		int index = posicao + 2;
		if(posicao >= 0 && posicao < tamanho)
			return memoria[index];
		else
			throw new AcessoIndevidoAMemoriaCheckedException("Tentativa de acesso a memoria invalida.");
	}

	public int getTamanho(){
		return this.tamanho;
	}

	public void imprimirMemoria () {
		for (int i = 0; i < tamanho/32; i++) {
			System.out.printf("Mem[%04d] = %d\n", i, memoria[i]);
		}
	}

	public int[] getMemoria(){
		return memoria;
	}

}