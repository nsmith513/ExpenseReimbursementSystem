package com.project.one.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.project.one.Money;
import com.project.one.model.ReimbDetailed;
import com.project.one.model.ReimbStatus;
import com.project.one.model.ReimbType;
import com.project.one.model.Reimbursement;

/**
 * DAO layer for reimbursement table.
 * 
 * @author Nicholas Smith
 */
public class ReimbDao extends DataAccessObject {
	private static ReimbDao inst = null;
	
	private PreparedStatement psSelectReimb;
	private PreparedStatement psSelectReimbDetailed;
	
	private PreparedStatement psSelectAllReimb;
	private PreparedStatement psSelectAllReimbDetailed;
	
	private PreparedStatement psInsertReimb;
	private PreparedStatement psUpdateReimbStatus;
	
	private ReimbDao() {
		Connection conn = connection();
		try {
			psSelectReimb = conn.prepareStatement("SELECT * FROM reimbursement WHERE reimb_author = ?");
			psSelectReimbDetailed = conn.prepareStatement("SELECT * FROM reimb_detailed WHERE author = ?");
			
			psSelectAllReimb = conn.prepareStatement("SELECT * FROM reimbursement");
			psSelectAllReimbDetailed = conn.prepareStatement("SELECT * FROM reimb_detailed");
			
			psUpdateReimbStatus = conn.prepareStatement(
					"UPDATE reimbursement\n" + 
					"SET\n" + 
					"	reimb_status_id = ?,\n" + 
					"	reimb_resolver = ?,\n" + 
					"	reimb_resolved = current_timestamp\n" + 
					"WHERE reimb_id = ?");
			psInsertReimb = conn.prepareStatement(
					"INSERT INTO reimbursement VALUES (\n" + 
					"	DEFAULT,\n" + 
					"	?,\n" + 
					"	current_timestamp,\n" + 
					"	NULL,\n" + 
					"	?,\n" + 
					"	NULL,\n" + 
					"	?,\n" + 
					"	NULL,\n" + 
					"	(SELECT reimb_status_id FROM reimbursement_status WHERE reimb_status = 'PENDING'),\n" + 
					"	?\n" + 
					")\n" + 
					"RETURNING *");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return The current instance of the singleton class {@code ReimbDao}.
	 */
	public static ReimbDao getInstance() {
		if (inst == null) 
			inst = new ReimbDao();
		return inst;
	}
	
	/**
	 * @param rs - {@code ResultSet} selected from the reimbursement table.
	 * @return A {@code Reimbursement} object representing a row from {@code rs}.
	 * @throws SQLException
	 */
	private Reimbursement decodeReimb(ResultSet rs) throws SQLException {
		return new Reimbursement(
				rs.getInt(1),
				new Money(rs.getLong(2)),
				rs.getTimestamp(3),
				rs.getTimestamp(4),
				rs.getString(5),
				rs.getBytes(6),
				rs.getInt(7),
				rs.getInt(8),
				ReimbStatus.values()[rs.getInt(9)],
				ReimbType.values()[rs.getInt(10)]);
	}
	
	/**
	 * @param rs - {@code ResultSet} selected from the reimb_detailed view.
	 * @return A {@code ReimbDetailed} object representing a row from {@code rs}.
	 * @throws SQLException
	 */
	private ReimbDetailed decodeReimbDetailed(ResultSet rs) throws SQLException {
		return new ReimbDetailed(
				rs.getInt(1),
				new Money(rs.getLong(2)),
				rs.getTimestamp(3),
				rs.getTimestamp(4),
				rs.getString(5),
				rs.getBytes(6),
				rs.getInt(7),
				rs.getString(8),
				rs.getString(9),
				rs.getInt(10),
				rs.getString(11),
				rs.getString(12),
				ReimbStatus.values()[rs.getInt(13)],
				ReimbType.values()[rs.getInt(14)]);
	}
	
	/**
	 * Selects all reimbursements by a certain author.
	 * 
	 * @param author - ID of author to select by.
	 * @return A list of reimbursements, or {@code null} if there are no reimbursements by {@code author}.
	 * @throws SQLException
	 */
	public List<Reimbursement> selectReimbursements(int author) throws SQLException {
		psSelectReimb.setInt(1, author);
		ResultSet rs = psSelectReimb.executeQuery();
		
		if (!rs.next())
			return null;
		
		List<Reimbursement> res = new ArrayList<Reimbursement>();
		do res.add(decodeReimb(rs));
		while (rs.next());
		
		return res;
	}
	
	/**
	 * Selects all reimbursements by a certain author.
	 * 
	 * @param author - ID of author to select by.
	 * @param detailed - {@code false} to select from reimbursement table,
	 * {@code true} to select from reimb_detailed view.
	 * @return A list of reimbursements, or {@code null} if there are no reimbursements by {@code author}.
	 * @throws SQLException
	 */
	public List<Reimbursement> selectReimbursements(int author, boolean detailed) throws SQLException {
		if (!detailed)
			return selectReimbursements(author);
		
		psSelectReimbDetailed.setInt(1, author);
		ResultSet rs = psSelectReimbDetailed.executeQuery();
		
		if (!rs.next())
			return null;
		
		List<Reimbursement> res = new ArrayList<Reimbursement>();
		do res.add(decodeReimbDetailed(rs));
		while (rs.next());
		
		return res;
	}
	
	/**
	 * Selects all reimbursements.
	 * 
	 * @return A list of reimbursements, or {@code null} if there are no reimbursements.
	 * @throws SQLException
	 */
	public List<Reimbursement> selectAllReimbursements() throws SQLException {
		ResultSet rs = psSelectAllReimb.executeQuery();
		
		if (!rs.next())
			return null;
		
		List<Reimbursement> res = new ArrayList<Reimbursement>();
		do res.add(decodeReimb(rs));
		while (rs.next());
		
		return res;
	}
	
	/**
	 * Selects all reimbursements.
	 * 
	 * @param detailed - {@code false} to select from reimbursement table,
	 * {@code true} to select from reimb_detailed view.
	 * @return A list of reimbursements, or {@code null} if there are no reimbursements.
	 * @throws SQLException
	 */
	public List<Reimbursement> selectAllReimbursements(boolean detailed) throws SQLException {
		if (!detailed)
			return selectAllReimbursements();
		
		ResultSet rs = psSelectAllReimbDetailed.executeQuery();
		
		if (!rs.next())
			return null;
		
		List<Reimbursement> res = new ArrayList<Reimbursement>();
		do res.add(decodeReimbDetailed(rs));
		while (rs.next());
		
		return res;
	}
	
	/**
	 * Attempts to insert a new ticket into the reimbursement table.
	 * 
	 * @param amount - Amount for the reimbursement.
	 * @param desc - Description of the reimbursement.
	 * @param author - ID of the reimbursement's author.
	 * @param type - Type of the reimbursement.
	 * @return The reimbursement inserted, or {@code null} if it was not inserted.
	 * @throws SQLException
	 */
	public Reimbursement insertReimbursement(Money amount, String desc, int author, ReimbType type) throws SQLException {
		psInsertReimb.setLong(1, amount.getAmount());
		psInsertReimb.setString(2, desc);
		psInsertReimb.setInt(3, author);
		psInsertReimb.setInt(4, type.getValue());
		ResultSet rs = psInsertReimb.executeQuery();
		
		if (!rs.next())
			return null;
		
		return decodeReimb(rs);
	}
	
	/**
	 * Updates the status of a reimbursement to anything but {@code ReimbStatus.PENDING}.
	 * 
	 * @param reimb - ID of reimbursement to update.
	 * @param resolver - ID of resolver.
	 * @param status - Status to set.
	 * @return Number of rows altered.
	 * @throws SQLException
	 */
	public int updateReimbursementStatus(int reimb, int resolver, ReimbStatus status) throws SQLException {
		if (status == ReimbStatus.PENDING)
			return 0;
		
		psUpdateReimbStatus.setInt(1, status.getValue());
		psUpdateReimbStatus.setInt(2, resolver);
		psUpdateReimbStatus.setInt(3, reimb);
		
		return psUpdateReimbStatus.executeUpdate();
	}
}
