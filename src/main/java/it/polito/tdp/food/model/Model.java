package it.polito.tdp.food.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDAO;

public class Model {
	
	FoodDAO db;
	Map<Integer, Food> foodIdMap;
	Map<Integer, Food> foodRistrettiIdMap;
	SimpleWeightedGraph<Food, DefaultWeightedEdge> graph;
	boolean grafoCreato;
	
	Simulator s;
	List<Food> cibiPreparati;
	Duration tempoSimulazione;
	
	public void Simula(int n, Food f) {
		this.s = new Simulator(n, this.graph, f);
		this.cibiPreparati = new ArrayList<Food>(s.preparati);
		this.tempoSimulazione = s.ultimoTempo;
		
		for (Food preparato : this.cibiPreparati)
			System.out.println(preparato.toString()+"\n");
		System.out.println("Tempo impiegato in minuti: "+this.tempoSimulazione.toMinutes()+"\n");
		
	}
	
	public boolean getStatoGrafo() {
		return this.grafoCreato;
	}
	
	public Model() {
		db = new FoodDAO();
		this.foodIdMap = new HashMap<>();
		for (Food f : this.db.listAllFoods())
			this.foodIdMap.put(f.getFood_code(), f);
		this.grafoCreato = false;
	}
	
	public List<Food> getFoodByNumberOfCondiments(int n){
		return this.db.listAllFoodsByNumberOfCondiments(n, this.foodIdMap);
	}

	public void creaGrafo(int numeroPorzioni) {
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.foodRistrettiIdMap = new HashMap<>();
		for (Food f : this.getFoodByNumberOfCondiments(numeroPorzioni)) {
			this.graph.addVertex(f);
			this.foodRistrettiIdMap.put(f.getFood_code(), f);
		}
	//	System.out.println("Grafo creato con "+this.graph.vertexSet().size()+" vertici!\n");
		for (Adiacenza a : this.db.listAdiacenze(foodRistrettiIdMap)) {
			Graphs.addEdgeWithVertices(this.graph, a.getF1(), a.getF2(), a.getPeso());
		}
	//	System.out.println("Al grafo sono stati aggiungi "+this.graph.edgeSet().size()+" archi!\n");
		this.grafoCreato = true;
	}

	public List<Vicino> getCalorieMassimeAdiacenza(Food value) {
		List<Vicino> listaVicini = new ArrayList<Vicino>();
		for (Food f : Graphs.neighborListOf(this.graph, value)) {
			DefaultWeightedEdge eTemp = this.graph.getEdge(value, f);
			listaVicini.add(new Vicino(f, this.graph.getEdgeWeight(eTemp)));
		}
		Collections.sort(listaVicini);
		return listaVicini;
	}
	
	
}
