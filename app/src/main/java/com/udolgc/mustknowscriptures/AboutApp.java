package com.udolgc.mustknowscriptures;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;


public class AboutApp extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_app);

//		Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
//		setSupportActionBar(mActionBarToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		mbAccDetails = getIntent().getExtras().getString("accdetails").trim();

		TextView aboutAppTV = (TextView) findViewById(R.id.about_app_textview);
		TextView devName = (TextView) findViewById(R.id.developer_name);
		TextView devEmail = (TextView) findViewById(R.id.developer_email);
		TextView devNumber = (TextView) findViewById(R.id.developer_number);


	}


//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		menu.findItem(R.id.refresh).setVisible(false);
//		return true;
//	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			//	         NavUtils.navigateUpFromSameTask(this);S
			AboutApp.this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed() {
		//		moveTaskToBack(true); 
		AboutApp.this.finish();
	}



}
