package ece1779.appengine.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.*;

import ece1779.appengine.basic.Subscription;
import ece1779.appengine.basic.UserSub;
import ece1779.appengine.helper.InfoFetcher;


// import ece1779.appengine.helper.SendMail;
 
@SuppressWarnings("serial")
public class FetchAll extends HttpServlet {
 
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	
        resp.setContentType("text/plain");

    	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    	Query q = new Query("Subscription");

    	PreparedQuery pq = datastore.prepare(q);
    	Date today = new Date();
    	
    	for (Entity result : pq.asIterable()) {
    		String url = (String) result.getProperty("url");

        	InfoFetcher info = new InfoFetcher(url);	
    		if(info.isValidURL()) {
	            Subscription subscription = Subscription.getSubscriptionForURL( url );
	            
	            if(subscription.getFetchDate().getDay()   != today.getDay()   ||
	               subscription.getFetchDate().getMonth() != today.getMonth() ||
	               subscription.getFetchDate().getYear()  != today.getYear() ) {
		        	subscription.addNewPrice(new Double(info.getPrice()));
		        	subscription.setPhotoURL(info.getPhotoURL());
		        	subscription.save();
	        	
		        	ArrayList<UserSub> userSubs = subscription.getAllUserSub();
		        	for(UserSub sub : userSubs) {
		        		if(sub.shouldNotify())	sub.notifyUser();
		        	}
	            }
	        }    	
    	}   
    }
}

