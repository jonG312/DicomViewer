package com.bit.dicomdw.dcm.img;

import com.imebra.ColorTransformsFactory;
import com.imebra.Image;
import com.imebra.ReadingDataHandlerNumeric;
import com.bit.dicomdw.dcm.color.MyColor;


/**
 * MyDcmImg
 * (包外不可见)
 * 封装了imebra的Image类
 * 外部不需要知道imebra库如何使用就可以获取Image的相关信息
 * (不过只提供了部分接口)
 */
public class MyDcmImg extends MyImg {
    private Image img;
    private ReadingDataHandlerNumeric handler;
    //这个布尔型变量指示了img是否为灰度图(true表示是)
    private boolean monochromeMode;
    private int w;
    private int h;

    public MyDcmImg(Image imebra_img){
        this.img=imebra_img;
        this.handler=imebra_img.getReadingDataHandler();
        if(ColorTransformsFactory.isMonochrome(img.getColorSpace())){
            monochromeMode=true;
        }else{
            monochromeMode=false;
        }
        //原本时long,现在转int了(为了兼容myimg)
        this.w=(int)this.img.getWidth();
        this.h=(int)this.img.getHeight();
    }

    @Override
    public int getWidth() {
        //return img.getWidth();
        return this.w;
    }

    @Override
    public int getHeight(){
        //return img.getHeight();
        return this.h;
    }

    @Override
    public void getPixelColor(int x, int y,MyColor color){
        if(monochromeMode){
            int gray=handler.getSignedLong(getWidth()*y+x);
            color.set(gray,gray,gray);
        }else{
            int r = handler.getSignedLong((y * w + x) * 3);
            int g = handler.getSignedLong((y * w + x) * 3 + 1);
            int b = handler.getSignedLong((y * w + x) * 3 + 2);
            color.set(r,g,b);
        }
    }
}
