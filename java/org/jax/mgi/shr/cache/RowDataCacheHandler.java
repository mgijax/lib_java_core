package org.jax.mgi.shr.cache;

import java.util.Map;
import java.util.Iterator;
import java.util.HashMap;
import java.io.OutputStream;
import java.io.IOException;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.log.LoggerFactory;
import org.jax.mgi.shr.log.ConsoleLogger;
import org.jax.mgi.shr.config.LogCfg;
import org.jax.mgi.shr.config.CacheCfg;
import org.jax.mgi.shr.config.ConfigException;

/**
 * An abstract class for coordinating data between the database and
 * an in-memory cache.
 * @has a RowDataCacheStrategy object for handling cache access which
 * can be either of type lazy of full (see <a href="CacheConstants.html">
 * CacheConstants</a> class for these definitions).
 * It also has a logger for logging statistics if the logger is in debug mode
 * and a Configurator object for configuring whether or not to log debug
 * information.
 * @does provides the sql for initialing the cache and for obtaining new
 * cache entries from the database.
 * @abstract this class provides a setter method for setting the internal
 * cache and a print method for printing the cache. The subclasses
 * provide the actual lookup method for looking up objects in the cache.
 * The subclass is also required to implement the getter methods for the
 * various sql statements required for managing the cache such as perfoming
 * cache initialization and obtaining new cache entries from the database.
 * Finally, the subclass is required to provide a RowDataInterpreter for
 * creating a KeyValue object from a database row. The KeyValue object is a
 * critical component in managing caches. It is required that all
 * implementations of RowDataCacheHandler provide interpreter classes
 * (see <a href="../dbutils/RowDataInterpreter.html">
 * org.jax.mgi.shr.dbutils.RowDataInterpreter</a>) which create a KeyValue
 * object from the query results. This is obtained through the
 * getRowDataInterpreter() method.
 * @company Jackson Labortory
 * @author MWalker
 */
abstract public class RowDataCacheHandler
{
    /*
     * the following constant definitions are exceptions thrown by this class
     */
    private static String UnknownStrategy =
        CacheExceptionFactory.UnknownStrategy;
    private static String ConfigErr =
        CacheExceptionFactory.ConfigErr;
    /**
     * the class used to create KeyValue objects from a database row
     * which is used by the cache strategy class for adding new entries
     * into the cache from the results of a database query
     */
    protected RowDataInterpreter interpreter;
    /**
     * the strategy class for performing cache lookups
     */
    protected RowDataCacheStrategy cacheStrategy;
    /**
     *  A copy of the cache reference being defined in the base class.
     *  This reference is set during initializtion of the cache by the
     *  RowDataCacheStrategy
     */
    protected Map cache = new HashMap();

    /**
     * constructor which accepts a cache type and a SQLDataManager
     * @param cacheType the cache type either LAZY_CACHE or FULL_CACHE
     * (see CacheContants class from this package)
     * @param sqlDataManager the SQLDataManager to use
     * @throws CacheException thrown if the cache type is unknown
     */
    public RowDataCacheHandler(int cacheType,
                               SQLDataManager sqlDataManager)
        throws
        CacheException
    {
        setup(cacheType, sqlDataManager);
    }

    /**
     * prints the values from the cache onto the given output stream
     * @param out the output stream to print on
     * @throws IOException thrown if a write error occurs during printing
     */
    public void printCache(OutputStream out)
        throws IOException
    {
        for (Iterator it = cache.keySet().iterator(); it.hasNext(); )
        {
            Object key = it.next();
            Object value = cache.get(key);
            out.write(key.toString().getBytes());
            out.write(new String(" ").getBytes());
            out.write(value.toString().getBytes());
            out.write(new String("\n").getBytes());
        }
    }

    /**
     * get the current size of the cache
     * @assumes nothing
     * @effects nothing
     * @return the current size of the cache
     */
    public int cacheSize()
    {
        return this.cache.size();
    }

    /**
     * set the logger for this instance. If not set then the logger is
     * obtained from the SQLDataManager which was given in the constructor
     * @effects log messages will be sent to this logger
     * @assumes nothing
     * @param logger the logger
     */
    public void setLogger(Logger logger)
    {
        this.cacheStrategy.setLogger(logger);
    }

    /**
     * get the logger for this instance
     * @assumes nothing
     * @effects nothing
     * @return the logger fro this instance
     */
    public Logger getLogger()
    {
        return this.cacheStrategy.getLogger();
    }



