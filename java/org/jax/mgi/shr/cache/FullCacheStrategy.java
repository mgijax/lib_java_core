package org.jax.mgi.shr.cache;

import java.util.Map;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.ResultsNavigator;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.log.ConsoleLogger;

/**
 * @is an extension of the RowDataCacheStrategy class that provides a full
 * cache pattern. That is it fully initializes a cache to provide all
 * the data required for subsequent lookups. It does not add new entries
 * after the initialization. If a value is not found on
 * lookup, no additional searches on the database are made.
 * @has see RowDataCacheStrategy as this is an extension of that class
 * @does executes a full init query and puts the results in the cache. Looks
 * up keys in the cache and returns null if not found.
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */
public class FullCacheStrategy
    extends RowDataCacheStrategy
{
    /**
     * the logger to use
     */
    private Logger logger = null;
    /*
     * the following are constants for exceptions thrown by this class
     */
    private static final String InitializationErr =
        CacheExceptionFactory.InitializationErr;
    public FullCacheStrategy(SQLDataManager dataManager)
    {
        super(dataManager);
        this.logger = new ConsoleLogger();
    }
    public FullCacheStrategy(SQLDataManager dataManager, Logger logger)
    {
        super(dataManager);
        this.logger = logger;
    }


    /**
     * initializes the cache
     * @assumes nothing
     * @effects puts initial objects in the cache
     * @param cache the cache to initialize
     * @throws CacheException thrown if the RowDataCacheHandler for this class
     * does not create the proper KeyValue object required for inserting into
     * the cache
     * @throws DBException thorwn if there is an exception with the database
     */
    public void init(Map cache)
        throws CacheException, DBException
    {
        super.hasBeenInitialized = true;
        String sql = super.cacheHandler.getFullInitQuery();
        if (sql == null)
        {
            CacheExceptionFactory eFactory = new CacheExceptionFactory();
            CacheException e =
                (CacheException) eFactory.getException(InitializationErr);
            throw e;
        }
        logger.logDebug("initializing cache with the following sql:\n" + sql);
        ResultsNavigator nav = super.dataManager.executeQuery(sql);
        /**
         * The CacheStrategyHelper class is used to navigate through the query
         * results and place objects in the cache
         */
        CacheStrategyHelper.putResultsInMap(nav, cache,
                                            this.cacheHandler,
                                            this.logger);
    }

    /**
     * lookup a value in the cache for a given key
     * @assumes nothing
     * @effects nothing
     * @param key the key object to lookup
     * @param cache the cache to look in
     * @return the value found which could be null
     * @throws DBException thrown if there is an error accessing the database
     * @throws CacheException thrown if there is an error accessing the cache
     */
    public Object lookup(Object key, Map cache)
        throws DBException, CacheException
    {
        if (!super.hasBeenInitialized)
        {
            init(cache);
        }
        Object o = cache.get(key);
        if (logger.isDebug())
        {
            if (o != null)
            {
                logger.logDebug("key found in cache: " + key);
            }
            else
            {
                logger.logDebug("key not found in cache: " + key);
            }
        }
        return cache.get(key);
    }
}