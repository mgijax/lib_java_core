package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.dbutils.ScriptWriter;
import org.jax.mgi.shr.dbutils.ScriptException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.DBExceptionFactory;

/**
 * @is a class which implements the InsertStrategy, UpdateStrategy,
 * and DeleteStrategy using a scripting strategy
 * @has a ScriptWriter.
 * @does casts the given DAO to a SQLTranslatable from which to
 * obtain the sql and executes the sql for either insert, update or
 * delete by using a database script.
 * @Copyright Jackson Lab
 * @author M Walker
 * @version 1.0
 */


public class ScriptStrategy
    implements DeleteStrategy, InsertStrategy, UpdateStrategy
{
	
	private ScriptWriter writer = null;
	
	// the following constant defintions are exceptions thrown by this class
	private static final String ExecuteScriptErr =
			DBExceptionFactory.ExecuteScriptErr;
	
	/**
	 * constructor
	 * @param batch the BatchProcessor to use
	 */
	public ScriptStrategy(ScriptWriter writer)
	{
		this.writer = writer;
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
  	try 
  	{
			writer.write(sql);
      writer.go();
  	}
		catch (ScriptException e)
		{
			DBExceptionFactory eFactory = new DBExceptionFactory();
			DBException e2 = (DBException)
					eFactory.getException(ExecuteScriptErr, e);
			throw e2;  		
		}
		
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
		try 
		{
			writer.write(sql);
      writer.go();
		}
		catch (ScriptException e)
		{
			DBExceptionFactory eFactory = new DBExceptionFactory();
			DBException e2 = (DBException)
					eFactory.getException(ExecuteScriptErr, e);
			throw e2;  		
		}
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
		String sql = ((SQLTranslatable)dbComponent).getInsertSQL();
		try 
		{
			writer.write(sql);
      writer.go();
		}
		catch (ScriptException e)
		{
			DBExceptionFactory eFactory = new DBExceptionFactory();
			DBException e2 = (DBException)
					eFactory.getException(ExecuteScriptErr, e);
			throw e2;  		
		}
  }
}
