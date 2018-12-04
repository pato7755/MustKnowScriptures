package com.udolgc.mustknowscriptures;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

	TextView titleTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		titleTextView = (TextView) findViewById(R.id.title);

		Typeface regularText = Typeface.createFromAsset(getAssets(), "HelveticaRegular.otf");
		Typeface mediumText = Typeface.createFromAsset(getAssets(), "HelveticaMedium.otf");

		titleTextView.setTypeface(regularText);

		Thread timer = new Thread() {
			public void run() {
				try {
					sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();

				} finally {
					Intent openMainActivity = new Intent(SplashScreen.this, BooksOfTheBible.class);
					startActivity(openMainActivity);
				}
			}
		};
		timer.start();
	}



@Override
public void onStop() {
	// TODO Auto-generated method stub
	super.onStop();

	finish();
}

/*@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	super.onBackPressed();
	
	finishFromChild(SplashScreen.this);
}*/
	

}
