package data;

/**
 * Eccezione controllata espulsa qualora il numero k di cluster inserito da tastiera è maggiore rispetto al numero di
 * centroidi generabili dall'insieme di transazioni
 */
public class OutOfRangeSampleSize extends Exception {

    /**
     * Costruttore della classe
     * Genera un messaggio di errore predefinito quando la grandezza del cluster indicata è troppo grande.
     */
    OutOfRangeSampleSize(){
        super("[!] Grandezza del cluster indicato troppo grande");
    }
}
