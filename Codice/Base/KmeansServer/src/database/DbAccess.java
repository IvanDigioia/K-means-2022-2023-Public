package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * classe che realizza l'accesso alla base di dati
 */
public class DbAccess {
    private String DRIVER_CLASS_NAME="com.mysql.cj.jdbc.Driver";
    private final String DBMS ="jdbc:mysql";
    private final String SERVER="localhost"; //identificativo del server su cui risiede la base di dati (per esempio localhost)
    private final String DATABASE="MapDB"; //contiene il nome della base di dati
    private final String PORT="3306"; // porta du cui il DBMS MySQL accetta le connessioni
    private final String USER_ID="MapUser"; //contiene il nome dell'utente per l'accesso alla base di dati
    private final String PASSWORD="map"; //contiene la password di autenticazione per l'utente identificato da USER_ID
    Connection conn;

    //Metodi

    /**
     * impartisce al class loader l’ordine di caricare il driver mysql, inizializza la connessione riferita da conn.
     * @throws DatabaseConnectionException in caso di fallimento della connessione al database
     */
    public void initConnection() throws DatabaseConnectionException {
       try {
            Class.forName(DRIVER_CLASS_NAME).newInstance();
        }catch(ClassNotFoundException e) {
            throw new DatabaseConnectionException(e);
        }catch (InstantiationException e){
            throw new DatabaseConnectionException(e);
        }catch(IllegalAccessException e){
            throw new DatabaseConnectionException(e);
        }
        String connectionString= DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE + "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";
        //System.out.println("Connection String: " +connectionString); //DEBUG
        try{
            conn = DriverManager.getConnection(connectionString, USER_ID, PASSWORD);
        }catch(SQLException e){
            throw new DatabaseConnectionException(e);
        }

    }

    /**
     * restituisce la connessione attiva
     * @return conn
     */
    Connection getConnection(){return conn;}

    /**
     * chiude la connessione
     * @throws DatabaseConnectionException qualora la connessione al db non si chiudesse correttamente
     */
    public void closeConnection() throws DatabaseConnectionException { //throws non indicato dalla prof, ma comunque sqlexeption è controllata, quindi uscirebbe quella, quindi boh
        try{
            conn.close();
        }catch (SQLException e){
            throw new DatabaseConnectionException(e);
        }
    }

}
