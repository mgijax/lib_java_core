package org.jax.mgi.shr.bucketizer;

import java.util.*;
import org.jax.mgi.shr.graphs.*;
import org.jax.mgi.shr.graphs.Traversals.CCIterator;
import org.jax.mgi.shr.exception.MGIException;
import org.jax.mgi.shr.dbutils.DataIterator;
import org.jax.mgi.shr.sva.SVAIndex;
import org.jax.mgi.shr.sva.SVASet;
import org.jax.mgi.shr.ioutils.OutputDataFile;
import org.jax.mgi.shr.timing.Stopwatch;

/**
 *
 * A class which sorts out the relationships between members from two sets
 * of data based on their shared attributes. A relationship is defined as a
 * one to one, one to many, one to zero, many to one, zero to one, and many to
 * many. The attributes are stored as SVASets, which are sets of mult-valued
 * attributes.
 * For example, one SVA can represent genback sequences associated to the
 * object. And another could be the set of XM refseq sequences associated to
 * the object. The SVASet in this example would then contain these two sets
 * plus possibly others. The bucketizer algorithm finds attributes from each
 * SVA that are shared between objects across the two data sets and if that
 * attribute is shared by only one object from set 1 and only one from set 2,
 * then that pair of objects is considered a match. By finding all matches
 * between all objects in both sets, the bucketizer can sort the objects out
 * by the cardinality of their relationships.
 * @has two data sets and a Decider class which is called when two members
 * from the the two data sets are found to match. The Decider interface
 * provides a simple boolean operation to determine whether the two objects
 * truely match. The default version of the Decider always returns true.
 * To incorporate another Decider, implement the Decider interface and
 * set the Decider instance variable in the base class.
 * @does computes the relationships between objects from the two data sets and
 * groups them according to their cardinalities.
 * @abstract the process methods for handling each group of related objects
 * is abstract and will need to be implemented in the base class.
 * @company Jackson Laboratory
 * @author M Walker
 *
 */


abstract public class AbstractBucketizer
{

    protected Decider decider = new DefaultDecider();

    protected SVAIndex index = null;

    private Hashtable namedDeciderItems = new Hashtable();
    private Graph connectedComponentsGraph = new SimpleGraph();
    private String[] attributeNamesToIndexOn = {};

    private Runtime runtime = Runtime.getRuntime();
    private long memStart = runtime.freeMemory();
    private long memLast = 0;
    //private OutputDataFile stats = null;

    /**
     * constructor
     * @param it1 an Iterator for data set one
     * @param it2 an Iterator for data set two
     * @param attrNames an array of names of the attributes used to sort out
     * the relationships between the objects of the two data sets.
     * @throws MGIException thrown if an error occurs processing the data
     */
    public AbstractBucketizer(Iterator it1, Iterator it2, String[] attrNames)
        throws MGIException
    {
        this.index = new SVAIndex(attrNames);
        initialize(it1, it2, attrNames);
    }

    /**
     * constructor
     * @param it1 a DataIterator for data set one
     * @param it2 a DataIterator for data set two
     * @param attrNames an array of names of the attributes used to sort out
     * the relationships between the objects of the two data sets.
     * @throws MGIException thrown if an error occurs processing the data
     */
    public AbstractBucketizer(DataIterator it1,
                              DataIterator it2, String[] attrNames)
        throws MGIException
    {
        this.index = new SVAIndex(attrNames);
        initialize(it1, it2, attrNames);
    }


