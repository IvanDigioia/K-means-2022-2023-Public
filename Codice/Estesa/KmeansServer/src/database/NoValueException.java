package database;

/**
 * Eccezione controllata che modella l'assenza di un valore all'interno di un resultset
 */
public class NoValueException extends Exception{

    /**
     * Costruttore dell'eccezione
     * Genera un messaggio di errore predefinito per l'assenza di un valore nel resultset.
     */
    NoValueException(){
        super("Nessun valore simile nel ResultSet");
    }
}
