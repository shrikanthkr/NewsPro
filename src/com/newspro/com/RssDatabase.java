package com.newspro.com;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;



public class RssDatabase {
	
	private static final String KEY_ROWID="rowid";
	private static final String KEY_NAME="name";
	private static final String KEY_RSS="rss";
	private static final String KEY_CATEGORY="category";
	private static final String KEY_IMAGE="image";
	
	private static final String DATABASE_NAME="RssDatabase_updated";
	private static final int DATABASE_VERSION=1;
	private static final String DATABASE_TABLE="CalorieTable_updated";
	
	private Context ourContext;
	private SQLiteDatabase ourDatabase;
	private DbHelp ourHelper;
	
	public RssDatabase(Context c)
	{
		ourContext=c;
	}
	
	private static class DbHelp extends SQLiteOpenHelper
	{
		public DbHelp(Context context){
			super(context, DATABASE_NAME,null,DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE  " + DATABASE_TABLE + " ( " +
				    KEY_ROWID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			        KEY_NAME + " TEXT NOT NULL, " +
			        KEY_RSS + " TEXT NOT NULL, " +
					KEY_CATEGORY + " TEXT NOT NULL " +
			       // KEY_IMAGE + " BLOB" +
					" );" );
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}

	public RssDatabase open()
	{
		ourHelper=new DbHelp(ourContext);
		ourDatabase=ourHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		ourHelper.close();
	}
	
	public void Insert(String name,String  rss,String category,byte[] image)
	{
		//Inserting User Input Values to Database
		
		ContentValues cv=new ContentValues();
		cv.put(KEY_NAME, name);
		cv.put(KEY_RSS, rss);
		cv.put(KEY_CATEGORY,category);
		
		ourDatabase.insert(DATABASE_TABLE, null, cv);
		Log.i("Sucess", "Inserting");
	}
	
	public RssDataHold getAll(){

	
		String[] column={KEY_NAME,KEY_RSS,KEY_CATEGORY};
		Cursor c=ourDatabase.query(DATABASE_TABLE, column, null, null, null, null, null);
		int i;
		RssDataHold rd=new RssDataHold();
		for(c.moveToFirst(), i=0;!c.isAfterLast();c.moveToNext(),i++){
			
			rd.putRssDataName((c.getString(c.getColumnIndex(KEY_NAME))),i) ;
			rd.putRssDataRss(c.getString(c.getColumnIndex(KEY_RSS)), i);
			rd.putRssDataCategory(c.getString(c.getColumnIndex(KEY_CATEGORY)), i);
			
			Log.d("Book-Shelf", "RssDatabase Setting :"+i+":::::");
		}
		
		return rd;
		
	}
	public String getUrlByName(String name){
		String url="";
		String query="SELECT "+KEY_RSS+" FROM  "+DATABASE_TABLE+" WHERE "+KEY_NAME +" LIKE '" + name +"%' ; ";
		Cursor c=ourDatabase.rawQuery(query, null);
		 c.moveToFirst();
		 int count=c.getCount();
		 for(int k=0;k<count;k++){
		    	url=c.getString(c.getColumnIndex(KEY_RSS));
				c.moveToNext();
		    }
		 Log.d("FromDatabase", url);
		    c.close();
		return url;
	}

	public void delete() {
		// TODO Auto-generated method stub
		
		ourDatabase.delete(DATABASE_TABLE, null, null);
		SQLiteDatabase.releaseMemory();
		
	}
}