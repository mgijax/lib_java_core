package org.jax.mgi.shr.cache;

/*
* $Header$
* $Name$
*/

/**
* @module ExpiringObjectCache.java
* @author jsb
*/

import java.util.HashMap;

/** provides a mechanism for temporarily associating an object with a
*    String identifier.
* A cache for objects, each identified by a String identifer, and each
*    with an expiration time at which it is no longer considered valid.
* @has a mapping of String IDs to objects.  Each mapping can have an
*    expiration time independent of all others.
* @does associates an object with a key, retrieves an object using a key,
*    removes objects which have expired.
* @notes You may create individual <tt>ExpiringObjectCache</tt> objects as
*    needed, but you may also use the <tt>getSharedCache()</tt> method to
*    retrieve a single instance to be shared.  This shared instance would be
*    useful in cases where you'd like multiple objects to share a cache, but
*    do not want to pass it around as a parameter.
*/
public class ExpiringObjectCache
{
    /////////////////////
    // instance variables
    /////////////////////

    // maps String identifiers to the CacheEntry objects to which they refer
    private HashMap cache = null;

    // standard lifetime of an object in this cache (in milliseconds) if no
    // specific time is given when the object is added
    private long defaultLifetime;

    ///////////////////
    // static variables
    ///////////////////

    // instance of this class to be shared among objects requesting it
    private static ExpiringObjectCache sharedCache = null;

    // standard lifetime of an object in a cache if no default lifetime is
    // specified when the cache is instantiated.  default is ten minutes,
    // measured in milliseconds.
    private static long standardDefaultLifetime = 10 * 60 * 1000;

    ///////////////
    // constructors
    ///////////////

    /** constructor.   uses the standard default lifetime for objects added to
    *    this cache without a particular lifetime specified.  (ten minutes)
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    */
    public ExpiringObjectCache ()
    {
        this.cache = new HashMap();
	this.defaultLifetime = standardDefaultLifetime;
	return;
    }

    /** constructor.  uses the specified <tt>defaultLifetime</tt> for objects
    *    added to this cache without a particular lifetime specified.
    * @param defaultLifetime default lifetime for objects added to this cache
    *    without a particular lifetime specified.  This is measured in
    *    seconds.
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    */
    public ExpiringObjectCache (long defaultLifetime)
    {
        this.cache = new HashMap();
	this.defaultLifetime = defaultLifetime * 1000;
	return;
    }

    //////////////////////////
    // public instance methods
    //////////////////////////

    /** adds the given <tt>obj</tt> to the cache, associating it with the
    *    given <tt>key</tt> and using the default timeout value for this
    *    cache.
    * @param key unique String used to identify this <tt>obj</tt> in the cache
    * @param obj Object to be identified by the given <tt>key</tt>
    * @return nothing
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    * @notes If there is already an item associated with <tt>key</tt> in this
    *    cache, then the new <tt>obj</tt> replaces it.
    */
    public void put (String key, Object obj)
    {
	CacheEntry entry = new CacheEntry (obj,
				this.defaultLifetime +
				    System.currentTimeMillis() );
	this.cache.put (key, entry);
	return;
    }

    /** adds the given <tt>obj</tt> to the cache, associating it with the
    *    given <tt>key</tt> and using the given <tt>lifetime</tt> as its
    *    timeout value.
    * @param key unique String used to identify this <tt>obj</tt> in the cache
    * @param obj Object to be identified by the given <tt>key</tt>
    * @param lifetime number of seconds for which this <tt>obj</tt> will be
    *    considered valid
    * @return nothing
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    * @notes If there is already an item associated with <tt>key</tt> in this
    *    cache, then the new <tt>obj</tt> replaces it.
    */
    public void put (String key, Object obj, long lifetime)
    {
	CacheEntry entry = new CacheEntry (obj,
				1000 * lifetime + System.currentTimeMillis());
	this.cache.put (key, entry);
        return;
    }

