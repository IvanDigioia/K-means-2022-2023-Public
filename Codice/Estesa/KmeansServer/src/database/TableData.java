package database;

import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import database.TableSchema.Column;

/**
 * Classe che modella i singoli attributi e transazioni estrapolate dal DB
 */
public class TableData {

	DbAccess db;


	/**
	 * Modella l'insieme di transazioni collezionate in una tabella.
	 * @param db la connessione alla tabella MySQL
	 */
	public TableData(DbAccess db) {this.db=db;}
	/**
	 * Ricava lo schema della tabella con nome table.
	 * Esegue una interrogazione per estrarre le tuple distinte da tale tabella.
	 * Per ogni tupla del resultset, si crea un oggetto, istanza della classe Example,
	 * il cui riferimento va incluso nella lista da restituire.
	 * In particolare, per la tupla corrente nel resultset,
	 * si estraggono i valori dei singoli campi (usando getFloat() o getString()),
	 * e li si aggiungono all’oggetto istanza della classe Example che si sta costruendo.
	 * @param table	il nome della tabella nel database
	 * @return	Lista di transazioni distinte memorizzate nella tabella
	 * @throws SQLException in caso di operazioni illegali
	 * @throws EmptySetException in caso in cui la tabella fosse vuota
	 */
	public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException{
		List<Example> result=new ArrayList<Example>();
		Statement s= db.getConnection().createStatement();
		String tempstatement="SELECT DISTINCT * FROM "+table;
		//System.out.println(tempstatement); //DEBUG
		ResultSet r=s.executeQuery("SELECT DISTINCT * FROM "+table);
		if (!r.next()) //verifica se il resultset è vuoto
			throw new EmptySetException();
			else r.next();
		while (!r.isAfterLast()) {//scorriamo le righe. qui abbiamo 5 righe, ma da capire come fare il loop
			//le colonne iniziano da 1
			Example temp = new Example();
			temp.add(r.getString(1));	//colonna outlook
			temp.add(r.getDouble(2));	//colonna temperature (da trattare come double)
			temp.add(r.getString(3)); 	//colonna humidity
			temp.add(r.getString(4)); 	//colonna wind
			temp.add(r.getString(5)); 	//colonna playtennis
			result.add(temp);
			r.next();
		}
		return result;
	}



	/**
	 *	Formula ed esegue una interrogazione SQL per estrarre i valori distinti ordinati di column
	 *	e popolare un insieme da restituire.
	 * @param table	Nome della tabella
	 * @param column	nome della colonna nella tabella
	 * @return	Insieme di valori distinti ordinati in modalità ascendente che l’attributo identificato da nome column assume nella tabella identificata dal nome table.
	 * @throws SQLException nel caso di operazioni illegali SQL
	 */
	public  Set<Object>getDistinctColumnValues(String table,Column column) throws SQLException{
		TreeSet<Object> result=new TreeSet<Object>(); //oppure treeset<Examples>? boh
		Statement s=db.getConnection().createStatement();
		String tempstatement="SELECT DISTINCT "+column+"FROM "+table;
		ResultSet r=s.executeQuery("SELECT DISTINCT "+column+"FROM "+table);
		while(!r.isAfterLast()){
			result.add(r.getObject(1));
			r.next();
		}
	return result;
	}

	/**
	 * Formula ed esegue una interrogazione SQL per estrarre il valore aggregato (minimo o massimo) cercato nella colonna <i>column</i>
	 * della tabella di nome <i> table</i>
	 * @param table nome della tabella
	 * @param column nome della colonna
	 * @param aggregate operatore SQL di aggregazione (min,max)
	 * @return aggregato cercato
	 * @throws SQLException qualora vengano effettuare operazioni SQL illegali
	 * @throws NoValueException eccezione se il resultset è vuoto o uguale a null
	 */
	public  Object getAggregateColumnValue(String table,Column column,QUERY_TYPE aggregate) throws SQLException,NoValueException {
		Object result;
		Statement s = db.getConnection().createStatement();
		String tempstatement = "SELECT " + aggregate + "(" + column.getColumnName() + ") " + "FROM " + table+" AS "+column.getColumnName();
		//System.out.println(tempstatement); //DEBUG
		ResultSet r = s.executeQuery(tempstatement);
		r.next(); // per qualche motivo r è in una posizione prima dell'inizio del risultato, quindi...
		if (r.getObject(1) == null) {
			throw new NoValueException();
		}
		else{
			result = r.getDouble(1);
		}
		return result;
	}

}
