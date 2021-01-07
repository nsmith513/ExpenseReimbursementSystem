package com.project.one;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Level;

import com.project.one.dao.DataAccessObject;

/**
 * Contains logic to execute on server start.
 * 
 * @author Nicholas Smith
 */
public class Init implements ServletContextListener {
	static {
		Support.logSetLevel(Level.ALL);
	}
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		DataAccessObject.ConnectionStatus status = DataAccessObject.connect();
		if (status.connected) {
			Support.logInfo("Connection to database established successfully.");
		} else {
			Support.logFatal("Unable to establish connection to database!\n" + status.what());
			System.exit(1);
		}
	}
}
