package org.jax.mgi.shr.dbutils.dao;

import java.util.Vector;

import java.sql.ResultSetMetaData;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.Table;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.bcp.BCPException;
import org.jax.mgi.shr.dbutils.bcp.BCPExceptionFactory;
import org.jax.mgi.shr.dbutils.RowToVectorInterpreter;

import org.jax.mgi.shr.exception.MGIException;
/**
 * <p>@is </p>
 * <p>@has </p>
 * <p>@does </p>
 * <p>@company The Jackson Laboratory</p>
 * @author not attributable
 *
 */

public class DAOImposter extends DAO
{

    private RowReference row = null;
    private RowToVectorInterpreter interpreter = null;

    // the following constant defintions are exceptions thrown by this class
    private static final String BCPVectorErr =
        BCPExceptionFactory.BCPVectorErr;


    public DAOImposter(RowReference row)
    {
        this.row = row;
    }

    /**
     * get the vector which represents column values for a row or a vector of
     * vectors for multiple rows which is called by a BCPWriter when writing
     * to a bcp file
     * @assumes nothing
     * @effects nothing
     * @param table the target Table object which is being bcped into
     * @return the vector of column values used for writing a line to a bcp
     * file or a vector of vectors if multiple lines are to be written to
     * the bcp file.
     * @throws BCPException if there is an error obtaining the bcp vector
     */
    public Vector getBCPVector(Table table)
        throws BCPException
    {
        try
        {
            return (Vector)this.interpreter.interpret(this.row);
        }
        catch (DBException e)
        {
            BCPExceptionFactory factory = new BCPExceptionFactory();
            BCPException e2 = (BCPException)factory.getException(BCPVectorErr);
            throw e2;
        }
    }

    /**
     * get the list of supported tables when inserting via bcp. BCPStrategy
     * class will use this method within the insert(DAO) method to see which
     * tables need to be bcp into for this DAO instance.
     * @assumes nothing
     * @effects nothing
     * @return the list of supported tables for bcping
     */
    public Vector getBCPSupportedTables()
    {
        return null;
    }


    /**
     * get the sql statement for performing an update into the database
     * @assumes nothing
     * @effects nothing
     * @return the sql statement for performing an update into the database
     * @throws DBException thrown if there is an error accessing the database
     */
    public String getUpdateSQL()
        throws DBException
    {
        throw MGIException.getUnsupportedMethodException();
    }

    /**
     * get the sql statement for performing a delete from the database
     * @assumes nothing
     * @effects nothing
     * @return the sql statement for performing a delete from the database
     * @throws DBException thrown if there is an error accessing the database
     */
    public String getDeleteSQL()
        throws DBException
    {
         throw MGIException.getUnsupportedMethodException();
    }

    /**
     * get the sql string for performing an insert into the database
     * @assumes nothing
     * @effects nothing
     * @return the sql string for performing an insert into the database
     * @throws DBException thrown if there is an error accessing the database
     */
    public String getInsertSQL()
        throws DBException
    {
         throw MGIException.getUnsupportedMethodException();
    }


}