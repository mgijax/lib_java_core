package org.jax.mgi.shr.bucketizer;

import java.util.*;

import org.jax.mgi.shr.graphs.Graph;

/**
 *
 * An object used to store a grouping of members from two data sets which were
 * determined by the bucketizer algorithm from the AbstractBucketizer to be
 * related. This object is passed to the process methods for subsequent
 * processing of the members
 * @has members from set 1 and two, and label objects which describes why
 * any two members are related
 * @does stores the data and provides accessors and calculates the cardinality
 * between the two sets
 * @company Jackson Laboratory
 * @author M Walker
 *
 */

public class BucketItem
{
    public static final int ONE_TO_ONE = 0;
    public static final int ONE_TO_MANY = 1;
    public static final int ONE_TO_ZERO = 2;
    public static final int MANY_TO_ONE = 3;
    public static final int MANY_TO_MANY = 4;
    public static final int ZERO_TO_ONE = 5;
    public static final int ZERO_TO_ZERO = 6;
    public static final int ZERO_TO_MANY = 7;
    public static final int MANY_TO_ZERO = 8;
    public static final int UNKNOWN = 9;

    protected Graph graph = null;

    // object reference established for reuse
    private Bucketizable bucketizable = null;

    /**
     * constructor
     * @param graph a graph of connected components representing the related
     * members from the two data sets
     */
    protected BucketItem(Graph graph)
    {
        this.graph = graph;
    }

    /**
     * gets the reason why any two members are associated to each other. It is
     * assigned by the AbstractBucketizer and represents all the attributes
     * which were found in common between the two members
     * @param b1 member one
     * @param b2 member two
     * @return the String value of the reason why the two members are
     * associated
     */
    public String getLabel(Bucketizable b1, Bucketizable b2)
    {
        return (String)this.graph.getEdgeLabel(b1, b2);
    }

    /**
     * access all the members with an Iterator
     * @return an Iterator for all the members of this instance
     */
    public Iterator membersIterator()
    {
        return graph.iterNodes();
    }

    /**
     * access all the members for a given provider with an Iterator
     * @return an Iterator for all the members of this instance for a given
     * provider
     */
    public Iterator membersIterator(String provider)
    {
        ArrayList list = new ArrayList();
        for (Iterator i = graph.iterNodes(); i.hasNext(); )
        {
            Bucketizable b = (Bucketizable) i.next();
            if (b.getProvider().equals(provider))
                list.add(b);
        }
        return list.iterator();
    }


    /**
     * returns an Iterator of all the pairs of associated members
     * @param provider the provider name which determines which dataset is
     * used as the focal set (that is the set which comes first when refering
     * to the grouping as a one to one, one to many, etc)
     * @return
     */
    public Iterator associationsIterator(String provider)
    {
        HashSet associations = new HashSet();
        for (Iterator i = graph.iterNodes(); i.hasNext();)
        {
            Bucketizable b = (Bucketizable)i.next();
            if (b.getProvider().equals(provider))
            {
                for (Iterator j = graph.iterNeighbors(b); j.hasNext();)
                {
                    Bucketizable neighbor = (Bucketizable)j.next();
                    String label = (String)graph.getEdgeLabel(b, neighbor);
                    Association a = new Association(b, neighbor, label);
                    associations.add(a);
                }
            }
        }
        return associations.iterator();

    }

    /**
     * gets the cardinality of this grouping, which can be a one to one,
     * one to many, one to zero, etc
     * @param provider1 the name of the provider which determines the first
     * data set when naming the cardinality
     * @param provider2 the name of the provider which determines the second
     * data set when naming the cardinality
     * @return an int constant which represents the cardinality name. These
     * are public static constants of this class
     */
    public int getCardinality(String provider1, String provider2)
    {
        int cntProvider1 = 0;
        int cntProvider2 = 0;
        for (Iterator i = graph.iterNodes(); i.hasNext(); )
        {
            bucketizable = (Bucketizable) i.next();
            if (bucketizable.getProvider().equals(provider1))
                cntProvider1++;
            else if (bucketizable.getProvider().equals(provider2))
                cntProvider2++;
        }
        if (cntProvider1 == 0 && cntProvider2 == 0)
            return ZERO_TO_ZERO;
        else if (cntProvider1 == 1 && cntProvider2 == 0)
            return ONE_TO_ZERO;
        else if (cntProvider1 == 1 && cntProvider2 == 1)
            return ONE_TO_ONE;
        else if (cntProvider1 == 1 && cntProvider2 > 1)
            return ONE_TO_MANY;
        else if (cntProvider1 > 1 && cntProvider2 == 0)
            return MANY_TO_ZERO;
        else if (cntProvider1 > 1 && cntProvider2 == 1)
            return MANY_TO_ONE;
        else if (cntProvider1 == 0 && cntProvider2 == 1)
            return ZERO_TO_ONE;
        else if (cntProvider1 == 0 && cntProvider2 > 1)
            return ZERO_TO_MANY;
        else if (cntProvider1 > 1 && cntProvider2 > 1)
            return MANY_TO_MANY;

        return UNKNOWN;
    }

    /**
     * creates a string representation of this instance
     * @return a string representation of this instance
     */
    public String toString()
    {
        HashSet set = new HashSet();
        for (Iterator i = membersIterator(); i.hasNext(); )
        {
            set.add(i.next());
        }
        return set.toString();
    }

    /**
     *
     * An objects which represents an association between two members, one from
     * each data set, which were determined to be related
     * @has a member from each data set and a label describing why the two
     * members are related (a list of attributes which were found in common)
     * @does nothing
     * @company Jackson Laboratory
     * @author M Walker
     *
     */
    public class Association
    {
        private Bucketizable b1 = null;
        private Bucketizable b2 = null;
        private String label = null;

        protected Association(Bucketizable b1, Bucketizable b2, String label)
        {
            this.b1 = b1;
            this.b2 = b2;
            this.label = label;
        }

        public Bucketizable getMember(String provider)
        {
            if (b1.getProvider().equals(provider))
                return b1;
            if (b2.getProvider().equals(provider))
                return b2;
            return null;
        }

        public String getLabel()
        {
            return this.label;
        }
    }

}