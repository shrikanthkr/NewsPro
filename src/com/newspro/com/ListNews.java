package com.newspro.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ListNews extends Activity implements OnItemClickListener, OnItemLongClickListener{
	LazyAdapter adapter;
	ListView title;
	ListNews context;
	ArrayList<String> titleList,desList,urlList,imageList; 
	Bundle b=new Bundle();
	String name,rss;
	RssDatabase rb;
	HttpPost p;
	HttpClient hclient;
	HttpResponse response;
	DocumentBuilder builder;
	Document doc;
	InputStream in;
	int failureFlag=0;
	int clickFlag=0;
	String error="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		context=this;
		b=getIntent().getExtras();
		name=b.getString("TITLE");
		
		titleList=new ArrayList<String>();
		desList=new ArrayList<String>();
		urlList=new ArrayList<String>();
		imageList=new ArrayList<String>();
		rb=new RssDatabase(this);
		rb.open();
		rss=rb.getUrlByName(name);
		rb.close();
		Toast.makeText(this,rss, 2).show();
		p=new HttpPost(rss);
		hclient=new DefaultHttpClient();
		new AsyncClass().execute();
		
		
		
	}
	
	public final String getElementValue( final Node elem, final int i ) {
        
		 
        final DataHold datahold=new DataHold();
        
       NodeList nl= elem.getChildNodes();
      
        if( elem != null){
            if (elem.hasChildNodes()){
            	
            	
            	
            			for(int z=0;z<nl.getLength();z++){
            				
            				nl.item(z);
            				if( (nl.item(z).getNodeName().toString().trim().equals("link") ) ){
            					urlList.add(i,nl.item(z).getTextContent());
            				}
            				else if((nl.item(z).getNodeName().toString().trim().equals("title") )){
            					titleList.add(i,nl.item(z).getTextContent());
            				}
            				else if((nl.item(z).getNodeName().toString().trim().equals("description") )){
            					desList.add(i,nl.item(z).getTextContent());
            				}
            				else if((nl.item(z).getNodeName().toString().trim().contains("media") )){
            					try{
                                	
                                	imageList.add(i,nl.item(z).getAttributes().getNamedItem("url").getNodeValue().toString());
                                	Log.d("IMAGE", nl.item(z).getAttributes().getNamedItem("url").getNodeValue().toString());
                                	
                                	}catch(Exception e){
                                		datahold.setImageLink(null);
                                    	imageList.add(i,datahold.getImageLink());
                                		
                                	}
            				}
            			}
                    	
                    	
                    	
                    	
                    	
                  
                    	
                    	
        			
        				
        				
        				
                
            }
        }
        return "";
 }
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		clickFlag=0;
		if(clickFlag==0){
		Toast.makeText(getApplicationContext(), titleList.get(arg2), 2).show();
		Bundle b=new Bundle();
		Intent i=new Intent(this,FullNews.class);
		b.putString("TITLE", titleList.get(arg2));
		b.putString("DESCRIPTION", desList.get(arg2));
		b.putString("URL", urlList.get(arg2));
		i.putExtras(b);
		startActivity(i);
		}
		
	}
	public class AsyncClass extends AsyncTask<String,Integer,String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			
			try {
				response=hclient.execute(p);
				
				in=response.getEntity().getContent();
				
				StringBuilder stringBuilder=new StringBuilder();
				
				int b;
				while(( b = in.read() )!= -1){
		        	stringBuilder.append((char)b);
		        	
		        }
				
					Log.d("AsynClass", "Stringbuilder done");
					builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					doc = builder.parse(new InputSource(new StringReader(stringBuilder.toString())));
					NodeList result=doc.getElementsByTagName("item");
					Node node;	
					for(int i=0;i<result.getLength();i++){
						node=result.item(i);
						 getElementValue(node,i);
						Log.d("AsyncClass", "Got Value"+i);
					}
					failureFlag=1;
					}
			catch(Exception e){
				failureFlag=0;
				error=e.getMessage();
				
				
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(failureFlag!=0){
				setContentView(R.layout.main);
				title=(ListView)findViewById(R.id.listView);
			adapter=new LazyAdapter(context, titleList, imageList, title);
			title.setAdapter(adapter);
			title.setOnItemClickListener(context);
			title.setOnScrollListener(new ListAdapterScrollListener(adapter));
			title.setOnItemLongClickListener(context);
			
			}
			else
			{
				setContentView(R.layout.background);
				Log.i("TAG", error);
				Toast.makeText(getApplicationContext(), "No Intenet conection found", 2).show();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			setContentView(R.layout.list_pre);
		}
		
		
	}
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		clickFlag=1;
		Toast.makeText(getApplicationContext(), "Adding to read list"+titleList.get(arg2), 2).show();
		ReadListDatabase rldb=new ReadListDatabase(this);
		rldb.open();
		rldb.Insert(titleList.get(arg2), urlList.get(arg2));
		rldb.close();
		
		
		return false;
	}


	

}
