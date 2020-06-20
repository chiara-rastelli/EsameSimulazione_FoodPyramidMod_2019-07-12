package it.polito.tdp.food.model;

import java.time.Duration;

public class Event implements Comparable<Event>{

	public enum EventType{
		NUOVA_PREPARAZIONE,
		PREPARAZIONE_TERMINATA
	}
	
	public EventType type;
	public Food cibo;
	public Food precedente;
	public Duration tempo;
	public int stazione;

	public int getStazione() {
		return stazione;
	}

	public void setStazione(int stazione) {
		this.stazione = stazione;
	}

	public Food getCibo() {
		return cibo;
	}

	public void setCibo(Food cibo) {
		this.cibo = cibo;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Duration getTempo() {
		return tempo;
	}

	public void setTempo(Duration tempo) {
		this.tempo = tempo;
	}

	public Event(EventType type, Food cibo, Duration tempo, Food precedente, int stazione) {
		super();
		this.type = type;
		this.cibo = cibo;
		this.tempo = tempo;
		this.precedente = precedente;
		this.stazione = stazione;
	}

	public Food getPrecedente() {
		return precedente;
	}

	public void setPrecedente(Food precedente) {
		this.precedente = precedente;
	}

	@Override
	public String toString() {
		return "Event [type=" + type + ", cibo=" + cibo + ", tempo=" + tempo + "]";
	}

	@Override
	public int compareTo(Event o) {
		return this.tempo.compareTo(o.tempo);
	}

}
