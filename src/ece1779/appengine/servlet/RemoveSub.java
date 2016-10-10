package ece1779.appengine.servlet;

import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import ece1779.appengine.basic.*;

@SuppressWarnings("serial")
public class RemoveSub extends HttpServlet {
    public void doPost(HttpServletRequest req,
            		   HttpServletResponse resp) 
        throws IOException {

    	// Get Parameters from the request
    	String url   = req.getParameter("link");
    	String name  = req.getParameter("name");

    	if(url != null && name != null) {
    		UserService userService = UserServiceFactory.getUserService();
    		User user = userService.getCurrentUser();
    		UserPref pref = UserPref.getPrefForUser(user);
    		
            Subscription subscription = Subscription.getSubscriptionForURL( url );
    		UserSub userSubscription = UserSub.getSubForUser(user, name, subscription);
    		
            subscription.removeSub(userSubscription);
        	subscription.save();
        		        	
        	pref.removeSub(userSubscription);
        	pref.save();
        	
        	userSubscription.remove();
    	}
        resp.sendRedirect("/user/Homepage.jsp");
    }
}


