package org.jax.mgi.shr.dbutils.dao;

/**
 * @is a common marker interface extended by all the types of RecordStamp
 * interfaces. The RecordStamper accepts objects of this type and then
 * determines precisely which RecordStamp type it is and calls the
 * appropriate record stamping methods.
 * @has nothing
 * @does provides the interface
 * @author mbw
 * @company The Jackson Laboratory
 */
public interface RecordStampable
{}