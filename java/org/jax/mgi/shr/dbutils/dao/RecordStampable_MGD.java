package org.jax.mgi.shr.dbutils.dao;

import java.sql.Timestamp;

/**
 * An RecordStampable interface that follows the MGD record stamp format,
 * meaning it stamps the following fields: _CreatedBy_key, _ModifiedBy_key,
 * creation_date, modification_date
 * @has nothing
 * @does provides the interface
 * @company The Jackson Laboratory
 * @author M Walker
 */
public interface RecordStampable_MGD
    extends RecordStampable
{
    /**
     * set the createdBy field
     * @param key the key to use
     */
    public void setCreatedByKey(Integer key);

    /**
     * set the modifiedBy field
     * @param key the key to use
     */
    public void setModifiedByKey(Integer key);

    /**
     * set the creationDate field
     * @param t the date to use
     */
    public void setCreationDate(Timestamp t);

    /**
     * set the modificationDate
     * @param t the date to use
     */
    public void setModificationDate(Timestamp t);
}