package org.jax.mgi.shr.cache;

import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.log.Logger;

/**
 * @is an abstract class which extends the RowDataCacheHandler class to
 * provide general database lookup functionality. Two lookup methods are
 * provided. One throws an exception if the key is not found and the
 * other returns null.
 * @has nothing
 * @does provides some basic lookup functionality to support simplifying
 * the development of concrete Lookup classes.
 * @abstract This class implements no abstract methods from the
 * RowDataCacheHandler class. It only adds some helper methods for performing
 * database lookups. The subclasses will be required to implement the lookup
 * methods and provide the necessary casting for a particular data type and
 * can use these lookups to support those methods. All other methods required
 * by the RowDataCacheHandler class will still need to be implemented.
 */
public abstract class CachedLookup
    extends RowDataCacheHandler
{
    /**
     * constructor which accepts a cache type and a SQLDataManager
     * @param cacheType either lazy or full (see CacheConstants from this
     * package)
     * @param sqlMgr the SQLDataManager to use for doing database lookups
     * @throws CacheException thrown if there is an error establishing a cache
     */
    public CachedLookup(int cacheType, SQLDataManager sqlMgr)
        throws
        CacheException
    {
        super(cacheType, sqlMgr);
    }

    /**
     * constructor which accepts a cache type and a SQLDataManager
     * @param cacheType either lazy or full (see CacheConstants from this
     * package)
     * @param sqlMgr the SQLDataManager to use for doing database lookups
     * @throws CacheException thrown if there is an error establishing a cache
     */
    public CachedLookup(int cacheType, SQLDataManager sqlMgr, Logger logger)
        throws
        CacheException
    {
        super(cacheType, sqlMgr, logger);
    }



    /**
     * lookup a key in the cache and if not found throw an exception
     * @param key the key to search on
     * @return the value found
     * @throws DBException thrown if there was an error with the database
     * @throws CacheException thrown if there is an error with the cache
     * @throws KeyNotFoundException if the key wss not found on lookup
     */
    protected Object lookup(Object key)
        throws DBException, CacheException,
        KeyNotFoundException
    {
        Object o = null;
        o = super.cacheStrategy.lookup(key, super.cache);
        if (o == null)
        {
            throw new KeyNotFoundException(key, this.getClass().getName());
        }
        return o;
    }

    /**
     * lookup a key in the cache and if not found return null
     * @param key the key to search on
     * @return the object found from search or null if not found
     * @throws CacheException thrown if there is an error with the cache
     * @throws DBException thrown if there was an error with the database
     */
    protected Object lookupNullsOk(Object key)
        throws CacheException,
        DBException
    {
        Object o = null;
        try
        {
            o = lookup(key);
        }
        catch (KeyNotFoundException e)
        {
            return null;
        }
        return o;
    }
}