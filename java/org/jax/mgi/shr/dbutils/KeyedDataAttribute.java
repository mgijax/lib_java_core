package org.jax.mgi.shr.dbutils;

/**
 * @is a class that represents the value of a data column from the database
 * along with the value of the column's table's primary key
 * @has a key and a value
 * @does provides accessor methods for the key and the value object
 * @author MWalker
 * @version 1.0
 */

public class KeyedDataAttribute {

  /**
   * the key
   */
  private String value;

  /**
   * the value
   */
  private Integer key;

  /**
   * constructor
   * @param key the key object
   * @param value the value object
   */
  public KeyedDataAttribute(Integer key, String value)
  {
    this.key = key;
    this.value = value;
  }

  /**
   * the value accessor
   * @return the value object
   */
  public String getValue()
  {
    return value;
  }

  /**
   * the key accessor
   * @return  the key object
   */
  public Integer getKey()
  {
    return key;
  }
}
