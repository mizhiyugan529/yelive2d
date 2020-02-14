/**
 *
 *  You can modify and use this source freely
 *  only for the development of application related Live2D.
 *
 *  (c) Live2D Inc. All rights reserved.
 */
 package jp.live2d.sample;

import java.io.IOException;
import java.io.InputStream;

import jp.live2d.android.Live2DModelAndroid;
import jp.live2d.android.UtOpenGL;
import jp.live2d.framework.L2DPhysics;
import jp.live2d.framework.L2DPose;
import jp.live2d.framework.L2DStandardID;
import jp.live2d.framework.L2DTargetPoint;
import jp.live2d.framework.L2DViewMatrix;
import jp.live2d.motion.Live2DMotion;
import jp.live2d.motion.MotionQueueManager;
import jp.live2d.utils.android.FileManager;
import jp.live2d.utils.android.SimpleImage;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import net.rbgrn.android.glwallpaperservice.*;

import static android.opengl.GLES10.glLoadIdentity;

public class Live2DRenderer implements GLWallpaperService.Renderer
{
	Context con;

	Live2DModelAndroid	live2DModel ;
	Live2DMotion motion;
	MotionQueueManager motionMgr;
	L2DTargetPoint dragMgr;
	L2DPhysics physics;
	L2DPose pose;
	private SimpleImage bg;

	final String MODEL_PATH = "yexiu/model.moc" ;
	final String TEXTURE_PATHS[] =
		{
			"yexiu/model/texture_00_0.png" ,
			"yexiu/model/texture_01_0.png" ,
			"yexiu/model/texture_02_0.png"
		} ;
	final String MOTION_PATH="yexiu/motion/25.mtn";
	final String PHYSICS_PATH="yexiu/QianJi.physics.json";
	final String POSE_PATH="yexiu/QianJi.pose.json";
	//随机动作
	final String RANDOM_MOTION_PATH[]={
			"yexiu/motion/23.mtn",
			"yexiu/motion/25.mtn",
			"yexiu/motion/28.mtn",
			"yexiu/motion/29.mtn",
			"yexiu/motion/QianJi_2.mtn",
			"yexiu/motion/QianJi_3.mtn",
			"yexiu/motion/QianJi_4.mtn",
			"yexiu/sharedmotions/BuGaoXing_D_1.mtn",
			"yexiu/sharedmotions/ChaoFeng_D_1.mtn",
			"yexiu/sharedmotions/DaJiao.mtn",
			"yexiu/sharedmotions/DaXiao_D_1.mtn",
			"yexiu/sharedmotions/DaXiao_J_1.mtn",
			"yexiu/sharedmotions/DouBi_Surprise.mtn",
			"yexiu/sharedmotions/Idle_nohands.mtn",
			"yexiu/sharedmotions/Idle_nohands.mtn",
			"yexiu/sharedmotions/Idle_nohands.mtn",
			"yexiu/sharedmotions/idle0_GuiYiPiFu_1.mtn",
			"yexiu/sharedmotions/Idle11.mtn",
			"yexiu/sharedmotions/Idle19.mtn",
			"yexiu/sharedmotions/Idle22.mtn",
			"yexiu/sharedmotions/JingYia_D_1.mtn",
			"yexiu/sharedmotions/JinLing_AoJiao2.mtn",
			"yexiu/sharedmotions/JinLing_DaXiao.mtn",
			"yexiu/sharedmotions/JinLing_JingYa.mtn",
			"yexiu/sharedmotions/JinLing_KenDing.mtn",
			"yexiu/sharedmotions/JinLing_QingXiao.mtn",
			"yexiu/sharedmotions/JinLing_RenBuZhuXiao.mtn",
			"yexiu/sharedmotions/JinLing_ShengQi2.mtn",
			"yexiu/sharedmotions/Practice.mtn",
			"yexiu/sharedmotions/TuLong_shoushang.mtn",
			"yexiu/sharedmotions/WangHouTiao_L.mtn",
			"yexiu/sharedmotions/WangHouTiao_R.mtn",
			"yexiu/sharedmotions/YiTian_YaoTou.mtn"
	};
	//背景
	final static String BACK_IMAGE_NAME="image/back4.jpg" ;

	float glWidth=0;
	float glHeight=0;


