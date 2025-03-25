package data;

import database.*;

import java.sql.SQLException;
import java.util.*;
import java.lang.*;
import javax.swing.text.AttributeSet;

/**
 * La classe data.Data rappresenta un insieme di transazioni con attributi discreti o continui.
 */
public class Data {
	/**Matrice nXm di tipo Object dove ogni riga modella una transazione*/
	List<Example> data= new ArrayList<Example>();
	/** Cardinalità dell'insieme di transazioni (numero di righe)*/
	int numberOfExamples;
	/**Vettore degli attributi presenti in ogni transazione (numero di colonne)*/
	List<Attribute> attributeSet;
	/**numero di transazioni uniche nella matrice*/
	int distinctTuples;

	/**
	 * Costruttore della classe data.Data
	 * carica i dati di addestramento da una tabella della base di dati. Inizializza la matrice, il numero di transazioni e l'insieme degli attributi
	 * @param table il nome della tabella SQL da cui leggere le transazioni
	 * @throws SQLException quando il connettore SQL non è attivo o se effettuiamo una operazione illegale
	 * @throws DatabaseConnectionException quando non troviamo il connettore, se effettuiamo una operazione illegale (su una tabella vuota)
	 * @throws EmptySetException Quando il ResultSet risulta vuoto
	 */
	public Data(String table) throws SQLException, DatabaseConnectionException, EmptySetException{
		//inizializzazione dei dati
		TreeSet<Example> tempData= new TreeSet<Example>();
		attributeSet = new LinkedList<Attribute>();
		//definizione degli attributi discreti
		TreeSet<String> outLookValues= new TreeSet<String>();
		outLookValues.add("sunny");
		outLookValues.add("rain");
		outLookValues.add("overcast");
		TreeSet<String> temperatureValues= new TreeSet<String>();
		temperatureValues.add("hot");
		temperatureValues.add("mild");
		temperatureValues.add("cold");
		TreeSet<String> humidityValues= new TreeSet<String>();
		humidityValues.add("high");
		humidityValues.add("normal");
		TreeSet<String> windValues= new TreeSet<String>();
		windValues.add("weak");
		windValues.add("strong");
		TreeSet<String> playValues= new TreeSet<String>();
		playValues.add("no");
		playValues.add("yes");
		//accesso alla tabella de db
		try{
			DbAccess dbaccess=new DbAccess();
			dbaccess.initConnection();
			TableData dbcrawler=new TableData(dbaccess);
			ArrayList<Example> examplelist=new ArrayList<Example>(dbcrawler.getDistinctTransazioni(table));
			Iterator<Example> it=examplelist.iterator();
			while(it.hasNext()){
				data.add(it.next()); // aggiungiamo una per una(si potrebbe semplificare, ma vabbè) gli elementi dalla tabella alla list<example> in data

			}
			//inizializza numberOfExamples
			numberOfExamples = getNumberOfExamples();

			TableSchema column=new TableSchema(dbaccess,table);

			attributeSet.add(new DiscreteAttribute("Outlook", 0, outLookValues));
			double min = (double) dbcrawler.getAggregateColumnValue(table,column.getColumn(1),QUERY_TYPE.MIN); //prende il valore minimo dalla colonna 1 (che presumo sia la colonna temperature, ma non lo so con certezza)
			double max = (double) dbcrawler.getAggregateColumnValue(table,column.getColumn(1),QUERY_TYPE.MAX);
			attributeSet.add(new ContinuousAttribute("Temperature",1, min, max));

			attributeSet.add(new DiscreteAttribute("Humidity", 2, humidityValues));
			attributeSet.add(new DiscreteAttribute("Wind", 3, windValues));
			attributeSet.add(new DiscreteAttribute("PlayTennis", 4, playValues));

			distinctTuples = getNumberOfExamples();

			//chiusura della connessione
			dbaccess.closeConnection();
		}
		catch (NoValueException e){
			System.out.println("[!] nessun valore"+e.getMessage());
		}

	}

	/**
	 * Restituisce la cardinalità dell'insieme di transazioni.
	 *
	 * @return Il numero di transazioni presenti nei dati.
	 */
	public int getNumberOfExamples() {
		return data.size();
	}

	/**
	 * Restituisce la cardinalità dell'insieme degli attributi.
	 *
	 * @return Il numero di attributi presenti nei dati.
	 */
	public int getNumberOfAttributes() {
		return attributeSet.size();
	}

	/**
	 * Restituisce lo schema dei dati.
	 *
	 * @return Un array di oggetti data.Attribute rappresentante lo schema dei dati.
	 */
	List<Attribute> getAttributeSchema() { //List o LikedList?? da controllare
		return attributeSet;
	}

	/**
	 * Restituisce il valore di un attributo specifico per una determinata transazione.
	 *
	 * @param exampleIndex    L'indice della transazione desiderata.
	 * @param attributeIndex  L'indice dell'attributo desiderato.
	 * @return Il valore dell'attributo nella transazione specificata.
	 */
	public Object getAttributeValue(int exampleIndex, int attributeIndex) {
		//return data[exampleIndex][attributeIndex];
		Example exresult = data.get(exampleIndex); //cerco l'oggetto example (che corrisponde alla riga)
		return exresult.get(attributeIndex); // cerco l'attributo (che corrisponde alla colonna)
	}

	/**
	 * Restituisce l'attributo corrispondente a un indice specificato.
	 *
	 * @param index L'indice dell'attributo desiderato.
	 * @return L'oggetto data.Attribute corrispondente all'indice specificato.
	 */
	Attribute getAttribute(int index) {
		//return attributeSet[index];
		return attributeSet.get(index);
	}

