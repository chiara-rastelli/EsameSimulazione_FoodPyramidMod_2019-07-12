package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import it.polito.tdp.food.model.Vicino;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno A --> switchare al branch master_turnoB per turno B

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtPorzioni;

    @FXML
    private TextField txtK;

    @FXML
    private Button btnAnalisi;

    @FXML
    private Button btnCalorie;

    @FXML
    private Button btnSimula;

    @FXML
    private ComboBox<Food> boxFood;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalorie(ActionEvent event) {
    	this.txtResult.clear();
    	if (this.model.getStatoGrafo() == false) {
    		this.txtResult.setText("Prima devi creare il grafo selezionando un numero di porzioni!\n");
    		return;
    	}
    	if (this.boxFood.getValue() == null) {
    		this.txtResult.setText("Devi prima selezionare dal menu' a tendina un cibo!\n");
    		return;
    	}
    	List<Vicino> listaVicini = this.model.getCalorieMassimeAdiacenza(this.boxFood.getValue());
    	if (listaVicini.size() == 0) {
    		this.txtResult.setText("Mi dispiace, nel grafo creato il cibo selezionato non ha vicini!\n");
    		return;
    	}
    	this.txtResult.setText("Ecco i cinque cibi vicini a quello selezionato nel grafo con il massimo valore di calorie congiunte con esso:\n");
    	if (listaVicini.size() < 5) {
    		int i = 1;
    		for (Vicino v : listaVicini) {
    			this.txtResult.appendText("Cibo vicino numero "+i+": "+v.getVicino()+"; media calorie congiunte: "+v.getPeso()+"\n");
    			i++;
    		}
    		return;
    	}
    	for (int i = 0; i < 5; i++) {
    			this.txtResult.appendText("Cibo vicino numero "+(i+1)+": "+listaVicini.get(i).getVicino()+"; media calorie congiunte: "+listaVicini.get(i).getPeso()+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	int numeroPorzioni = 0;
    	try {
    		numeroPorzioni = Integer.parseInt(this.txtPorzioni.getText());
    		if (numeroPorzioni < 0) {
    			this.txtResult.setText("Il numero di porzioni deve essere maggiore di zero!\n");
    			return;
    		}
    		
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Devi inserire un valore intero per il numero di porzioni!\n");
    		return;
    	}
    	this.model.creaGrafo(numeroPorzioni);
    	this.boxFood.getItems().addAll(this.model.getFoodByNumberOfCondiments(numeroPorzioni));
    }

    @FXML
    void doSimula(ActionEvent event) {
    	this.txtResult.clear();
    	if (this.model.getStatoGrafo() == false) {
    		this.txtResult.setText("Devi prima creare un grafo ai punti precedenti!\n");
    		return;
    	}
    	int numeroPostazioni = 0;
    	try {
    		numeroPostazioni = Integer.parseInt(this.txtK.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("Devi inserire un numero di postazioni per la simulazione intero!\n");
    		return;
    	}
    	if (numeroPostazioni > 10 || numeroPostazioni < 1) {
    		this.txtResult.appendText("Il numero di postazioni per la simulazione deve essere compreso tra 1 e 10, estremi inclusi!\n");
    		return;
    	}
    	this.model.Simula(numeroPostazioni, this.boxFood.getValue());
    }

    @FXML
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}
