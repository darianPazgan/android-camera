package com.example.darian;

import android.hardware.Camera;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class CustomSpinnerListener implements OnItemSelectedListener {

	public Camera myCamera;
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		//parent.getItemAtPosition(pos)
		System.out.println(parent.getItemAtPosition(pos).toString().toUpperCase());
		String str = parent.getItemAtPosition(pos).toString();
		if(str.equals("None")) {
			Camera.Parameters params = myCamera.getParameters();
			params.setColorEffect(Camera.Parameters.EFFECT_NONE);
			myCamera.setParameters(params);
		}
		if(str.equals("Aqua")) {
			Camera.Parameters params = myCamera.getParameters();
			params.setColorEffect(Camera.Parameters.EFFECT_AQUA);
			myCamera.setParameters(params);
		}
		if(str.equals("Blackboard")) {
			Camera.Parameters params = myCamera.getParameters();
			params.setColorEffect(Camera.Parameters.EFFECT_BLACKBOARD);
			myCamera.setParameters(params);
		}if(str.equals("Mono")) {
			Camera.Parameters params = myCamera.getParameters();
			params.setColorEffect(Camera.Parameters.EFFECT_MONO);
			myCamera.setParameters(params);
		}if(str.equals("Negative")) {
			Camera.Parameters params = myCamera.getParameters();
			params.setColorEffect(Camera.Parameters.EFFECT_NEGATIVE);
			myCamera.setParameters(params);
		}if(str.equals("Solarize")) {
			Camera.Parameters params = myCamera.getParameters();
			params.setColorEffect(Camera.Parameters.EFFECT_SOLARIZE);
			myCamera.setParameters(params);
		}if(str.equals("Sepia")) {
			Camera.Parameters params = myCamera.getParameters();
			params.setColorEffect(Camera.Parameters.EFFECT_SEPIA);
			myCamera.setParameters(params);
		}
		if(str.equals("Posterize")) {
			Camera.Parameters params = myCamera.getParameters();
			params.setColorEffect(Camera.Parameters.EFFECT_POSTERIZE);
			myCamera.setParameters(params);
		}
		if(str.equals("Whiteboard")) {
			Camera.Parameters params = myCamera.getParameters();
			params.setColorEffect(Camera.Parameters.EFFECT_WHITEBOARD);
			myCamera.setParameters(params);
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// Do nothing
		
	}

}
