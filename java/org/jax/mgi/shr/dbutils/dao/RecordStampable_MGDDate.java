package org.jax.mgi.shr.dbutils.dao;

import java.sql.Timestamp;


/**
 * @is a interface used by the DAOState classes to distinguish it as a
 * class that follows the MGDDate record stamp format, meaning is it has the 
 * following properties: creation_date, modification_date
 * @has nothing
 * @does noting
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public interface RecordStampable_MGDDate extends RecordStampable
{
  public void setCreationDate(Timestamp t);
  public void setModificationDate(Timestamp t);
}
