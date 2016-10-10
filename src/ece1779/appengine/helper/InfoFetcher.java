package ece1779.appengine.helper;

public class InfoFetcher {
	private String link;
	private boolean valid;
	private double price    = 0.0;
	private String photoURL = null;

	public InfoFetcher(String url) {
		this.link = url;
		if( 	   url.startsWith("http://www.amazon.ca/") ) {
			this.valid = fetchAmazon();
		} else if( url.startsWith("http://www.bestbuy.ca/") ) {
			this.valid = fetchBestbuy();
		} else if( url.startsWith("http://www.canadacomputers.com/") ) {
			this.valid = fetchCanadacomputers();
		} else if( url.startsWith("http://www.walmart.ca/") ) {
			this.valid = fetchWalmart();
		}else {
			this.valid = false;
		}
	}
	
	/***	Public Getter Functions		***/
	public boolean isValidURL() {		return this.valid;		}
	public double getPrice() 	{		return this.price;		}
	public String getPhotoURL() {		return this.photoURL;	}	


	/***	Amazon Data		***/
	private boolean fetchAmazon() {
		try{
			this.price=new Double(WebCatch.Info(link, 1));
			this.photoURL=WebCatch.Info(link, 2);
		}catch (Exception e){
			return false;
		}
		return true;
	}

	/***	Bestbuy Data	***/
	private boolean fetchBestbuy() {
		try{
			this.price=new Double(WebCatch.Info(link, 3));
			this.photoURL=WebCatch.Info(link, 4);
		}catch (Exception e){
			return false;
		}
		return true;
	}
	private boolean fetchCanadacomputers() {
		try{
			this.price=new Double(WebCatch.Info(link, 5));
			this.photoURL=WebCatch.Info(link, 6);
		}catch (Exception e){
			return false;
		}
		return true;
	}
	private boolean fetchWalmart() {
		try{
			this.price=new Double(WebCatch.Info(link, 7));
			this.photoURL=WebCatch.Info(link, 8);
		}catch (Exception e){
			return false;
		}
		return true;		
	}
}
