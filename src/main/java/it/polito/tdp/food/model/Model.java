package it.polito.tdp.food.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.db.FoodDAO;

public class Model {
	
	FoodDAO db;
	Map<Integer, Food> foodIdMap;
	
	public Model() {
		db = new FoodDAO();
		this.foodIdMap = new HashMap<>();
		for (Food f : this.db.listAllFoods())
			this.foodIdMap.put(f.getFood_code(), f);
	}
	
	public List<Food> getFoodByNumberOfCondiments(int n){
		return this.db.listAllFoodsByNumberOfCondiments(n, this.foodIdMap);
	}
	
	
}
