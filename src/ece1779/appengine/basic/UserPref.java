package ece1779.appengine.basic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Persistent;
import javax.jdo.PersistenceManager;
import javax.jdo.JDOObjectNotFoundException;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import net.sf.jsr107cache.Cache;

@SuppressWarnings("serial")
@PersistenceCapable
public class UserPref implements Serializable {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private User user;
	
	// Set for UserSub
	@Persistent
	private Set<Key> subscriptions = new TreeSet<Key>();
	
	/***	Constructor		***/
	public UserPref(Key key, User user) {
		this.key = key;
		this.user = user;
	}
	
	/***	Public Getter Functions		***/    
	public ArrayList<UserSub> getAllUserSub() {
		ArrayList<UserSub> mySub = new ArrayList<UserSub>();
    	Cache cache = CMF.get();

    	for(Key k : this.subscriptions) {
    		UserSub sub = (UserSub) cache.get(k);
    		if(sub == null) {
    			PersistenceManager pm = PMF.get().getPersistenceManager();
    	        try {
    	        	sub = pm.getObjectById(UserSub.class, k);
    	        } finally {
    	        	pm.close();
    	        }
    	        cache.put(k, sub);
    		}
    		mySub.add(sub);
    	}
		return mySub;
	}
	
    /***	Public Setter Functions		***/
	public void addNewSub(UserSub sub) {
		this.subscriptions.add(sub.getKey());
	}
	
	public void removeSub(UserSub sub) {
		this.subscriptions.remove(sub.getKey());
	}
    
	public void save() {
		Cache cache = CMF.get();
		cache.put(this.key, this);
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
        	pm.makePersistent(this);
        } finally {
        	pm.close();
        }
	}
	
	/***	Static Public Functions		***/
    public static UserPref getPrefForUser(User user) {
    	Key k = KeyFactory.createKey(UserPref.getClassName(), user.getUserId());        

    	Cache cache = CMF.get();
    	UserPref detached = (UserPref) cache.get(k);
    	if(detached == null) {
    		UserPref myUserPref = null;
    		PersistenceManager pm = PMF.get().getPersistenceManager();
    		try {
    			myUserPref = pm.getObjectById(UserPref.class, k);
    		} catch (JDOObjectNotFoundException e ) {
    			myUserPref = new UserPref(k, user);
    			pm.makePersistent(myUserPref);
    		} finally {
    			detached = pm.detachCopy(myUserPref);
    			pm.close();
    		}
        	cache.put(k, detached);
        }

        return detached;
    }
    
    public static String getClassName() {    	return "UserPref";		}
}

