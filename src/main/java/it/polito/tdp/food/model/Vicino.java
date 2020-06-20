package it.polito.tdp.food.model;

public class Vicino implements Comparable<Vicino>{

	public Food getVicino() {
		return vicino;
	}

	public void setVicino(Food vicino) {
		this.vicino = vicino;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	Food vicino;
	Double peso;
	
	public Vicino(Food vicino, Double peso) {
		this.vicino = vicino;
		this.peso = peso;
	}

	@Override
	public int compareTo(Vicino o) {
		return o.peso.compareTo(this.peso);
	}
	
}
