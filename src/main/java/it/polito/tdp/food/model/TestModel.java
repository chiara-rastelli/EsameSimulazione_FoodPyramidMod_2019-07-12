package it.polito.tdp.food.model;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Model m = new Model();
		
		for (Food f : m.getFoodByNumberOfCondiments(6))
			System.out.println(f.toString());
	}

}
