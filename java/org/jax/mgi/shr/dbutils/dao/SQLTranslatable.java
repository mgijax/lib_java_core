package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.dbutils.DBException;

/**
 * @is An object which can translate it's internal contents into sql
 * statements for insert, update and delete.
 * @has nothing.
 * @does provides sql statements for insert, update and delete.
 * @company Jackson Laboratory
 * @author M. Walker
 * @version 1.0
 */


public interface SQLTranslatable {

  /**
   * get the sql for update
   * @assumes nothing
   * @effects nothing
   * @return update sql
   */
  String getUpdateSQL() throws DBException;

  /**
   * get the sql for delete
   * @assumes nothing
   * @effects nothing
   * @return the delete sql
   */
  String getDeleteSQL() throws DBException;

  /**
   * get the sql for insert
   * @assumes nothing
   * @effects nothing
   * @return the insert sql
   */
  String getInsertSQL() throws DBException;
}
