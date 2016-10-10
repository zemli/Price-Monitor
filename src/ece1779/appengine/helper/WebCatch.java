package ece1779.appengine.helper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebCatch  {
	public static String Info(String URL, int index) throws IOException{
		if(index<=0||index>8) return null;
		String finalvalue=new String();
		String url = URL;
		Map<String, String> params = new HashMap<String, String>();
		params.put("searcharg", "java");
		params.put("searchtype", "t");
		params.put("SORT", "DZ");
		params.put("extended", "0");
		Document doc = Jsoup.connect(url)
				.userAgent("Mozilla")  
				.timeout(10*1000)   
				.data(params)       
				.get();             
		String html=doc.html();
		if(index==1){
			int position1=html.indexOf("<span id=\"priceblock_ourprice\" class=\"a-size-medium a-color-price\">");
			int position2=html.indexOf("$",position1);
			int position3=html.indexOf("</span>",position2);
			finalvalue=html.substring(position2+2, position3);
			finalvalue=finalvalue.replace(",", "");
			try{
				 double check = Double.parseDouble(finalvalue);
				}catch(Exception e)
				{ return null;}
			}
		if(index==2){
			int position1=html.indexOf("http://ecx.images-amazon.com/images/I/");
			int position2=html.indexOf("\"",position1);
			finalvalue=html.substring(position1, position2);	
		}
		if(index==3){
			int position1=html.indexOf("<span class=\"amount\">");
			int position2=html.indexOf("$",position1);
			int position3=html.indexOf("</span>",position2);
			finalvalue=html.substring(position2+1, position3);
			finalvalue=finalvalue.replace(",", "");
			try{
				 double check = Double.parseDouble(finalvalue);
				}catch(Exception e)
				{ return null;}
		}
		if(index==4){
			int position1=html.indexOf("og:image");
			int position2=html.indexOf("http://",position1);
			int position3=html.indexOf("\"",position2);
			finalvalue=html.substring(position2, position3);	
		}
		if(index==5){
			int position1=html.indexOf("<span id=\"SalePrice\"");
			int position2=html.indexOf("$",position1);
			int position3=html.indexOf(".",position2);
			finalvalue=html.substring(position2+1, position3+3);
			finalvalue=finalvalue.replace(",", "");
			try{
				 double check = Double.parseDouble(finalvalue);
				}catch(Exception e)
				{ return null;}
		}
		if(index==6){
			int position1=html.indexOf("<img src=\"http://img.canadacomputers.com/");
			int position2=html.indexOf("http://",position1);
			int position3=html.indexOf("\"",position2);
			finalvalue=html.substring(position2, position3);	
		}
		if(index==7){
			int position1=html.indexOf("price_store_price");
			int position2=html.indexOf("[",position1);
			int position3=html.indexOf(".",position2);
			finalvalue=html.substring(position2+2, position3+3);
			finalvalue=finalvalue.replace(",", "");
			try{
				 double check = Double.parseDouble(finalvalue);
				}catch(Exception e)
				{ return null;}
		}
		if(index==8){
			int position1=html.indexOf("http://a9.wal.co/images/");
			int position2=html.indexOf("\"",position1);
			finalvalue=html.substring(position1, position2);	
		}
		
		return finalvalue;
	}

}
