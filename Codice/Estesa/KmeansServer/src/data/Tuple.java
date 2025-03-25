package data;

import java.io.Serializable;

/**
 * classe data.Tuple che rappresenta una tupla come sequenza di coppie attributo-valore.
 */
public class Tuple implements Serializable {
    private Item[] tuple;

    /**
     * Costruttore della classe Tuple
     * @param size numero di item che costituirà la tupla
     */
    Tuple(int size) {
        tuple = new Item[size];
    }

    /**
     * restituisce la lunghezza della tupla
     * @return tuple.lenght
     */
    public int getLength() {
        return tuple.length;
    }

    /**
     * restituisce l'item contenuto nella tupla
     * @param i posizione in cui prendere l'Item
     * @return Item in posizione i
     */
    public Item get(int i) {
        return tuple[i];
    }

    /**
     * memorizza un Item nella tupla
     * @param c Item da aggiungere a Tuple
     * @param i posizione in cui memorizzare l'Item
     */
    void add(Item c, int i) {
        tuple[i] = c;
    }

    /**
     * determina la distanza tra la tupla riferita da obj e la tupla corrente (riferita da this)
     * @param obj tupla con cui onfrontare la distanza della tupla corrente
     * @return distanza come somma delle distanze tra gli item in posizioni uguali nelle due tuple
     */
    public double getDistance(Tuple obj) {
        // Creazione di una variabile per memorizzare la distanza
        double distance = 0;
        // Iterazione su tutti gli elementi delle tuple, fino al minimo tra le loro lunghezze
        for (int i = 0; i < this.getLength(); i++) {
            Item item1 = get(i);
            Item item2 = obj.get(i);
            if (!item1.getValue().equals(item2.getValue())) {
                distance += this.get(i).distance(obj.get(i));
                // Aggiungi 1 alla distanza se i valori non corrispondono
            }
        }
        // Restituzione della distanza
        return distance;
    }

    /**
     * Calcola la media delle distanze tra la tupla corrente e le altre tuple
     * @param data          righe della matrice
     * @param clusteredData indice della matrice da cui trovare la distanza media
     * @return restituisce la media delle distanze tra la tupla corrente e quelle ottenibili dalle righe della matrice in data aventi indice in clusteredData.
     */
    public double avgDistance(Data data, Integer[] clusteredData){
        double p=0.0,sumD=0.0;
        for(int i=0;i<clusteredData.length;i++){
            double d= getDistance(data.getItemSet(clusteredData[i]));
            sumD+=d;
        }
        p=sumD/clusteredData.length;
        return p;
    }

    /**
     * stampa la tupla corrente
     * @return una stringa contenente la tupla da stampare
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < getLength(); i++) {
            sb.append(get(i)).append(", ");
        }

        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }

        return sb.toString();
    }


}
