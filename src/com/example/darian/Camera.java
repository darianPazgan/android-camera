package com.example.darian;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class Camera extends Activity {
	
	private static final int SPLASH_TIME = 250;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.splash);
		
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent intent = new Intent(Camera.this, CameraActivity.class);
				Camera.this.startActivity(intent);
				Camera.this.finish();
				overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			}
		}, SPLASH_TIME);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

}
