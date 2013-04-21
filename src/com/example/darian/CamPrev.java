package com.example.darian;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;

public class CamPrev extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
	private Camera mCamera;
	private Context context;
	private ImageView cross;
	
	public CamPrev(Context context, Camera camera) {
		super(context);
		mCamera = camera;
		this.context = context;
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		
		if(mCamera==null) {mCamera = getCameraInstance();}
		
		
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
			//Log.d(TAG, "Error setting camera preview: " + e.getMessage());
			//return;
		}
		cross = (ImageView)findViewById(R.id.crosstop);
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		//empty.
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int x, int h) {
		//maybe let it rotate
		
		if(mHolder.getSurface() == null) {
			return;
		}
		
		//stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			//ignore
		}
		
		//start preview with new settings
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
		} catch (Exception e) {
			//whatever
		}
	}
	
	public void onInflate() {
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		//cross.setVisibility(View.VISIBLE);
		mCamera.autoFocus(null);
		//cross.setVisibility(View.INVISIBLE);
		//ImageView v = (ImageView)findViewById(R.id.crosstop);
		//Animation a = AnimationUtils.loadAnimation(context, R.anim.focus);
		//v.startAnimation(a);
		//mAutoFocus = false;
		return true;
	}
	
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
	


}
