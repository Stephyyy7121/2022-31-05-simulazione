package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.nyc.db.NYCDao;

public class Model {
	
	private NYCDao dao;
	private Graph<City, DefaultWeightedEdge> grafo;
	private List<String> provider;
	private List<City> quartieri;
	
	
	//variabili per il punto 2
	private int durataTotale;
	private List<Integer> revisionati;
	
	public Model() {
		dao = new NYCDao();
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class) ;
	}
	
	
	public List<String> getProvider() {
		
		if (provider == null) {
			provider = dao.getAllProvider();
		}
		
		
		return provider;
		
	}
	
	public void loadNodes(String provider) {
		
		if (quartieri.isEmpty()) {
			quartieri = dao.getAllQuartieri(provider);
		}
	}
	
	public void clearGraph() {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class) ;
		this.quartieri = new ArrayList<>(); 
	}
	
	public void creaGrafo(String provider) {
		
		clearGraph();
		loadNodes(provider);
		
		//aggiungere i vertici
		Graphs.addAllVertices(this.grafo, this.quartieri);
		
		for (City c : this.grafo.vertexSet()) {
			System.out.println(c);
		}
		
		//aggiungere gli archi
		// due nodi sono collegati se il provider fornisce almeno un hotspot nei due quartieri --> ovvero tutte le citta' nella lista sono collegate fra di loro ma avranno peso diverso
		
		for (City c1 : this.quartieri) {
			for (City c2 : this.quartieri) {
				if (!c1.equals(c2)) {
					//calcolare il peso degli archi
					double peso = LatLngTool.distance(c1.getPosizione(), c2.getPosizione(), LengthUnit.KILOMETER);
					
					//aggiungere 
					Graphs.addEdgeWithVertices(this.grafo, c1, c2, peso);
				}
			}
		}
	}
	
	//lista delle citta'
	public List<City> getQuartieri() {
		return this.quartieri;
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
		
	}
	
	//metodo per prendere i nodi adiacenti --> mettere in ordine di distanza --> necessario creare una classe a parte di citta' con la distanza per poter implementare Comparable
	
	public List<CityDistance> getAdiacenti(City city) {
		
		List<CityDistance> adiacenti = new ArrayList<>();
		List<City> lista = Graphs.neighborListOf(this.grafo, city); //lista dei adiacenti ma oggetto = city e non cityDistance
		
		//conversione
		for (City c : lista) {
			adiacenti.add(new CityDistance(c, this.grafo.getEdgeWeight(this.grafo.getEdge(c, city))));
		}
		Collections.sort(adiacenti);
		
		return adiacenti;
	}
	
	//simulazione
	public void simulazione(int numTecnici, City cittaPartenza) {
		
		Simulator simulazione = new Simulator(grafo, quartieri);
		simulazione.init(cittaPartenza, numTecnici);
		simulazione.run();
		this.durataTotale = simulazione.getDurata();
		this.revisionati = simulazione.getRevisionati();
	}
	
	public int getDurataTotale() {
		return this.durataTotale;
	}
	
	public List<Integer> getRevisionati() {
		return this.revisionati;
	}
	
	
}
