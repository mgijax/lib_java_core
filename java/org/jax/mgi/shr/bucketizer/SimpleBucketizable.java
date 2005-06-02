package org.jax.mgi.shr.bucketizer;

import java.util.*;

import org.jax.mgi.shr.sva.SVASet;

/**
 * A basic implementation of the Bucketizable interface
 * @has an SVASet of attributes, an id and a provider tag
 * @does provides accessors for adding and retreiving attributes from the
 * SVASet
 * @company Jackson Laboratory
 * @author M Walker
 *
 */

public class SimpleBucketizable implements Bucketizable
{
    protected String id = null;
    protected String provider = null;
    protected SVASet svaSet = null;

    /**
     * Constructor
     * @param id the id tag
     * @param provider the provider tag
     * @param svaNames an array of names for the SVAs used in the SVASet
     */
    public SimpleBucketizable(String id, String provider, String[] svaNames)
    {
        this.id = id;
        this.provider = provider;
        this.svaSet = new SVASet(svaNames);
    }

    /**
     * return the id tag
     * @return the id tag
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * return the provider tag
     * @return the provider tag
     */
    public String getProvider()
    {
        return this.provider;
    }

    /**
     * add a new attribute to the SVASet
     * @param svaName the name of the SVA
     * @param svaValue the value of the attribute
     */
    public void add(String svaName, Object svaValue)
    {
        this.svaSet.addSVAMember(svaName, svaValue);
    }

    /**
     * get the SVASet
     * @return the SVASet
     */
    public SVASet getSVASet()
    {
        return this.svaSet;
    }

    /**
     * create a String representation of this instance
     * @return a String representation of this instance
     */
    public String toString()
    {
        StringBuffer buff = new StringBuffer();
        buff.append(this.provider + ":" + this.id);
        String[] svaNames = this.svaSet.getSVANames();
        for (int i = 0; i < svaNames.length; i++)
        {
            buff.append(",");
            Set set = this.svaSet.getSVA(svaNames[i]);
            buff.append(svaNames[i] + "=" + set.toString());
        }
        return buff.toString();
    }

}