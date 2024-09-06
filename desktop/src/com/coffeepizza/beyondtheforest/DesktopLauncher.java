package com.coffeepizza.beyondtheforest;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.coffeepizza.beyondtheforest.BeyondManager;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);

		//config.vsy .width = 1200;
		//config.height = 624;
		//config.vSyncEnabled = true;
		config.setWindowedMode(1920, 1080);
		config.useVsync(true);
		config.setTitle("Beyond the Forest");
		new Lwjgl3Application(new BeyondManager(), config);
	}
}
