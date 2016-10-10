<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.TimeZone" %>

<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<%@ page import="ece1779.appengine.basic.*" %>

<%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();      

	String username=user.getNickname();
	username=username.split("@")[0];

	SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String currenttime=fmt.format(new Date());
	TimeZone.setDefault(TimeZone.getTimeZone("America/Toronto"));

	UserPref pref = UserPref.getPrefForUser(user);
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Price Monitor User Homepage</title>
		<link rel="stylesheet" type="text/css" href="/css/style.css">

		<script type="text/javascript">
                function verifyLink(){
                	var url = addSubForm.link.value.toString();
                	if( url.startsWith("http://www.amazon.ca/") ) {
                    	document.getElementById("addSubFormImg").src="/images/amazon-logo.png";                		
                	} else if( url.startsWith("http://www.bestbuy.ca/") ) {
                       	document.getElementById("addSubFormImg").src="/images/best-buy-logo.png";
            		} else if( url.startsWith("http://www.canadacomputers.com/") ) {
                    	document.getElementById("addSubFormImg").src="/images/canada-computers-logo.png";
            		} else if( url.startsWith("http://www.walmart.ca/") ) {
                    	document.getElementById("addSubFormImg").src="/images/walmart-logo.jpeg"; 
                	} else {
                		alert("Invalid webpage :" + url);
                	}
                }
        </script>
	</head>
	<body>
		<div class="container">
			<div class="content">
				<img src="/images/banner.jpg" />
				<hr />
				<div  class="navigation">
					<a href="/index.html">Home</a> | 
					<a href="/mail.html">Contact Us</a> | 
					<a href="/user/Logout">Log Out</a>
				</div>
				<hr />
				<p>Dear <%=username%>, welcome to the Price Monitor.</p>
				<br />
		
				<table>
					<tr>	<th colspan="9">Your Subscription List</th>	</tr>
					<tr>
						<td style=" width:10%; height:35px;">Item</td>
						<td style=" width:9%;">Name</td>
						<td style=" width:9%;">Current Price</td>
						<td style=" width:8%;">Drop Ratio</td>
						<td style=" width:8%;">Price History</td>
						<td style=" width:22%;">Link</td>
						<td style=" width:24%;">Notification</td>
						<td style=" width:5%;"></td>
						<td style=" width:5%;"></td>
					</tr>
					<tr> <form name="addSubForm" method="post" action="/sub/AddSub">
						<td style=" width:10%; height:80px;">
							<img src="" id="addSubFormImg" border="0" height="70" width="70">
						</td>
						<td> <input type="text" style="width:100%" name="name" value="My Item">	</td>
						<td />
						<td />
						<td />
						<td> <input type="text" style="width:60%" name="link"> 
						     <input type="button" value="verify" onClick="verifyLink()"> 
						</td>
						<td>
		 				 	Alert <select name="alert">
		 				 		<option value="yes" selected="selected">yes</option>
		 				 		<option value="no">no </option>
		 				 	</select>
		 				 	Ratio <input type="number" name="ratio" value="0.1" 
		 				 				 min="0" max="1" step="0.05">
		 				</td>
		 				<td> <input style=" width:100%" type="submit" value="add"> </td>
		 				<td />
					 </form> </tr>
<%		 		for( UserSub sub: pref.getAllUserSub() ) { 	%>
					<tr>
						<td style=" width:10%; height:80px;">
							<img src="<%=sub.getSub().getPhotoURL()%>" border="0" height="70" width="70">
						</td>
						<td><%=sub.getName() %></td>
						<td align="right">
							<%=String.format("%.2f", sub.getSub().getCurPrice())%>
						</td>
						<td align="right" 
							<% if(sub.getSub().getChangeRatio() > 0) out.print("style=\"color:#00FF00\"");
							   else if(sub.getSub().getChangeRatio() < 0) out.print("style=\"color:#FF0000\"");
							   else out.print("style=\"color:#C0C0C0\"");%> >
							<%=String.format("%.2f", sub.getSub().getChangeRatio())%>
						</td>
						<td align="middle"> 
							<form style=" margin-bottom:0em" 
								  method="post" action="/user/ShowDetail.jsp" target="_blank">
								<input type="hidden" name="link" value="<%=sub.getSub().getURL()%>">
								<input type="submit" value="view">
							</form>
						</td>
						<td  align="center"> <input type="button"value="view" onClick="window.open('<%=sub.getSub().getURL()%>')"></td>
						<td>
		 				 	<form style=" margin-bottom:0em" 
		 				 		  method="post" action="/sub/UpdateSub" >
		 				 		Alert <select name="alert">
		 				 			<option value="yes" <% if(sub.getAlert()) out.print("selected=\"selected\""); %>>yes</option>
		 				 			<option value="no" <% if(!sub.getAlert()) out.print("selected=\"selected\""); %>>no </option>
		 				 		</select>
		 				 		Ratio <input type="number" name="ratio" value=<%=sub.getRatio()%> 
		 				 						min="0" max="1" step="0.05">
		 				 		<input type="hidden"  name="link" value="<%=sub.getSub().getURL()%>">
		 				 		<input type="hidden"  name="name" value="<%=sub.getName()%>">
		 				 </td>
		 				 <td>
		 				 		<input type="submit" value="update" width=100%>
		 					</form>
		 				</td>
		 				<td>
		 					<form style=" margin-bottom:0em" method="post" action="/sub/RemoveSub">
								<input type="hidden"  name="link" value="<%=sub.getSub().getURL()%>">
		 				 		<input type="hidden"  name="name" value="<%=sub.getName()%>">
								<input type="submit" value="unsubscription">
							</form>
		 				</td>
					</tr>
<%		 		} %>
				</table>
				
				
				<br /><hr />
				<div class="bottom">
					Popular sites that we support:<br /><br />
					<table class="sites" align="center" width="70%"> <tr class="sites">
						<td width="25%" align="center"><img src="/images/amazon-logo.png" border="0" height="40" width="40"></td>
		    			<td width="25%" align="center"><img src="/images/walmart-logo.jpeg" border="0" height="40" width="40"></td>
		    			<td width="25%" align="center"><img src="/images/canada-computers-logo.png" border="0" height="40" width="40"></td>
						<td width="25%" align="center"><img src="/images/best-buy-logo.png" border="0" height="40" width="40"></td>
					</tr>
					<tr class="sites">
						<td align="center"> <a href="http://www.amazon.ca/" target=_blank> Amazon </a> </td>
						<td align="center"> <a href="http://www.walmart.ca/" target=_blank> Walmart </a> </td>
						<td align="center"> <a href="http://www.canadacomputers.com/" target=_blank> Canada Computers </a> </td>
						<td align="center"> <a href="http://www.bestbuy.ca/" target=_blank> Best&nbsp;Buy </a> </td>
					</tr>
					</table>
					<br />	The current time is <%=currenttime %> <br />
				</div>
			</div>
		</div>		
	</body>
</html>