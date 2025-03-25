package database;

/**
 * Eccezione controllata espulsa quando un resultset restituito risulta vuoto
 */
public class EmptySetException extends Exception{

    /**
     * Costruttore per creare un'eccezione EmptySetException.
     * Genera un messaggio di errore predefinito per un dataset vuoto.
     */
    EmptySetException() {
        super("[!] DataSet vuoto: Operazione illegale su un dataset vuoto");
    }
}
