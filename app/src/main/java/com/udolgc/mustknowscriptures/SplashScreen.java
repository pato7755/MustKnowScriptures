package com.udolgc.mustknowscriptures;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

	TextView titleTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splashscreen);

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
