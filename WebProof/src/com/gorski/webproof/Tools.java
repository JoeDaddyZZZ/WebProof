package com.gorski.webproof;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.xml.datatype.XMLGregorianCalendar;

import com.gorski.webproof.DBSQL;

public class Tools {
	
    public static boolean testString(String s1, String s2) {
    	boolean okay = false;
    	if(s1 != null && s2 != null) {
    		okay = s1.contains(s2);
    		System.out.println(s1 + " = " + s2);
    	} else {
    		if(s1 == null && s2 == null) okay = true;
    	}
    	return okay;
    }

    public static String checkDate(Date dateToCheck, String form) {
    	String checkedString ="";
        SimpleDateFormat sdf = new SimpleDateFormat(form);
       	if(dateToCheck != null) 
       		checkedString = sdf.format(dateToCheck);
		return checkedString;
    }
    public static String checkDate(Date dateToCheck, SimpleDateFormat sdf) {
    	String checkedString ="";
       	if(dateToCheck != null) 
       		checkedString = sdf.format(dateToCheck);
		return checkedString;
    }
	public static String checkDate(XMLGregorianCalendar submitDate, SimpleDateFormat sdf) {
    	String checkedString ="";
       	if(submitDate != null)  {
       		Date dateToCheck = submitDate.toGregorianCalendar().getTime();
       		checkedString = sdf.format(dateToCheck);
       	}
		return checkedString;
	}
    @SuppressWarnings("resource")
	public static List<String> readCSV(String csvFile) {
        BufferedReader br = null;
        String line = "";
        List<String> lines = new ArrayList<String>();
            try {
				br = new BufferedReader(new FileReader(csvFile));
				while ((line = br.readLine()) != null) {
					lines.add(line);
				}
			} catch (FileNotFoundException e) {
				System.out.println("File Not Found");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("File Error");
				e.printStackTrace();
			}
            return lines;

    }
	/**
	 * simple query processor, passes back all data in each row as a comma
	 * separated string
	 * 
	 * @param dbName
	 * @param sql
	 * @return
	 */
	public static List<String> DoSQL(String dbHost, String dbName, String sql) {
		/*
		 * get database data
		 */
		List<String> sqlResults = new ArrayList<String>();
		DBSQL sqlCon = new DBSQL( dbName);
		//Reporter.log("       sql = " + sql);
		ResultSet rs = sqlCon.getDBRow(sql);
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				/*
				 * comma separate if more than one column
				 */
				if(rsmd.getColumnCount()>1) {
					String sqlResult = "";
					for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
						sqlResult = sqlResult.concat(rs.getString(i) + ",");
					}
					sqlResults.add(sqlResult);
				} else {
					sqlResults.add(rs.getString(1));
				}
			}
			sqlCon.closeCon();
		} catch (SQLException e) {
			e.printStackTrace();
			sqlCon.closeCon();
		}
		return sqlResults;
	}
	/**
	 * 
	 * @param dbName
	 * @param sql
	 * @return
	 */
	public static List<String> DoSQL(String dbName, String sql) {
		/*
		 * get database data
		 */
		List<String> sqlResults = new ArrayList<String>();
		DBSQL sqlCon = new DBSQL(dbName);
		//Reporter.log("       sql = " + sql);
		ResultSet rs = sqlCon.getDBRow(sql);
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				/*
				 * comma separate if more than one column
				 */
				if(rsmd.getColumnCount()>1) {
					String sqlResult = "";
					for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
						sqlResult = sqlResult.concat(rs.getString(i) + ",");
					}
					sqlResults.add(sqlResult);
				} else {
					sqlResults.add(rs.getString(1));
				}
			}
			sqlCon.closeCon();
		} catch (SQLException e) {
			e.printStackTrace();
			sqlCon.closeCon();
		}
		return sqlResults;
	}

	   public static Properties loadProp(){
		   InputStream input = null;
		   Properties prop = new Properties();
       	//System.out.println(" trying " + "res/serviceProperties.properties");
       try {
           input = new FileInputStream("res/serviceProperties.properties");
           prop.load(input);
       } catch (FileNotFoundException ex) {
    	   System.out.println(" FILE NOT FOUND " + "res/serviceProperties.properties");
           //Logger.getLogger(loadProp.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
       		System.out.println(" IO error " + "res/serviceProperties.properties");
           //Logger.getLogger(loadProp.class.getName()).log(Level.SEVERE, null, ex);
       }
       return prop;
   }
   public static void listProps(Properties theseprops) {
		for(Object prop:theseprops.keySet()) {
   		System.out.println( " properties "+prop.toString()+": "+ theseprops.get(prop));
   		
   	}
   }


}
