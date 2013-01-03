package com.newspro.com;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ReadListActivity extends Activity implements OnItemClickListener, OnClickListener, OnItemLongClickListener{
	ReadLisstAdapter adapter;
	ListView title;
	ReadListDatabase db;
	ArrayList<String> title_array,url_array;
	Activity activity;
	Button list_button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.readlist);
		list_button=(Button)findViewById(R.id.list_button);
		list_button.setOnClickListener(this);
		Log.i("READLIST ACTIVITY", "CONTET VIEW SET");
		title=(ListView)findViewById(R.id.listView_readList);
		db=new ReadListDatabase(this);
		title_array=new ArrayList<String>();
		url_array=new ArrayList<String>();
		activity=this;
		db.open();
		title_array=db.getTitle();
		url_array=db.getLinks();
		db.close();
		adapter=new ReadLisstAdapter(activity,title_array,url_array,title);
		title.setAdapter(adapter);
		title.setOnItemClickListener(this);
		title.setOnItemLongClickListener(this);
		
	}
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
		Toast.makeText(getApplicationContext(), title_array.get(arg2), 2).show();
		Bundle b=new Bundle();
		Intent i=new Intent(this,FullNews.class);
		b.putString("TITLE", title_array.get(arg2));
		b.putString("DESCRIPTION", "");
		b.putString("URL", url_array.get(arg2));
		i.putExtras(b);
		startActivity(i);
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		onBackPressed();
		
	}
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		db.open();
		db.deleteThis(url_array.get(arg2));
		
		adapter.notifyDataSetChanged();
		db.close();
		Toast.makeText(getApplicationContext(),"DELETED ::"+ url_array.get(arg2),2).show();
		url_array.remove(arg2);
		//Intent x=new Intent(this,ReadListActivity.class);
		//startActivity(x);
		//finish();
		onBackPressed();
		return false;
	}
	
	
	
	

}
