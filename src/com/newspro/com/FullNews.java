package com.newspro.com;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class FullNews extends Activity{
	
	
	WebView wb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fullnews);
		wb=(WebView)findViewById(R.id.webView);
		Bundle b=getIntent().getExtras();
		wb.loadUrl(b.getString("URL"));
		
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	

}
