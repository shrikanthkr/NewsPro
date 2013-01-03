package com.newspro.com;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class DataHold implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6922209159714817953L;

	String title,link,description,pubdate,image;
	Parcelable.Creator<DataHold> CREATOR;
	Parcel source;
	DataHold(){
		title=image="";
		link=description=pubdate="";
		
		
	}
	
	public String  getTitle(){
		return title;
		
	}
	public String  getDescription(){
		return description;
		
	}
	public String  getlink(){
		return link;
		
	}
	public String  getImageLink(){
		return image;
		
	}
	public void  setTitle(String ti){
		title= ti;
		
		
	}
	public void  setLink(String lin){
		link=lin;
		
		
	}
	public void  setDescription(String des){
		description=des;
		
	}
	public void  setImageLink(String ima){
		image=ima;
		
	}
	
	
	
}