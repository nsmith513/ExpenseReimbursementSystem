package com.project.one.service;

import java.sql.SQLException;

import com.project.one.dao.UserDao;
import com.project.one.model.User;

/**
 * Service layer for users table.
 * 
 * @author Nicholas Smith
 */
public class UserService {
	private static UserService inst = null;
	
	private UserDao userDao;
	
	private UserService (UserDao userDao) {
		this.userDao = userDao;
	}
	
	/**
	 * @return The current instance of the singleton class {@code UserService}.
	 */
	public static UserService getInstance() {
		if (inst == null)
			inst = new UserService(UserDao.getInstance());
		return inst;
	}
	
	/**
	 * @param userDao - The instance of {@code UserDao} to use.
	 * @return The current instance of the singleton class {@code UserService}.
	 */
	public static UserService getInstance(UserDao userDao) {
		if (inst == null)
			inst = new UserService(userDao);
		return inst;
	}
	
	/**
	 * Gets user account info.
	 * 
	 * @param id - ID of the account.
	 * @return A {@code User} object, or {@code null} if
	 * an account with the specified ID does not exist.
	 * @throws SQLException
	 */
	public User getUser(int id) throws SQLException {
		return userDao.selectUser(id);
	}
	
	/**
	 * Gets user account info.
	 * 
	 * @param username - Username of the account.
	 * @return A {@code User} object, or {@code null} if
	 * an account with the specified username does not exist.
	 * @throws SQLException
	 */
	public User getUser(String username) throws SQLException {
		return userDao.selectUser(username);
	}
}
