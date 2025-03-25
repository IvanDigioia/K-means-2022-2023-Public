package maininterface;

import server.ServerException;
import connessione.Connessione;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.util.converter.IntegerStringConverter;

import java.awt.*;
import java.io.File;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.UnaryOperator;

/**
 * Controller per la schermata di caricamento da file.
 */
public class ScenaCaricaFile {

    private static Connessione conn;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    @FXML
    private Button Esegui;

    @FXML
    private Button Esplora;

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
     * Metodo l'esecuzione del programma dedicato al Carica da file tramite gli input dell'utente.
     * @param event click del bottone "Esegui".
     */
    @FXML
    void EseguiFile(ActionEvent event) {
        try {
            String tabella = this.NomeTabella.getText(); //ottengo il nome della tabella
            int cluster = Integer.parseInt(this.NumeroCluster.getText()); //ottengo il numero di cluster
            in=conn.getConnectionInput(); //ottengo la connessione per il client
            out=conn.getConnectionOutput(); //ottengo la connessione per il server
            out.writeObject(3); //per riferimento, funzione MainTest.storeTableFromFile()
            out.writeObject(tabella);
            out.writeObject(cluster);
            String resultStirng=null;
            Object res=in.readObject(); //restituisce OK se la lettura della tabella da file è andata a buon fine
            if (res instanceof String)
                resultStirng=(String) res;
            else if (res instanceof ServerException)
                throw (ServerException) res;
            if (!resultStirng.equals("OK")) {
                throw (ServerException) res;
            } else {
                risultati.appendText("Clustering Output:" + (String) in.readObject()); //mostra a video il risultato
            }
        } catch(IOException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Problema dal server");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }catch(ClassNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Oops");
            alert.setHeaderText("Classe non trovata");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }catch(ServerException e){
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
     * Metodo l'esplorazione della cartella 'File memorizzati'.
     * @param event click del bottone "Esplora cartella".
     */
    @FXML
    void esploraCartella(ActionEvent event) { //funzione di apertura della cartella 'File memorizzati'
        String currentDirectory = System.getProperty("user.dir"); //ottengo la directory dell'utente
        String targetDir = "K-means-22-23"; //arrivo fino alla cartella 'K-means-22-23' e mi fermo
        //questa operazione prende il percorso della directory corrente, individua l'ultima occorrenza di una specifica directory
        int endIndex = currentDirectory.lastIndexOf(targetDir) + targetDir.length();
        //all'interno di endIndex restituiscono una nuova stringa (desiredDir) contenente tutto il percorso fino alla fine di questa directory
        String desiredDir = currentDirectory.substring(0, endIndex);
        String folderPath = desiredDir + File.separator + "File memorizzati"; //entro nella cartella 'File memorizzati'
        File folder = new File(folderPath);
        try {
            Desktop.getDesktop().open(folder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo la pulizia del pannello dei risultati.
     * @param event click del bottone "Pulisci pannello".
     */
    @FXML
    void pulisciPannello(ActionEvent event){
        this.risultati.setText("");
    } //sostituisce tutto il testo del pannello degli output con un carattere vuoto

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
            System.out.println(e.getMessage());
        }
    }

    /**
     * Inizializza il campo NumeroCluster. Vincola NumeroCluster ad accettare soltanto valori numerici
     */
    @FXML
    public void initialize() {
        UnaryOperator<TextFormatter.Change> filter= change -> { //funzione lambda
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
