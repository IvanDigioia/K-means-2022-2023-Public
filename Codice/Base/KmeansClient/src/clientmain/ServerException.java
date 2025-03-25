package clientmain;
/**
 * Eccezione sollevata dal server e trasmessa al client tramite stream di connessione.
 */
public class ServerException extends Exception{
    /**
     * Costruttore che inizializza l'eccezione con un messaggio specifico.
     * @param result Il messaggio di errore associato all'eccezione.
     */
    ServerException(String result){
        super(result);
    }
}
