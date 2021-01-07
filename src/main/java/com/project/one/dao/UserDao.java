package com.project.one.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.project.one.model.User;
import com.project.one.model.UserRole;

/**
 * DAO layer for users table.
 * 
 * @author Nicholas Smith
 */
public class UserDao extends DataAccessObject {
	private static UserDao inst = null;
	
	private PreparedStatement psSelectUserById;
	private PreparedStatement psSelectUserByUname;
	
	private UserDao() {
		Connection conn = connection();
		try {
			psSelectUserById = conn.prepareStatement("SELECT * FROM users WHERE ers_users_id = ?");
			psSelectUserByUname = conn.prepareStatement("SELECT * FROM users WHERE ers_username = ?");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return The current instance of the singleton class {@code UserDao}.
	 */
	public static UserDao getInstance() {
		if (inst == null) 
			inst = new UserDao();
		return inst;
	}
	
	/**
	 * @param rs - {@code ResultSet} selected from the user table.
	 * @return A {@code User} object representing a row from {@code rs}.
	 * @throws SQLException
	 */
	private User decodeUser(ResultSet rs) throws SQLException {
		return new User(
				rs.getInt(1),
				rs.getString(2),
				rs.getString(3),
				rs.getString(4),
				rs.getString(5),
				rs.getString(6),
				UserRole.values()[rs.getInt(7)]);
	}
	
	/**
	 * Selects a user account from the users table.
	 * 
	 * @param id - ID of the account.
	 * @return A {@code User} object, or {@code null} if
	 * an account with the specified ID is not in the users table.
	 * @throws SQLException
	 */
	public User selectUser(int id) throws SQLException {
		psSelectUserById.setInt(1, id);
		ResultSet rs = psSelectUserById.executeQuery();
		return rs.next() ? decodeUser(rs) : null;
	}
	
	/**
	 * Selects a user account from the users table.
	 * 
	 * @param username - Username of the account.
	 * @return A {@code User} object, or {@code null} if
	 * an account with the specified username is not in the users table.
	 * @throws SQLException
	 */
	public User selectUser(String username) throws SQLException {
		psSelectUserByUname.setString(1, username);
		ResultSet rs = psSelectUserByUname.executeQuery();
		return rs.next() ? decodeUser(rs) : null;
	}
}
