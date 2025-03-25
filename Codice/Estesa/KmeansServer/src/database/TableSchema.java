package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * Rappresenta lo schema di una tabella nel database, fornendo informazioni sugli attributi della tabella.
 */
public class TableSchema {
	DbAccess db;

	/**
	 * Rappresenta una colonna nella tabella, contenente il nome e il tipo di dati.
	 */
	public class Column{
		private String name;
		private String type;

		/**
		 * Costruttore della classe. Costruisce una nuova colonna con il nome e il tipo specificati.
		 *
		 * @param name Il nome della colonna.
		 * @param type Il tipo di dati della colonna.
		 */
		Column(String name,String type){
			this.name=name;
			this.type=type;
		}

		/**
		 * Restituisce il nome della colonna.
		 *
		 * @return Il nome della colonna.
		 */
		public String getColumnName(){
			return name;
		}

		/**
		 * Verifica se il tipo della colonna è numerico.
		 *
		 * @return true se il tipo della colonna è numerico, false altrimenti.
		 */
		public boolean isNumber(){
			return type.equals("number");
		}

		/**
		 * Restituisce una rappresentazione testuale della colonna nel formato "nome:tipo".
		 *
		 * @return Il nome della colonna e il tipo.
		 */
		@Override
		public String toString(){
			return name+":"+type;
		}
	}
	List<Column> tableSchema=new ArrayList<Column>();

	/**
	 * Costruisce uno schema per la tabella specificata nel database.
	 * @param db l'accesso al db
	 * @param tableName Il nome della tabella di cui si vuole ottenere lo schema.
	 * @throws SQLException  Se si verifica un errore durante l'accesso al database.
	 */
	public TableSchema(DbAccess db, String tableName) throws SQLException{
		this.db=db;
		HashMap<String,String> mapSQL_JAVATypes=new HashMap<String, String>();
		//http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
		mapSQL_JAVATypes.put("CHAR","string");
		mapSQL_JAVATypes.put("VARCHAR","string");
		mapSQL_JAVATypes.put("LONGVARCHAR","string");
		mapSQL_JAVATypes.put("BIT","string");
		mapSQL_JAVATypes.put("SHORT","number");
		mapSQL_JAVATypes.put("INT","number");
		mapSQL_JAVATypes.put("LONG","number");
		mapSQL_JAVATypes.put("FLOAT","number");
		mapSQL_JAVATypes.put("DOUBLE","number");
		
		
	
		 Connection con=db.getConnection();

		 DatabaseMetaData meta = con.getMetaData();
	     ResultSet res = meta.getColumns(null, null, tableName, null);

	     while (res.next()) {
	         
	         if(mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
	        		 tableSchema.add(new Column(
	        				 res.getString("COLUMN_NAME"),
	        				 mapSQL_JAVATypes.get(res.getString("TYPE_NAME")))
	        				 );
	
	         
	         
	      }
	      res.close();
	
	
	    
	    }

	/**
	 * Restituisce il numero di attributi nella tabella.
	 *
	 * @return Il numero di attributi nella tabella.
	 */
		public int getNumberOfAttributes(){
			return tableSchema.size();
		}

	/**
	 * Restituisce la colonna corrispondente all'indice specificato.
	 *
	 * @param index L'indice della colonna desiderata.
	 * @return La colonna corrispondente all'indice specificato.
	 */
		public Column getColumn(int index){
			return tableSchema.get(index);
		}

		
	}

		     


