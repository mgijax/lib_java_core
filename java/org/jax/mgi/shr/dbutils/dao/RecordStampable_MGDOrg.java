package org.jax.mgi.shr.dbutils.dao;

import java.sql.Timestamp;

/**
 * An interface that follows the MGD record stamp format, meaning it
 * stamps the following fields:  createdBy, modifiedBy, creation_date,
 * modification_date
 * @has nothing
 * @does provides the interface
 * @company The Jackson Laboratory
 * @author M Walker
 */
public interface RecordStampable_MGDOrg
    extends RecordStampable
{
    /**
     * set the createdBy field
     * @param name the name to use
     */
    public void setCreatedBy(String name);

    /**
     * set the modifiedBy field
     * @param name the name to use
     */
    public void setModifiedBy(String name);

    /**
     * set the modificationDate field
     * @param t the date to use
     */
    public void setModificationDate(Timestamp t);

    /**
     * set the creationDate field
     * @param t the date to use
     */
    public void setCreationDate(Timestamp t);
}