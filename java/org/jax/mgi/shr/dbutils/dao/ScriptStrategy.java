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
     * @param writer the ScriptWriter to use
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
     * @param dao the object to update
     * @throws DBException thrown if there is an error accessing the database
     */
    public void update(DAO dao)
        throws DBException
    {
        String sql = ( (SQLTranslatable) dao).getUpdateSQL();
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
     * @param dao the object to delete
     * @throws DBException thrown if there is an error accessing the database
     */
    public void delete(DAO dao)
        throws DBException
    {
        String sql = ( (SQLTranslatable) dao).getDeleteSQL();
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
     * @param dao the object to insert
     * @throws DBException thrown if there is an error accessing the database
     */
    public void insert(DAO dao)
        throws DBException
    {
        String sql = ( (SQLTranslatable) dao).getInsertSQL();
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