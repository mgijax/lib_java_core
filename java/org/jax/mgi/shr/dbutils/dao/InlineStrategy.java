package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.DBExceptionFactory;

/**
 * A class which implements the InsertStrategy, UpdateStrategy,
 * and DeleteStrategy using a direct in-line JDBC approach
 * @has a SQLDataManager.
 * @does extracts SQL from DAO objects and executes the sql as direct inline
 * @author M Walker
 */
public class InlineStrategy
    implements InsertStrategy, DeleteStrategy, UpdateStrategy
{
    /**
     * the SQLDataManager to use
     */
    private SQLDataManager sqlDataManager;

    /*
     * the following constant definitions are exceptions thrown by this class
     */
    private static String DAOErr = DBExceptionFactory.DAOErr;

    /**
     * constructor
     * @assumes nothing
     * @effects nothing
     * @param sqlMgr the SQLDataManager for performing inserts, updates and
     * deletes
     */
    public InlineStrategy(SQLDataManager sqlMgr)
    {
        sqlDataManager = sqlMgr;
    }

    /**
     * update the given DAO using direct inline JDBC
     * @assumes nothing
     * @effects the given DAO will be updated in the database
     * @param dao the dao object to update
     * @throws DBException thrown if an exception occurs when executing
     * the update
     */
    public void update(DAO dao)
        throws DBException
    {
        SQLTranslatable translator = (SQLTranslatable) dao;
        String sql = translator.getUpdateSQL();
        sql = SQLStrategyHelper.convertToJDBCProc(sql);
        try
        {
            sqlDataManager.execute(sql);
        }
        catch (DBException e)
        {
            DBExceptionFactory eFactory = new DBExceptionFactory();
            DBException e2 = (DBException)
                eFactory.getException(DAOErr, e);
            e2.bind(dao.getClass().getName());
            throw e2;
        }
    }

    /**
     * delete the given DAO using direct inline JDBC
     * @assumes nothing
     * @effects the given DAO will be deleted from the database
     * @param dao the dao object to delete
     * @throws DBException thrown if an exception occurs when executing
     * the delete
     */
    public void delete(DAO dao)
        throws DBException
    {
        SQLTranslatable translator = (SQLTranslatable) dao;
        String sql = translator.getDeleteSQL();
        sql = SQLStrategyHelper.convertToJDBCProc(sql);
        try
        {
            sqlDataManager.execute(sql);
        }
        catch (DBException e)
        {
            DBExceptionFactory eFactory = new DBExceptionFactory();
            DBException e2 = (DBException)
                eFactory.getException(DAOErr, e);
            e2.bind(dao.getClass().getName());
            throw e2;
        }
    }

    /**
     * insert the given DAO using direct inline JDBC
     * @assumes nothing
     * @effects the given DAO will be inserted into the database
     * @param dao the dao object to insert
     * @throws DBException thrown if an exception occurs when executing
     * the insert
     */
    public void insert(DAO dao)
        throws DBException
    {
        SQLTranslatable translator = (SQLTranslatable) dao;
        String sql = translator.getInsertSQL();
        sql = SQLStrategyHelper.convertToJDBCProc(sql);
        try
        {
            sqlDataManager.execute(sql);
        }
        catch (DBException e)
        {
            DBExceptionFactory eFactory = new DBExceptionFactory();
            DBException e2 = (DBException)
                eFactory.getException(DAOErr, e);
            e2.bind(dao.getClass().getName());
            throw e2;
        }
    }
}