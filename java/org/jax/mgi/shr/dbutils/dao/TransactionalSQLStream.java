package org.jax.mgi.shr.dbutils.dao;

/**
 * @is a SQLStream which can execute sql in a transactional way.
 * @has nothing.
 * @does provides a commit and rollback method and the sql statements for
 * insert, update and delete.
 * @company Jackson Laboratory
 * @author M. Walker
 * @version 1.0
 */
public abstract class TransactionalSQLStream
    extends SQLStream
{
    /**
     * set whether or not to use transactions when executing sql.
     * @assumes nothing
     * @effects subsequent execution of sql will be either transactional or
     * not depending upon the given boolean value
     * @param bool true if the sql should be executing using transaction
     * management, false otherwise
     */
    public abstract void setTransactionOn(boolean bool);

    /**
     * commit the current transaction
     * @assumes nothing
     * @effects all outstanding transactional sql will be commited to the
     * database
     */
    public abstract void commit();

    /**
     * rollback the current transaction
     * @assumes nothing
         * @effects the current transaction will be roolbacked and a new transaction
     * will be started if the class has transactions on.
     */
    public abstract void rollback();
}