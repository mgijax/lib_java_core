package org.jax.mgi.shr.dbutils.dao;

import java.sql.Timestamp;

/**
 * An interface that follows the RADAR record stamp format, meaning it
 * stamps the following fields: jobStreamKey, creation_date
 * @has nothing
 * @does provides the interface
 * @company The Jackson Laboratory
 * @author M Walker
 */
public interface RecordStampable_RADAR
    extends RecordStampable
{
    public void setJobStreamKey(Integer jobKey);

    public void setCreationDate(Timestamp t);
}