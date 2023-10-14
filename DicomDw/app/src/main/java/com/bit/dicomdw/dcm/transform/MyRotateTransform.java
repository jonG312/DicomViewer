package com.bit.dicomdw.dcm.transform;

import com.bit.dicomdw.dcm.color.MyColor;
import com.bit.dicomdw.dcm.img.MyImg;
import com.bit.dicomdw.dcm.img.MyRealImg;

//正方向旋转90度
public class MyRotateTransform implements MyTransform {
    private MyColor c=new MyColor(0,0,0);
    public MyImg work(MyImg img){
        int H=img.getHeight();
        int W=img.getWidth();
        MyRealImg _img=new MyRealImg(H,W);
        for(int y=0;y<W;y++){
            for(int x=0;x<H;x++){
                img.getPixelColor(y,H-x-1,c);
                _img.setColor(x,y,c);
            }
        }
        return _img;
    }
}
