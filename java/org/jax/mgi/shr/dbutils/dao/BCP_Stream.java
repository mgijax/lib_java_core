package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.exception.MGIException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.DBExceptionFactory;
import org.jax.mgi.shr.dbutils.bcp.BCPManager;

/**
 * A SQLStream for doing inserts with bcp. Updates and deletes cannot be
 * performed and calling thoe methods will cause an exception to be raised
 * @has a BCPManager for managing the writing and executing of multiple
 * bcp files and a SQLDataManager for performing bcp related database
 * operations
 * @does inserts DAO objects into the database using bcp
 * @company The Jackson Laboratory
 * @author M Walker
 */
public class BCP_Stream
    extends SQLStream
{
    /**
     * the BCPManager class for performing database inserts with bcp
     */
    private BCPManager bcpManager = null;

    /*
     * the following constant definitions are exceptions thrown by this class
     */
    private static String SQLStreamCloseErr =
        DBExceptionFactory.SQLStreamCloseErr;

    /**
     * constructor
     * @assumes nothing
     * @effects nothing
     * @param sqlMgr the SQLDataManager to use
     * @param bcpMgr the BCPManager to use
     */
    public BCP_Stream(SQLDataManager sqlMgr, BCPManager bcpMgr)
    {
        super();
        this.bcpManager = bcpMgr;
        BCPStrategy bcpStrategy = new BCPStrategy(sqlMgr, bcpMgr);
        super.setInsertStrategy(bcpStrategy);
    }

    /**
     * this method overrides the super class method by throwing an exception
     * @assumes nothing
     * @effects an exception will be thrown.
     * @param dao the object to delete
     * @throws DBException at all times
     */
    public void delete(DAO dao) throws DBException
    {
      throw MGIException.getUnsupportedMethodException();
    }

    /**
     * this method overrides the super class method be throwing an exception
     * @assumes nothing
     * @effects an exception will be thrown.
     * @param dao the object to update
     * @throws DBException at all times
     */
    public void update(DAO dao) throws DBException
    {
      throw MGIException.getUnsupportedMethodException();
    }


    /**
     * execute the bcp commands
     * @assumes nothing
     * @effects the bcp command is executed
     * @throws DBException thrown if there is an error executing the bcp
     * command
     */
    public void close()
        throws DBException
    {
        try
        {
            bcpManager.executeBCP();
        }
        catch (MGIException e)
        {
            DBExceptionFactory eFactory = new DBExceptionFactory();
            DBException e2 = (DBException)
                eFactory.getException(SQLStreamCloseErr, e);
            e2.bind(this.getClass().getName());
            throw e2;
        }
    }
}