package com.project.one.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.project.one.Support;

/**
 * Parent class for data access objects. Contains a static initializer for
 * establishing a connection to the database.
 * 
 * @author Nicholas Smith
 */
public abstract class DataAccessObject {
	/**
	 * Object describing the status of the data access objects' connection to the database.
	 * The {@code connected} field can be accessed directly and will tell whether the DAOs are connected.
	 * The {@code what} function will return a string describing what went wrong in the case that
	 * {@code connected} is false.
	 * 
	 * @author Nicholas Smith
	 */
	public static class ConnectionStatus {
		public final boolean connected;
		private String whatField;
		
		public ConnectionStatus(boolean connected) {
			this.connected = connected;
			whatField = connected ? "connected" : "not connected";
		}
		
		public ConnectionStatus(String whatField) {
			connected = false;
			this.whatField = whatField;
		}
		
		public ConnectionStatus(boolean connected, String whatField) {
			this.connected = connected;
			this.whatField = whatField;
		}
		
		public String what() {
			return whatField;
		}
	}
	
	static {
        // Initialize JDBC's PostgreSQL driver
		try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e) {
            System.out.println("Unable to initilize PostgreSQL driver: " + e.getMessage());
        }
    }
	
	private static Connection conn = null;
	
	/**
	 * Attempt to connect to the database defined in daoConfig.json.
	 * 
	 * @return {@code ConnectionStatus} object describing the status of the connection.
	 */
	public static ConnectionStatus connect() {
		ConnectionStatus status;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> config = Support.MAPPER.readValue(
					DataAccessObject.class.getClassLoader().getResourceAsStream("daoConfig.json"), HashMap.class);
			
			@SuppressWarnings("unchecked")
			HashMap<String, String> login = Support.MAPPER.readValue(
					new FileInputStream(config.get("dbLoginInfoPath")), HashMap.class);
			
			status = DataAccessObject.connect(login.get("url"), login.get("username"), login.get("password"));
		} catch (JsonParseException e) {
			return new ConnectionStatus(e.getMessage());
		} catch (JsonMappingException e) {
			return new ConnectionStatus(e.getMessage());
		} catch (IOException e) {
			return new ConnectionStatus(e.getMessage());
		}
		return status;
	}
	
	/**
	 * Attempt to connect to a database.
	 * 
	 * @param url - URL of the database.
	 * @param uname - Username to use.
	 * @param pwd - Password to use.
	 * @return {@code ConnectionStatus} object describing the status of the connection.
	 */
	public static ConnectionStatus connect(String url, String uname, String pwd) {
		try {
			conn = DriverManager.getConnection(url, uname, pwd);
		} catch (SQLException e) {
			return new ConnectionStatus(e.getMessage());
		}
		return new ConnectionStatus(true);
	}
	
	/**
	 * Set the connection to the database.
	 * 
	 * @param c - Connection to set.
	 * @return {@code ConnectionStatus} object describing the status of the connection.
	 */
	public static ConnectionStatus connect(Connection c) {
		conn = c;
		return new ConnectionStatus(true);
	}
	
	/**
	 * @return The data access objects' connection to the database.
	 */
	protected static Connection connection() { return conn; }
}
