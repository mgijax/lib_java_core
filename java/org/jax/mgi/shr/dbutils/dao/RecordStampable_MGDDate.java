package org.jax.mgi.shr.dbutils.dao;

import java.sql.Timestamp;

/**
 * A interface that follows the MGDDate record stamp format, meaning it
 * stamps the following fields: creation_date, modification_date
 * @has nothing
 * @does provides the interface
 * @company The Jackson Laboratory
 * @author M Walker
 */
public interface RecordStampable_MGDDate
    extends RecordStampable
{
    public void setCreationDate(Timestamp t);

    public void setModificationDate(Timestamp t);
}