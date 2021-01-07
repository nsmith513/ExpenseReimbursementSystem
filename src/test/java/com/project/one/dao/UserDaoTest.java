package com.project.one.dao;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.project.one.DatabaseTest;
import com.project.one.model.User;
import com.project.one.model.UserRole;

public class UserDaoTest extends DatabaseTest {
	
	private static UserDao userDao;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		userDao = UserDao.getInstance();
	}

	@Test
	public void selectUserByIdTest() throws SQLException {
		User user = userDao.selectUser(getUserId("bob"));
		assertArrayEquals(
				"Get existing user",
				new Object[] {"bob", "123", "Bob", "Bobson", "bob@gmail.com", UserRole.EMPLOYEE},
				new Object[] {
						user.getUsername(),
						user.getPassword(),
						user.getFirstName(),
						user.getLastName(),
						user.getEmail(),
						user.getRole()});
		assertNull("Get nonexistent user", userDao.selectUser(Integer.MIN_VALUE));
	}

	@Test
	public void selectUserByUnameTest() throws SQLException {
		User user = userDao.selectUser("bob");
		assertArrayEquals(
				"Get existing user",
				new Object[] {"bob", "123", "Bob", "Bobson", "bob@gmail.com", UserRole.EMPLOYEE},
				new Object[] {
						user.getUsername(),
						user.getPassword(),
						user.getFirstName(),
						user.getLastName(),
						user.getEmail(),
						user.getRole()});
		assertNull("Get nonexistent user", userDao.selectUser("bill"));
	}

}
