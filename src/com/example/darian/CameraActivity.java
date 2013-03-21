package com.example.darian;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class CameraActivity extends Activity {
	
	
	private boolean mAutoFocus = false;
	private Button snap;
	private LayoutAnimationController animController;
	
	private AutoFocusCallback cb = new AutoFocusCallback(){

	    public void onAutoFocus(boolean autoFocusSuccess, Camera arg1) {
	        //mAutoFocus = true;
	    }};
	
	private static File getOutputMediaFile() {
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
		
		if(!mediaStorageDir.exists()) {
			if(! mediaStorageDir.mkdirs()) {
				return null;
			}
		}
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
        "IMG_"+ timeStamp + ".jpg");
		
		
		return mediaFile;
	}
	
	
	private PictureCallback mPicture = new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			isTakingPic = false;
			File pic = getOutputMediaFile();
			
			if(pic == null) {return;}
			
			try {
				FileOutputStream fos = new FileOutputStream(pic);
				fos.write(data);
				fos.close();
				
			} catch (FileNotFoundException e) {
				
			} catch (IOException e) {
				
			}
		}
	};
	
	boolean isTakingPic = false;
	
	
	public void snap(View view) {
		
		mCamera.takePicture(null, null, mPicture);
		isTakingPic = true;
		mAutoFocus = false;
	}
	
	private Camera mCamera;
	private CamPrev mPreview;
	
	public static Camera getCameraInstance() {
		Camera c = null;
		
		try {
			c = Camera.open(0);
		}
		catch (Exception e) {
			//camera is unavailable
			System.out.println("No camera?");
		}
		return c;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mCamera.release();
		mCamera = null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_camera);
		
		Animation rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotation);
		animController = new LayoutAnimationController(rotateAnim,0);
		
		
		mCamera = getCameraInstance();
		if(mCamera == null) {return;}
		
		Camera.Parameters params = mCamera.getParameters();
		params.setColorEffect(Camera.Parameters.EFFECT_NONE);
		params.setJpegQuality(100);
		
		List<String> focusModes = params.getSupportedFocusModes();
		if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
		    params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
		}
		else {
			params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		}
		mCamera.setParameters(params);
		
		//initialize and populate the spinner
		Spinner spinner = (Spinner)findViewById(R.id.filters);
		//Create an arrayAdapter 
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.filter_names, android.R.layout.simple_spinner_item);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		CustomSpinnerListener csl = new CustomSpinnerListener();
		csl.myCamera = mCamera;
		spinner.setOnItemSelectedListener(csl);

		
		mCamera.setDisplayOrientation(90);
		mCamera.setParameters(params);
		
		
		
		
		mPreview = new CamPrev(this, mCamera);
		FrameLayout preview = (FrameLayout)findViewById(R.id.camera_preview);
		//preview.setLayoutAnimation(animController);
		RelativeLayout con = (RelativeLayout)findViewById(R.id.control);
		preview.addView(mPreview);
		
		snap = (Button)findViewById(R.id.snap);
		
		OnTouchListener lst = new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent e) {
				if(!mAutoFocus) {
					mAutoFocus = true;
					mCamera.autoFocus(null);
				}
				
				return false;
			}
		};
		snap.setOnTouchListener(lst);
		
		
		con.bringToFront();
		
	}
	
	public void onConfigurationChanged(Configuration newconfig) {
		super.onConfigurationChanged(newconfig);
		System.out.println("Changed!");
		overridePendingTransition(R.anim.rotation,R.anim.rotation);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}

}
