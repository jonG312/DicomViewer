package com.bit.dicomdw.dcm.img;

import com.bit.dicomdw.dcm.color.MyColor;
import com.bit.dicomdw.dcm.colorstrategy.MyColorStrategy;
import com.bit.dicomdw.dcm.colorstrategy.MyNullColorStrategy;
import com.bit.dicomdw.dcm.transform.MyTransform;

/**
 * MyDcmImg实现自接口MyImg
 * MyDcmImgWrapper时MyDcmImg和MyColorStrategy的包装类
 * 功能是,取出MyDcmImg像素值,经过MyColorStrategy调窗后返回结果像素值
 */
public class MyImgWrapper extends MyImg {
    //dcmImg
    private MyImg img;
    //颜色策略,用于调窗
    private MyColorStrategy cs;


    public MyImgWrapper(MyImg img){
        this(img,new MyNullColorStrategy());
    }

    public MyImgWrapper(MyImg img, MyColorStrategy cs){
        this.img=img;
        setColorStrategy(cs);
    }

    public void setImg(MyImg img){
        this.img=img;
    }

    public void change(MyTransform transform){
        setImg(transform.work(this.img));
    }

    //设置ColorStrategy
    public void setColorStrategy(MyColorStrategy cs){
        if(null==cs)
            return;
        this.cs=cs;
    }
    @Override
    public int getWidth(){
        return (int)img.getWidth();
    }

    @Override
    public int getHeight(){
        return (int)img.getHeight();
    }

    //获取像素值
    @Override
    public void getPixelColor(int x, int y, MyColor color){
        img.getPixelColor(x,y,color);
        cs.map(color,color);
    }

}
