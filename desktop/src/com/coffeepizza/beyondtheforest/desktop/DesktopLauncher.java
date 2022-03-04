package com.coffeepizza.beyondtheforest.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.coffeepizza.beyondtheforest.BeyondManager;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1200;
		config.height = 624;
		config.vSyncEnabled = true;

		new LwjglApplication(new BeyondManager(), config);
	}
}
