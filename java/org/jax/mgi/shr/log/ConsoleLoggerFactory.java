package org.jax.mgi.shr.log;

/**
 * @is this is an implementation of the LoggerFactory interface and provides
 * a way to obtain an implementation of the Logger interface.
 * @has nothig
 * @does creates a new Logger class
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public class ConsoleLoggerFactory implements LoggerFactory
{

    /**
     * get an instance of a Logger
     * @assumes nothing
     * @effects nothing
     * @return a Logger instance
     */
    public Logger getLogger()
    {
        return new ConsoleLogger();
    }

}