package com.example.darian;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.AutoFocusMoveCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class CameraActivity extends Activity implements OnClickListener {
	

    View.OnTouchListener gestureListener;
	
	private Context context;
	private boolean mAutoFocus = false;
	private Button snap;
	private SeekBar sb;
	private OrientationEventListener oel;
	private int lastAngle = 0;
	private ImageView cross, glow;
	private TextView tv;
	private ImageView qual;
	

	
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
	
	private AutoFocusCallback mFocus = new AutoFocusCallback() {

		@Override
		public void onAutoFocus(boolean arg0, Camera arg1) {
			// TODO Auto-generated method stub
			ImageView cross = (ImageView)findViewById(R.id.crosstop);
			cross.setVisibility(View.GONE);
			
		}
		
	};
	
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
			ImageView cross = (ImageView)findViewById(R.id.crosstop);
			cross.setVisibility(View.GONE);
			mCamera.startPreview();
		}
	};
	
	boolean isTakingPic = false;
	
	
	public void snap(View view) {
		
		mCamera.takePicture(null, null, mPicture);
		mCamera.stopPreview();
		//Animation a = AnimationUtils.loadAnimation(this, R.anim.snapflash);
		//FrameLayout prev = (FrameLayout)findViewById(R.id.camera_preview);
		//prev.startAnimation(a);
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
		context = this;
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_camera);
		
		//load animations
		final Animation rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotation);
		final Animation rotateAnim2 = AnimationUtils.loadAnimation(this, R.anim.rotation2);
		final Animation rotateAnim3 = AnimationUtils.loadAnimation(this, R.anim.rotation3);
		final Animation rotateAnim4 = AnimationUtils.loadAnimation(this, R.anim.rotation4);
		final Animation qualFadeIn = AnimationUtils.loadAnimation(this, R.anim.qualfadein);
		final Animation qualFade = AnimationUtils.loadAnimation(this, R.anim.qualfadeout);
		
		oel = new OrientationEventListener(this,SensorManager.SENSOR_DELAY_NORMAL) {
			@Override
			public void onOrientationChanged(int orientation) {
				//rotate button
				if(lastAngle != 90 && orientation >= 45 && orientation < 180){
					snap.startAnimation(rotateAnim);
					//tv.startAnimation(rotateAnim);
					//qual.startAnimation(rotateAnim);
					lastAngle = 90;
				}
				else if(lastAngle != 270 && orientation >= 180 && orientation < 270) {
					snap.startAnimation(rotateAnim2);
					//tv.startAnimation(rotateAnim2);
					//qual.startAnimation(rotateAnim2);
					lastAngle = 270;
				}
				else if(lastAngle != 0 && (orientation < 45 ||  orientation >= 300)) {
					if(lastAngle == 90) {
						snap.startAnimation(rotateAnim3);
						//tv.startAnimation(rotateAnim3);
						//qual.startAnimation(rotateAnim3);
					} else {
						snap.startAnimation(rotateAnim4);
						//tv.startAnimation(rotateAnim4);
						//qual.startAnimation(rotateAnim4);
					}
					lastAngle = 0;
				}
			}
		};
		
		mCamera = getCameraInstance();
		if(mCamera == null) {return;}
		cross = (ImageView)findViewById(R.id.crosstop);
		cross.setVisibility(View.GONE);
		
		qual = (ImageView)findViewById(R.id.quality);
		qual.setVisibility(View.GONE);
		
		Camera.Parameters params = mCamera.getParameters();
		params.setColorEffect(Camera.Parameters.EFFECT_NONE);
		params.setJpegQuality(50); //add a setting to modify it later! (slider?)
		params.setPictureSize(params.getPreviewSize().width,params.getPreviewSize().height);
		
		List<String> focusModes = params.getSupportedFocusModes();
		if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
		    params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
		}
		else {
			//params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
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
		oel.enable();
		
		snap = (Button)findViewById(R.id.snap);
		glow = (ImageView)findViewById(R.id.glow);
		/**ImageView shade = (ImageView)findViewById(R.id.shade_bar);
		shade.setAlpha(0.5f);**/
		
		sb = (SeekBar)findViewById(R.id.quality_bar);
		
		tv = new TextView(this);
		tv = (TextView)findViewById(R.id.quality_val);
		tv.setText("50");
		tv.setWidth(150);
		tv.setHeight(65);
		tv.setTextColor(0xBBFFFFFF);
		tv.setTextSize(40.0f);
		tv.setVisibility(View.GONE);
		//tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

		
		
		OnSeekBarChangeListener seek = new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				
				//update the image quality
				if(arg1 != 0) {
				Camera.Parameters p = mCamera.getParameters();
				p.setJpegQuality(arg1);
				mCamera.setParameters(p);
				
				tv.setText(Integer.toString(arg1));
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				tv.setVisibility(View.INVISIBLE);
				qual.setVisibility(View.INVISIBLE);
				tv.startAnimation(qualFadeIn);
				qual.startAnimation(qualFadeIn);
				tv.setVisibility(View.VISIBLE);
				qual.setVisibility(View.VISIBLE);
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// fade out the overlay
				qual.startAnimation(qualFade);
				tv.startAnimation(qualFade);
				qual.setVisibility(View.GONE);
				tv.setVisibility(View.GONE);
			}
			
			
		};
		
		sb.setOnSeekBarChangeListener(seek);
		
		OnTouchListener lst = new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent e) {
				if(!mAutoFocus) {
					mAutoFocus = true;
					//cross.setVisibility(View.INVISIBLE);
					cross.setVisibility(View.VISIBLE);
					cross.bringToFront();
					mCamera.autoFocus(null);
					//cross.setVisibility(View.INVISIBLE);
					//glow.bringToFront();
					//cross.setVisibility(View.GONE);
					//glow.startAnimation(swipe);
					//System.out.println("onTouch()!");
					
				}
				
				
				//snap.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotation))
				//cross.setVisibility(View.INVISIBLE);
				return false;
			}
		};
		snap.setOnTouchListener(lst);
		
		
		con.bringToFront();
		tv.bringToFront();
		glow.setVisibility(View.GONE);
		
	}
	
	public void onConfigurationChanged(Configuration newconfig) {
		super.onConfigurationChanged(newconfig);
		System.out.println("Changed!");
		
	}
	
	public void swipe(View view) {
		glow.bringToFront();
		Animation a = AnimationUtils.loadAnimation(context, R.anim.swipe);
		glow.startAnimation(a);
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}
	/**@Override
	public void onPause() {
		super.onPause();
		if(mCamera != null) {
			mCamera.release();
			mCamera = null;
			
		}
	}**/
	@Override
	public void onResume() {
		super.onResume();
		if(mCamera == null) {mCamera = getCameraInstance();}
	}


	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
