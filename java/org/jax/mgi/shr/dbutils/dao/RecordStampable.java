package org.jax.mgi.shr.dbutils.dao;

/**
 * @is a common marker interface extended by all the types of RecordStamp
 * interfaces. the RecordStampSetter accepts objects of this type and then
 * determines precisely which RecordStamp type it is and calls the 
 * appropriate setter method.
 * @has nothing
 * @does nothing
 * @author mbw
 * @company The Jackson Laboratory
 */
public interface RecordStampable {}
