package com.example.darian;

import java.io.IOException;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CamPrev extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
	private Camera mCamera;
	
	public CamPrev(Context context, Camera camera) {
		super(context);
		mCamera = camera;
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
			//Log.d(TAG, "Error setting camera preview: " + e.getMessage());
			//return;
		}
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		//empty.
		mCamera.release();
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
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		mCamera.autoFocus(null);
		//mAutoFocus = false;
		return true;
	}
	


}
