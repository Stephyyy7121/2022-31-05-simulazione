/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.nyc;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.nyc.model.City;
import it.polito.tdp.nyc.model.CityDistance;
import it.polito.tdp.nyc.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaLista"
    private Button btnCreaLista; // Value injected by FXMLLoader

    @FXML // fx:id="cmbProvider"
    private ComboBox<String> cmbProvider; // Value injected by FXMLLoader

    @FXML // fx:id="cmbQuartiere"
    private ComboBox<City> cmbQuartiere; // Value injected by FXMLLoader

    @FXML // fx:id="txtMemoria"
    private TextField txtMemoria; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML // fx:id="clQuartiere"
    private TableColumn<?, ?> clQuartiere; // Value injected by FXMLLoader
 
    @FXML // fx:id="clDistanza"
    private TableColumn<?, ?> clDistanza; // Value injected by FXMLLoader
    
    @FXML // fx:id="tblQuartieri"
    private TableView<?> tblQuartieri; // Value injected by FXMLLoader
    
    private boolean grafoCreato = false;

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	txtResult.clear();
    	String provider = this.cmbProvider.getValue();
    	
    	//controllo input 
    	if (provider.equals("")) {
    		txtResult.setText("Inserire un valore");
    	}
    	
    	model.creaGrafo(provider);
    	
    	txtResult.setText("Grafo creato! +\n#Vertici: " + model.getNumVertici() + "\n#Archi: " + model.getNumArchi());
    	
    	grafoCreato = true;
    	
    	//aggiungere roba nel combobox
    	this.cmbQuartiere.getItems().addAll(model.getQuartieri());
    }

    @FXML
    void doQuartieriAdiacenti(ActionEvent event) {
    	
    	txtResult.clear();
    	//il grafo deve essere creato altrimenti niente
    	if(!grafoCreato) {
    		txtResult.appendText("Creare il grafo!");
    	}
    	City inputCity = this.cmbQuartiere.getValue();
    	
    	if (inputCity == null) {
    		txtResult.appendText("Inserire citta'");
    	}
    	
    	List<CityDistance> adiacenti = model.getAdiacenti(inputCity);
    	for (CityDistance c : adiacenti) {
    		txtResult.appendText(c.getCitta() + " "+ c.getDistanza() + "\n");
    	}
    	
    	
    	
    }

    @FXML
void doSimula(ActionEvent event) {
    	
    	City scelto = cmbQuartiere.getValue();
    	if(scelto==null) {
    		txtResult.appendText("Errore: seleziona un quartiere\n");
    		return;
    	}

    	int N = 0;
    	try {
    		N = Integer.parseInt(txtMemoria.getText());
    	} catch(NumberFormatException ex) {
    		txtResult.appendText("Errore: inserire un numero valido\n");
    		return;
    	}
    	
    	model.simulazione(N, scelto);
    	
    	txtResult.appendText("Durata simulazione: "+model.getDurataTotale()+" minuti\n");
    	txtResult.appendText("Impegni dei tecnici: "+model.getRevisionati()+"\n");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaLista != null : "fx:id=\"btnCreaLista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbProvider != null : "fx:id=\"cmbProvider\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbQuartiere != null : "fx:id=\"cmbQuartiere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMemoria != null : "fx:id=\"txtMemoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert clDistanza != null : "fx:id=\"clDistanza\" was not injected: check your FXML file 'Scene.fxml'.";
        assert clQuartiere != null : "fx:id=\"clQuartiere\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbProvider.getItems().addAll(model.getProvider());
    }

}
