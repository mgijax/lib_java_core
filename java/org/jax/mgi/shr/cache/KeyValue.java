package org.jax.mgi.shr.cache;

/**
 * @is a class for holding a key and a value object which is used by for
 * storing data obtained from a database query which is to be placed into
 * a mapping cache
 * @has a key and a value represented by a Object reference
 * @does provides accessor methods for the key and the value object
 * @author MWalker
 * @version 1.0
 */

public class KeyValue {

  /**
   * the key
   */
  private Object value;

  /**
   * the value
   */
  private Object key;

  /**
   * constructor
   * @param key the key object which will be used as the key to a cache
   * mapping entry
   * @param value the value object which will be used as the value to a
   * cache maping entry
   */
  public KeyValue(Object key, Object value)
  {
    this.key = key;
    this.value = value;
  }

  /**
   * the value accessor
   * @assumes nothing
   * @effects nothing
   * @return the value object
   */
  public Object getValue()
  {
    return value;
  }

  /**
   * the key accessor
   * @assumes nothing
   * @effects nothing
   * @return  the key object
   */
  public Object getKey()
  {
    return key;
  }
}
