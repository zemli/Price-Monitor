package ece1779.appengine.servlet;

import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import ece1779.appengine.basic.*;
import ece1779.appengine.helper.*;

@SuppressWarnings("serial")
public class AddSub extends HttpServlet {
    public void doPost(HttpServletRequest req,
            		   HttpServletResponse resp) 
        throws IOException {

    	// Get Parameters from the request
    	String url   = req.getParameter("link");
    	String name  = req.getParameter("name");
    	String alert = req.getParameter("alert");
    	String ratio = req.getParameter("ratio");

    	if(url != null && name != null && ratio != null && alert != null) {
    		InfoFetcher info = new InfoFetcher(url);
    		
    		if(info.isValidURL()) {
	    		UserService userService = UserServiceFactory.getUserService();
	    		User user = userService.getCurrentUser();
	    		UserPref pref = UserPref.getPrefForUser(user);
	
	            Subscription subscription = Subscription.getSubscriptionForURL( url );
	    		UserSub userSubscription = UserSub.getSubForUser(user, name, subscription);
	    		userSubscription.setAlert(alert.equals("yes"));
	    		userSubscription.setRatio(Double.parseDouble(ratio));
	    		userSubscription.save();
	
	            subscription.addNewSub(userSubscription);
	            if( subscription.isNew() ) {
	            	subscription.addNewPrice(new Double(info.getPrice()));
	            	subscription.setPhotoURL(info.getPhotoURL());
	            }
	        	subscription.save();
	        		        	
	        	pref.addNewSub(userSubscription);
	        	pref.save();
	        	
    		}
    	}
        resp.sendRedirect("/user/Homepage.jsp");
    }
}

