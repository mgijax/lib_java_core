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
import org.jax.mgi.shr.log.ConsoleLogger;

/**
 * @is: An abstract class for coordinating data between the database and
 * an in-memory cache.
 * @has: a CacheStrategy object for handling the accessing of cache entries.
 * This strategy class can be either a lazy of full strategy (see
 * CacheConstants class for these definitions). It also has a logger for
 * logging statistics if the logger is in debug mode.
 * @does: provides the sql for initialing the cache and for obtaining new
 * cache entries from the database whether it is implementing either a
 * lazy or full cache strategy. No contstraints are made in these regards at
 * this level of the class hierarchy. See the LazyCacheStrategy and the
 * FullCacheStrategy classes for more specific implementations.
 * @abstract this class provides a setter method for setting the internal
 * cache and a print method for printing the cache. The subclasses
 * provide the actual lookup method for looking up objects in the cache.
 * The subclass is also required to implement the getter methods for the
 * various sql statements required for managing the cache such as for perfoming
 * cache initialization and for obtaining new cache entries from the database.
 * Finally, the subclass is required to provide a RowDataInterpreter for
 * creating a KeyValue object from a database row. The KeyValue object is a
 * critical component in managing caches. It is required that all
 * implementations of RowDataCacheHandler provide results interpreter classes
 * (see org.jax.mgi.shr.dbutils.RowDataInterpreter) which create a KeyValue
 * object from the query results. This is obtained through the
 * getRowDataInterpreter() method.
 * @company: Jackson Labortory
 * @author MWalker
 * @version 1.0
 */
abstract public class RowDataCacheHandler
{
    /*
     * the following constant definitions are exceptions thrown by this class
     */
    private static String UnknownStrategy =
        CacheExceptionFactory.UnknownStrategy;
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
        init(cacheType, sqlDataManager, new ConsoleLogger());
    }
    /**
     * constructor which accepts a cache type, SQLDataManager and a Logger
     * @param cacheType the cache type either LAZY_CACHE or FULL_CACHE
     * (see CacheContants class from this package)
     * @param sqlDataManager the SQLDataManager to use
     * @throws CacheException thrown if the cache type is unknown
     */
    public RowDataCacheHandler(int cacheType,
                               SQLDataManager sqlDataManager,
                               Logger logger)
        throws
        CacheException
    {
        init(cacheType, sqlDataManager, logger);
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
     * obtain the sql for accessing a given object in the database which
     * entails creating a query string for accessing the given object
     * from the database. This method is used in conjunction with the
     * getRowDataInterpreter() method for querying new cache entries and
     * placing them into the cache
     * @assumes nothing
     * @effects nothing
     * @param addObject the given object to lookup in the database
     * @return the sql string
     */
    public abstract String getAddQuery(Object addObject);

    /**
     * obtain the RowDataInterpreter object for creating a KeyValue object
     * from a database row of the query results and is used for creating a new
     * cache entry
     * @assumes nothing
     * @effects nothing
     * @return the RowDataInterpreter object
     */
    public abstract RowDataInterpreter getRowDataInterpreter();

    /**
     * sets the internal reference of the cache which is defined in the
     * base class
     * @param cache the cache
     * @throws DBException thrown if there is an error with the database
     * @throws CacheException thropwn if there is an error with the cache
     */
    public void setCache(Map cache)
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
     * @return
     */
    public Map getCache()
    {
        return this.cache;
    }


    protected void init(int cacheType,
                        SQLDataManager sqlDataManager,
                        Logger logger)
        throws CacheException
    {
        RowDataCacheStrategy strategy = null;
        switch (cacheType)
        {
            case CacheConstants.LAZY_CACHE:
                strategy = new LazyCacheStrategy(sqlDataManager, logger);
                break;
            case CacheConstants.FULL_CACHE:
                strategy = new FullCacheStrategy(sqlDataManager, logger);
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