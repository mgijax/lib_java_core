package org.jax.mgi.shr.dbutils.dao;

import java.sql.Timestamp;

/**
 * @is a interface used by the DAOState classes to distinguish it as a
 * class that follows the MGD record stamp format, meaning is it has the 
 * following properties:  createdBy, modifiedBy, creation_date,
 * modification_date
 * @has nothing
 * @does nothing
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public interface RecordStampable_MGDOrg extends RecordStampable
{
   public void setCreatedBy(String name);
   public void setModifiedBy(String name);
   public void setModificationDate(Timestamp t);
   public void setCreationdate(Timestamp t);
}
