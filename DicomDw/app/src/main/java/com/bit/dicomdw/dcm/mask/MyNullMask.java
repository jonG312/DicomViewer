package com.bit.dicomdw.dcm.mask;

import com.bit.dicomdw.dcm.color.MyColor;

public class MyNullMask extends MyMask {
    @Override
    public boolean getPixelColor(int x,int y,MyColor color){
        return false;
    }
}
