// $HEADER$
// $NAME$

package org.jax.mgi.shr.bucketizer;

import java.util.Hashtable;

import org.jax.mgi.shr.sva.SVASet;

/**
 * An implementation of a Decider which always returns true. It is used
 * by the AbstractBucketizer class as the default implementation for
 * the Decider instance
 * @has nothing
 * @does returns an object which represents true
 * @company Jackson Laboratory
 * @author M Walker
 *
 */


public class DefaultDecider implements Decider
{
    public Object decide(Bucketizable b1, Bucketizable b2, SVASet svaSet)
    {
        return svaSet;
    }

}

// $LOG$
