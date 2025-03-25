package data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Modella un generico item (coppia attributo-valore).
 */
public abstract class Item implements Serializable {
    /** Attributo coinvolto nell'item. */
    private Attribute attribute;
    /** Valore assegnato all'attributo.*/
    private Object value;

    /**
     * Inizializza i valori dei membri attributi.
     *
     * @param attribute Attributo coinvolto nell'item.
     * @param value Valore assegnato all'attributo.
     */
    Item(Attribute attribute, Object value){
        this.attribute = attribute;
        this.value = value;
    }

    /**
     * Restituisce l'attributo dell'item.
     *
     * @return attributo dell'item.
     */
    Attribute getAttribute(){
        return attribute;
    }

    /**
     * Restituisce il valore dell'item.
     *
     * @return valore dell'item.
     */
    Object getValue(){
        return value;
    }

    /**
     * Restituisce il valore dell'item come stringa.
     *
     * @return valore dell'item come stringa.
     */
    public String toString(){
        return value.toString();
    }

    /**
     * Calcola la distanza tra l'item corrente e un oggetto dato.
     *
     * @param a oggetto da confrontare con l'item corrente.
     * @return distanza tra l'item corrente e l'oggetto dato.
     */
    abstract double distance(Object a);

    /**
     * Modifica il valore dell'item assegnandogli il valore restituito da `data.computePrototype(clusteredData, attribute)`.
     *
     * @param data riferimento ad un oggetto della classe data.Data.
     * @param clusteredData insieme di indici delle righe della matrice in data che formano il cluster.
     */

    public void update(Data data, Set<Integer> clusteredData){
        this.value = data.computePrototype((HashSet<Integer>) clusteredData, attribute);
    }
}