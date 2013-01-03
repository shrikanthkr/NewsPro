package com.newspro.com;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter implements OnScrollListener{

	private Activity activity;
	ArrayList<String> title_name_array;
	ArrayList<String> image_name_array;
	private LayoutInflater inflater=null;
	ImageLoader mLoader=null;
	BaseAdapter adapter;
	Thread t;
	ListView lv;
	
	public LazyAdapter(ListNews dailymonitor,ArrayList<String> title_array,final ArrayList<String> image_array,ListView lv)
	{
		activity=dailymonitor;
		title_name_array=title_array;
		image_name_array=image_array;
		inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 mLoader=new ImageLoader(this);
		this.lv=lv;
		
		
		  t=new Thread(){
			 public void run(){
		 mLoader.loadImage(0, 10);
		
			 }
		 };
			t.start();
		 inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		 
		
	}
	
	public ArrayList<String> getUrlList(){
		return image_name_array;
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
		public TextView food_name;
		public ImageView image_view;
		public ProgressBar pb;
	}

	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		final View vi;
		final ViewHolder holder;
		
		vi=inflater.inflate(R.layout.list_item, null);
		holder=new ViewHolder();
		holder.food_name=(TextView) vi.findViewById(R.id.food_name);
		holder.image_view=(ImageView) vi.findViewById(R.id.image);
		holder.pb=(ProgressBar)vi.findViewById(R.id.progressBar_listitem);
		vi.setTag(holder);
		
	
			
		holder.food_name.setText(title_name_array.get(arg0));
		
		holder.image_view.setImageBitmap(mLoader.getDrawble(arg0,holder.pb));
		
			return vi;
		
	}

	public void onScroll(final AbsListView arg0, final int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		t.interrupt();
		t.suspend();
		
		
		
	}

	public void onScrollStateChanged(final AbsListView arg0, int scrollState) {
		// TODO Auto-generated method stub
		
		
		
	}

	public void setScrollStatus(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void scrollIdle(final AbsListView view) {
		// TODO Auto-generated method stub
		try{
			t.suspend();
		}catch(Exception e){}
		 t=new Thread(){

			public void run(){
				
		mLoader.loadImage(view.getFirstVisiblePosition(), view.getLastVisiblePosition());
			}
		};
		t.start();
	}
	
}
