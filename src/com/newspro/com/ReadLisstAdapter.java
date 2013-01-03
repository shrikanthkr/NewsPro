package com.newspro.com;

import java.util.ArrayList;

import com.newspro.com.LazyAdapter.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ReadLisstAdapter extends BaseAdapter{
	
	
	
	private Activity activity;
	ArrayList<String> title_name_array;
	ArrayList<String> url_array;
	private LayoutInflater inflater=null;
	BaseAdapter adapter;
	Thread t;
	ListView lv;
	
	public ReadLisstAdapter(Activity activity2,ArrayList<String> title_array,ArrayList<String> url_array,ListView lv)
	{
		activity=activity2;
		title_name_array=title_array;
		this.url_array=url_array;
		inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.lv=lv;
		

	}
	
	
	public ArrayList<String> getUrlList(){
		return url_array;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return title_name_array.size();
	}
	
	public ListView getListView(){
		return lv;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	
	public class ViewHolder
	{
		public TextView title;
	}
	
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		final View vi;
		final ViewHolder holder;
		
		vi=inflater.inflate(R.layout.readlist_item, null);
		holder=new ViewHolder();
		try{
		holder.title=(TextView) vi.findViewById(R.id.head_textView);
		holder.title.setText(title_name_array.get(arg0));
		}catch(Exception e){}
		return vi;
	}
}
