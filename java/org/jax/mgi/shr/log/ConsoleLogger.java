package org.jax.mgi.shr.log;

/**
 * <p>@is a Logger class for logging to the console</p>
 * <p>@has nothing</p>
 * <p>@does routes log messages to the console</p>
 * <p>@company The Jackson Laboratory</p>
 * @author M Walker
 *
 */
public class ConsoleLogger implements Logger
{

	private boolean debugOn = false;

	/**
	 * log an informational message to the logger
	 * @assumes nothing
	 * @effects a message get logged
	 * @param message the message to log
	 */
	public void logInfo(String message)
	{
		System.out.println(message + "\n");
	}

	/**
	 * log an error message to the logger
	 * @assumes nothing
	 * @effects a message get logged
	 * @param message the message to log
	 */
	public void logError(String message)
	{
		System.out.println(message + "\n");
	}


	/**
	 * log a debug message to the logger
	 * @assumes nothing
	 * @effects a message get logged
	 * @param message the message to log
	 */
	public void logDebug(String message)
	{
		if (debugOn)
			System.out.println("DEBUG\n" + message + "\n");
	}


	/**
	 * turn debug logging on or off
	 * @assumes nothing
	 * @effects debug messages will either be allowed or prevented
	 * @param bool true if debug should be set to on, false otherwise
	 */
	public void setDebug(boolean bool)
	{
		debugOn = bool;
	}

	/**
	 * get whether debug logging is on or off
	 * @assumes nothing
	 * @effects nothing
	 * @return boolean indicator
	 */
	public boolean isDebug()
	{
		return debugOn;
	}

	/**
	 * closes any file handlers
	 * @assumes nothing
	 * @effects file handlers would be closed
	 */
	public void close() {}
}
