package org.jax.mgi.shr.dbutils.dao;

import java.sql.Timestamp;

/**
 * @is a interface used by the DAOState classes to distinguish it as a
 * class that follows the MGD record stamp format, meaning is it has the 
 * following properties: _CreatedBy_key, _ModifiedBy_key, creation_date,
 * modification_date
 * @has nothing
 * @does nothing
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public interface RecordStampable_MGD extends RecordStampable
{
  public void setCreatedByKey(Integer key);
  public void setModifiedByKey(Integer key);
  public void setCreationDate(Timestamp t);
  public void setModificationDate(Timestamp t);
}