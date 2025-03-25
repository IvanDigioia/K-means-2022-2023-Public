package database;

import java.sql.SQLException;

/**
 * Eccezione controllata espulsa qualora la connessione al database fallisse
 */
public class DatabaseConnectionException extends Exception{

    /**
     * Costruttore per gestire un'eccezione SQLException.
     * @param e SQLException sollevata durante la connessione al database.
     */
    DatabaseConnectionException(SQLException e){
        super(errorMessage(e));
    }

    /**
     * Costruttore per gestire un'eccezione ClassNotFoundException.
     * @param e ClassNotFoundException sollevata durante la ricerca del driver JDBC.
     */
    DatabaseConnectionException(ClassNotFoundException e){
        super("[!] Driver non trovato:"+e.getMessage());
    }

    /**
     * Costruttore per gestire un'eccezione InstantiationException.
     * @param e InstantiationException sollevata durante l'istanziazione del driver JDBC.
     */
    DatabaseConnectionException(InstantiationException e){
        super("[!] Errore durante l'instanziazione: "+e.getMessage());
    }

    /**
     * Costruttore per gestire un'eccezione IllegalAccessException.
     * @param e IllegalAccessException sollevata durante l'accesso al driver JDBC.
     */
    DatabaseConnectionException(IllegalAccessException e){
        super("[!] impossibile accedere al driver: "+e.getMessage());
    }

    /**
     * Restituisce un messaggio di errore dettagliato per un'eccezione SQLException.
     * @param e SQLException sollevata durante la connessione al database.
     * @return Una stringa contenente i dettagli dell'errore SQL.
     */
    private static String errorMessage(SQLException e){
        StringBuilder sb = new StringBuilder();
        sb.append("[!] SQLException: ").append(e.getMessage()).append("\n");
        sb.append("[!] SQLState: ").append(e.getSQLState()).append("\n");
        sb.append("[!] VendorError: ").append(e.getErrorCode()).append("\n");
        return sb.toString();

    }
}
