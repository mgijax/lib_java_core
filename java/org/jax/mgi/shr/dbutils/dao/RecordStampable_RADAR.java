package org.jax.mgi.shr.dbutils.dao;

import java.sql.Timestamp;

/**
 * @is a interface used by the DAOState classes to distinguish it as a
 * class that follows the RADAR record stamp format, meaning is it has the 
 * following properties: jobStreamKey, creation_date
 * @has a BCPManagerCfg for finding the JOBSTREAM parameter value
 * @does creates a string containing the fields required for record
 * stamping each seperated by a designated delimiter
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public interface RecordStampable_RADAR extends RecordStampable
{
  public void setJobStreamKey(Integer jobKey);
  public void setCreationDate(Timestamp t);
}
