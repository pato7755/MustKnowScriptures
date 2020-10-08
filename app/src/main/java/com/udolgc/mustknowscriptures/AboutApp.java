package com.udolgc.mustknowscriptures;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class AboutApp extends AppCompatActivity implements View.OnClickListener {

	TextView websiteTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_app);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		TextView aboutAppTV = (TextView) findViewById(R.id.about_app_textview);
		websiteTextView = (TextView) findViewById(R.id.website_textview);
		TextView devName = (TextView) findViewById(R.id.developer_name);
		TextView devEmail = (TextView) findViewById(R.id.developer_email);
		TextView devNumber = (TextView) findViewById(R.id.developer_number);

		websiteTextView.setOnClickListener(this);



	}


	private void openWebPage(String url) {
		try {
			System.out.println("url: " + url);
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			System.out.println("error opening web page");
			System.out.println(e.getMessage());
		}
	}

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


	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.website_textview){

			openWebPage(websiteTextView.getText().toString());

		}
	}
}
