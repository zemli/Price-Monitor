<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="java.util.Date" %>
<%@ page import="java.util.ArrayList" %>

<%@ page import="ece1779.appengine.basic.*" %>

<% 
	String url = request.getParameter("link");

	Subscription subscription = Subscription.getSubscriptionForURL( url );
	ArrayList<Double> history = subscription.getPriceHistory();
%>

<html>
  <head>
    <script type="text/javascript" src="https://www.google.com/jsapi?autoload={'modules':[{'name':'visualization','version':'1','packages':['annotationchart']}]}"></script>
	<link rel="stylesheet" type="text/css" href="/css/style.css">
 
    <script type='text/javascript'>
      google.load('visualization', '1', {'packages':['annotationchart']});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = new google.visualization.DataTable();
        var myDate = new Date("<%=subscription.getFetchDate().toString()%>");
        
        data.addColumn('date', 'Date');
        data.addColumn('number', 'Price');
        data.addRows([
<%		for( int i = 0; i < history.size(); i++ ) { 	%>
          [new Date(myDate.getFullYear(), myDate.getMonth(), myDate.getDate() + <%=i%>), <%=(history.get(i)).doubleValue()%>],
<%		} %>
        ]);

        var chart = new google.visualization.AnnotationChart(document.getElementById('chart_div'));

        var options = {
          displayAnnotations: true
        };

        chart.draw(data, options);
      }
    </script>
  </head>

  <body>  
  	<div class="container">
  		<div class="content">
  		  	<img src="/images/banner.jpg" />
			<br /><br />
  		
    		<div id='chart_div' style='width: 900px; height: 500px;' />
    	</div>
    </div>
  </body>
</html>