package org.jax.mgi.shr.cache;

import java.util.Map;
import java.util.Iterator;
import java.util.HashMap;
import java.io.OutputStream;
import java.io.IOException;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.DBException;

/**
 * @is: An abstract class for coordinating data between the database and
 * an in-memory cache.
 * @has: a CacheStrategy object for handling the accesing of cache entries.
 * @does: provides the sql for initialing the cache and for obtaining new
 * cache entries from the database
 * @abstract this class provides a setter method for setting the internal
 * cache and a print method for printing the cache. The subclasses
 * provide the actual lookup method for looking up objects in the cache.
 * The subclass is also required to implement the getter methods for the
 * various sql statements required for cache initialization and for obtaining
 * new cache entries from the database. Finally, the subclass is required
 * to provide a RowDataInterpreter for creating a KeyValue object from a
 * database row.
 * @company: Jackson Labortory
 * @author MWalker
 * @version 1.0
 */

abstract public class RowDataCacheHandler {

  /**
   * the following defines the types of cache strategies available
   */
  protected static final int LAZY_CACHE = 1;
  protected static final int FULL_CACHE = 2;

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
   * constructor
   * @param cacheType the cache type either LAZY_CACHE or FULL_CACHE
   * @param sqlDataManager the SQLDataManager to use
   * @throws CacheException thrown if the cache type is unknown
   */
  public RowDataCacheHandler(int cacheType,
                             SQLDataManager sqlDataManager)
  throws CacheException
  {
    RowDataCacheStrategy strategy = null;
    switch (cacheType)
    {
      case LAZY_CACHE:
        strategy = new LazyCacheStrategy(sqlDataManager);
        break;
      case FULL_CACHE:
        strategy = new FullCacheStrategy(sqlDataManager);
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

  /**
   * prints the values from the cache onto the given output stream
   * @param out the output stream to print on
   * @throws IOException thrown if a write error occurs during printing
   */
  public void printCache(OutputStream out)
  throws IOException
  {
    for (Iterator it=cache.keySet().iterator(); it.hasNext(); ) {
      Object key = it.next();
      Object value = cache.get(key);
      out.write(key.toString().getBytes());
      out.write(new String(" ").getBytes());
      out.write(value.toString().getBytes());
      out.write(new String("\n").getBytes());
    }
  }

  /**
   * obtain the sql for fully initializing a cache
   * @assumes nothing
   * @effects nothing
   * @return the sql string
   */
  public abstract String getFullInitQuery();

  /**
   * obtain the sql for partially initializing a cache
   * @assumes nothing
   * @effects nothing
   * @return the sql string
   */
  public abstract String getPartialInitQuery();

  /**
   * obtain the sql for accessing a given object in the database
   * @assumes nothing
   * @effects nothing
   * @param addObject the given object
   * @return the sql string
   */
  public abstract String getAddQuery(Object addObject);

  /**
   * obtain the RowDataInterpreter object for creating a KeyValue object
   * from a database row used for creating a new cache entry
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
  protected void setCache(Map cache) throws DBException, CacheException
  {
    this.cache = cache;
    this.cacheStrategy.init(cache);
  }



}
