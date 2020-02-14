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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import net.rbgrn.android.glwallpaperservice.*;

public class LiveWallpaperService extends GLWallpaperService {


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
			renderer = new Live2DRenderer(getApplicationContext());
			setRenderer(renderer);
			setRenderMode(RENDERMODE_CONTINUOUSLY);
		}

		@Override
		public void onTouchEvent(MotionEvent event) {
			//mViewWidth 是整个屏幕的宽度
			//就是在屏幕的一半+100和-100之间的宽度 同理高度
			boolean isCenterOfX = event.getX() < 628
					&& event.getX() > 448;
			boolean isCenterOfY = event.getY() < 1683
					&& event.getY() >1000;
			//如果点击的位置是在这个方形之间

//			Log.i("touch!!!!!", "touchwidth:"+event.getX());
//			Log.i("touch!!!!!", "touchheight:"+event.getY());
			if(event.getAction() == MotionEvent.ACTION_UP){
			renderer.judgePart(event.getX(),event.getY());}
//			if(isCenterOfX && isCenterOfY){
//				//必须要点击之后手指离开才进行监听
//				if(event.getAction() == MotionEvent.ACTION_UP){
//					Log.i("touch!!!!!", "onTouch: Center");
//					renderer.haixiu();
//					//回调方法 B中注册使用 A就是该Activity进行监听回调
//				}
//				//返回true代表这个事件不向下分发，到这里就停止了
//				//不会进行下面的方法了
//				return;
//			}

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
