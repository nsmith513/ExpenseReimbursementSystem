package com.project.one.service;

import java.sql.SQLException;
import java.util.List;

import com.project.one.Money;
import com.project.one.dao.ReimbDao;
import com.project.one.model.ReimbStatus;
import com.project.one.model.ReimbType;
import com.project.one.model.Reimbursement;

/**
 * Service layer for reimbursement table.
 * 
 * @author Nicholas Smith
 */
public class ReimbService {
	private static ReimbService inst = null;
	
	private ReimbDao reimbDao;
	
	private ReimbService (ReimbDao reimbDao) {
		this.reimbDao = reimbDao;
	}
	
	/**
	 * @return The current instance of the singleton class {@code ReimbService}.
	 */
	public static ReimbService getInstance() {
		if (inst == null)
			inst = new ReimbService(ReimbDao.getInstance());
		return inst;
	}
	
	/**
	 * @param userDao - The instance of {@code ReimbDao} to use.
	 * @return The current instance of the singleton class {@code ReimbService}.
	 */
	public static ReimbService getInstance(ReimbDao reimbDao) {
		if (inst == null)
			inst = new ReimbService(reimbDao);
		return inst;
	}
	
	/**
	 * Gets all reimbursements created by a specified author.
	 * 
	 * @param author - ID of author to get reimbursements of.
	 * @return List of reimbursements by the specified author, or {@code null} if
	 * there are no reimbursements by {@code author}.
	 * @throws SQLException
	 */
	public List<Reimbursement> getReimbursements(int author) throws SQLException {
		return reimbDao.selectReimbursements(author, true);
	}
	
	/**
	 * Gets all reimbursements by all authors.
	 * 
	 * @return List of reimbursements, or {@code null} if
	 * there are no reimbursements.
	 * @throws SQLException
	 */
	public List<Reimbursement> getAllReimbursements() throws SQLException {
		return reimbDao.selectAllReimbursements(true);
	}
	
	/**
	 * Attempts to add a new reimbursement ticket.
	 * 
	 * @param amount - Amount for the reimbursement.
	 * @param desc - Description of the reimbursement.
	 * @param author - ID of the reimbursement's author.
	 * @param type - Type of the reimbursement.
	 * @return The reimbursement added, or {@code null} if it was not added.
	 * @throws SQLException
	 */
	public Reimbursement addReimbursement(Money amount, String desc, int author, ReimbType type) throws SQLException {
		return reimbDao.insertReimbursement(amount, desc, author, type);
	}
	
	/**
	 * Sets the status of a reimbursement to anything but {@code ReimbStatus.PENDING}.
	 * 
	 * @param reimb - ID of reimbursement to update.
	 * @param resolver - ID of resolver.
	 * @param status - Status to set.
	 * @return Number of rows altered in DB table.
	 * @throws SQLException
	 */
	public int setReimbursementStatus(int reimb, int resolver, ReimbStatus status) throws SQLException {
		return reimbDao.updateReimbursementStatus(reimb, resolver, status);
	}
}

