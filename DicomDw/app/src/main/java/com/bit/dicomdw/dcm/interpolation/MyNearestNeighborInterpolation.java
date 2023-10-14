package com.bit.dicomdw.dcm.interpolation;

import com.bit.dicomdw.dcm.color.MyColor;
import com.bit.dicomdw.dcm.img.MyImg;

public class MyNearestNeighborInterpolation extends MyInterpolation {

    public MyNearestNeighborInterpolation(){}
    public MyNearestNeighborInterpolation(MyImg img){
        super(img);
    }

    @Override
    public void getPixelColor(double x, double y, MyColor color){
        if(null==img){
            getDefaultColor(color);
            return;
        }
        if(check_outside(x,y)){
            getDefaultColor(color);
            return;
        }
        int xl=(int)x,xu=xl+1;
        int yl=(int)y,yu=yl+1;
        if(xu>=img_w)xu--;
        if(yu>=img_h)yu--;

        int target_x=(x-xl>xu-x)?xu:xl;
        int target_y=(y-yl>yu-y)?yu:yl;

        img.getPixelColor(target_x,target_y,color);
    }
}
