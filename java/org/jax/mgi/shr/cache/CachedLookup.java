package org.jax.mgi.shr.cache;

import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.SQLDataManager;

/**
 * @is an abstract class which extends the RowDataCacheHandler class to
 * provide general database lookup functionality
 * @has nothing
 * @does provides some basic lookup functionality to support simplifying
 * the development of concrete Lookup classes. It provodes a indicator to
 * control whether it is ok to allow nulls returned when performing a lookup.
 * @abstract This class implements no abstract methods from the
 * RowDataCacheHandler class. It only adds some helper methods for performing
 * database lookups. The subclasses will be required to implement the lookup
 * method for looking up objects in the cache and the getter methods for
 * obtaining the various sql statements required for cache initialization and
 * adding new cache entries from the database. Finally, the subclass is
 * required to provide a RowDataInterpreter for creating a KeyValue object
 * from a database row.
 *
 */

public abstract class CachedLookup
    extends RowDataCacheHandler {

  public CachedLookup(int cacheType, SQLDataManager sqlMgr) throws
      CacheException {
    super(cacheType, sqlMgr);
  }

  /**
   * lookup a key in the cache and if not found return null;
   * @param key the key to search on
   * @return the value found or null if not found
   * @throws LookupException if there was an error with the cache or database
   */
  protected Object lookup(Object key)
      throws DBException, CacheException, KeyNotFoundException
  {
    Object o = null;
    o = super.cacheStrategy.lookup(key, super.cache);
    if (o == null) {
      throw new KeyNotFoundException(key, this.getClass().getName());
    }
    return o;
  }

  protected Object lookupNullsOk(Object key) throws CacheException, DBException
  {
    Object o = null;
    try {
      o = lookup(key);
    }
    catch (KeyNotFoundException e) {
        return null;
    }
    return o;
  }


}
