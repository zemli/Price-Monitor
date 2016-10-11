# Price-Monitor

<h2>Description of the application</h2>
The web application we have built is called “Price Monitor”. The main functionality the application provides is to track and monitor the price of online products. Users of our web application searches for their interested online products on the popular sites as usual. When they find the items they want, they provide the URL of that item to the application. After that, the application tracks the price of the product and notifies the users through email if there is a price drop. The goal of the application is to help online shoppers find the proper price to buy the products that they are interested. 

<h2>How to use the application</h2>
Here is a step-by-step instruction about how to use our web application. 
<li>
1.	Go to the Home page through http://uoftece1779-test.appspot.com/. Within the Home page, a high level description of the application, a start button, and a list of the sites the application supports. 
</li><li>
2.	You can login by clicking the start button or by clicking on the “Log In” text on the navigation bar. After that, please use your Google account to login to the application. 
</li><li>
3.	After you have successfully logged in, you will be directed to the homepage for the user. In this page, you will see the list of all the online products that you are currently subscripting. You will have an empty list if it’s the first time you use the application.
</li><li>
4.	To add a new subscription, you will need to find an URL for the product that you are interested. Once you have the URL, please paste it to the text box under “Link” and click on the “verify” button. If your URL is from a supported site, you will see the site’s logo show up on the left. You can then type in the name for your item and modify the notification settings when needed. Finally, you click the “add” button to add the item. 
</li><li>
5.	For example, here is the link for a Dell Alienware gaming laptop on the Amazon, http://www.amazon.ca/gp/product/B00IDC0606/ref=s9_simh_gw_p147_d5_i2?pf_rd_m=A3DWYIK6Y9EEQB&pf_rd_s=center-2&pf_rd_r=0B3M8QDFAKDTDRRYPZC6&pf_rd_t=101&pf_rd_p=1687860322&pf_rd_i=915398 
</li><li>
6.	Once you have added at least one item to the list, you will be able to see the details of the item(s) you have added. The subscription list shows the photo of the products you are interested, the name you define for the product, the current price, and drop ratio. The drop ratio is the comparison of the current price and the price of the previous day, so in most cases, it will be 0. 
</li><li>
7.	Besides the basic information for the item, you can see the price history of the product by clicking the “view” button under “Price History”. Please be aware that the price history starts from the date when this item is first added to the database. So if some other user have added the same item before you, you will see the price history before the date you add. 
</li><li>
8.	You can go to the URL for the product through clicking “view” button under “Link”. 
</li><li>
9.	You can update the notification settings by modifying it and then click on the “update” button. The settings include whether you want to receive an email alert for the price drop and what’s the threshold ratio for notification. 
</li><li>
10.	You can always unsubscribe the item by clicking on the “unsubscription” button. 
</li><li>
11.	Last but not the least, you can send email to the application developer about any feedback by clicking on the “Contact Us” text on the navigation bar. 
</li>
<h2>Appengine Service used by the application</h2>
Our web application uses multiple service provided by Google, includes:
<ol><li>
Google Accounts Service; it’s used for user login
</li><li>
Datastore and Memcache; it’s used for storing data
Cron Jobs; it’s used for running the scheduled task to fetch new price data every day
</li><li>
Mail API; it’s used for sending email
</li><li>
Google Chart; it’s used to plot the chart for price history
</li></ol>

<h2>Description of the architecture</h2>
<ol>
<li>package ece1779.appengine.basic
a.	CMF - The Cache Manager Factory, which is the class for accessing Memcahce;
b.	PMF - The Persistence Manager Factory, which is the class for using JDO to store persistence object.
c.	Subscription - One instance of the Subscription class represents one online product that is being monitored by our web application. It’s contains information about the product, such as the url, the date when this product is first added, the current price and etc. It also maintains a list of the UserSub instances that are related to it. The content of the instances of the subscription class is stored to datastore through JDO. 
d.	UserSub - One instance of the UserSub class represents one subscription entry from a user. It contains information about the user, the subscription product (through Key mapping), and the user’s notification settings. The content of this class is stored through JDO.
e.	UserPref - One instance of the UserPref class represents one user of our application. It contains information about the user and a list of the UserSub instances that belongs to this user. The content of this class is stored through JDO. 
</li><li>package ece1779.appengine.helper 
a.	InfoFetcher - The helper class for fetching information when given an url for an online product. 
b.	WebCatch - The helper class which is based on JSoup API and is performing the actual parsing of the html content to get the current price and profile photo for the given online product. Different website and information type is identified by the series number in this helper class.
c.	SendMail - The helper class for sending mails. The function of sending emails to a specific address is realized by this class.
</li><li>package ece1779.appengine.servlet
a.	UserFeedback - This class sends feedbacks which created by users in mail.html to admin
b.	UpdateSub - updates link, name, whether to send alert email and ratio. Users use this function to change their settings of items listed in Homepage.jsp.
c.	RemoveSub - deletes chosen items in the subscription list.
d.	Logout - realizes logout google account function.
e.	Login - judges whether the user has existing account and login 
f.	FetchAll - The servlet class used by the cron job to perform the daily task (fetching latest price for all subscribed online product.)
g.	AddSub - allows users add new items to subscription list and does a duplication check 
</li><li>HTML and JSP
a.	index.html - the Welcome homepage for the web application	
b.	mail.html - the web page to gather information about users’ feedback and send mails to admin.
c.	user/Homepage.jsp - the homepage for user after successful login. Most of the functionality of the web application is provided in this page. 
d.	user/ShowDetail.jsp - the jsp page to plot the price history for specific product using Google Chart.
</li></ol>


