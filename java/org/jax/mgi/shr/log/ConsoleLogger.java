/*
 * Created on Oct 9, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.jax.mgi.shr.log;

/**
 * @author mbw
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
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
