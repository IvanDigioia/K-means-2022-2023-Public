package database;

import java.util.ArrayList;
import java.util.List;


/**
 * Modella una transazione letta dalla base di dati.
 */
public class Example implements Comparable<Example>{
	/**
	 * Lista degli attributi che rappresentano una transazione.
	 * Ogni elemento della lista corrisponde a un singolo attributo della transazione
	 */
	private List<Object> example=new ArrayList<Object>();

	/**
	 * Aggiunge un nuovo attributo alla transazione.
 	 * @param o L'attributo da aggiungere alla transazione.
	 */
	public void add(Object o){
		example.add(o);
	}

	/**
	 * Restituisce l'attributo presente nella posizione specificata all'interno della transazione.
	 * @param i La posizione dell'attributo desiderato all'interno della transazione.
	 * @return L'attributo presente nella posizione specificata.
	 */
	public Object get(int i){
		return example.get(i);
	}

	/**
	 * Confronta l'istanza corrente di Example con un'altra istanza.
	 * Questo metodo confronta gli attributi contenuti nelle due transazioni
	 * @param ex l'altra istanza di Example con cui confrontare l'istanza corrente
	 * @return Valore intero:
	 *  - Negativo se ogni attributo conenuto risulta minore dell'altra istanza
	 *  - Positivo se ogni attributo contenuto risulta maggiore dell'altra istanza
	 *  - zero se le due istanze sono considerate equivalenti
	 */
	public int compareTo(Example ex) {
		
		int i=0;
		for(Object o:ex.example){
			if(!o.equals(this.example.get(i)))
				return ((Comparable)o).compareTo(example.get(i));
			i++;
		}
		return 0;
	}

	/**
	 * Restituisce una rappresentazione testuale della transazione.
	 *
	 * @return Una stringa che rappresenta la transazione, concatenando le rappresentazioni testuali degli attributi.
	 */
	@Override
	public String toString(){
		String str="";
		for(Object o:example)
			str+=o.toString()+ " ";
		return str;
	}
	
}