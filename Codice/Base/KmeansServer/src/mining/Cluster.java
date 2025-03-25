package mining;

import data.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Rappresenta un cluster di dati nell'ambito dell'algoritmo di clustering.
 * Un cluster è costituito da un centroide e da un insieme di dati clusterizzati intorno a esso.
 */
public class Cluster implements Serializable {
	private Tuple centroid;

	private Set<Integer> clusteredData;
	/*mining.Cluster(){
		
	}*/
	/**
	 * Costruisce un nuovo cluster con la tupla specificata.
	 *
	 * @param centroid Il centroide del cluster.
	 */
	Cluster(Tuple centroid){
		this.centroid=centroid;
		clusteredData = new HashSet<Integer>();
	}

	/**
	 * Restituisce il centroide del cluster.
	 *
	 * @return Il centroide del cluster.
	 */
	Tuple getCentroid(){
		return centroid;
	}

	/**
	 * Calcola il nuovo centroide del cluster usando la classe Data
	 *
	 * @param data I dati utilizzati per calcolare il nuovo centroide.
	 */
	void computeCentroid(Data data){
		for(int i=0;i<centroid.getLength();i++){
			centroid.get(i).update(data, clusteredData);
		}
		
	}
	/**
	 * Aggiunge un dato al cluster.
	 *
	 * @param id L'identificatore della tupla da aggiungere al cluster.
	 * @return true se la tupla ha cambiato cluster, false altrimenti.
	 */
	//return true if the tuple is changing cluster
	boolean addData(int id){
		return clusteredData.add(id);
		
	}
	/**
	 * Verifica se una transazione è clusterizzata nell'array corrente
	 *
	 * @param id posizione della tupla da verificare.
	 * @return true se il dato è presente nel cluster corrente, false altrimenti.
	 */
	//verifica se una transazione � clusterizzata nell'array corrente
	boolean contain(int id){
		return clusteredData.contains(id); //Ritorna true se contiene l'id e lo ripassa al metodo "contain"
	}

	/**
			* Rimuove la tupla che è cambiata di cluster
	 * @param id posizione della tupla da cancellare
	 */
	//remove the tuplethat has changed the cluster
	void removeTuple(int id){
		clusteredData.remove(id);
		
	}

	/**
	 * Stampa il centroide e le tuple contenute
	 * @return stringa che contiene alla prima riga il centroide e successivamente le tuple che contiene
	 */
	@Override
	public String toString(){
		String str="Centroid=(";
		for(int i=0;i<centroid.getLength();i++)
			str+=centroid.get(i);
		str+=")";
		return str;
		
	}


	/**
	 * Stampa tutti i centroidi creati
	 * @param data la tabella da cui prendere le transazioni
	 * @return striga contenente in ordine tutti i centroidi e le rispettive transazioni contenute
	 */
	public String toString(Data data){
		String str = "Centroid=(";
		for (int i = 0; i < centroid.getLength(); i++)
			str += centroid.get(i) + " ";
		str+=")\nExamples:\n";

		Integer[] array = clusteredData.toArray(new Integer[0]); //restituisce gli elementi partendo dall'indice zero

		for(int i=0;i<array.length;i++){
			str+="[";
			for(int j=0;j<data.getNumberOfAttributes();j++)
				str+=data.getAttributeValue(array[i], j)+" ";
			str+="] dist="+getCentroid().getDistance(data.getItemSet(array[i]))+"\n";
			
		}
		str+="\nAvgDistance="+getCentroid().avgDistance(data, array);
		return str;
	}

}
