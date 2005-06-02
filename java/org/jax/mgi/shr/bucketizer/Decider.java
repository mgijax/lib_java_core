package org.jax.mgi.shr.bucketizer;

import java.util.Hashtable;

/**
 *
 * An interface for defining a class which can decide whether two objects are
 * truely associated. The AbstractBucketizer has a Decider which it calls
 * when it derives that the two objects are associated and will allow an
 * implementation to accept or decline the association. A default Decider is
 * used by the AbstractBucketizer which always returns true.
 * @has nothing
 * @does returns an object (defined by the implementation) if true else returns
 * null if false.
 * @company Jackson Laboratory
 * @author M Walker
 *
 */

public interface Decider {
    Object decide(Bucketizable setMember1, Bucketizable setMember2, Hashtable sva);
}
