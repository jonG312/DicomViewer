package com.bit.dicomdw;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.dicomdw.dcm.MyDcmSavingType;
import com.bit.dicomdw.dcm.MyDcmTag;
import com.bit.dicomdw.dcm.MyMatrix;
import com.bit.dicomdw.dcm.MyPair;
import com.bit.dicomdw.dcm.colorstrategy.MyNormalColorStrategy;
import com.bit.dicomdw.dcm.exception.MyDcmImgLoadException;
import com.bit.dicomdw.dcm.img.MyImg;
import com.bit.dicomdw.menu_activity.InfoActivity;
import com.bit.dicomdw.menu_activity.ScaleActivity;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;

import com.bit.dicomdw.menu_activity.TuneActivity;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {


    /*事件返回参数*/

    private static final int CHOOSE_FILE=1999;
    private static final int Scale_Activity_Back=2001;
    private static final int SAVE_FILE=2002;
    private static final int Tune_Activity_Back=2000;
    private static final int Info_Activity_Back=2003;

    private TextView mTextMessage;
    private static final double MOVE_DISTANCE=0.002;
    private ImageView P1;
    private MyImg img;
    private MenuItem Navigate_Frame;
    private MainHandler handler;
    private double ImgViewHeight,ImgViewWidth;
    private int Frame,MAX_FRAME;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_upFrame:
                    Log.d("frame","upFrame");
                    Frame--;
                    boolean Frame_Valid=handler.setFrame(Frame);
                    Log.d("aaaaa",String.valueOf(Frame_Valid));
                    if(Frame_Valid) {
                        initImageView();
                        updateImageView();
                        Navigate_Frame.setTitle(String.valueOf(Frame+1)+"/"+String.valueOf(MAX_FRAME));
                    }
                    else{
                        //error
                    }
                    return true;
                case R.id.navigation_Frame:
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_downFrame:
                    Log.d("frame","downFrame");
                    Frame++;
                    Frame_Valid=handler.setFrame(Frame);
                    Log.d("aaaaa",String.valueOf(Frame_Valid));
                    if(Frame_Valid) {
                        initImageView();
                        updateImageView();
                        Navigate_Frame.setTitle(String.valueOf(Frame+1)+"/"+String.valueOf(MAX_FRAME));
                    }
                    else{
                        //error
                    }
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //获取权限
        PermissionUtils.isGrantExternalRW(this,1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Navigate_Frame=navigation.getMenu().findItem(R.id.navigation_Frame);
        P1=(ImageView)findViewById(R.id.imageView);


        P1.post(new Runnable() {
            @Override
            public void run() {
                int w=P1.getWidth();
                int h=P1.getHeight();
                Log.d("w",String.valueOf(w));
                Log.d("h",String.valueOf(h));
                handler=new MainHandler(w,h,Bitmap.Config.ARGB_8888);
            }
        });
        P1.setOnTouchListener(this);
    }


    // Author :Mr.Z


    //事件读取的位置 index=0的手指
    MyPair last_pos,now_pos;
    //转换后操作的参数
    MyPair last_param,now_param;

    //index 为１的手指
    MyPair last_pos_two,now_pos_two;
    MyPair last_param_two,now_param_two;
    int MOVE_CNT=0;
    int INTERVAL=1;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int pointerCount=event.getPointerCount();
        if(pointerCount==1){
            switch(event.getActionMasked()){
                case MotionEvent.ACTION_DOWN:
                    Log.d("qqq","手指按下");

                    last_pos=new MyPair(event.getX()/ImgViewWidth,event.getY()/ImgViewHeight);

                    //初始检测移动时间的间隔
                    MOVE_CNT=0;
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d("qqq","手指离开");

                    now_pos=new MyPair(event.getX()/ImgViewWidth,event.getY()/ImgViewHeight);

                    //利用哈弗曼距离，当移动距离小于一定值时不进行操作
                    Log.d("qqq",String.valueOf(handler.HuffmanDist(now_pos,last_pos)));
                    if(handler.HuffmanDist(now_pos,last_pos)>MOVE_DISTANCE) {


                        last_param = new MyPair(last_pos);
                        now_param = new MyPair(now_pos);
                        handler.posMap(last_param);
                        handler.posMap(now_param);
                        /*                初始点为last_pos,移动后的点为now_pos,这里进行操作
                        //TODO 单指操作
                        操作完毕*/

//                        MyMatrix mat=MatrixFactory.getTransformationMatrix(last_param,now_param);
//                        MyMatrix former_mat=handler.getCanvasMatrix();
//                        mat=mat.matrixMul(former_mat);
//                        handler.setCanvasMatrix(mat);
//                        updateImageView();

                        //对last_pos进行迭代

                    }
                    handler.assignment(last_pos, now_pos);

                    break;



                case MotionEvent.ACTION_MOVE:
                    MOVE_CNT=(MOVE_CNT + INTERVAL)%INTERVAL;
                    Log.d("qqq",String.valueOf(MOVE_CNT));
                    if(MOVE_CNT%INTERVAL==0){
                        now_pos=new MyPair(event.getX()/ImgViewWidth,event.getY()/ImgViewHeight);
                        Log.d("qqq",String.valueOf(handler.HuffmanDist(now_pos,last_pos)));
                        if(handler.HuffmanDist(now_pos,last_pos)>MOVE_DISTANCE) {
                            Log.d("qqq","手指移动啦");
                            last_param = new MyPair(last_pos);
                            now_param = new MyPair(now_pos);
                            handler.posMap(last_param);
                            handler.posMap(now_param);
                            /*     初始点为last_pos,移动后的点为now_pos,这里进行操作
                            //TODO 单指操作
                            操作完毕*/

                            MyMatrix mat=MatrixFactory.getTransformationMatrix(last_param,now_param);
                            MyMatrix former_mat=handler.getCanvasMatrix();
                            mat=mat.matrixMul(former_mat);
                            handler.setCanvasMatrix(mat);
                            updateImageView();

                            //对last_pos进行迭代
                            handler.assignment(last_pos, now_pos);
                        }

                    }
                    break;
            }
        }
        if(pointerCount==2){
            int index=event.getActionIndex();
            switch(event.getActionMasked()){
//                case MotionEvent.ACTION_DOWN:
//                    Log.d("double","第一根手指按下");
//                    break;
//                case MotionEvent.ACTION_UP:
//                    Log.d("double","最后一根手指抬起");
//                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    Log.d("double",String.valueOf(index+1)+"手指落下");

                    //欧阳:TODO 似乎这里两个触屏点的初始化有问题

                    last_pos=new MyPair(event.getX(0)/ImgViewWidth,event.getY(0)/ImgViewHeight);
                    last_pos_two=new MyPair(event.getX(1)/ImgViewWidth,event.getY(1)/ImgViewHeight);
                    //last_pos_two=new MyPair(event.getX()/ImgViewWidth,event.getY()/ImgViewHeight);


                    //初始检测移动时间的间隔
                    MOVE_CNT=0;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    Log.d("double",String.valueOf(index+1)+"手指抬起");
                    //先进行运算
                    now_pos=new MyPair(event.getX(0)/ImgViewWidth,event.getY(0)/ImgViewHeight);
                    now_pos_two=new MyPair(event.getX(1)/ImgViewHeight,event.getY(1)/ImgViewHeight);

                    //对移动距离进行判断是否进行操作
                    if(handler.HuffmanDist(now_pos,last_pos)>MOVE_DISTANCE || handler.HuffmanDist(now_pos_two,last_pos_two)>MOVE_DISTANCE) {
                        now_param = new MyPair(now_pos);
                        now_param_two = new MyPair(now_pos_two);
                        last_param = new MyPair(last_pos);
                        last_param_two = new MyPair(last_pos_two);
                        handler.posMap(last_param);
                        handler.posMap(last_param_two);
                        handler.posMap(now_param);
                        handler.posMap(now_param_two);
               /*         进行处理　手指１　的上一个位置last_param 　现在的位置now_param　手指２ 的上一个位置last_param_two 现在的位置now_param_two

                        // TODO 双指操作

                        处理完毕　返回bitmap*/
//                        MyMatrix mat=MatrixFactory.getTransformationMatrix(
//                                last_param,last_param_two,now_param,now_param_two);
//                        MyMatrix former_mat=handler.getCanvasMatrix();
//                        mat=mat.matrixMul(former_mat);
//                        handler.setCanvasMatrix(mat);
//                        updateImageView();

                    }
                    //此时第一根手指离开了，第二根手指变成了第一根手指，要把第二个手指的位置值　赋值到第一根手指的记录上，然后新的第一根手指进行
                    if(index==0){
                        handler.assignment(last_pos,now_pos_two);
                    }
                    //手指２离开时，切换成单手值模式，直接进行
                    else{
                        handler.assignment(last_pos,now_pos);
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    MOVE_CNT=(MOVE_CNT + INTERVAL)%INTERVAL;
                    if(MOVE_CNT%INTERVAL==0) {

                        now_pos=new MyPair(event.getX(0)/ImgViewWidth,event.getY(0)/ImgViewHeight);
                        now_pos_two=new MyPair(event.getX(1)/ImgViewHeight,event.getY(1)/ImgViewHeight);
                        //用哈弗曼距离判断是否进行操作
                        if(handler.HuffmanDist(now_pos_two,last_pos_two)>MOVE_DISTANCE||handler.HuffmanDist(last_pos,now_pos)>MOVE_DISTANCE) {
                            Log.d("double", "两只手指移动呀");
                            now_param = new MyPair(now_pos);
                            now_param_two = new MyPair(now_pos_two);
                            last_param = new MyPair(last_pos);
                            last_param_two = new MyPair(last_pos_two);
                            handler.posMap(last_param);
                            handler.posMap(last_param_two);
                            handler.posMap(now_param);
                            handler.posMap(now_param_two);
         /*                   进行处理　手指１　的上一个位置last_param 　现在的位置now_param　手指２ 的上一个位置last_param_two 现在的位置now_param_two
                            // TODO 双指操作

                            处理完毕　返回bitmap*/
                            MyMatrix mat=MatrixFactory.getTransformationMatrix(
                                    last_param,last_param_two,now_param,now_param_two);
                            MyMatrix former_mat=handler.getCanvasMatrix();
                            mat=mat.matrixMul(former_mat);
                            handler.setCanvasMatrix(mat);
                            updateImageView();

                            //将现在的位置赋值给last
                            handler.assignment(last_pos, now_pos);
                            handler.assignment(last_pos_two, now_pos_two);
                        }
                        break;
                    }


            }
        }
        return true;
    }

    // Author :Mr.Z

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);

        return true;
    }

    //实现跳转的活动
    public Intent intent;
    //定义菜单响应事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId=item.getItemId();

        Runnable openFile=new Runnable() {
            @Override
            public void run() {
//                intent=new Intent(Intent.ACTION_GET_CONTENT);
                Intent intent=new Intent(MainActivity.this,OpenFileDialogActivity.class);
                intent.putExtra("DefaultFilePath",Environment.getExternalStorageDirectory().getPath());
                /**更改于20190314*/
                intent.putExtra("Ext",".dcm .jpg .jpeg .png");
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,CHOOSE_FILE);
            }
        };

        //如果当前图片为空,只能选择打开图片
        if(!handler.checkCanvasImgNotNull()){
            if(R.id.menu_item_open==itemId){
                openFile.run();
            }
            return true;
        }

        switch (itemId){
            case R.id.menu_item_open:
                openFile.run();
                break;
            case R.id.menu_item_save:
                Intent intent=new Intent(MainActivity.this,SaveFileDialogActivity.class);
                intent.putExtra("DefaultFilePath",Environment.getExternalStorageDirectory().getPath());
                intent.putExtra("DefaultFileName","filename_without_suffix");
                /**更改于20190314*/
                intent.putExtra("Ext",".dcm .jpg .jpeg .png");
                startActivityForResult(intent,SAVE_FILE);
                break;
            case R.id.menu_item_recovery:
                MyImgRecover MIR=new MyImgRecover();
                handler.transform(MIR);
                initImageView();
                updateImageView();
                break;
            case R.id.menu_item_scale:
                intent =new Intent(MainActivity.this,ScaleActivity.class);
                startActivityForResult(intent,Scale_Activity_Back);
                break;
            case R.id.menu_item_rotate:
                handler.imgRotate();
                initImageView();
                updateImageView();
                break;

            case R.id.menu_item_init:
                initImageView();
//                initImage();
                updateImageView();
                break;
            case R.id.menu_item_tune:
                intent=new Intent(MainActivity.this,TuneActivity.class);
                startActivityForResult(intent,Tune_Activity_Back);
                break;
            case R.id.menu_item_infomation:
                intent=new Intent(MainActivity.this,InfoAcitivity.class);
                intent.putExtra("info_name",backtheInformation(MyDcmTag.PatientName));
                intent.putExtra("info_age",backtheInformation(MyDcmTag.StudyPatientAge));
                intent.putExtra("info_sex",backtheInformation(MyDcmTag.PatientSex));
                intent.putExtra("info_date",backtheInformation(MyDcmTag.StudyDate));
                intent.putExtra("info_type",backtheInformation(MyDcmTag.StudyModalities));
                intent.putExtra("info_area",backtheInformation(MyDcmTag.StudyBodyPart));
                startActivityForResult(intent,Info_Activity_Back);
                break;
            case R.id.menu_item_detect:
                // TODO 疾病检测
                this.sendCurrentFrameForDetecting();
            default:
        }
        return true;
    }
    //检测提取信息异常
    public String backtheInformation(MyDcmTag type){
        String backString;
        try {
            backString=handler.DCM.getInfo(type);
        }
        catch (RuntimeException e){
            backString="NULL";
        }
        return backString;
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //选取文件
            case CHOOSE_FILE:
                String path = data.getStringExtra("FilePathName");
                if (path.length() <= 0) {
                    break;
                }
                boolean fileExist = fileIsExists(path);
                if (fileExist) {
                    Log.d("exist1", "OK");
//                        DCM=new MyDcm(p);
                } else {
                    Log.d("exist", "NO");
                    break;
                }
                try {
                    handler.read_DICOM(path);
                } catch (MyDcmImgLoadException e) {
                    break;
                }
                handler.setCanvasMatrix(new MyMatrix(MyMatrix.SpecialMatirxType.IDENTITY));
                initImageView();
                Log.d("frame_cnt", String.valueOf(handler.getFrameCnt()));
                Frame = 0;
                MAX_FRAME = handler.getFrameCnt();
                Navigate_Frame.setTitle("1/" + String.valueOf(MAX_FRAME));
                updateImageView();
                ImgViewHeight = P1.getHeight();
                ImgViewWidth = P1.getWidth();
                Log.d("width", String.valueOf(ImgViewWidth));
                Log.d("height", String.valueOf(ImgViewHeight));
                P1.setOnTouchListener(this);
                break;
            case Scale_Activity_Back:
                Log.d("qaqa","qqq");
                if(resultCode==Activity.RESULT_OK){
                    String str_scale=data.getStringExtra("scale");
                    Log.d("aqaq",str_scale);
                    double scale_rate=Double.valueOf(str_scale);
                    handler.imgScale(scale_rate);
                    updateImageView();
                }
                break;
            case SAVE_FILE:
                Log.d("savefile","yes or no");
                if(resultCode==SaveFileDialogActivity.RESULT_CODE){
                    String FilePathName = data.getStringExtra("FilePathName");
                    Log.d("Savefile",FilePathName);
                    //保存
//                    String Write_Str = (new Gson()).toJson(this.mData);
//                    try {
//                        FileOutputStream fout = new FileOutputStream(FilePathName);
//                        byte [] bytes = Write_Str.getBytes();
//                        fout.write(bytes);

//                        fout.close();
//                        Toast.makeText(this,"保存到" + FilePathName, Toast.LENGTH_LONG).show();
//                    } catch (IOException e) {
//                        Toast.makeText(this,"保存到" + FilePathName +"失败，可能是权限不够！", Toast.LENGTH_LONG).show();
//                    }
                    handler.save(FilePathName,MyDcmSavingType.JPEG);

                }
                break;
            case Tune_Activity_Back:
                if(resultCode==Activity.RESULT_OK){
                    String str_center=data.getStringExtra("center");
                    String str_radius=data.getStringExtra("radius");
                    int center,radius,left,right;
                    center=Integer.valueOf(str_center);
                    radius=Integer.valueOf(str_radius);
                    left=center-radius;
                    right=center+radius;
                    MyNormalColorStrategy oneColorStrategy=new MyNormalColorStrategy(left,right);
                    handler.setColorStrategy(oneColorStrategy);
                    updateImageView();
                }
                break;
        }
    }


    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void updateImageView(){
        Bitmap bm=handler.getBitmap();
        Drawable TestDrawable=new BitmapDrawable(getResources(),bm);
        P1.setBackground(TestDrawable);
    }

    private void initImageView(){
        if(!handler.checkCanvasImgNotNull())
            return;
        else{
            int window_width,window_height,img_width,img_height;
            window_width=handler.getCanvasWidth();
            window_height=handler.getCanvasHeight();
            img_width=handler.getImgWidth();
            img_height=handler.getImgHeight();
            MyMatrix init_mat=MatrixFactory.getInitMatrix(
                    window_width,window_height,img_width,img_height);
            handler.setCanvasMatrix(init_mat);
        }
    }


    private void sendCurrentFrameForDetecting(){
        /**
         * 把当前帧发送用于检测
         * 开启一个线程用于处理mask的生成
         */
        Thread detectThread=new Thread(new Runnable(){
            @Override
            public void run(){
                int frameNum=handler.getCurrentFrameIndex();
                if(handler.checkOnDetecting(frameNum)){
                    //如果正在处理当前帧，直接返回
                    return;
                }
                try{
                    handler.detect();
                    handler.setMask(frameNum);
                    updateImageView();
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //detectThread.run();
        detectThread.start();
    }

//    private void initImage(){
//        if(!handler.checkCanvasImgNotNull())
//            return;
//        else{
//            handler.resetImg();
//            int window_width,window_height,img_width,img_height;
//            window_width=handler.getCanvasWidth();
//            window_height=handler.getCanvasHeight();
//            img_width=handler.getImgWidth();
//            img_height=handler.getImgHeight();
//            MyMatrix init_mat=MatrixFactory.getInitMatrix(
//                    window_width,window_height,img_width,img_height);
//            handler.setCanvasMatrix(init_mat);
//        }
//    }

}