	public Live2DRenderer(Context context)
	{
		con = context;
		dragMgr=new L2DTargetPoint();
		motionMgr=new MotionQueueManager();
	}
	public void judgePart(float x,float y){
		if( glHeight!=0f){
//			Log.i("touch!!!!!", "onTouch: "+x+"/"+y);
//			Log.i("touch!!!!!", "width: "+glWidth+"/"+glHeight);
			float modelWidth = live2DModel.getCanvasWidth();
			float modelHeight = live2DModel.getCanvasHeight();
//			Log.i("touch!!!!!", "modelwidth: "+modelWidth+"/"+modelHeight);
			float floa=glWidth*9/8;
//			Log.i("touch!!!!!", "judgewidth: "+glWidth/2+"/"+floa);
			if (x<=glWidth/2+200&&x>=glWidth/2-50&&y<=glWidth*9/8+500&&y>=glWidth*9/8+200){
				wink();
			}else if(x<=glWidth/2+100&&x>=glWidth/2-100&&y<=glWidth*9/8+700&&y>=glWidth*9/8-200){
//				Log.i("touch!!!!!", "center/");
			haixiu();}

		}
	}
	private static int entNo;
	public void haixiu() {
		if (motionMgr.isFinished(entNo)) {

			try {
				AssetManager mngr = con.getAssets();
				InputStream in = mngr.open("yexiu/sharedmotions/FouDing_D_1.mtn");
//				InputStream in = mngr.open("yexiu/motion/heart.mtn");
				Log.i("touch!!!!!", "height");
				motion = Live2DMotion.loadMotion(in);
				in.close();
				motionMgr.stopAllMotions();
				entNo = motionMgr.startMotion(motion, false);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private static int entNo2;
	public void wink() {
		if (motionMgr.isFinished(entNo2)) {

			try {
				AssetManager mngr = con.getAssets();
				InputStream in = mngr.open("yexiu/sharedmotions/wink.mtn");
				motion = Live2DMotion.loadMotion(in);
				in.close();
				motionMgr.stopAllMotions();
				entNo2 = motionMgr.startMotion(motion, false);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void onDrawFrame(GL10 gl) {
		float dragX=dragMgr.getX();
		float dragY=dragMgr.getY();
		//try
		gl.glEnable( GL10.GL_TEXTURE_2D ) ;
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY) ;
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY) ;
		gl.glPushMatrix() ;
		{

			if(bg!=null){
//				Log.d("111111!!!!!", "onSurfaceCreated: true!!");

				gl.glPushMatrix() ;
				{
					float SCALE_X = 0.25f ;
					float SCALE_Y = 0.1f ;
					gl.glScalef(100, 70, 1);
//					gl.glTranslatef( -SCALE_X  * 0 , SCALE_Y * 0 , 0 ) ;
					gl.glTranslatef(  dragX , SCALE_Y * 0 , 0 ) ;


					bg.draw(gl);
				}
				gl.glPopMatrix() ;
			}


		}
		gl.glPopMatrix() ;




        // Your rendering code goes here

		gl.glMatrixMode(GL10.GL_MODELVIEW ) ;
		gl.glLoadIdentity() ;
//		gl.glClear( GL10.GL_COLOR_BUFFER_BIT ) ;
		gl.glEnable( GL10.GL_BLEND ) ;
		gl.glBlendFunc( GL10.GL_ONE , GL10.GL_ONE_MINUS_SRC_ALPHA ) ;
		gl.glDisable( GL10.GL_DEPTH_TEST ) ;
		gl.glDisable( GL10.GL_CULL_FACE ) ;


		live2DModel.loadParam();

		if(motionMgr.isFinished())
		{

			try {
				AssetManager mngr = con.getAssets();
				int max=RANDOM_MOTION_PATH.length;
				Integer random=(int)(Math.random() * max);
				String random_motion=RANDOM_MOTION_PATH[random];
				Log.d("random_motion", "onDrawFrame: "+random_motion);
				InputStream in = mngr.open( random_motion ) ;
				motion = Live2DMotion.loadMotion( in ) ;
				in.close() ;
				motionMgr.startMotion(motion, false);
				if(random_motion=="yexiu/sharedmotions/Practice.mtn"){
					try
				{
					in=mngr.open("yexiu/QianJi.pose2.json");
					pose=L2DPose.load(in);
					in.close();
					pose.updateParam(live2DModel);
					live2DModel.update();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}}else{
					try
					{
						in=mngr.open("yexiu/QianJi.pose.json");
						pose=L2DPose.load(in);
						in.close();
						pose.updateParam(live2DModel);
						live2DModel.update();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		else
		{
			motionMgr.updateParam(live2DModel);
		}

		live2DModel.saveParam();

		dragMgr.update();


		live2DModel.addToParamFloat(L2DStandardID.PARAM_ANGLE_X, dragX*30);
		live2DModel.addToParamFloat(L2DStandardID.PARAM_ANGLE_Y, dragY*30);
		live2DModel.addToParamFloat(L2DStandardID.PARAM_BODY_ANGLE_X, dragX*10);

		physics.updateParam(live2DModel);


		live2DModel.setGL( gl ) ;

		live2DModel.update() ;
		live2DModel.draw() ;

    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {

		gl.glViewport( 0 , 0 , width , height ) ;

		
		gl.glMatrixMode( GL10.GL_PROJECTION ) ;
		gl.glLoadIdentity() ;

		float modelWidth = live2DModel.getCanvasWidth();
		float modelHeight = live2DModel.getCanvasHeight();
		gl.glOrthof(
				-1 ,
				modelWidth ,
				modelWidth*9/8,
				-height ,
				1f ,	-1f
		) ;
		Log.d("surface", "width:"+width+";height:"+height+";modelwidth:"+modelWidth+
				":modelheight:"+modelHeight);
//		gl.glOrthof(
//				0 ,
//				modelWidth ,
//				modelWidth * height / width,
//				0 ,
//				0.5f ,	-0.5f
//				) ;

		glWidth=width;
		glHeight=height;
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
		setupBackground(gl);
//    	gl.glClearColor(1.0f, 0.0f, 1.0f, 0.5f);
//    	gl.glReadPixels();
//		gl.glDrawpixels
//		Canvas canvas = holder.lockCanvas();
//		canvas.drawColor(Color.RED);
//		holder.unlockCanvasAndPost(canvas);
		//背景的尝试

//		Resources res =con.getResources();
//		this.mTexture = GraphicUtil.loadTexture(gl, res, R.drawable.image);


		AssetManager mngr = con.getAssets();


		try
		{
			InputStream in = mngr.open( MODEL_PATH ) ;
			live2DModel = Live2DModelAndroid.loadModel( in ) ;
			in.close() ;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
//
		try
		{
			//texture
			for (int i = 0 ; i < TEXTURE_PATHS.length ; i++ )
			{
				InputStream in = mngr.open( TEXTURE_PATHS[i] ) ;
				int texNo = UtOpenGL.loadTexture(gl , in , true ) ;
				live2DModel.setTexture( i , texNo ) ;
				in.close();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
//
		try
		{
			InputStream in = mngr.open( MOTION_PATH ) ;
			motion = Live2DMotion.loadMotion( in ) ;
			in.close() ;

			in=mngr.open(PHYSICS_PATH);
			physics=L2DPhysics.load(in);
			in.close();

			in=mngr.open(POSE_PATH);
			pose=L2DPose.load(in);
			in.close();
			pose.updateParam(live2DModel);
			live2DModel.update();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}


    }

    /**
     * Called when the engine is destroyed. Do any necessary clean up because
     * at this point your renderer instance is now done for.
     */
    public void release() {
    }

    public void resetDrag()
    {
    	dragMgr.set(0, 0);
    }


    public void drag(float x,float y)
    {
    	float screenX=x/glWidth*2-1;
    	float screenY=-y/glHeight*2+1;

//    	Log.i("", "x:"+screenX+" y:"+screenY);

    	dragMgr.set(screenX,screenY);
    }

	private void setupBackground(GL10 context) {
		try {
			FileManager.init(con);
			InputStream in = FileManager.open(BACK_IMAGE_NAME);
			bg=new SimpleImage(context,in);
			bg.setDrawRect(
					0f,
					25f,
					40.0f,
					-40.0f);


			bg.setUVRect(0.0f,1.0f,0.0f,1.0f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
