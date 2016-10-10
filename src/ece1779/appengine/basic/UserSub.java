package ece1779.appengine.basic;

import java.io.Serializable;

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

import ece1779.appengine.helper.SendMail;

@SuppressWarnings("serial")
@PersistenceCapable
public class UserSub implements Serializable {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private User user;
	
	@Persistent
	private String name;
	
	@Persistent
	private Key subscription;
	
	// Whether want to be alerted about price change
	@Persistent
	private boolean alert = false;
	
	// What is the change ratio for sending alert
	@Persistent
	private double ratio = 0.1;
		
	/***	Constructor		***/
	public UserSub(Key key, User user, String name, Key sub) {
		this.key = key;
		this.user = user;
		this.name = name;
		this.subscription = sub;
	}
		
	/*** 	Public Getter Functions 	***/
	public Key getKey() 		{		return this.key;		}
    public User getUser() 		{    	return this.user;		}
    public String getName() 	{    	return this.name;		}
    public boolean getAlert() 	{    	return this.alert;		}
    public double getRatio() 	{    	return this.ratio;		}
    
    public Subscription getSub() {
    	Cache cache = CMF.get();
    	Subscription mySub = (Subscription) cache.get(this.subscription);
    	if(mySub == null) {
    		PersistenceManager pm = PMF.get().getPersistenceManager();
    		try {
    			mySub = pm.getObjectById(Subscription.class, this.subscription);
    		} finally {
    			pm.close();
    		}
    		cache.put(this.subscription, mySub);
    	}

    	return mySub;
    }
    
    /*** 	Public Setter Functions 	***/
    public void setAlert(boolean alert)		{		this.alert = alert;		}
    public void setRatio(double ratio)		{		this.ratio = ratio; 	}
    
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
	
	public void remove() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        UserSub myUserSub = pm.getObjectById(UserSub.class, this.key);
        try {
        	pm.deletePersistent(myUserSub);
        } finally {
        	pm.close();
        }
    }
	
	/***	Public Functions		***/
	public boolean shouldNotify() {
		return this.getSub().getChangeRatio() > this.ratio;
	}
	
	public void notifyUser() {
        String subject = "Price Drop Alert";
        String msgBody = "Dear " + this.user.getNickname() + "\n" +
        				 "\nThe item \"" + this.name + "\" has a change ratio of more than " + this.ratio + ".\n";
        SendMail.send(	"zhi.li.gz@gmail.com", "Price Monitor Admin",
        				this.user.getEmail(), this.user.getNickname(),         				
        				subject, msgBody);
	}
	
	/***	Static Public Functions	***/
    public static UserSub getSubForUser(User user, String name, Subscription sub) {
    	Key k = KeyFactory.createKey(UserSub.getClassName(), user.getUserId() + name);        

    	Cache cache = CMF.get();
    	UserSub detached = (UserSub) cache.get(k);
    	if(detached == null) {    	
    		UserSub myUserSub = null;
    		PersistenceManager pm = PMF.get().getPersistenceManager();
    		try {
    			myUserSub = pm.getObjectById(UserSub.class, k);
    		} catch (JDOObjectNotFoundException e ) {
    			myUserSub = new UserSub(k, user, name, sub.getKey());
    			pm.makePersistent(myUserSub);
    		} finally {
    			detached = pm.detachCopy(myUserSub);
    			pm.close();
    		}
        	cache.put(k, detached);
    	}
        return detached;
    }
    
    public static String getClassName() {		return "UserSub";		}
}

