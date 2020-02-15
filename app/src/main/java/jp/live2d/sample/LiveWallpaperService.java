/**
 *
 *  You can modify and use this source freely
 *  only for the development of application related Live2D.
 *
 *  (c) Live2D Inc. All rights reserved.
 */
package jp.live2d.sample;


import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import net.rbgrn.android.glwallpaperservice.*;

public class LiveWallpaperService extends GLWallpaperService {
	static String picString;

	public LiveWallpaperService() {
		super();

	}


	public Engine onCreateEngine() {
		MyEngine engine = new MyEngine();
		return engine;
	}

	class MyEngine extends GLEngine {
		Live2DRenderer renderer;

		public MyEngine() {
			super();
			// handle prefs, other initialization
			SharedPreferences sp=getApplicationContext().getSharedPreferences("yelive2d", Context.MODE_PRIVATE);
			picString=sp.getString("img", "");
			Log.d("thepicstring", "MyEngine: "+picString);
			if (picString !=null&&picString !=""){
				renderer = new Live2DRenderer(getApplicationContext(), picString);
			}else{
				Log.d("thepicstring", "here"+picString);
			renderer = new Live2DRenderer(getApplicationContext());}
			setRenderer(renderer);
			setRenderMode(RENDERMODE_CONTINUOUSLY);
		}

		@Override
		public void onTouchEvent(MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_UP){
			renderer.judgePart(event.getX(),event.getY());}

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_UP:
				renderer.resetDrag();
				break;
			case MotionEvent.ACTION_MOVE:
				renderer.drag(event.getX(), event.getY());
				break;
			case MotionEvent.ACTION_CANCEL:
				break;
			}
		}

		public void onDestroy() {
			super.onDestroy();
			if (renderer != null) {
				renderer.release();
			}
			renderer = null;
		}
	}
}
