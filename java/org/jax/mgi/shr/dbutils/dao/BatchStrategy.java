package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.dbutils.BatchProcessor;
import org.jax.mgi.shr.dbutils.DBException;

/**
 * @is a class which implements the InsertStrategy, UpdateStrategy,
 * and DeleteStrategy using a JDBC batch strategy
 * @has a SQLDataManager and BatchProcessor.
 * @does casts the given DAO to a SQLTranslatable from which to
 * obtain the sql and executes the sql for either insert, update or
 * delete as a batch statement.
 * @Copyright Jackson Lab
 * @author M Walker
 * @version 1.0
 */


public class BatchStrategy
    implements DeleteStrategy, InsertStrategy, UpdateStrategy
{
	
	private BatchProcessor batch = null;
	
	/**
	 * constructor
	 * @param batch the BatchProcessor to use
	 */
	public BatchStrategy(BatchProcessor batch)
	{
		this.batch = batch;
	}
	
  /**
   * update the given DAO object
   * @assumes nothing
   * @effects the given DAO will be batched up for update into the
   * database
   * @param dbComponent the object to update
   */
  public void update(DAO dbComponent) throws DBException
  { 
  	String sql = ((SQLTranslatable)dbComponent).getUpdateSQL();
  	batch.addBatch(sql);
  }

  /**
   * delete the given DAO object
   * @assumes nothing
   * @effects the given DAO will be batched up for delete from the
   * database
   * @param dbComponent the object to delete
   */
  public void delete(DAO dbComponent) throws DBException
  { 
		String sql = ((SQLTranslatable)dbComponent).getDeleteSQL();
		batch.addBatch(sql);
  }

  /**
   * insert the given DAO object
   * @assumes nothing
   * @effects the given DAO will be batched up for insert into the
   * database
   * @param dbComponent the object to insert
   */
  public void insert(DAO dbComponent) throws DBException
  { 
		String sql = ((SQLTranslatable)dbComponent).getDeleteSQL();
		batch.addBatch(sql);
  }
}
