package com.newspro.com;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

public class ImageLoader {
	
	LazyAdapter mAdapter=null;
	private HashMap<Integer, SoftReference<Bitmap> >Cache = null;
	private ExecutorService _exec =null;
	ArrayList<String> urlList;
	public ImageLoader(LazyAdapter arg)
	{
		mAdapter=arg;
		Cache=new HashMap<Integer,  SoftReference<Bitmap>>();
		urlList=mAdapter.getUrlList();
	}
	
	
	
	private class WorkerThread extends AsyncTask<Void, Void, Void> implements Runnable{

		
		String url=null;
		int intex=0;
		
	
		public WorkerThread(String ur,int pos) {
		
			url=ur;
			intex=pos;
			}
		
		@Override
		protected Void doInBackground(Void... params) {
			
		
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			mAdapter.notifyDataSetChanged();
			
			super.onProgressUpdate(values);
			
		}
		public   void run() {
			
			Cache.put(intex,new SoftReference<Bitmap>(readDrawableFromNetwork(url)));
			
			publishProgress();
			Log.i("In Threar:" + intex, url+"");
			
		}
	}

	public Bitmap getDrawble(int pos, ProgressBar pb)
	{
		SoftReference<Bitmap> mReference=Cache.get(pos);
		if(mReference!=null){
			pb.setVisibility(View.INVISIBLE);
			return mReference.get();
		}
		else
			return null;
		
	}
	
	
	/*method to get Image if already or to start new task to download*/
	public void loadImage(int First,int Last) {
		try{
				if(_exec!=null){
					_exec.shutdownNow();
					_exec=null;
				}
				_exec = Executors.newFixedThreadPool(5);
				
				for(int pos=First;pos<=Last;pos++){
					
					if(Cache.containsKey(pos)){
						
						if(Cache.get(pos).get()==null)	{
							_exec.execute(new  WorkerThread(urlList.get(pos),pos));
							Log.i("KeyFound :"+pos, "Bu null");
						}
						else{
							Log.i("From hash map", "Not download");
						}
					}
					else{
						_exec.execute(new  WorkerThread(urlList.get(pos),pos));
					}
					
				}
		}catch (Exception e) {
	
		}
				
	}//end of method

	
	private static Bitmap readDrawableFromNetwork(String url ) {
	   	Bitmap drawable=null;
	    	try {
	    		
	    			URL Url = new URL(url);
	    			InputStream is = (InputStream) Url.getContent();
	    			
	    			return BitmapFactory.decodeStream(is);
	    					
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
	    	
	    return drawable;
	}//end of method
}
