package ece1779.appengine.servlet;

import java.io.IOException;
import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class Login extends HttpServlet {
    public void doGet(HttpServletRequest req,
                      HttpServletResponse resp)
        throws IOException {
    	UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();      
        String login;
        if (user != null) {										// have login
        	resp.sendRedirect("/user/Homepage.jsp");
        } else {														// have not login
            login = userService.createLoginURL("/user/Homepage.jsp");
            resp.sendRedirect(login);
        }
        resp.setContentType("text/html");
    }
}