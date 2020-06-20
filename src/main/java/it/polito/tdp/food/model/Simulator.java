package it.polito.tdp.food.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.model.Event.EventType;

public class Simulator {
	
	int N;		// numero di postazioni di cucina, inserite dall'utente --> controllo lato controller che sia minore o uguale a 10
	SimpleWeightedGraph<Food, DefaultWeightedEdge> graph; // il peso degli archi e' il tempo per passare dalla preparazione
														  // da un cibo all'altro
	Food f1; 	// partiamo dai cibi adiacenti a questo, selezionato dall'utente, nel grafo
	
	PriorityQueue<Event> queue;	// coda degli eventi
	Duration ultimoTempo;		// momento di fine dell'ultima preparazione
	List<Food> preparati;		// lista dei cibi preparati
	Map<Integer, Boolean> stazioniOccupate;
	
	public Simulator(int N, SimpleWeightedGraph<Food, DefaultWeightedEdge> graph, Food f1) {
		this.N = N; 
		this.graph = graph;
		this.queue = new PriorityQueue<>();
		
		this.stazioniOccupate = new HashMap<>();
		for (int i = 0; i < this.N; i++) {
			this.stazioniOccupate.put(i, false);
		}
		
		List<Vicino> listaViciniIniziale = new ArrayList<>();
		for (Food f : Graphs.neighborListOf(this.graph, f1)) {
			DefaultWeightedEdge eTemp = this.graph.getEdge(f1, f);
			listaViciniIniziale.add(new Vicino(f, this.graph.getEdgeWeight(eTemp)));
		}
		Collections.sort(listaViciniIniziale);
		
		for (int i = 0; (i < this.N) &&( i < listaViciniIniziale.size()); i++) {
			this.queue.add(new Event(EventType.NUOVA_PREPARAZIONE, listaViciniIniziale.get(i).getVicino(),Duration.ofMinutes(0), f1,i));
			this.stazioniOccupate.put(i, true);
		}
		this.preparati = new ArrayList<Food>();
		this.ultimoTempo = Duration.ofMinutes(0);
		
		this.run();
	}
	
	public void run() {
		while (!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			System.out.println(e.toString());
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		
		switch (e.getType()) {
		
			case NUOVA_PREPARAZIONE:
				DefaultWeightedEdge eTemp = this.graph.getEdge(e.getCibo(), e.getPrecedente());
				e.cibo.setInPreparazione(true);
				Duration tempoPreparazione = e.tempo.plusMinutes((long) this.graph.getEdgeWeight(eTemp));
				this.queue.add(new Event(EventType.PREPARAZIONE_TERMINATA, e.cibo ,tempoPreparazione, null,e.getStazione()));
			
			break;	
			case PREPARAZIONE_TERMINATA:
				this.stazioniOccupate.put(e.getStazione(), false);
				e.getCibo().setGiaPreparato(true);
				this.preparati.add(e.getCibo());
				this.ultimoTempo = e.getTempo();
				Food next = this.getNextPreparazione(e.getCibo());
			
				if (next != null) {
					this.queue.add(new Event(EventType.NUOVA_PREPARAZIONE, next, e.getTempo(), e.getCibo(), e.getStazione()));
					this.stazioniOccupate.put(e.getStazione(), true);
				}
			break;
		}
		
	}

	private Food getNextPreparazione(Food cibo) {
		List<Vicino> listaVicini = new ArrayList<>();
		for (Food f : Graphs.neighborListOf(this.graph, cibo)) {
			DefaultWeightedEdge eTemp = this.graph.getEdge(cibo, f);
			listaVicini.add(new Vicino(f, this.graph.getEdgeWeight(eTemp)));
		}
		Collections.sort(listaVicini);
		
		for (int i = 0; i < listaVicini.size(); i++)
			if (!listaVicini.get(i).getVicino().getInPreparazione())
				if(!listaVicini.get(i).getVicino().getGiaPreparato())
					return listaVicini.get(i).getVicino();
		
		return null;
	}

}
