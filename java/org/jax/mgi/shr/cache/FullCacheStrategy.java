package org.jax.mgi.shr.cache;

import java.util.Map;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.ResultsNavigator;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.DBException;


/**
 * @is a RowDataCacheStrategy which fully initializes a cache and does not
 * add new entries after the init
 * @has an SQLDataManager and a RowDataCacheHandler
 * @does executes a full init query and puts the results in the cache. Looks
 * up keys in the cache and returns null if not found.
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public class FullCacheStrategy extends RowDataCacheStrategy {

  /*
   * the following are constants for exceptions thrown by this class
   */
  private static final String InitializationErr =
      CacheExceptionFactory.InitializationErr;

  public FullCacheStrategy(SQLDataManager dataManager)
  {
    super(dataManager);
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
          (CacheException)eFactory.getException(InitializationErr);
      throw e;
    }
    ResultsNavigator nav = super.dataManager.executeQuery(sql);
    RowDataInterpreter interp = super.cacheHandler.getRowDataInterpreter();
    nav.setInterpreter(interp);
    /**
     * The CacheStrategyHelper class is used to navigate through the query
     * results and place objects in the cache
     */
    CacheStrategyHelper.putResultsInMap(nav, cache, this.cacheHandler);
  }

  /**
   * lookup a value in the cache for a given key
   * @assumes nothing
   * @effects nothing
   * @param key the key object to lookup
   * @param cache the cache to look in
   * @return the value found which could be null
   */
  public Object lookup(Object key, Map cache)
      throws DBException, CacheException
  {
    if (!super.hasBeenInitialized)
    {
      init(cache);
    }
    return cache.get(key);
  }
}
