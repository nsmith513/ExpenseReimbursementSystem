package com.project.one;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;

import com.project.one.dao.DataAccessObject;

public abstract class DatabaseTest {

	private static final String H2_DIR = "./h2";
	
	private static Connection conn;
	private static PreparedStatement psResetData;
	
	private static PreparedStatement psSelectUserId;
	
	@BeforeClass
	public static void makeFakeConnection() throws Exception {
		// Clean out the database in case its been created by a previous test
		FileUtils.cleanDirectory(new File(H2_DIR));
		
		// Initialize DAOs with h2 connection
		try {
			conn = DriverManager.getConnection("jdbc:h2:" + H2_DIR + "/data;MODE=PostgreSQL", "sa", "sa");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot establish h2 database connection.");
		}
		DataAccessObject.connect(conn);
		
		// Set up the database schema
		conn.prepareStatement(Support.readFile(new File("./schema.sql"))).executeUpdate();
		
		// Prepare statement to reset all data in the database
		psResetData = conn.prepareStatement(Support.readFile(new File("./schema-testing.sql")));
		
		// Define some utility statements
		psSelectUserId = connection().prepareStatement("SELECT ers_users_id FROM users WHERE ers_username = ?");
	}
	
	@Before
	public void resetAllData() throws Exception {
		psResetData.executeUpdate();
	}

	protected static Connection connection() { return conn; }
	
	protected int getUserId(String uname) throws SQLException {
		psSelectUserId.setString(1, uname);
		ResultSet rs = psSelectUserId.executeQuery();
		rs.next();
		return rs.getInt(1);
	}

}
