package org.jax.mgi.shr.bucketizer;

import java.util.Set;
import org.jax.mgi.shr.sva.SVASet;

/**
 *
 * An interface for defining a class which which can be represented within the
 * bucketizer algorithm of the AbstractBucketizer as one that contains an
 * SVASet (attributes used for finding the members that are related to each
 * other from two data sets). Two sets of Bucketizables are compared by the
 * AbstractBucketizer and all the relationships between Bucketizables are
 * calculated
 * @has an id, a provider and a SVASet of attributes
 * @does nothing
 * @company Jackson Laboratory
 * @author M Walker
 *
 */

public interface Bucketizable {
    public String getId();
    public String getProvider();
    public SVASet getSVASet();
}
