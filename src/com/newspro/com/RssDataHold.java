package com.newspro.com;

import java.util.ArrayList;

import android.util.Log;

public class RssDataHold {
	
	ArrayList<String> name,rss,category;
	ArrayList<byte[]> imageBytes;
	
	RssDataHold(){
		name=new ArrayList<String>();
		rss=new ArrayList<String>();
		category=new ArrayList<String>();
		imageBytes=new ArrayList<byte[]>();
	}
	
	public void putRssDataName(String nam,int index){
		name.add(index, nam);
		
	}
	public void putRssDataRss(String rs,int index){
		rss.add(index, rs);
		
	}
	public void putRssDataCategory(String cat,int index){
		category.add(index, cat);
		
	}
	public void putRssDataImage(byte[] ima,int index){
		Log.d("Book-shelf", "Imagebytes"+ima.toString());
		imageBytes.add(index, ima);
		
	}
	
	public String getRssDataName(int index){
		return name.get(index);
	}
	public String getRssDataRss(int index){
		return rss.get(index);
	}
	public String getRssDataCategory(int index){
		return category.get(index);
	}
	public byte[] getRssDataImage(int index){
		return imageBytes.get(index);
	}
	public int getcount(){
		return name.size();
	}

}
