package mining;

import data.*;
//import keyboardinput.*;
import mining.*;

import java.io.Serializable;

/**
 * Rappresenta un insieme di cluster e fornisce metodi per manipolare e gestire i cluster.
 */
public class ClusterSet implements Serializable {
    private Cluster[] C;
    private int i = 0;

    /**
     * Crea un nuovo oggetto mining.ClusterSet con la capacità specificata.
     *
     * @param k la capacità del mining.ClusterSet
     */
    public ClusterSet(int k) {
        C = new Cluster[k];
    }

    /**
     * Aggiunge un cluster all'insieme di cluster.
     *
     * @param c il cluster da aggiungere
     */
    public void add(Cluster c) {
        C[i] = c;
        i++;
    }

    /**
     * Restituisce il cluster in corrispondenza dell'indice specificato.
     *
     * @param i l'indice del cluster
     * @return il cluster corrispondente all'indice specificato
     */
    public Cluster get(int i) {
        return C[i];
    }

    /**
     * Inizializza i centroidi dei cluster basandosi sui dati specificati.
     *
     * @param data i dati utilizzati per l'inizializzazione dei centroidi
     * @throws OutOfRangeSampleSize se il numero di centroidi è superiore al numero di transazioni
     */
    public void initializeCentroids(Data data) throws OutOfRangeSampleSize {
        int[] centroidIndexes = data.sampling(C.length);
        for (int i = 0; i < centroidIndexes.length; i++) {
            Tuple centroidI = data.getItemSet(centroidIndexes[i]);
            add(new Cluster(centroidI));
        }
    }

    /**
     * Restituisce il cluster più vicino alla tupla specificata.
     *
     * @param tuple la tupla di riferimento
     * @return il cluster più vicino alla tupla
     */
    public Cluster nearestCluster(Tuple tuple) {
        double distance = Double.MAX_VALUE;
        Cluster closest = C[0];

        for (int j = 0; j < i; j++) {
            double tempdistance = C[j].getCentroid().getDistance(tuple);
            if (tempdistance < distance) {
                closest = C[j];
                distance = tempdistance;
            }
        }

        return closest;
    }


    /**
     * Restituisce il cluster corrente in cui è clusterizzato l'elemento con l'ID specificato.
     *
     * @param id l'ID dell'elemento
     * @return il cluster corrente dell'elemento
     */

    public Cluster currentCluster(int id) {

        for (int j = 0; j < C.length; j++) {
            if (C[j].contain(id)) {
                return C[j];
            }
        }
        return null;
    }




    /**
     * Aggiorna i centroidi dei cluster basandosi sui dati specificati.
     *
     * @param data i dati utilizzati per l'aggiornamento dei centroidi
     */
    public void updateCentroids(Data data) {
        for (int j = 0; j < i; j++) {
            C[j].computeCentroid(data);
        }
    }

    /**
     * Restituisce una stringa che rappresenta l'insieme di cluster con i relativi centroidi.
     *
     * @return una stringa che rappresenta l'insieme di cluster con i relativi centroidi
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < i; j++) {
            sb.append(C[j].toString()).append(" ");
        }
        return sb.toString();
    }


    /**
     * Restituisce una stringa che rappresenta l'insieme di cluster con i relativi centroidi e gli esempi associati.
     *
     * @param data i dati utilizzati per ottenere gli esempi associati ai cluster
     * @return una stringa che rappresenta l'insieme di cluster con i relativi centroidi e gli esempi associati
     */
    public String toString(Data data) {
        String str = "";
        for (int i = 0; i < C.length; i++) {
            if (C[i] != null) {
                str += i + ":" + C[i].toString(data) + "\n";
            }
        }
        return str;
    }
}