    /**
     * set whether to log debug messages to the logger. The Logger instance
     * will also have to be set with debug turned on. This is an extra level
     * of control for logging messages pertaining to caches since they are
     * rarely ever needed.
     */
    public void setDebug(boolean debug)
    {
        this.cacheStrategy.setDebug(debug);
    }

    /**
     * get the debug state of this instance
     * @assumes nothing
     * @effects nothing
     * @return the debug state of this instance
     */
    public boolean getDebug()
    {
        return this.cacheStrategy.getDebug();
    }


    /**
     * obtain the sql for fully initializing a cache
     * @assumes nothing
     * @effects nothing
     * @return the sql string
     */
    public abstract String getFullInitQuery();

    /**
     * obtain the sql for partially initializing a cache. This query is
     * used along with RowDataInterpreter provided in the
     * getRowDataInterpreter() method to query the database for intial
     * cache entries and placing them into the cache.
     * @assumes nothing
     * @effects nothing
     * @return the sql string
     */
    public abstract String getPartialInitQuery();

    /**
     * obtain the sql for accessing a given object in the database.
     * This method is used in conjunction with the
     * getRowDataInterpreter() method for querying new cache entries and
     * placing them into the cache
     * @assumes nothing
     * @effects nothing
     * @param addObject the given object to lookup in the database
     * @return the sql string
     */
    public abstract String getAddQuery(Object addObject);

    /**
     * obtain a RowDataInterpreter object which implements the
     * interpret(RowReference) method by returning a KeyValue object.
     * @assumes nothing
     * @effects nothing
     * @return the RowDataInterpreter object
     */
    public abstract RowDataInterpreter getRowDataInterpreter();

    /**
     * forces the initialization of the internal cache which by default does
     * not get initialized until a lookup is called.
     * @assumes nothing
     * @effects the cache will be initialized
     * @throws DBException thrown if there is an error with the database
     * @throws CacheException thropwn if there is an error with the cache
     */
    public void initCache()
        throws DBException, CacheException
    {
        this.cacheStrategy.init(this.cache);
    }

    /**
     * sets the internal reference of the cache and initializes it.
     * @assumes nothing
     * @effects the cache will be initialized
     * @param cache the cache
     * @throws DBException thrown if there is an error with the database
     * @throws CacheException thropwn if there is an error with the cache
     */
    public void initCache(Map cache)
        throws DBException, CacheException
    {
        this.cache = cache;
        this.cacheStrategy.init(cache);
    }


    /**
     * get the internal cache
     * @assumes nothing
     * @effects nothing
     * @return the internal cache
     */
    public Map getCache()
    {
        return this.cache;
    }

    /**
     * setup this instance
     * @param cacheType the cache type from CacheConstants (either lazy or
     * full)
     * @param sqlDataManager the SQLDataManager to use
     * @throws CacheException thrown if there is an error with the cache
     */
    protected void setup(int cacheType,
                        SQLDataManager sqlDataManager)
        throws CacheException
    {
        Logger logger = null;
        Boolean debug = null;
        try
        {
            LogCfg logCfg = new LogCfg();
            CacheCfg cacheCfg = new CacheCfg();
            LoggerFactory factory = logCfg.getLoggerFactory();
            logger = factory.getLogger();
            debug = cacheCfg.getDebug();
        }
        catch (ConfigException e)
        {
            CacheExceptionFactory f = new CacheExceptionFactory();
            CacheException e2 = (CacheException)f.getException(ConfigErr, e);
            throw e2;
        }

        RowDataCacheStrategy strategy = null;
        switch (cacheType)
        {
            case CacheConstants.LAZY_CACHE:
                strategy = new LazyCacheStrategy(sqlDataManager, logger);
                strategy.setDebug(debug.booleanValue());
                break;
            case CacheConstants.FULL_CACHE:
                strategy = new FullCacheStrategy(sqlDataManager, logger);
                strategy.setDebug(debug.booleanValue());
                break;
            default:
                CacheExceptionFactory eFactory = new CacheExceptionFactory();
                CacheException e = (CacheException)
                    eFactory.getException(UnknownStrategy);
                e.bind(cacheType);
                throw e;
        }
        strategy.setCacheHandler(this);
        this.cacheStrategy = strategy;
    }

}