	/**
	 * Restituisce una tupla contenente un insieme di elementi basati sull'indice specificato.
	 *
	 * @param index l'indice dell'elemento desiderato all'interno della matrice data
	 * @return un oggetto data.Tuple contenente l'insieme di elementi creati
	 */
	public Tuple getItemSet(int index) {
		Tuple tuple = new Tuple(getNumberOfAttributes());
		for (int i = 0; i < getNumberOfAttributes(); i++) { //rtti da provare
			if(getAttribute(i) instanceof DiscreteAttribute)
				tuple.add(new DiscreteItem((DiscreteAttribute) getAttribute(i),
						(String) getAttributeValue(index, i)), i);
			else if (getAttribute(i) instanceof ContinuousAttribute)
				tuple.add(new ContinuousItem((ContinuousAttribute) getAttribute(i),
						(double) getAttributeValue(index,i)),i);
		}
		return tuple;
	}

	/**
	 * Genera un array di indici rappresentanti i centroidi campionati casualmente.
	 *
	 * @param k il numero di centroidi da campionare
	 * @return un array di interi contenente gli indici dei centroidi campionati
	 * @throws OutOfRangeSampleSize se si usa un valore di centroidi minore di zero o maggiore di distinctTuples
	 */
	public int[] sampling(int k) throws OutOfRangeSampleSize{
		if (k<=0 || k > distinctTuples)
			throw new OutOfRangeSampleSize();
		int[] centroidIndexes =new int[k];
		//choose k random different centroids in data.
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		for (int i=0;i<k;i++){
			boolean found=false;
			int c;
			do
			{
				found=false;
				c=rand.nextInt(getNumberOfExamples());
				// verify that centroid[c] is not equal to a centroide already stored in CentroidIndexes
				for(int j=0;j<i;j++)
					if(compare(centroidIndexes[j],c)){
						found=true;
						break;
					}
			}
			while(found);
			centroidIndexes[i]=c;
		}
		return centroidIndexes;
	}

	/**
	 * Confronta due elementi utilizzando gli indici specificati.
	 *
	 * @param i l'indice del primo elemento da confrontare
	 * @param j l'indice del secondo elemento da confrontare
	 * @return true se gli elementi sono uguali, false altrimenti
	 */
	private boolean compare(int i, int j) {
		//return Arrays.equals(data[i], data[j]);
		return data.get(i).equals(data.get(j)); //dovrebbe andare
	}

	/**
	 * Usa l'RTTI per determinare se attribute riferisce una istanza di ContinuosAttribute o di DiscreteAttribute
	 * @param idList insieme di indici di riga
	 * @param attribute	attributo rispetto al quale calcolare il prototipo
	 * @return oggetto contenente valore prototipo come media valori oppure una stringa rappresentante il prototipo calolato
	 */
	Object computePrototype(Set idList, Attribute attribute){
		Object result = null;
		if (attribute instanceof DiscreteAttribute)
			result = computePrototype(idList,(DiscreteAttribute) attribute);
		else if(attribute instanceof ContinuousAttribute)
			result = computePrototype(idList,(ContinuousAttribute) attribute);
		return result;
	}


	/**
	 * Determina il valore prototipo come media dei valori osservati per attribute nelle transazioni di data aventi indice di riga in idList
	 * @param idList		l'insieme di identificatori su cui calcolare il prototipo
	 * @param attribute		l'attributo (continuo) per il quale calcolare il prototipo
	 * @return valore numerico costituito dalla frazione dell'intero sulla somma
	 */
	double computePrototype(Set<Integer> idList, ContinuousAttribute attribute) {
		double sum = 0.0;
		int count = 0;

		for (Integer id : idList) {
			double value = (double) getAttributeValue(id, attribute.getIndex());
			sum += value;
			count++;
		}

		return sum / count;
	}


	/**
	 * Calcola il prototipo per l'attributo specificato basandosi sulla lista di identificatori fornita.
	 *
	 * @param idList    l'insieme di identificatori su cui calcolare il prototipo
	 * @param attribute l'attributo (discreto) per il quale calcolare il prototipo
	 * @return una stringa rappresentante il prototipo calcolato
	 */
	public String computePrototype(Set<Integer> idList, DiscreteAttribute attribute) {
		String prototype = "";
		int freq = 0;
		Iterator<String> attributeIterator= attribute.iterator();
		while (attributeIterator.hasNext()) {
			String temp = attributeIterator.next(); //sarebbe meglio forse un nome più significativo?
			int freqtemp = attribute.frequency(this, idList, temp);
			// La frequenza è calcolata qui, mentre il resto del codice serve a scomporre l'attributo in stringhe
			if (freqtemp > freq) {
				prototype = temp;
				freq = freqtemp;
			}
		}
		return prototype;
	}

	/**
	 * Restituisce una stringa che rappresenta lo stato dell'oggetto data.Data.
	 *
	 * @return Una stringa che rappresenta lo stato dell'oggetto data.Data.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		// Aggiunta dello schema degli attributi
		for (Attribute attribute : getAttributeSchema()) {
			sb.append(attribute).append(",");
		}
		//sb.deleteCharAt(sb.length() - 1); // Rimozione dell'ultima virgola
		sb.append("\n");

		// Aggiunta delle transazioni di esempio
		for (int i = 0; i < getNumberOfExamples(); i++) {
			sb.append(i).append(":");
			for (int j = 0; j < getNumberOfAttributes(); j++) {
				sb.append(getAttributeValue(i,j)).append(",");
			}
			sb.deleteCharAt(sb.length() - 1); // Rimozione dell'ultima virgola
			sb.append("\n");
		}

		return sb.toString();
	}

}