    /**
     * the method for running the bucketizer algorithm
     * @param provider1 the name of provider one. Which provider is
     * considered to be one vs two determines how a cardinality is
     * assigned. For example all cardinilaties are based on provider1 to
     * provider2, so that a one to many defines one object from provider1
     * and many objevt from provider2
     * @param provider2 the name of provider two
     * @throws MGIException thrown if there is an error while processing
     * the data
     */
    public void run(String provider1, String provider2)
        throws MGIException
    {
        //stats = new OutputDataFile("reports/bucketizerStats.txt");
        for (Iterator i = this.namedDeciderItems.keySet().iterator(); i.hasNext(); )
        {
            DeciderItem decision = (DeciderItem)this.namedDeciderItems.get(i.
                next());
            Object truth = this.decider.decide(decision.b1,
                                               decision.b2,
                                               decision.decisionSVA);
            if (truth != null)
                this.connectedComponentsGraph.addEdge(decision.b1,
                    decision.b2, decision.decisionSVA.toString());
        }
        Iterator it = Graphs.iterConnectedComponents(connectedComponentsGraph);
/*
        Graph connectedComponent = (Graph)it.next();
        BucketItem bucketItem = new BucketItem(connectedComponent);
        int cardinality = bucketItem.getCardinality(provider1, provider2);
        while (cardinality != BucketItem.ONE_TO_ONE)
        {
            connectedComponent = (Graph)it.next();
            bucketItem = new BucketItem(connectedComponent);
            cardinality = bucketItem.getCardinality(provider1, provider2);
        }
*/
        Graph connectedComponent = null;
        BucketItem bucketItem = null;
        Stopwatch loadStopWatch = new Stopwatch();
        loadStopWatch.start();

        while (it.hasNext())
        {


            connectedComponent = (Graph)it.next();
            bucketItem = new BucketItem(connectedComponent);
            int cardinality = bucketItem.getCardinality(provider1, provider2);

            switch (cardinality)
            {
                case BucketItem.ONE_TO_ONE:
                    this.process_One_To_One(bucketItem);
                    break;
                case BucketItem.ONE_TO_MANY:
                    this.process_One_To_Many(bucketItem);
                    break;
                case BucketItem.ONE_TO_ZERO:
                    this.process_One_To_Zero(bucketItem);
                    break;
                case BucketItem.ZERO_TO_ONE:
                    this.process_Zero_To_One(bucketItem);
                    break;
                case BucketItem.MANY_TO_ONE:
                    this.process_Many_To_One(bucketItem);
                    break;
                case BucketItem.MANY_TO_MANY:
                    this.process_Many_To_Many(bucketItem);
                    break;
            }
            long memEnd = runtime.freeMemory();
            long totalDiff = this.memStart - memEnd;
            long delta = this.memLast - memEnd;
            this.memLast = memEnd;
            //stats.write("Total bytes: " + totalDiff);
            //stats.write("Incr bytes: " + delta);


        }
        loadStopWatch.stop();
        double totalProcessTime = loadStopWatch.time();
        //this.stats.close();
        postProcess();
    }

    /**
     * an empty method intended to overridden by the base class if any post
     * processing is required
     * @throws MGIException can throw any MGIException
     */
    public void postProcess()
        throws MGIException
    {}

    /**
     * processes a one to one group
     * @param bucketItem represents the grouping of objects
     * @throws MGIException can throw any MGIException
     */
    abstract public void process_One_To_One(BucketItem bucketItem)
        throws
        MGIException;

    /**
     * processes a one to many group
     * @param bucketItem represents the grouping of objects
     * @throws MGIException can throw any MGIException
     */
    abstract public void process_One_To_Many(BucketItem bucketItem)
        throws
        MGIException;

    /**
     * processes a many to one group
     * @param bucketItem represents the grouping of objects
     * @throws MGIException can throw any MGIException
     */
    abstract public void process_Many_To_One(BucketItem bucketItem)
        throws
        MGIException;

    /**
     * processes a many to many group
     * @param bucketItem represents the grouping of objects
     * @throws MGIException can throw any MGIException
     */
    abstract public void process_Many_To_Many(BucketItem bucketItem)
        throws
        MGIException;

    /**
     * processes a zero to one group
     * @param bucketItem represents the grouping of objects
     * @throws MGIException can throw any MGIException
     */
    abstract public void process_Zero_To_One(BucketItem bucketItem)
        throws
        MGIException;

    /**
     * processes a one to zero group
     * @param bucketItem represents the grouping of objects
     * @throws MGIException can throw any MGIException
     */
    abstract public void process_One_To_Zero(BucketItem bucketItem)
        throws
        MGIException;

    /**
     * initializes any caches required by the bucketizer algorithm
     * @param it1 an Iteartor for data set 1
     * @param it2 an Iterator for data set 2
     * @param attrNames an array of names of attributes to be used in
     * determining the groupings
     * @throws MGIException thrown if there is an error processing the data
     */
    protected void initialize(Iterator it1, Iterator it2, String[] attrNames)
        throws MGIException
    {
        class DataIteratorImpl implements DataIterator
        {
            Iterator it = null;
            public DataIteratorImpl(Iterator it) {this.it = it;}
            public boolean hasNext() {return this.it.hasNext();}
            public Object next() {return this.it.next();}
            public void close() {}
        }
        DataIterator dataIterator1 = new DataIteratorImpl(it1);
        DataIterator dataIterator2 = new DataIteratorImpl(it2);
        initialize(dataIterator1, dataIterator2, attrNames);
    }

