package org.jax.mgi.shr.cache;

import org.jax.mgi.shr.exception.MGIException;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.cache.RowDataCacheHandler;
import org.jax.mgi.shr.cache.CacheException;

/**
 * @is an abstract class which extends CachedLookup and uses a lazy cache
 * strategy
 * @has nothing
 * @does provides general lookup functionality using a lazy cache strategy
 * @abstract the getFullInitQuery method is implemented by throwing a runtime
 * exception to indicate that this method is not supported for a lazy cache
 * strategy. The subclass will be required to implement getPartialInitQuery(),
 * getAddQuery() and getInterpreter().
 *
 */

public abstract class LazyCachedLookup extends CachedLookup {

  public LazyCachedLookup(SQLDataManager sqlMgr)
      throws CacheException
  {
    super(RowDataCacheHandler.LAZY_CACHE, sqlMgr);
  }

  /**
   * implements this method by throwing a RuntimeException
   * @return nothing. a RuntimeException is thrown
   */
  public String getFullInitQuery() {
    throw MGIException.getUnsupportedMethodException();
  }

}
