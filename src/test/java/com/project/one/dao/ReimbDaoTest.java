package com.project.one.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import com.project.one.DatabaseTest;
import com.project.one.model.ReimbStatus;
import com.project.one.model.Reimbursement;

public class ReimbDaoTest extends DatabaseTest {

	private static ReimbDao reimbDao;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		reimbDao = ReimbDao.getInstance();
	}

	@Test
	public void selectReimbursementsTest() throws SQLException {
		assertEquals("Select reimbursements for a user", 2, reimbDao.selectReimbursements(getUserId("bob")).size());
		assertNull("Select reimbursements for nonexistent user", reimbDao.selectReimbursements(2000));
	}
	
	@Test
	public void updateReimbursementStatusTest() throws SQLException {
		Random r = new Random();
		ReimbStatus[] statuses = ReimbStatus.values();
		
		// Set all pending reimbursements to something other than pending
		for (Reimbursement reimb : reimbDao.selectAllReimbursements())
			if (reimb.getStatus() == ReimbStatus.PENDING)
				reimbDao.updateReimbursementStatus(reimb.getId(), getUserId("jbright"), statuses[r.nextInt(statuses.length - 1) + 1]);
		
		// Make sure no reimbursements are pending
		for (Reimbursement reimb : reimbDao.selectAllReimbursements())
			assertNotEquals("Reimburement is not pending", ReimbStatus.PENDING, reimb.getStatus());
	}

}
