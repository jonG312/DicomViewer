package com.bit.dicomdw.dcm.img;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.bit.dicomdw.dcm.color.MyColor;

public class MyBitmapImg extends MyImg {

    private Bitmap bitmap;

    public MyBitmapImg(String path){
        readImg(path);
    }
    public MyBitmapImg(Bitmap bitmap){this.bitmap=bitmap;}

    public void readImg(String path){
        this.bitmap=BitmapFactory.decodeFile(path);
    }

    @Override
    public int getWidth(){
        return this.bitmap.getWidth();
    }

    @Override
    public int getHeight(){
        return this.bitmap.getHeight();
    }

    //获取像素值
    @Override
    public void getPixelColor(int x, int y, MyColor color){
        int c = bitmap.getPixel(x, y);//x、y为bitmap所对应的位置
//        int r = Color.red(_color);
//        int g = Color.green(_color);
//        int b = Color.blue(_color);
//        int a = Color.alpha(_color);
        color.set(Color.red(c),Color.green(c),Color.blue(c));
    }

}
