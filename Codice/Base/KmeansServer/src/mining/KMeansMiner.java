package mining;

import data.Data;
import data.OutOfRangeSampleSize;

import java.io.*;

/**
 * Rappresenta un miner di K-Means che esegue l'algoritmo di clustering K-Means su un insieme di dati.
 */
public class KMeansMiner implements Serializable {
    private ClusterSet C;

    /**
     * Crea un nuovo oggetto mining.KMeansMiner con il numero di cluster specificato.
     *
     * @param k il numero di cluster desiderato
     */
    public KMeansMiner(int k) {
        C = new ClusterSet(k);
    }

    /**
     * Costruttore della classe che carica la classe da un file salvato
     * @param fileName  il nome del file da deserializzare
     * @throws FileNotFoundException quando il file non è trovato (se il nome del file non è valido)
     * @throws IOException quando non riesce ad accedere o a leggere il file
     * @throws ClassNotFoundException quando si cerca di caricare una classe che non implementa serializable
     */
    //Essenzialmente deserializza l'oggetto
    public KMeansMiner(String fileName) throws FileNotFoundException,
            IOException, ClassNotFoundException {
        String currentDirectory = System.getProperty("user.dir");
        String targetDir = "K-means-22-23";
        int endIndex = currentDirectory.lastIndexOf(targetDir) + targetDir.length();
        String desiredDir = currentDirectory.substring(0, endIndex);
        String folderPath = desiredDir + File.separator + "File memorizzati";
        File folder = new File(folderPath);

        fileName = folder + File.separator + fileName + ".ser";
        FileInputStream fileInput = new FileInputStream(fileName);
        ObjectInputStream inputStream = new ObjectInputStream(fileInput);

        C = (ClusterSet) inputStream.readObject();
        inputStream.close();
        fileInput.close();

    }

    /**
     * Salva la classe su file
     * @param fileName il nome del file su cui salvare la classe
     * @throws FileNotFoundException quando il file non è trovato
     * @throws IOException quando ci sono errori nella scrittura del file
     */
    public void salva(String fileName) throws FileNotFoundException,
            IOException {
        String currentDirectory = System.getProperty("user.dir");
        String targetDir = "K-means-22-23";
        int endIndex = currentDirectory.lastIndexOf(targetDir) + targetDir.length();
        String desiredDir = currentDirectory.substring(0, endIndex);
        String folderPath = desiredDir + File.separator + "File memorizzati";
        File folder = new File(folderPath);

        fileName = folder + File.separator + fileName + ".ser";
        FileOutputStream fileOutput = new FileOutputStream(fileName); //fileName
        ObjectOutputStream outputStream = new ObjectOutputStream(fileOutput);

        outputStream.writeObject(C);
        outputStream.close();
        fileOutput.close();
    }

    /**
     * Restituisce l'insieme di cluster associato al miner di K-Means.
     *
     * @return l'insieme di cluster
     */
    public ClusterSet getC() {
        return C;
    }

    /**
     * Esegue l'algoritmo di clustering K-Means sui dati specificati.
     * Restituisce il numero d'iterazioni necessarie per raggiungere la convergenza.
     *
     * @param data i dati su cui eseguire il clustering
     * @return il numero d'iterazioni necessarie per raggiungere la convergenza
     * @throws OutOfRangeSampleSize se il numero di cluster è superiore al numero di centroidi generabili dalle transazioni
     */
    public int kmeans(Data data) throws OutOfRangeSampleSize {
        int numberOfIterations=0;
        //STEP 1
        C.initializeCentroids(data);
        boolean changedCluster=false;
        do{
            numberOfIterations++;
            //STEP 2
            changedCluster=false;
            for(int i=0;i<data.getNumberOfExamples();i++){
                Cluster nearestCluster = C.nearestCluster(
                        data.getItemSet(i));
                Cluster oldCluster=C.currentCluster(i);
                boolean currentChange=nearestCluster.addData(i);
                if(currentChange)
                    changedCluster=true;
                    //rimuovo la tupla dal vecchio cluster
                if(currentChange && oldCluster!=null)
                    //il nodo va rimosso dal suo vecchio cluster
                    oldCluster.removeTuple(i);
            }
            //STEP 3
            C.updateCentroids(data);
        }
        while(changedCluster);
        return numberOfIterations;
    }

}