package org.jax.mgi.shr.dbutils.dao;

import java.sql.Timestamp;

/**
 * An interface that follows the MGDDate record stamp format, meaning it
 * stamps the following fields: creation_date, modification_date, release_date
 * @has nothing
 * @does provides the interface
 * @company The Jackson Laboratory
 * @author M Walker
 */
public interface RecordStampable_MGDRelease
    extends RecordStampable
{
    /**
     * set the creationDate field
     * @param t the date to use
     */
    public void setCreationDate(Timestamp t);

    /**
     * set the modificationDate field
     * @param t the date to use
     */
    public void setModificationDate(Timestamp t);

    /**
     * set the releaseDate field
     * @param t the date to use
     */
    public void setReleaseDate(Timestamp t);
}