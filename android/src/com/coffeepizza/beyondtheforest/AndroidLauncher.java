package com.coffeepizza.beyondtheforest;

import android.os.Bundle;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.coffeepizza.beyondtheforest.interfaces.Service;

public class AndroidLauncher extends AndroidApplication implements Service {

	public View gameView;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		/*
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new BeyondManager(), config);
		*/

		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		// Set orientation
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

		// Hide Navigation
		config.useImmersiveMode = true;

//		initialize(new BeyondManager(), config);
		gameView = initializeForView(new BeyondManager(this), config);
		setContentView(gameView);
	}

	@Override
	public void keepScreenOn(final boolean isOn) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				gameView.setKeepScreenOn(isOn);
			}
		});
	}
}
