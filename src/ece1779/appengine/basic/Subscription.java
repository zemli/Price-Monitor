package ece1779.appengine.basic;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Persistent;
import javax.jdo.PersistenceManager;
import javax.jdo.JDOObjectNotFoundException;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Key;

import net.sf.jsr107cache.Cache;

@SuppressWarnings("serial")
@PersistenceCapable
public class Subscription implements Serializable {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String url;

	@Persistent
	private Date firstFetch;

	@Persistent
	private double curPrice = 0.0;
	
	@Persistent
	private double changeRatio = 0.0;
	
	@Persistent
	private String photoURL = "";
	
	@Persistent
	private ArrayList<Double> priceHistory = new ArrayList<Double>();

	// Set for UserSub
	@Persistent
	private Set<Key> subscriptors = new TreeSet<Key>();

	/***	Constructor		***/
	public Subscription(Key key, String url, Date date) {
		this.key 		= key;
		this.url 		= url;
		this.firstFetch = date;
	}
	
	/***	Public Getter Functions		***/
	public Key getKey() 						{		return this.key;				}
	public String getURL()						{		return this.url;				}
	public Date getFetchDate()					{		return this.firstFetch;			}
	public double getCurPrice()					{		return this.curPrice;			}
	public double getChangeRatio()				{		return this.changeRatio;		}
	public String getPhotoURL()					{		return this.photoURL;			}
	public ArrayList<Double> getPriceHistory() 	{		return this.priceHistory;		}

	public ArrayList<UserSub> getAllUserSub() {
		ArrayList<UserSub> myUserSub = new ArrayList<UserSub>();
    	Cache cache = CMF.get();

        for(Key k : this.subscriptors) {
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
        	myUserSub.add(sub);
        }
		return myUserSub;
	}

	/***	Public Setter Functions		***/
	public void addNewPrice(Double price) {
		this.curPrice = price.doubleValue();

		if( !this.isNew() ) {
			double prevPrice = this.priceHistory.get(this.priceHistory.size()-1).doubleValue();
			this.changeRatio = (prevPrice - this.curPrice) / prevPrice;
		}
		this.priceHistory.add(price);
	}

	public void addNewSub(UserSub sub) {
		this.subscriptors.add(sub.getKey());
	}
	
	public void setPhotoURL(String url) {
		this.photoURL = url;
	}
	
	public void removeSub(UserSub sub) {
		this.subscriptors.remove(sub.getKey());
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

	/***	Public Functions	***/
	public boolean isNew() {
		return (this.priceHistory.size() == 0);
	}
	
	/***	Static Public Functions		***/
    public static Subscription getSubscriptionForURL(String url) {
        Key k = KeyFactory.createKey(Subscription.getClassName(), url);        

        Cache cache = CMF.get();
        Subscription detached = (Subscription) cache.get(k);
        if(detached == null) {
        	Subscription mySubscription = null;
        	PersistenceManager pm = PMF.get().getPersistenceManager();
        	try {
        		mySubscription = pm.getObjectById(Subscription.class, k);
        	} catch (JDOObjectNotFoundException e ) {
        		mySubscription = new Subscription(k, url, new Date());
        		pm.makePersistent(mySubscription);
        	} finally {
        		detached = pm.detachCopy(mySubscription);
        		pm.close();
        	}
        	cache.put(k, detached);
        }
        return detached;
    }
    
    public static String getClassName() {		return "Subscription";		}
}






