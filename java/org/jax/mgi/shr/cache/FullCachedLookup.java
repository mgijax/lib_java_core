package org.jax.mgi.shr.cache;

import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.exception.MGIException;

/**
 * @is an abstract class which extends CachedLookup and uses a full cache
 * strategy
 * @has nothing
 * @does provides general lookup functionality using a full cache strategy
 * @abstract the getPartialInitQuery() and getAddQuery() method are implemented
 * by throwing a runtime exception to indicate that these methods are not
 * supported for a full cache strategy. The subclass will be required to
 * implement getFullInitQuery() and getInterpreter().
 *
 */


public abstract class FullCachedLookup extends CachedLookup {

  /**
   * constructor
   * @assumes nothing
   * @effects nothing
   * @param sqlMgr the SQLDataManager class to use for performing database
   * queries.
   */
  public FullCachedLookup(SQLDataManager sqlMgr) throws CacheException
  {
    super(RowDataCacheHandler.FULL_CACHE, sqlMgr);
  }

  /**
   * throws a RuntimeException indicating that this method is not supported
   * @return nothing.
   * @throws a RunTimeException always
   */
  public String getPartialInitQuery() {
    throw MGIException.getUnsupportedMethodException();
  }

  /**
   * throws a RuntimeException indicating that this method is not supported
   * @return nothing.
   * @throws a RunTimeException always
   */
  public String getAddQuery(Object addObject) {
    throw MGIException.getUnsupportedMethodException();
  }

}