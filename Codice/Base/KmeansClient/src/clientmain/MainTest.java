package clientmain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import keyboardinput.Keyboard;

/**
 * Classe principale per il client.
 * Crea la connessione, si connette al server e legge/riceve le informazioni e le stampa
 */
public class MainTest {

	private ObjectOutputStream out; //stream con output del server
	private ObjectInputStream in ;  //stream con richieste del client

	/**
	 * Costruttore della classe MainTest per inizializzare il socket e la connessione al server.
	 * @param ip indirizzo IP del server
	 * @param port numero di porta del server
	 * @throws IOException se si verifica un errore di I/O durante l'inizializzazione della connessione
	 */
	public MainTest(String ip, int port) throws IOException{
		InetAddress addr = InetAddress.getByName(ip); //ip
		System.out.println("addr = " + addr);
		Socket socket = new Socket(addr, port); //Port
		System.out.println(socket);
		
		out = new ObjectOutputStream(socket.getOutputStream()); //stream con output del server
		in = new ObjectInputStream(socket.getInputStream()); 	//stream con richieste del client
	}

	/**
	 * Metodo per visualizzare il menu e ottenere la scelta dell'utente.
	 * @return la scelta dell'utente
	 */
	private int menu(){
		int answer;
		System.out.println("Scegli una opzione");
		do{
			System.out.println("(1) Carica Cluster da File");
			System.out.println("(2) Carica Dati");
			System.out.print("Risposta:");
			answer=Keyboard.readInt();
		}
		while(answer<=0 || answer>2);
		return answer;
		
	}

	/**
	 * Legge gli input dell'utente e manda il nome del file al server. Gli viene restituito il contenuto del file
	 * @return l'apprendimento ottenuto dal server
	 * @throws IOException se si verifica un errore di I/O
	 * @throws SocketException se si verifica un errore del socket
	 * @throws ServerException se il file non esiste o se si sono verificati altri problemi nel server
	 * @throws ClassNotFoundException se la classe richiesta non viene trovata
	 */
	private String learningFromFile() throws SocketException, ServerException,IOException,ClassNotFoundException{
		out.writeObject(3);
		
		System.out.print("Nome tabella:");
		String tabName=Keyboard.readString();
		out.writeObject(tabName);
		System.out.print("Numero iterate:");
		int k=Keyboard.readInt();
		out.writeObject(k);
		String result = (String)in.readObject();
		if(result.equals("OK"))
			return (String)in.readObject();
		else throw new ServerException(result);
		
	}

	/**
	 * Legge gli input dell'utente e restituire al server il nome della tabella nel database.
	 * @throws IOException se si verifica un errore di I/O
	 * @throws SocketException se si verifica un errore del socket
	 * @throws ServerException se si verifica un'eccezione sul server e deve essere letta sul client
	 * @throws ClassNotFoundException se la classe richiesta non viene trovata
	 */
	private void storeTableFromDb() throws SocketException, ServerException,IOException,ClassNotFoundException{
		out.writeObject(0);
		System.out.print("Nome tabella:");
		String tabName=Keyboard.readString();
		out.writeObject(tabName);
		String result = (String)in.readObject();
		if(!result.equals("OK"))
			throw new ServerException(result);
		
	}

	/**
	 * Legge gli input dell'utente e manda al server il numero di cluster su cui fare il Kmeans.
	 * @return l'apprendimento ottenuto dal server
	 * @throws IOException se si verifica un errore di I/O
	 * @throws SocketException se si verifica un errore del socket
	 * @throws ServerException se il numero di cluster è troppo elevato
	 * @throws ClassNotFoundException se la classe richiesta non viene trovata
	 */
	private String learningFromDbTable() throws SocketException, ServerException,IOException,ClassNotFoundException{
		out.writeObject(1);
		System.out.print("Numero di cluster:");
		int k=Keyboard.readInt();
		out.writeObject(k);
		String result = (String)in.readObject();
		if(result.equals("OK")){
			System.out.println("Clustering output:"+in.readObject());
			return (String)in.readObject();
		}
		else throw new ServerException(result);
	}

	/**
	 * Manda al server la richiesta di salvare il risultato su file.
	 * @throws IOException se si verifica un errore di I/O
	 * @throws SocketException se si verifica un errore del socket
	 * @throws ServerException se si verifica un'eccezione sul server
	 * @throws ClassNotFoundException se la classe richiesta non viene trovata
	 */
	private void storeClusterInFile() throws SocketException, ServerException,IOException,ClassNotFoundException{
		out.writeObject(2);

		String result = (String)in.readObject();
		if(!result.equals("OK"))
			 throw new ServerException(result);
		
	}

	/**
	 * Metodo principale per l'esecuzione del client.
	 * @param args argomenti della riga di comando: [IP_server] [porta_server]
	 */
	public static void main(String[] args) {
		String ip=args[0];
		int port = Integer.valueOf(args[1]).intValue();
		MainTest main=null;
		try{
			main=new MainTest(ip,port);
		}
		catch (IOException e){
			System.out.println(e);
			return;
		}

		do{
			int menuAnswer=main.menu();
			switch(menuAnswer)
			{
				case 1:
					try {
						String kmeans=main.learningFromFile();
						System.out.println(kmeans);
					}
					catch (SocketException e) {
						System.out.println(e);
						return;
					}
					catch (FileNotFoundException e) {
						System.out.println(e);
						return ;
					} catch (IOException e) {
						System.out.println(e);
						return;
					} catch (ClassNotFoundException e) {
						System.out.println(e);
						return;
					}
					catch (ServerException e) {
						System.out.println(e.getMessage());
					}
					break;
				case 2: // learning from db
				
					while(true){
						try{
							main.storeTableFromDb();
							break; //esce fuori dal while
						}
						
						catch (SocketException e) {
							System.out.println(e);
							return;
						}
						catch (FileNotFoundException e) {
							System.out.println(e);
							return;
							
						} catch (IOException e) {
							System.out.println(e);
							return;
						} catch (ClassNotFoundException e) {
							System.out.println(e);
							return;
						}
						catch (ServerException e) {
							System.out.println(e.getMessage());
						}
					} //end while [viene fuori dal while con un db (in alternativa il programma termina)]
						
					char answer='y'; //itera per learning al variare di k
					do{
						try
						{
							String clusterSet=main.learningFromDbTable();
							//System.out.println(clusterSet); commentato in quanto stampa due volte le stesse cose... sarebbe codice dato dal docente, e non vorremmo toccarlo
							
							main.storeClusterInFile();
									
						}
						catch (SocketException e) {
							System.out.println(e);
							return;
						}
						catch (FileNotFoundException e) {
							System.out.println(e);
							return;
						} 
						catch (ClassNotFoundException e) {
							System.out.println(e);
							return;
						}catch (IOException e) {
							System.out.println(e);
							return;
						}
						catch (ServerException e) {
							System.out.println(e.getMessage());
						}
						System.out.print("Vuoi ripetere l'esecuzione?(y/n)");
						answer=Keyboard.readChar();
					}
					while(answer=='y');
					break; //fine case 2
					default:
					System.out.println("Opzione non valida!");
			}
			
			System.out.print("Vuoi scegliere una nuova operazione da menu?(y/n)");
			if(Keyboard.readChar()!='y')
				break;
			}
		while(true);
		}
	}



