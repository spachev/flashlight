package com.fastrunningblog.flashlight;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.content.pm.PackageManager;

public class FlashLight extends Activity
{
	protected boolean lightOn = false;
	public static Camera cam = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final Button button = (Button) findViewById(R.id.light_button);

		if (!haveFlash())
		{
			button.setVisibility(View.GONE);
			showMsg("Your system does not appear to have a flash light");
			return;
		}

		//showMsg("Flash light detected");

		button.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				toggleLight();
				button.setText(getLightStateText(!isLightOn()));
			}
		});
	}

	public void showMsg(String msg)
	{
		final TextView msgTv = (TextView) findViewById(R.id.msg_tv);
		msgTv.setText(msg);
	}

	public String getLightStateText(boolean state)
	{
		return state ? "On":"Off";
	}

	public boolean isLightOn()
	{
		return lightOn;
	}

	public void flashOn()
	{
		cam = Camera.open();
		Parameters p = cam.getParameters();
		p.setFlashMode(Parameters.FLASH_MODE_TORCH);
		cam.setParameters(p);
		cam.startPreview();
	}

	public void onDestroy()
	{
		flashOff();
		super.onDestroy();
	}

	public void flashOff()
	{
		if (cam != null)
		{
			cam.stopPreview();
			cam.release();
			cam = null;
		}
	}

	public void setLightOn(boolean on)
	{
		try
		{
			if (on)
				flashOn();
			else
				flashOff();
		}
		catch (Exception e)
		{
			showMsg("Error working with flash light");
		}
		lightOn = on;
	}

	public void toggleLight()
	{
		setLightOn(!isLightOn());
	}

	public boolean haveFlash()
	{
		return getPackageManager().hasSystemFeature(
			PackageManager.FEATURE_CAMERA_FLASH);
	}
}
