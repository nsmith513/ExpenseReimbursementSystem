package com.project.one.service;

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

public class ReimbServiceTest extends DatabaseTest {

	private static ReimbService reimbServe;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		reimbServe = ReimbService.getInstance();
	}
	
	@Test
	public void getReimbursementsTest() throws SQLException {
		assertEquals("Get reimbursements for a user", 2, reimbServe.getReimbursements(getUserId("bob")).size());
		assertNull("Get reimbursements for nonexistent user", reimbServe.getReimbursements(2000));
	}
	
	// For some reason, even in PostgreSQL compatibility mode, h2 can't handle
	// the RETURNING clause, so there's no good way to test this
	
//	@Test
//	public void addReimbursementTest() throws SQLException {
//		
//	}
	
	@Test
	public void setReimbursementStatusTest() throws SQLException {
		Random r = new Random();
		ReimbStatus[] statuses = ReimbStatus.values();
		
		// Set all pending reimbursements to something other than pending
		for (Reimbursement reimb : reimbServe.getAllReimbursements())
			if (reimb.getStatus() == ReimbStatus.PENDING)
				reimbServe.setReimbursementStatus(reimb.getId(), getUserId("jbright"), statuses[r.nextInt(statuses.length - 1) + 1]);
		
		// Make sure no reimbursements are pending
		for (Reimbursement reimb : reimbServe.getAllReimbursements())
			assertNotEquals("Reimburement is not pending", ReimbStatus.PENDING, reimb.getStatus());
	}

}
