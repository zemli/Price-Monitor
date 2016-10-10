package ece1779.appengine.servlet;

import java.io.IOException;
import javax.servlet.http.*;
//import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class Logout extends HttpServlet {
    public void doGet(HttpServletRequest req,
                      HttpServletResponse resp)
        throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        String logout=userService.createLogoutURL("/");
        resp.sendRedirect(logout);
        resp.setContentType("text/html");
    }
}