package org.jax.mgi.shr.sva;

import java.util.*;

/**
 * A class which represents a collection of lookups which were derived from
 * a collection of SVASets and their associated objects. SVASets are sets of
 * named set-valued attributes (SVAs). An object can be associated with
 * an SVASet and this class allows one to lookup attribute values and get
 * back the associated objects. The names of the SVAs correspond to the names of
 * the lookups. The attribute values of the SVAs are the keys and the lookup
 * values are objects given to be associated with a SVASet.
 * @has an array of SVA names from which to obtain the attributes used in
 * creating the lookups
 * @does provides a way to add SVASets and their associated objects to the
 * index and a way to lookup the associated objects by attribute value for a
 * any name from the array of SVA names
 * @company Jackson Laboratory
 * @author M Walker
 *
 */

public class SVAIndex
{
    Hashtable indexes = new Hashtable();
    String[] indexNames = {};

    /**
     * Constructor
     * @param attrNames the names of the SVAs to use for indexing the associated
     * objects by their attribute values
     */

    public SVAIndex(String[] attrNames)
    {
        this.indexNames = attrNames;
        for (int i = 0; i < this.indexNames.length; i++)
        {
            Hashtable hash = new Hashtable();
            this.indexes.put(this.indexNames[i], hash);
        }
    }

    /**
     * add an association between an object and a SVASet. All attributes for
     * each SVA from the list of SVA names given to the constructor are
     * used to index the associated object by
     * @param object the associated object to index
     * @param svaSet the set of SVAs used for indexing
     */
    public void add(Object object, SVASet svaSet)
    {
        for (int i = 0; i < this.indexNames.length; i++)
        {
            String name = indexNames[i];
            Set values = svaSet.getSVA(name);
            if (values == null) continue;
            Hashtable hash = (Hashtable)this.indexes.get(name);
            for (Iterator j = values.iterator(); j.hasNext(); )
            {
                Object attrValue = j.next();
                // add to indexed hash
                Set indexedSet = (Set)hash.get(attrValue);
                if (indexedSet == null)
                    indexedSet = new HashSet();
                indexedSet.add(object);
                hash.put(attrValue, indexedSet);
            }
        }
    }

    /**
     * lookup a set of associated objects for a given attribute
     * @param attrName the name of the SVA which contains this attribute which
     * is also the name of the lookup for this attribute
     * @param key the attribute value to lookup
     * @return a set of objects which share the given attribute in common
     */
    public Set lookup(String attrName, Object key)
    {
        Hashtable hash = (Hashtable)this.indexes.get(attrName);
        if (hash == null)
            return null;
        else
            return (Set)hash.get(key);
    }

    /**
     * get all the names of SVAs used as lookups as provided in the constructor
     * @return the names of SVAs used as lookups
     */
    public String[] getIndexNames()
    {
        return this.indexNames;
    }


    /**
     * gets a set of all attribute values used as keys to a lookup with the
     * given name (the name corresponds also to the SVA name containing the
     * attribute)
     * @param indexName the name of the lookup or SVA containing the
     * attribute
     * @return the set of objects which share the attribute in common
     */
    public Set keySet(String indexName)
    {
        Hashtable t = (Hashtable)this.indexes.get(indexName);
        if (t == null)
            return null;
        else
            return t.keySet();
    }



}
