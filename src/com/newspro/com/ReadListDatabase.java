package com.newspro.com;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ReadListDatabase {
	
	
	
	
	private static final String KEY_ROWID="rowid";
	private static final String KEY_RSS="rss";
	private static final String KEY_TITLE="title";
	private static final String DATABASE_NAME="ReadListDatabase_updated";
	private static final int DATABASE_VERSION=1;
	private static final String DATABASE_TABLE="ReadListDB_updated";
	
	private Context ourContext;
	private SQLiteDatabase ourDatabase;
	private DbHelp ourHelper;
	
	public ReadListDatabase(Context c)
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
					KEY_TITLE + " TEXT NOT NULL, " +
			        KEY_RSS + " TEXT NOT NULL " +
					" );" );
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}

	public ReadListDatabase open()
	{
		ourHelper=new DbHelp(ourContext);
		ourDatabase=ourHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		ourHelper.close();
	}
	
	
	public void Insert(String name,String  url)
	{
		//Inserting User Input Values to Database
		
		ContentValues cv=new ContentValues();
		cv.put(KEY_RSS, url);
		cv.put(KEY_TITLE, name);
		
		long inserted=ourDatabase.insert(DATABASE_TABLE, null, cv);
		Log.d("READLIST", "READ LIST INSERTED:::::"+inserted);
	}
	
	public ArrayList<String> getLinks(){

		
		String[] column={KEY_RSS};
		ArrayList<String> links;
		
		links=new ArrayList<String>();
		Cursor c=ourDatabase.query(DATABASE_TABLE, column, null, null, null, null, null);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			
			links.add(c.getString(c.getColumnIndex(KEY_RSS)) );
			
			
		}
		c.close();
		return links;
		
	}
	
public ArrayList<String> getTitle(){

		
		String[] column={KEY_TITLE};
		ArrayList<String> title;
		
		title=new ArrayList<String>();
		Cursor c=ourDatabase.query(DATABASE_TABLE, column, null, null, null, null, null);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			
			title.add(c.getString(c.getColumnIndex(KEY_TITLE)) );
			
			
		}
		c.close();
		return title;
		
	}

	public void deleteThis(String url){
		
		ourDatabase.delete(DATABASE_TABLE, KEY_RSS + " = '" + url+"'", null);
	}

}