    /**
     * initializes any caches required by the bucketizer algorithm
     * @param it1 a DataIteartor for data set 1
     * @param it2 a DataIterator for data set 2
     * @param attrNames an array of names of attributes to be used in
     * determining the groupings
     * @throws MGIException thrown if there is an error processing the data
     */
    protected void initialize(DataIterator it1,
                              DataIterator it2, String[] attrNames)
        throws MGIException
    {
        while (it1.hasNext())
        {
            Bucketizable b = (Bucketizable)it1.next();
            this.connectedComponentsGraph.addNode(b);
            this.index.add(b, b.getSVASet());
        }
        while (it2.hasNext())
        {
            Bucketizable b = (Bucketizable)it2.next();
            this.connectedComponentsGraph.addNode(b);
            this.index.add(b, b.getSVASet());
        }

        createAllDeciderItems();

    }


    /**
     * builds an internal cache of all the groupings which is stored for
     * subsequent processing
     */
    private void createAllDeciderItems()
    {
        // create objects to pass to decider
        String[] indexNames = this.index.getIndexNames();
        for (int i = 0; i < indexNames.length; i++)
        {
            String indexName = indexNames[i];
            for (Iterator j = this.index.keySet(indexName).iterator();
                 j.hasNext(); )
            {
                Object indexKey = j.next();
                Set indexedItems = this.index.lookup(indexName, indexKey);
                Bucketizable[] bucketizables =
                    (Bucketizable[])indexedItems.toArray(new Bucketizable[1]);
                if (bucketizables.length == 2 &&
                    bucketizables[0].getProvider() !=
                    bucketizables[1].getProvider())
                {
                    String[] sortIds = new String[2];
                    sortIds[0] =
                        bucketizables[0].getProvider() +
                        bucketizables[0].getId();
                    sortIds[1] =
                        bucketizables[1].getProvider() +
                        bucketizables[1].getId();
                    Arrays.sort(sortIds);
                    boolean rearranged = false;
                    if (!(bucketizables[0].getProvider() +
                          bucketizables[0].getId()).equals(sortIds[0]))
                        rearranged = true;
                    String key = sortIds[0] + "-" + sortIds[1];
                    DeciderItem decision = (DeciderItem)this.namedDeciderItems.
                        get(key);
                    if (decision == null)
                    {
                        decision = new DeciderItem();
                        if (!rearranged)
                        {
                            decision.b1 = bucketizables[0];
                            decision.b2 = bucketizables[1];
                        }
                        else
                        {
                            decision.b1 = bucketizables[1];
                            decision.b2 = bucketizables[0];
                        }
                    }
                    HashSet ids = (HashSet)decision.decisionSVA.get(indexName);
                    if (ids == null)
                        ids = new HashSet();
                    ids.add(indexKey);
                    decision.decisionSVA.put(indexName, ids);
                    namedDeciderItems.put(key, decision);
                }
            }
        }

    }


/*
    private void addToIndex(Bucketizable b)
    {
        //SVASet set = createSVASet(b);
        //this.index.add(b, set);
        this.index.add(b, b.getSVASet());
    }
*/

/*
    private SVASet createSVASet(Bucketizable b)
    {
        String[] svaNames = b.getSVANames();
        SVASet set = new SVASet(svaNames);
        for (int i = 0; i < svaNames.length; i++)
        {
            set.addSVA(svaNames[i], b.getSVA(svaNames[i]));
        }
        return set;
    }
*/

/**
 *
 * An object used to store two members, one from each data set, and the
 * attribute values which caused the two members to be associated
 * @has the two matching members and the matching attributes
 * @does nothing
 * @company Jackson Laboratory
 * @author M Walker
 *
 */
    private class DeciderItem
    {
        public Bucketizable b1 = null;
        public Bucketizable b2 = null;
        public Hashtable decisionSVA = new Hashtable();
        public String toString()
        {
            StringBuffer buff = new StringBuffer();
            buff.append(b1.toString());
            buff.append("|");
            buff.append(b2.toString());
            buff.append("|");
            for (Iterator i = decisionSVA.keySet().iterator(); i.hasNext(); )
            {
                String s = (String)i.next();
                HashSet set = (HashSet)decisionSVA.get(s);
                buff.append(s + "=" + set.toString());
            }
            return buff.toString();
        }
    }

}
