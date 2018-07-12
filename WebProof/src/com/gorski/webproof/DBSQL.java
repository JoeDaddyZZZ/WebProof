package com.gorski.webproof;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Generic database connection object
 * @author jgorski
 *
 */
public class DBSQL {
	public String dbHost = "ord-qatestdb01";
	public String dbName = "jobwalker";
	public String dbUser = "qauser";
	public String dbPassword = "ClAr1ty!";
	private Connection con = null;
			
	
	/**
	 * create a direct database connection
	 * @param dbHost
	 * @param dbName
	 * @param dbUser
	 * @param dbPassword
	 */
public DBSQL(String dbHost,  String dbName, String dbUser, String dbPassword) {
		super();
		this.dbHost = dbHost;
		this.dbName = dbName;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
		con = this.getDBCon();
	}


/**
 * create a direct database connection using default user/password
 * @param dbHost
 * @param dbName
 */
public DBSQL(String dbHost,  String dbName) {
	super();
	this.dbHost = dbHost;
	this.dbName = dbName;
	con = this.getDBCon();
}
/**
 * 
 * @param dbName
 */
public DBSQL(String dbName) {
	super();
	/*
	 * read properties file
	 */
	Properties props = Tools.loadProp();
	//loadProp.listProps(props);
	this.dbHost = (String) props.get("dbServer");
	this.dbUser = (String) props.get("dbName");
	this.dbPassword = (String) props.get("dbPassword");
	this.dbName = dbName;
	con = this.getDBCon();
}
/**
 * create a direct database connection using default user/password/host/database
 * 
 */
public DBSQL() {
	con = this.getDBCon();
}


/**
 * main test driver
 * returns process_master database from jobwalker
 * @param args
 */
public static void main(String args[]){
		
	DBSQL qatestDB = new DBSQL(
			"jobwalker"
	);
	String query = "select * from process_master where start > CURRENT_DATE-3 order by job_id desc";
	//query = "select * from job";
	System.out.println(query);
	ResultSet rs;
	Integer jobid;
	String clientDBName=null;
	try {
		rs = qatestDB.getDBRow(query);
		System.out.println("ran query");
		while (rs.next()) {
			jobid = rs.getInt("job_id");
			clientDBName = rs.getString("db_name");
			System.out.println(jobid+ " " + clientDBName);
		} //end while
	} catch (SQLException  e) {
		System.out.println("ERR "+query);
		e.printStackTrace();
	}
	qatestDB.closeCon();
	System.out.println("closedCon");
}

public String getStringValue(String sql) {
	String value = null;
	ResultSet rs;
	try {
		rs = getDBRow(sql);
		while (rs.next()) {
			value = rs.getString(1);
		} //end while
		closeCon();
	} catch (SQLException  e) {
		e.printStackTrace();
	}
	
	return value;
}
	

	/**
	 * close connection
	 */
	public  void closeCon() {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * generate query results for given sql query
	 * @param query
	 * @return
	 */
	public  ResultSet getDBRow(String query) {
		ResultSet rs = null;
		try {
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException  e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * update data 
	 * @param statement
	 * @return
	 */
	public int  putDBInsert(String statement) {
		int result =0;
		try {
			Statement stmt = con.createStatement();
			result = stmt.executeUpdate(statement);
		} catch (SQLException  e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * create db connection for the class
	 * @return
	 */
	public  Connection getDBCon() {
		String dbUrl = "jdbc:mysql://"+dbHost+"/"+ dbName + "?zeroDateTimeBehavior=convertToNull" ;
		String dbClass = "com.mysql.jdbc.Driver";
		//System.out.println(dbUrl);
		Connection con = null;
		try {
			Class.forName(dbClass);
			con = DriverManager.getConnection(dbUrl,dbUser,dbPassword);
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println("SQL ERROR: " + dbUrl + " " );
			e.printStackTrace();
		}
		return con;
	}

}