    /** retrieves the Object associated with <tt>key</tt> in the cache, if it
    *    has not expired.
    * @param key unique String used to identify the Object you want to return
    * @return Object the Object associated with <tt>key</tt> in this cache.
    *    returns null if the Object has expired or if there is no definition
    *    for the given <tt>key</tt>.
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    */
    public Object get (String key)
    {
        CacheEntry entry = null;

	// if the given 'key' is not defined in this cache, return null

        if (!this.cache.containsKey(key))
	{
	    return null;
	}

	// otherwise, if the object has not expired, then get and return it.

	entry = (CacheEntry) this.cache.get(key);
	if (entry.getExpirationTime() > System.currentTimeMillis())
	{
	    return entry.getObject();
	}

	// At this point, we know that the object exists and that it has
	// expired, so just remove it from the cache.

	this.cache.remove (key);
	return null;
    }

    /** reset this cache by removing all keys and their associated objects.
    * @return nothing
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    */
    public void reset ()
    {
        this.cache.clear();
	return;
    }

    /** set the default liftime for objects added to this cache.  This value
    *    will be used whenever a lifetime is not explicitly specified for an
    *    object.
    * @param defaultLifetime default number of seconds for which an object
    *    defined in this cache is considered to be valid.
    * @return nothing
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    * @notes This value is not retroactive; that is, it applies only to
    *    objects added to the cache after this method is called.
    */
    public void setDefaultLifetime (long defaultLifetime)
    {
        this.defaultLifetime = defaultLifetime * 1000;
	return;
    }

    ////////////////////////
    // public static methods
    ////////////////////////

    /** retrieves a singleton ExpiringObjectCache which is instantiated once
    *    and then simply returned for all future calls of this method.
    * @return ExpiringObjectCache a singleton ExpiringObjectCache
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    * @notes This singleton ExpiringObjectCache would be useful for cases
    *    where you want the same cache shared among objects in different
    *    packages.
    */
    public static ExpiringObjectCache getSharedCache ()
    {
	// if the shared copy has not already been instantiated, then do it...

        if (sharedCache == null)
	{
	    sharedCache = new ExpiringObjectCache();
	}
	return sharedCache;
    }

    //////////////////////
    // private inner class
    //////////////////////

    /** serves as a container for an object and its expiration time.  This
    *    class is not meant for use outside the ExpiringObjectCache, so it
    *    is defined as a private inner class.
    *  one entry in an ExpiringObjectCache
    * @has an object and its expiration time
    * @does provides accessor methods to the attributes mentioned above.
    */
    private class CacheEntry
    {
	/////////////////////////////
	// private instance variables
	/////////////////////////////

	// time at which this object is no longer considered valid, measured
	// as a number of milliseconds since the epoch
	private long expirationTime;

	// the actual item entered in the cache
	private Object item;

	//////////////
	// constructor
	//////////////

        /** constructor.
	* @param item the Object entered into the cache
	* @param expirationTime time at which 'item' is no longer considered
	*    valid, measured in milliseconds since the epoch
	* @assumes nothing
	* @effects nothing
	* @throws nothing
	*/
	protected CacheEntry (Object item, long expirationTime)
	{
	    this.expirationTime = expirationTime;
	    this.item = item;
	    return;
	}

	///////////////////
	// instance methods
	///////////////////

	/** accessor method -- retrieves the expiration time for the object.
	* @return long time at which the object is no longer valid, measured
	*    in milliseconds since the epoch
	* @assumes nothing
	* @effects nothing
	* @throws nothing
	*/
	protected long getExpirationTime ()
	{
	    return this.expirationTime;
	}

	/** accessor method -- retrieves the actual cached object
	* @return Object the actual object added to the cache.
	* @assumes nothing
	* @effects nothing
	* @throws nothing
	*/
	protected Object getObject ()
	{
	    return this.item;
	}
    }
}

/*
* $Log$
* Revision 1.1  2003/12/30 16:49:50  mbw
* imported into this product
*
* Revision 1.1  2003/12/01 13:16:48  jsb
* Added per JSAM code review
*
* $Copyright$
*/
