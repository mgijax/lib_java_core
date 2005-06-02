package org.jax.mgi.shr.bucketizer;

/**
 *
 * An interface which defines a class that processes a BucketItem, an object
 * created by the AbstractBucketizer during bucketization which represents
 * a grouping of related members from the two data sets
 * @has nothing
 * @does processes a BucketItem in an implementation specific way
 * @company Jackson Laboratory
 * @author M Walker
 *
 */

public interface BucketItemProcessor {
    void processBucketItem(BucketItem bucketItem);
}
