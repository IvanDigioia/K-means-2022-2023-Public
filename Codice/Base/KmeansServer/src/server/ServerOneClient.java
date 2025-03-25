package server;

import java.io.*;
import java.net.*;
import java.sql.SQLException;


import data.*;
import mining.KMeansMiner;

/**
 * Classe che gestisce le connessioni con i client
 */
public class ServerOneClient extends Thread {
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    KMeansMiner kmeans;

    //metodi

    /**
     * Costruttore di classe. Inizializza gli attributi socket, in e out. Avvia il thread.
     * @param s socket su cui il server riceve la connessione
     * @throws IOException in caso di errori con l'invio/ricezione di messaggi
     */
    public ServerOneClient(Socket s) throws IOException{
        this.socket=s;
        in=new ObjectInputStream(socket.getInputStream());
        out=new ObjectOutputStream(socket.getOutputStream()); //come da manuale dalla prof, da vedere se funzionano davvero
        start();
    }

    /**
     * Esegue le operazioni di invio/ricezione con il client.
     * Si occupa di mandare i messaggi di conferma ricezione, di stampa dei cluster, e, nel caso, di messaggi di errore
     */
    @Override
    public void run(){
        int option=4;
        String tablename=null;
        int k=0;
        Data data; //perchè non inizializzarlo qui? perchè si presume che possiamo avere più tabelle nel DB, quindi magari vogliamo stamparne un'altra nello stesso programma
        while(!socket.isClosed()){
            try{
                option=(int) in.readObject();
            }catch(IOException e){ //brutto
                try{
                    out.writeObject(e.getMessage());
                }catch(IOException e1){
                    System.out.println(e.getMessage());
                    option=4; //lo manda alla chiusura della socket
                }
            }catch(ClassNotFoundException e){
                try{
                    out.writeObject(e.getMessage());
                }catch(IOException e1){
                    System.out.println("IOException");
                }
            }
            switch(option){
                case 0:

                    try {
                        tablename=(String) in.readObject();
                        if (!tablename.equals("playtennis")) //controllo molto arificiale, sarebbe da controllare come espellere l'errore da data()
                            throw new IOException("[404] La tabella "+tablename+" non esiste");
                        data=new Data(tablename); //gusto per espellere l'errore e controllare se la tabella esiste
                        //controlli per vedere se esiste!!
                        out.writeObject("OK");
                    }catch(ClassNotFoundException e){
                        try{
                            out.writeObject(e.getMessage());
                        }catch(IOException e1){
                            System.out.println("IOException");
                        }
                    }catch(Exception e){ //raccogle: SQLException,IOException, DatabaseConnectionException, EmptySetException
                        try{
                            throw new ServerException(e);
                        }catch(ServerException se){
                            try{
                                out.writeObject(se.getMessage());
                            }catch(IOException e1){}
                        }
                    }

                    break;
                case 1:
                    try {
                        k=(int) in.readObject();
                        data=new Data(tablename);
                        this.kmeans=new KMeansMiner(k);
                        int numIter = kmeans.kmeans(data); //non lo usiamo MA NON TOCCARLO
                        out.writeObject("OK");
                        String tempString=kmeans.getC().toString(data);
                        //System.out.println(tempString); //DEBUG
                        out.writeObject(tempString);
                        out.writeObject(tempString);
                    }catch(ClassNotFoundException e){
                        try{
                            out.writeObject(e.getMessage());
                        }catch(IOException e1){
                            System.out.println("IOException");
                        }
                    }catch(Exception e){ //raccogle: SQLException,IOException, DatabaseConnectionException, EmptySetException, OutOfRangeSampleSize
                        try{
                            throw new ServerException(e);
                        }catch(ServerException se){
                            try{
                                out.writeObject(se.getMessage());
                            }catch(IOException e1){}
                        }
                    }
                    break;
                case 2:
                    try {
                        //answer.concat(String.valueOf(k)); //qui attacca la stringa e k cluster
                        String answer = "playtennis";
                        answer = answer + k;
                        kmeans.salva(answer);
                        out.writeObject("OK");
                    }catch(IOException e){
                        try{
                            throw new ServerException(e);
                        }catch(ServerException se){
                            try{
                                out.writeObject(se.getMessage());
                            }catch(IOException e1){}
                        }
                    }
                    break;
                case 3:
                    try{
                        tablename=(String) in.readObject();
                        k=(int) in.readObject();
                        data=new Data(tablename);
                        String answer=tablename+k;
                        this.kmeans=new KMeansMiner(answer);
                        String tempString=kmeans.getC().toString(data);
                        out.writeObject("OK"); //if(result.equals("OK"));
                        out.writeObject(tempString);
                    }catch(FileNotFoundException e){
                        try{
                            out.writeObject("[404] "+e.getMessage());
                        }catch(IOException e1){
                            System.out.println("[!] impossibile comunicare con il client");
                        }
                    }
                    catch(IOException e){ //brutto
                        System.out.println("[!] IOException: "+e.getMessage());
                        option=4;
                    }catch(ClassNotFoundException e){
                        try{
                            out.writeObject(e.getMessage());
                        }catch(IOException e1){
                            System.out.println("IOException");
                        }
                    }catch(Exception e){ //raccogle: SQLException,IOException, DatabaseConnectionException, EmptySetException, FileNotFoundException
                        try{
                            throw new ServerException(e);
                        }catch(ServerException se){
                            try{
                                out.writeObject(se.getMessage());
                            }catch(IOException e1){}
                        }
                    }
                    break;
                case 4:
                    try{
                        socket.close();
                    }catch(IOException e){
                        System.out.println("Errore durante la chiusura della socket. Forzatura chiusura...");
                    }//non ho un finally perchè da specifiche non posso espellere l'eccezione fuori dalla funzione
                    break;

                }

            }

        }

    }


