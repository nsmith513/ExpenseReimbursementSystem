package com.project.one.service;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.project.one.DatabaseTest;
import com.project.one.model.User;
import com.project.one.model.UserRole;

public class UserServiceTest extends DatabaseTest {

	private static UserService userServe;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		userServe = UserService.getInstance();
	}

	@Test
	public void getUserByIdTest() throws SQLException {
		User user = userServe.getUser(getUserId("bob"));
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
		assertNull("Get nonexistent user", userServe.getUser(Integer.MIN_VALUE));
	}
	
	@Test
	public void getUserByUnameTest() throws SQLException {
		User user = userServe.getUser("bob");
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
		assertNull("Get nonexistent user", userServe.getUser("bill"));
	}

}
