package maininterface;

import server.ServerException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.util.converter.IntegerStringConverter;

import java.io.*;
import java.util.function.UnaryOperator;

import connessione.Connessione;

/**
 * Classe controller per gestire la schermata di caricamento dei dati da database.
 */
public class ScenaCarica{

    private static Connessione conn;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;


    @FXML
    private Button EseguiSalva;

    @FXML
    private Button Indietro;

    @FXML
    private TextField NomeTabella;

    @FXML
    private TextField NumeroCluster;

    @FXML
    private TextArea risultati;

    @FXML
    private Button Pulisci;

    /**
     * Metodo l'esecuzione del programma dedicato al Carica tramite gli input dell'utente.
     * @param event click del bottone "Esegui".
     */
    @FXML
    void Esegui(ActionEvent event) {
        try{
            String tabella = this.NomeTabella.getText(); //ottengo il nome della tabella
            int cluster = Integer.parseInt(this.NumeroCluster.getText()); //ottengo il numero di cluster
            in=conn.getConnectionInput(); //ottengo la connessione per il client
            out=conn.getConnectionOutput(); //ottengo la connessione per il server
            out.writeObject(0); //per riferimento, funzione MainTest.storeTableFromDb()
            out.writeObject(tabella); //restituisce OK se la lettura della tabella è andata a buon fine
            Object res=in.readObject();
            String stringResult=null;
            if (res instanceof String)
                stringResult=(String) res;
            else if (res instanceof ServerException)
                throw (ServerException) res;

            out.writeObject(1); //per riferimento, funzione MainTest.learningFromDbTable()
            out.writeObject(cluster); //restituisce OK se la lettuara del cluster è andata a buon fine
            res=in.readObject();
            if (res instanceof String)
                stringResult=(String) res;
            else if (res instanceof ServerException)
                throw (ServerException) res;
            if (stringResult.equals("OK")){
                risultati.appendText("Clustering Output:"+(String) in.readObject()); //mostra a video il risultato
                in.readObject();
            }

            out.writeObject(2); //per riferimento, funzione MainTest.storeClusterInFile()
            out.writeObject(cluster);
            res=in.readObject();
            if (res instanceof String)
                stringResult=(String) res; //restituisce OK se il salvataggio è andato a buon fine
            else if (res instanceof ServerException)
                throw (ServerException) res;

        }catch(IOException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Problema dal server");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }catch(ClassNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Oops");
            alert.setHeaderText("Classe non trovata");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        catch(ServerException e){
            Alert alert= new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(e.getOriginClass());
            alert.setHeaderText("Messaggio di errore dal server");
            alert.setContentText(e.getMessage());
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE); //aggiunsta la grandezza della schemata in base al messaggio
            alert.showAndWait();
        }
        catch(NumberFormatException e){
            Alert alert= new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Oops");
            alert.setHeaderText("Numero cluster vuoto");
            alert.setContentText("Assicurati di inserire un numero di cluster");
            alert.showAndWait();
        }
    }

    /**
     * Metodo per pulire il pannello dei risultati.
     * @param event click del bottone "Pulisci pannello".
     */
    @FXML
    void pulisciPannello(ActionEvent event){
        this.risultati.setText(""); //sostituisce tutto il testo del pannello degli output con un carattere vuoto
    }

    /**
     * Metodo ritornare alla scena principale ovvero 'Scena1'.
     * @param event click del bottone "Indietro".
     */
    @FXML
    void tornaScena(ActionEvent event) {
        Controller cs=new Controller(conn);
        try{
            cs.scenaUno(event);
        }catch(IOException e){
            System.out.println("[!] Error while loading javafx scene: "+e.getMessage());
        }
    }

    /**
     * Inizializza il campo NumeroCluster. Vincola NumeroCluster ad accettare soltanto valori numerici
     */
    @FXML
    public void initialize() {
        UnaryOperator<TextFormatter.Change> filter=change -> { //funzione lambda
            String text=change.getControlNewText(); //prendiamo il nuovo testo inserito
            if(text.isEmpty() || text.matches("^[1-9][0-9]*$")) { //regex: accetta al primo carattere valori da 1-9, accetta come successivi caratteri valori da 0-9. se sono diversi d aquesti, non vengono accettati
                return change;
            }
            return null;
        };
        TextFormatter<Integer> textFormatter=new TextFormatter<>(
                new IntegerStringConverter(),null,filter);
        NumeroCluster.setTextFormatter(textFormatter);
    }

    /**
     * Metodo per passare la connessione alla classe attuale.
     * @param conn la connessione da impostare.
     */
    void setConnessione(Connessione conn){this.conn=conn;}

}
