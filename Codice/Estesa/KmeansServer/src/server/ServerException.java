package server;

import java.io.Serializable;

/**
 * Eccezione controllata sollevata dal server e trasmessa al client tramite stream di connessione
 */
public class ServerException extends Exception implements Serializable {

    private String result;
    private String originClass;

    /**
     * Costruttore dell'eccezione. Genera un'eccezione del server basata sull'eccezione originale e registra il nome della classe di origine.
     * @param e l'eccezione originale da cui creare l'eccezione del server
     */
    ServerException(Exception e){
        super(e.getMessage());
        originClass=e.getClass().getName();
    }

    /**
     * Restituisce il nome della classe di origine che ha causato l'eccezione del server.
     * @return il nome della dell'eccezione di origine
     */
    public String getOriginClass(){
        return originClass;
    }
}
