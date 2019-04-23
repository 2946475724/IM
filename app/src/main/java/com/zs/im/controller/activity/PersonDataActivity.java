package com.zs.im.controller.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.zs.im.R;
import com.zs.im.model.Model;

import java.io.File;
import java.io.FileNotFoundException;


public class PersonDataActivity extends Activity {

    private TextView tv_name;
    private ImageView iv_photo;
    private Uri imageUri; //图片存储的路径
    private File file;  //需要存储的图片文件


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_data);

        initView();
        initData();

        file = new File( Environment.getExternalStorageDirectory(),"headPicture.jpg" );//新建一个文件，（路径和文件名）
        imageUri  = Uri.fromFile( file );

        iv_photo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture(v);
            }
        } );
    }

    private void initView() {
        tv_name = findViewById(R.id.tv_name);
        iv_photo = findViewById( R.id.iv_photo );
    }

    private void initData() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                tv_name.setText(EMClient.getInstance().getCurrentUser());
            }
        });
    }

    public void choosePicture(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "选择您的头像方式" );
        String[] str = {"拍照","从相册里选择"};
        builder.setItems( str, new DialogInterface.OnClickListener() {//(实现了charsequence的类数组这里用string数组，一个点击事件的监听器)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0://拍照
                        Intent openCamera  =new Intent( MediaStore.ACTION_IMAGE_CAPTURE );//打开系统照相机
                        openCamera.putExtra( MediaStore.EXTRA_OUTPUT,imageUri );//表示需要在imageUri这个位置存放东西
                        startActivityForResult( openCamera,0 );
                        break;
                    case 1://从相册里选择
                        Intent chooseInAlbum = new Intent( Intent.ACTION_PICK,MediaStore.Images.Media
                                .INTERNAL_CONTENT_URI );
                        startActivityForResult( chooseInAlbum,1 );
                        break;
                }
            }
        } );
       builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        switch (requestCode){
            case 0://拍照
                if(resultCode == RESULT_OK){
                    cropPicture( imageUri );
                }
                break;
            case 1://从相册获取
                if(resultCode == RESULT_OK){
                    cropPicture( data.getData() );
                }
                break;
            case 2://裁剪之后
                if(resultCode == RESULT_OK){
                    setHeadPicture();
                }
                break;
        }

    }

    private void setHeadPicture() {
        try {
            //根据imageUri用getContentResolver来获取流对象 再转化成bitmap
            Bitmap bitmap = BitmapFactory.decodeStream( getContentResolver().openInputStream( imageUri ) );
            if(bitmap == null){
                //判断bitmap是否为空
                Toast.makeText( this,"图像没有存储到sd卡根目录",Toast.LENGTH_SHORT ).show();
            }
            iv_photo.setImageBitmap( bitmap );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    private void cropPicture(Uri uri){
        //新建一个表示剪裁的Intent
        Intent intent = new Intent( "com.android.camera.action.CROP" );
        //表明我要剪裁的目标是uri这个地址，文件类型是图片
        intent.setDataAndType( uri,"image/*" );
        //指定长宽的比例为1:1
        intent.putExtra( "aspectX",1 );
        intent.putExtra( "aspectY",1 );
        //指定宽高为1000
        intent.putExtra( "return-data",false );
        intent.putExtra( "scale",true );
        intent.putExtra( "outputX",1000 );
        intent.putExtra( "outputY",1000 );
        intent.putExtra( MediaStore.EXTRA_OUTPUT,imageUri );
        startActivityForResult( intent,2 );
    }
}





