package org.jax.mgi.shr.sva;

import java.util.*;

/**
 * A class which represents a set of named set-valued attributes
 * @has named set-valued attribute
 * @does provides accessors for these attributes
 * @company Jackson Laboratory
 * @author M Walker
 *
 */

public class SVASet
{

    private List attrNames = null;
    private Hashtable hash = new Hashtable();

    /**
     * constructor
     * @param attrNames an array of names to be used when constructing the
     * SVASet. If a call to addSVA or addSVAMember is made given a SVA name
     * not provided in this list given to the constructor those method calls
     * become no-ops
     */
    public SVASet(String[] attrNames)
    {
        this.attrNames = Arrays.asList(attrNames);
    }

    /**
     * get the names of the SVAs used in this instance
     * @return the names of the SVAs used in this instance
     */
    public String[] getSVANames()
    {
        String[] names = {};
        return (String[])this.attrNames.toArray(names);
    }

    /**
     * get the SVA with the given name
     * @param attrName the name of the SVA
     * @return the set of attribute values with the given attribute name
     */
    public Set getSVA(String attrName)
    {
        return (Set)hash.get(attrName);
    }

    /**
     * add a SVA to this set with the given name. It will merge this given set
     * with any existing sets previously given
     * @param attrName the name of the SVA
     * @param data the collection of attribute values associated with the
     * given SVA.
     */
    public void addSVA(String attrName, Collection data)
    {
        if (attrNames.contains(attrName))
        {
            Set s = (Set)this.hash.get(attrName);
            if (s == null)
                s = new HashSet();
            s.addAll(data);
            this.hash.put(attrName, s);
        }
    }

    /**
     * add a single attribute value to a SVA from this set with the given name.
     * @param attrName the name of the SVA
     * @param data the object to add
     */
    public void addSVAMember(String attrName, Object data)
    {
        if (attrNames.contains(attrName))
        {
            Set s = (Set)this.hash.get(attrName);
            if (s == null)
                s = new HashSet();
            s.add(data);
            this.hash.put(attrName, s);
        }
    }

    /**
     * get a String representation of this instance
     * @return a String representation of this instance
     */
    public String toString()
    {
        StringBuffer buff = new StringBuffer();
        for (Iterator i = this.attrNames.iterator(); i.hasNext(); )
        {
            Object name = i.next();
            Set s = (Set)this.hash.get(name);
            buff.append("|" + name.toString() + "=" + s.toString());
        }
        if (buff.length() != 0)
            buff.append("|");
        return buff.toString();
    }

}
