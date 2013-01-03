package com.newspro.com;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractList;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Splash extends Activity{

	SharedPreferences app_start;
	SharedPreferences.Editor app_start_editor;
	Boolean checkFirst;
	ArrayList<String> url,name;
	//String url="http://feeds.bbci.co.uk/news/rss.xml";
	boolean onComplete=false;
	byte[] imageArray;
	String rss;
	String category="all";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		app_start=getSharedPreferences("NewsPro",MODE_WORLD_READABLE);
		checkFirst=app_start.getBoolean("NewsPro", true);
		url=new ArrayList<String>();
		name=new ArrayList<String>(); 
				
		url.add(0, "http://feeds.bbci.co.uk/news/rss.xml");
		name.add(0, "BBC-Top News");
		url.add(1, "http://news.google.com/news?pz=1&cf=all&ned=in&hl=en&output=rss");
		name.add(1,"Google");
		url.add(2,"http://feeds.bbci.co.uk/news/entertainment_and_arts/rss.xml");
		name.add(2, "BBC-Arts");
		url.add(3, "http://www.nytimes.com/services/xml/rss/nyt/World.xml");
		name.add(3, "NYT");
		url.add(4, "http://feeds.bbci.co.uk/news/technology/rss.xml");
		name.add(4, "BBC-Tech");
		url.add(5, "http://news.google.co.in/news?pz=1&cf=all&ned=in&hl=en&topic=m&output=rss");
		name.add(5, "Google-Sports");
		url.add(6,"http://feeds.bbci.co.uk/news/science_and_environment/rss.xml");
		name.add(6,"Science and Environment");
		url.add(7,"http://feeds.bbci.co.uk/news/education/rss.xml");
		name.add(7,"Edication");
		url.add(8,"http://feeds.bbci.co.uk/news/health/rss.xml");
		name.add(8, "Health");
		url.add(9, "http://feeds.bbci.co.uk/news/politics/rss.xml");
		name.add(9, "Politics");
		url.add(10, "http://feeds.bbci.co.uk/news/business/rss.xml");
		name.add(10,"Business");
		
		init();
		
	}
	private void init() {
		// TODO Auto-generated method stub
		if(checkFirst==true){
			//new DataInsertAsync().execute();
			RssDatabase rb=new RssDatabase(this);
			rb.open();
			rb.delete();
			for(int i=0;i<name.size();i++){
				rb.Insert(name.get(i), url.get(i), "all", null);
			}
			
			rb.close();
			app_start_editor=app_start.edit();
			app_start_editor.putBoolean("NewsPro", false);
			app_start_editor.commit();
			Intent x=new Intent(this,NewsProActivity.class);
			startActivity(x);
			finish();
			
		}
		else{
			Intent x=new Intent(this,NewsProActivity.class);
			startActivity(x);
			finish();
		}
		
		
		
	}
	
	public class DataInsertAsync extends AsyncTask<String,Integer,String>{

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(onComplete){
			//Toast.makeText(getApplicationContext(), "Back Process"+app_start.getBoolean("NewsPro", true), 2).show();
			Intent x=new Intent(Splash.this,NewsProActivity.class);
			startActivity(x);
			app_start_editor.putBoolean("NewsPro", false);
			app_start_editor.commit();
			finish();
			}else{Toast.makeText(getApplicationContext(), "No Internet", 2).show();}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
		for(int i=0;i<url.size();i++){
			
			HttpPost p=new HttpPost(url.get(i));
			
			parseIt(p);
			
		}
		
			return null;
		}

		

		private void parseIt(HttpPost p) {
			// TODO Auto-generated method stub
			HttpClient hclient=new DefaultHttpClient();
			HttpResponse response = null;
			DocumentBuilder builder = null;
			Document doc;
			InputStream in = null;	
			try {
				response=hclient.execute(p);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			try {
				in=response.getEntity().getContent();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			StringBuilder stringBuilder=new StringBuilder();
			
			int b;
			try {
				while(( b = in.read() )!= -1){
					stringBuilder.append((char)b);
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
				try {
					builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					doc = builder.parse(new InputSource(new StringReader(stringBuilder.toString())));
					getImage(doc);
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
		}
		
	}
	private void getImage(Document doc) {
		// TODO Auto-generated method stub
		NodeList nodeList=null;
		
		try{
		try{	
			nodeList=doc.getElementsByTagName("image").item(0).getChildNodes();
		}catch(Exception e){}
		if(nodeList==null) {
			Log.d("Book Shelf","In Channel if");
			nodeList=doc.getElementsByTagName("channel");
			;//.item(0).getChildNodes();
			for(int i=0;i<=nodeList.getLength();i++){
				Node child=null;
				Log.d("Book Shelf","Got channel element"+i);
				try{
					Log.d("Book-Shelf!!!!!!", nodeList.item(i).getNodeName().toString());
				child=nodeList.item(i);
				Log.d("Book-Shelf!!!!!!", child.getNodeName());
				}catch(Exception e){}
				if(child!=null){
					nodeList=child.getChildNodes();
				}
			}
			
		}
		Log.i("Activity", "Get Image Started");
		for(int i=0;i<nodeList.getLength();i++){
			Log.i("Value", "Settibg"+nodeList.item(i).getNodeName().toString());
			setValue(nodeList.item(i));
			
		}
		
		RssDatabase rssdb=new RssDatabase(this);
		rssdb.open();
		Log.i("database", "Beforeafterinserting");
		//rssdb.Insert(name, rss, category, imageArray);
		
		Log.i("database", "afterinserting"+name+":"+rss+":"+category+":");
		rssdb.close();
		onComplete=true;
		}catch(Exception e){
			//Toast.makeText(getApplicationContext(), "No Internet", 2).show();
			onComplete=false;
		}
		
	}
	private void setValue(Node item) {
		// TODO Auto-generated method stub
		Log.i("Activity", "DataSetting Started");
		if(item.getNodeName().toString().equals("url")){
			imageArray=new byte[1024];
			Bitmap drawable=null;
			Log.i("Activity", "Into Image function");
			try {
	    		
    			URL Url = new URL(item.getTextContent().toString());
    			InputStream is = (InputStream) Url.getContent();
    			drawable=BitmapFactory.decodeStream(is);
    			
    					
    		} catch (MalformedURLException e) {
				e.printStackTrace();
			}catch (OutOfMemoryError e) {
	    	   e.printStackTrace();
	    	   drawable= null;
	       }catch (IOException e) {
	            e.printStackTrace();
	            drawable= null;
        	}
	       catch (Exception e) {
			e.printStackTrace();
			drawable= null;
		}
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			drawable.compress(Bitmap.CompressFormat.PNG, 100, stream);
			
			imageArray=stream.toByteArray();
			Log.i("Activity", "image set");

		}
		else if(item.getNodeName().toString().equals("title")){
			//name=item.getTextContent().toString();
			Log.i("Seetting", "title:"+name);
		}
	
		
		
	}

	
	
	
	

}
