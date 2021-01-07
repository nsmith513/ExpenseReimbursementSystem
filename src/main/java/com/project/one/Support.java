package com.project.one;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Holds static support functions and objects for com.project.zero.
 * 
 * @author Nicholas Smith
 */
public class Support {
	/**
	 * Reads contents of a file into a string.
	 * 
	 * @param f - File to read.
	 * @return String containing all text in specified file.
	 * @throws IOException File could not be read.
	 */
	public static String readFile(File f) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line;
		while ((line = reader.readLine()) != null)
			sb.append(line + '\n');
		reader.close();
		
		return sb.toString();
	}
	
	
	/**
	 * {@code ObjectMapper} to be used anywhere json needs to be written/read.
	 */
	public static final ObjectMapper MAPPER = new ObjectMapper();
	
	
//	private static Pattern moneyPattern = Pattern.compile("^([1-9]\\d*(\\.\\d\\d)?|0?\\.\\d\\d)$");
//	
//	/**
//	 * @param amount - A string representing a dollar amount.
//	 * @return A {@code long} representing the number of cents in {@code amount}.
//	 */
//	public static long parseMoney(String amount) {
//		if (!moneyPattern.matcher(amount).find())
//			throw new IllegalArgumentException("Cannot parse \"" + amount + "\" as money.");
//		if (amount.indexOf('.') == -1)
//			return Long.parseLong(amount + "00");
//		return Long.parseLong(amount.replaceFirst("\\.", ""));
//	}
//	
//	/**
//	 * @param amount - A {@code long} representing money in cents.
//	 * @return A string representing a dollar amount.
//	 */
//	public static String stringifyMoney(long amount) {
//		StringBuilder sb = new StringBuilder(String.format("%02d", amount));
//		return sb.insert(sb.length() - 2, (sb.length() == 2) ? "0." : '.').toString();
//	}

	
	private static final Logger LOG = Logger.getLogger("");
	
	/**
     * @param level
     * @see Logger#setLevel(Level level)
     */
	public static void logSetLevel(Level level) { LOG.setLevel(level); }
	
	/**
     * @param message the message object to log 
     * @see Logger#trace(Object message)
     */
	public static void logTrace(Object message) {
		StackTraceElement e = Thread.currentThread().getStackTrace()[2];
		LOG.trace(e.getClassName() + ':' + e.getLineNumber() + " - " + message);
	}
	
	/**
     * @param message the message object to log 
     * @see Logger#debug(Object message)
     */
	public static void logDebug(Object message) {
		StackTraceElement e = Thread.currentThread().getStackTrace()[2];
		LOG.debug(e.getClassName() + ':' + e.getLineNumber() + " - " + message);
	}
	
	/**
     * @param message the message object to log 
     * @see Logger#info(Object message)
     */
	public static void logInfo(Object message) {
		StackTraceElement e = Thread.currentThread().getStackTrace()[2];
		LOG.info(e.getClassName() + ':' + e.getLineNumber() + " - " + message);
	}
	
	/**
     * @param message the message object to log 
     * @see Logger#warn(Object message)
     */
	public static void logWarn(Object message) {
		StackTraceElement e = Thread.currentThread().getStackTrace()[2];
		LOG.warn(e.getClassName() + ':' + e.getLineNumber() + " - " + message);
	}
	
	/**
     * @param message the message object to log 
     * @see Logger#error(Object message)
     */
	public static void logError(Object message) {
		StackTraceElement e = Thread.currentThread().getStackTrace()[2];
		LOG.error(e.getClassName() + ':' + e.getLineNumber() + " - " + message);
	}
	
	/**
     * @param message the message object to log 
     * @see Logger#fatal(Object message)
     */
	public static void logFatal(Object message) {
		StackTraceElement e = Thread.currentThread().getStackTrace()[2];
		LOG.fatal(e.getClassName() + ':' + e.getLineNumber() + " - " + message);
	}
}
