package org.jax.mgi.shr.cache;

import java.util.Map;

import org.jax.mgi.shr.dbutils.ResultsNavigator;
import org.jax.mgi.shr.dbutils.MultiRowInterpreter;
import org.jax.mgi.shr.dbutils.MultiRowIterator;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;

/**
 * @is a helper class for providing basic methods used by both the lazy and
 * full cache strategy classes.
 * @has nothing
 * @does provides static methods for doing tasks common to both the lazy
 * and full cache strategy classes.
 * @company The Jackson Laboratorye
 * @version 1.0
 */

public class CacheStrategyHelper
{

  /*
   * the following constant definitions are exceptions thrown by this class
   */
  private static String MissingKeyValue =
      CacheExceptionFactory.MissingKeyValue;

  /**
   * Parses through the given ResultsNavigator and places data into the cache.
   * It determines if the given cacheHandler supports muliple rows by
   * checking if it's RowDataInterpreter is an instance of
   * MultiRowDataInterpreter. If so, then this method uses a
   * MultiResultsIterator for parsing the results.
   * @assumes nothing
   * @effects new entries are added to the cache
   * @param navigator the ResultsNavigator object from which objects
   * are obtained and placed in the cache
   * @param cache the cache to add the objects to
   * @param cacheHandler the RowDataCacheHandler class from which to obtain
   * an object from a vector of rows which share the same key
   * @throws CacheException if there is an error putting objects in the cache
   * @throws DBException if there is an error accessing data from the database
   */

  protected static void putResultsInMap(ResultsNavigator navigator,
                                        Map cache,
                                        RowDataCacheHandler cacheHandler)
  throws CacheException, DBException
  {
    Object resultObj = null;
    RowDataInterpreter interpreter = cacheHandler.getRowDataInterpreter();

    /**
     * see if the cacheHandler supports multiple rows per cache entry
     */
    if (interpreter instanceof MultiRowInterpreter)
    {
      /**
       * process multiple rows for each cache entry using a
       * MultiResultsIterator
       */
      MultiRowInterpreter multiRowInterpreter =
          (MultiRowInterpreter) interpreter;
      MultiRowIterator iterator =
          new MultiRowIterator(navigator, multiRowInterpreter);
      while (iterator.hasNext())
      {
        resultObj = iterator.next();
        putObjectInCache(resultObj, cache, cacheHandler);
      }

    }
    else
    {
      /**
       * the cacheHandler is prepared to only handle one row per cache entry
       */
      navigator.setInterpreter(interpreter);
      while (navigator.next())
      {
        putObjectInCache(navigator.getCurrent(), cache, cacheHandler);
      }
    }
  }

  /**
   * checks the given object to see if it is an instance of KeyValue and
   * throws a CacheException if it is not
   * @param o the given object to check
   * @param cacheHandler used for error reporting
   * @throws CacheException thrown if the given object is not an instance of
   * KeyValue
   */
  private static void checkForKeyValue(Object o,
                                       RowDataCacheHandler cacheHandler)
      throws CacheException
  {
    if (! (o instanceof KeyValue))
    {
      CacheExceptionFactory eFactory = new CacheExceptionFactory();
      CacheException e = (CacheException)
          eFactory.getException(MissingKeyValue);
      e.bind(cacheHandler.getRowDataInterpreter().getClass().getName());
      throw e;
    }
  }

  /**
   * puts the given object in the cache
   * @param o the given object
   * @param cache the cache to put object in
   * @param cacheHandler used for error reporting
   * @throws CacheException
   */
  private static void putObjectInCache(Object o,
                                       Map cache,
                                       RowDataCacheHandler cacheHandler)
  throws CacheException
  {
    checkForKeyValue(o, cacheHandler); // assure that the object is a KeyValue
    KeyValue keyValue = (KeyValue)o;
    cache.put(keyValue.getKey(), keyValue.getValue());
  }


}
