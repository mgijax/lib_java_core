package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.exception.MGIException;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.DBExceptionFactory;
import org.jax.mgi.shr.dbutils.bcp.BCPManager;

/**
 * @is a SQLStream for doing inserts, updates and deletes with inline sql
 * @has a InlineSQLStrategy for performing updates, inserts and deletes
 * @does inserts, updates and deletes DAO objects onto an SQLStream
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */
public class Inline_Stream
    extends SQLStream
{
    /**
     * constructor
     * @assumes nothing
     * @effects nothing
     * @param sqlMgr the SQLDataManager to use
     */
    public Inline_Stream(SQLDataManager sqlMgr)
    {
        super();
        InlineStrategy inlineStrategy = new InlineStrategy(sqlMgr);
        super.setInsertStrategy(inlineStrategy);
        super.setUpdateStrategy(inlineStrategy);
        super.setDeleteStrategy(inlineStrategy);
    }

    /**
     * delete the given DAO object from the database
     * @assumes nothing
     * @effects deletes the given DAO from the database using inline
     * sql
     * @param dao the object to delete
     * @throws DBException thrown if there is an error accessing the database
     */
    public void delete(DAO dao)
        throws DBException
    {
        deleteStrategy.delete(dao);
    }

    /**
     * update the given DAO object in the database
     * @assumes nothing
     * @effects updates the given DAO in the database using inline
     * sql
     * @param dataInstance the object to update
     * @throws DBException thrown if there is a database error when executing
     * the update sql
     */
    public void update(DAO dataInstance)
        throws DBException
    {
        updateStrategy.update(dataInstance);
    }

    /**
     * insert the given DAO object in the database
     * @assumes nothing
     * @effects adds the given DAO to the appropriate bcp file or files
     * @param dataInstance the object to insert
     * @throws DBException thrown if there is an error when trying to add
     * the given object to the bcp file
     */
    public void insert(DAO dataInstance)
        throws DBException
    {
        insertStrategy.insert(dataInstance);
    }

    /**
     * execute the bcp commands followed by the batch statements
     * @assumes nothing
     * @effects the bcp command is executed for all BCPWriters created
     * through the BCPManager
         * @throws DBException thrown if there is an error executing the bcp command
     */
    public void close()
        throws DBException
    {
    }
}