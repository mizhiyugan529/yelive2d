package jp.live2d.sample;


import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import jp.live2d.Live2D;

public class SampleActivity extends Activity {
    private final int REQUEST_PICTURE_CHOOSE = 1;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private static final int MY_PERMISSIONS_REQUEST_READ = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp=this.getSharedPreferences("yelive2d", Context.MODE_PRIVATE);
        editor = sp.edit();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        setContentView(R.layout.activity_sample);
        Button wallpaper = (Button) findViewById(R.id.wallpaper);
        Button picture = (Button) findViewById(R.id.picture);
        wallpaper.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             editor.putString("img", "");
                                             editor.commit();
                                             Live2D.init();
                                             Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);

                                             intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(getApplicationContext(), LiveWallpaperService.class));
                                             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                                             getApplicationContext().startActivity(intent);
                                             //  启动设置的动态壁纸的Service
//        startService(intent);
                                             finish();
                                         }
                                     }


        );
        picture.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           //检查权限
                                               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                   if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                                       if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                                           // 向用户说明为什么需要该权限的提示
                                                           Toast.makeText(getApplicationContext(), "此app需要读取您的图片才能给叶叶设置壁纸", Toast.LENGTH_LONG).show();
                                                       }

                                                       // 请求权限，系统会显示一个获取权限的提示对话框，当前应用不能配置和修改这个对话框
                                                       requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE },MY_PERMISSIONS_REQUEST_READ
                                                               );
                                                       return;
                                                   }
                                               }
                                               Intent intent = new Intent();
                                           intent.setType("image/*");
                                           intent.setAction(Intent.ACTION_PICK);
                                           startActivityForResult(intent, REQUEST_PICTURE_CHOOSE);
                                       }
                                   }


        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 申请成功
                    Toast.makeText(this, "可以啦，你想把叶叶放在哪里呀_(:з)∠)_", Toast.LENGTH_SHORT).show();
                } else {
                    // 申请失败
                    Toast.makeText(this, "你拒绝了读取权限！", Toast.LENGTH_SHORT).show();
                    // 当用户选择拒绝并勾选记住选择存在问题
                    // 1、不重新申请的话，会永远获取不到权限
                    // 2、此处代码，重新申请会陷入死循环，一直提示申请失败，需要调整处理方式
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("chuandicanshu", "onactivityresult ");
        if (resultCode != RESULT_OK) {
            return;
        }
        String fileSrc = null;
        if (requestCode == this.REQUEST_PICTURE_CHOOSE) {
            if ("file".equals(data.getData().getScheme())) {
// 有些低版本机型返回的Uri模式为file
                fileSrc = data.getData().getPath();
            }else{
                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(data.getData(),
                        proj, null, null, null);
                cursor.moveToFirst();
                int idx = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                fileSrc = cursor.getString(idx);
                cursor.close();
            }
            //可以创建一个新的SharedPreference来对储存的文件进行操作

            editor.putString("img", fileSrc);
            editor.commit();
            Live2D.init();
            Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(getApplicationContext(), LiveWallpaperService.class));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            getApplicationContext().startActivity(intent);
        }
    }


}

