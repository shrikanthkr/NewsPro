package com.newspro.com;




import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class NewsProActivity extends Activity implements OnClickListener {

	 private static final String TAG = "Book-Shelf";
	NewsProActivity context;
	ArrayList<String> titleList,desList,urlList,imageList; 
	long dummy;
	ProgressBar pb;
	Thread t;
	int tryflag=0;
	RssDatabase rssdb;
	ListView title;
	Handler handler;
	RssDataHold rssdhold;
	StartPageAdapter adapter;
	ListView lv,empty_lv;
	Button feeds_button;
	private int totalPapers = 0;
    private int paperItem = 0;
    private int numberOfPaperRows = 0;
    private int extraPapers = 0;
    Adapter ad;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startpage);
        dummy=System.currentTimeMillis();
       	lv=(ListView)findViewById(R.id.shelfView);
       	feeds_button=(Button)findViewById(R.id.feeds_button);
       	feeds_button.setOnClickListener(this);
       	context=this;
       	
       	
        rssdb=new RssDatabase(this);
        rssdhold=new RssDataHold();
        rssdb.open();
        rssdhold=rssdb.getAll();
        rssdb.close();
        calculateRowsAndColumns();
        
      
        
        adapter=new StartPageAdapter(this,rssdhold,R.layout.shelf_ist_item);
        lv.setAdapter(adapter);
        
     addEmptyRow(3);
        
        
    }
    public void addEmptyRow(int count) {

        LinearLayout ll_addRows = (LinearLayout) findViewById(R.id.add_rows);
        LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        count = count * 2;
        for (int i = 0; i < count; i++) {

            ImageView bg = new ImageView(this);
            bg.setImageResource(R.drawable.shelf_list);
            bg.setLayoutParams(params);
            bg.setScaleType(ScaleType.FIT_XY);
            ll_addRows.addView(bg, i);

            Log.d(TAG, "i:" + i);
            
        }
        
            
        }
	

	public void calculateRowsAndColumns(){
        totalPapers = 0;
        paperItem = 0;
        numberOfPaperRows = 0;
        extraPapers = 0;

        totalPapers = rssdhold.getcount();
        paperItem = 3;//TODO: make it dynamic
        if(totalPapers < paperItem && totalPapers > 0){
            numberOfPaperRows = 1;
        }else{
            numberOfPaperRows = (totalPapers/paperItem);
            extraPapers = (totalPapers % paperItem);
            if( extraPapers != 0)
                 numberOfPaperRows++;
        }

        Log.d(TAG," Total Papers:"+totalPapers);
        Log.d(TAG," Number of books to be displayed:"+paperItem);
        Log.d(TAG," Number of rows :"+numberOfPaperRows);
        Log.d(TAG," Number of extra books(odd count):"+extraPapers);
    }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}	
	
	public class StartPageAdapter extends BaseAdapter{
		
		int rowXmlId=0;
		NewsProActivity context;
		 LayoutInflater mInflater;
		 ViewHolder holder;
         View v ;
         RssDataHold rssd;
         
		StartPageAdapter(NewsProActivity con,RssDataHold rssdh,int rowid){
			this.context=con;
			this.rowXmlId=rowid;
			rssd=rssdh;
			Log.i("Constructor", "Set");
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			
			
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return numberOfPaperRows;
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		public class ViewHolder {
	        ArrayList<ImageView> imgList = new ArrayList<ImageView>();
	        ArrayList<ImageView> staticList = new ArrayList<ImageView>();
	        ArrayList<TextView> textList=new ArrayList<TextView>();
	  
	    }
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			Log.d(TAG, " POSITION:" + position);

            
            v= convertView;
            if (convertView == null) {
                holder = new ViewHolder();
               
                v = mInflater.inflate(R.layout.shelf_ist_item, null);
                ImageView staticImage1=(ImageView) v.findViewById(R.id.ImageView_1);
                ImageView staticImage2=(ImageView) v.findViewById(R.id.ImageView_2);
                ImageView staticImage3=(ImageView) v.findViewById(R.id.ImageView_3);
                TextView tx1=(TextView) v.findViewById(R.id.name_textView1);
                TextView tx2=(TextView) v.findViewById(R.id.name_textView2);
                TextView tx3=(TextView) v.findViewById(R.id.name_textView3);
                holder.staticList.add(staticImage1);
                holder.staticList.add(staticImage2);
                holder.staticList.add(staticImage3);
                holder.textList.add(tx1);
                holder.textList.add(tx2);
                holder.textList.add(tx3);
                v.setTag(holder);
               

            } else {
                holder = (ViewHolder) v.getTag();
            }
            
			
	
			
            if( (position == numberOfPaperRows-1) &&  extraPapers!=0)
                for(int i=0; i<extraPapers; i++){ 
                    int k = (position * paperItem)+i;
                    try{
                    Log.d(TAG, "last row");
                    if(position <= numberOfPaperRows-1 ){
                    holder.staticList.get(i).setVisibility(View.VISIBLE);
                    holder.staticList.get(i).setOnClickListener(new OnBookClickListner(k));
                    Log.d(TAG, "Setting clickListener"+k);
                    holder.textList.get(i).setText(rssdhold.getRssDataName(k));
                    holder.textList.get(i).setOnClickListener(new OnBookClickListner(k));
                    holder.textList.get(i).setVisibility(View.VISIBLE);
                    holder.textList.get(i).setId(k);
                    
                    }
                    }catch(Exception e){}
                    
                }
            
            else
                for(int i=0; i<paperItem; i++){
                    int k = (position * paperItem)+i;
                    try{
                    	if(position <= numberOfPaperRows-1){
                    holder.staticList.get(i).setVisibility(View.VISIBLE);
                    holder.staticList.get(i).setOnClickListener(new OnBookClickListner(k));
                    holder.textList.get(i).setText(rssdhold.getRssDataName(k));
                    holder.textList.get(i).setVisibility(View.VISIBLE);
                    holder.textList.get(i).setOnClickListener(new OnBookClickListner(k));
                    holder.textList.get(i).setId(k);
                    Log.d(TAG, "Setting clickListener"+k);
                    
                    	}
                    }catch(Exception e)
                    {
                    	
                    }
                    
                }
            
		
		
            	
            adapter.notifyDataSetChanged();
            return v;
            
		}
			
	}
	
	public class OnBookClickListner implements OnClickListener{
		int id=0; 
		
       OnBookClickListner(int i){
    	id=i;   
       }
       OnBookClickListner(){
    	   
       }

		public void onClick(View v) {
            Log.d(TAG, " ITEM SELECTED IN LIST VIEW:"+id);
            TextView tv=(TextView)findViewById(id);
            
            
            Toast.makeText(getApplicationContext(), " Getting New for "+tv.getText(), 2).show();
            Bundle b=new Bundle();
            Intent x=new Intent(context,ListNews.class);
            b.putString("TITLE", tv.getText().toString());
            x.putExtras(b);
            startActivity(x);
            
        }

    }

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent x=new Intent(this,ReadListActivity.class);
		startActivity(x);
		
	}

	

 
	
	

}