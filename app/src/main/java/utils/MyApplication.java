package utils;

import android.app.Application;

public class MyApplication extends Application {

	private String id;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void onCreate() {
		TypefaceUtil.overrideFont(getApplicationContext(), "SERIF",
				"Rajdhani-SemiBold.ttf"); // font from assets:
											// "assets/fonts/Roboto-Regular.ttf
	}

}
