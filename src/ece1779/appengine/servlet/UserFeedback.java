package ece1779.appengine.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ece1779.appengine.helper.SendMail;
 
@SuppressWarnings("serial")
public class UserFeedback extends HttpServlet {
 
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	String name 	   = req.getParameter("name");
    	String email	   = req.getParameter("email");
    	String description = req.getParameter("description");
 
        String subject = "User Feedback from " + email;
        SendMail.send(	"zhi.li.gz@gmail.com", name, 
        				"zhi.li.gz@gmail.com", "Price Monitor Admin",
        				subject, description);

        resp.setContentType("text/plain");
        resp.getWriter().println("Thank you for using Price Monitor. An Email has been send out.");
    }
}