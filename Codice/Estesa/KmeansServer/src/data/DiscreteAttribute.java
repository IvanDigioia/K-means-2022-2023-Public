package data;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Classe concreta che estende data.Attribute e rappresenta un attributo discreto (categorico).
 */
public class DiscreteAttribute extends Attribute implements Iterable<String> {
    /** Insieme ordinato di valori unici che un attributo discreto può assumere*/
    TreeSet<String> values;

    /**
     * Costruttore della classe data.DiscreteAttribute.
     *
     * @param name   nome dell'attributo
     * @param index  identificativo numerico dell'attributo
     * @param values insieme di stringhe rappresentanti il dominio dell'attributo
     */
    DiscreteAttribute(String name, int index, TreeSet<String> values) {
        super(name, index);
        this.values = values;
    }

    /**
     * Restituisce il numero di valori discreti nel dominio dell'attributo.
     *
     * @return numero di valori discreti nel dominio dell'attributo
     */
    int getNumberOfDistinctValues() {
        return values.size();
    }

    /**
     * Determina il numero di volte che un valore discreto compare nell'attributo corrente negli esempi di dati
     * indicizzati dalla lista di identificatori specificata.
     *
     * @param data   l'oggetto data.Data contenente gli esempi di dati
     * @param idList l'insieme di identificatori indicizzando gli esempi di dati
     * @param v      il valore discreto da contare
     * @return il numero di occorrenze del valore discreto nell'attributo corrente (intero)
     */
    int frequency(Data data, Set<Integer> idList, String v) {
        int freq = 0;
        for (int i = 0; i < data.getNumberOfExamples(); i++) {
            //scorre le righe per cercare un valore true in idList
            if (idList.contains(i)) {
                for (int j = 0; j < data.getNumberOfAttributes(); j++) {
                    // trovato il valore vero scorre le colonne per confrontare v il valore dell'attributo
                    if (data.getAttributeValue(i, j).equals(v)) {
                        freq++;
                    }
                }
            }
        }
        return freq;
    }

    /**
     * Restituisce un iteratore per il dominio dell'attributo.
     *
     * @return iteratore per il dominio dell'attributo
     */
    @Override
    public Iterator<String> iterator() {
        return values.iterator();
    }
}
