
package org.jax.mgi.shr.cache;

import java.util.Map;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.ResultsNavigator;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.DBException;

/**
 * @is a RowDataCacheStrategy which partially initializes a cache and adds
 * new entries to the cache from the database on lookup if they do not
 * already exist in the cache
 * @has an SQLDataManager and a RowDataCacheHandler
 * @does executes a partial init query and puts the results in the cache. Looks
 * up keys in the cache and if not found then gets the value from the database
 * and adds it to the cache.
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public class LazyCacheStrategy extends RowDataCacheStrategy
{

  /**
   * constructor
   * @param dataManager the SQLDataManager
   */
  public LazyCacheStrategy(SQLDataManager dataManager)
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
    String sql = super.cacheHandler.getPartialInitQuery();
    if (sql == null)
      return;
    ResultsNavigator nav = super.dataManager.executeQuery(sql);
    /**
     * The CacheStrategyHelper class is used to navigate through the query
     * results and place objects in the cache
     */
    CacheStrategyHelper.putResultsInMap(nav, cache, this.cacheHandler);
  }

  /**
   * This method will lookup a value in the given cache for the given key
   * and if it is not found it will look it up in the database and add it
   * to the cache or return null if not found in the database or cache.
   * @assumes nothing
   * @effects nothing
   * @param key the target key to look up in the cache
   * @param cache the cache to look up the value in
   * @return the value found for the given key
   * @throws CacheException thrown if the RowDataCacheHandler for this class
   * does not create the proper KeyValue object required for inserting into
   * the cache
   * @throws DBException thorwn if there is an exception with the database
   */
  public Object lookup(Object key, Map cache)
      throws CacheException, DBException
  {
    if (!super.hasBeenInitialized)
    {
      init(cache);
    }
    Object value = cache.get(key);
    if (value != null)
      return value;
    // value was not found, see if it is in the database
    String sql = this.cacheHandler.getAddQuery(key);
    RowDataInterpreter interp = this.cacheHandler.getRowDataInterpreter();
    ResultsNavigator nav = dataManager.executeQuery(sql);
    nav.setInterpreter(interp);
    /**
     * The CacheStrategyHelper class is used to navigate through the query
     * results and place objects in the cache
     */
    CacheStrategyHelper.putResultsInMap(nav, cache, this.cacheHandler);
    // now retireve object from cache which can be null if it was not
    // found in the query results
    value = cache.get(key);
    return value;
  }


  }