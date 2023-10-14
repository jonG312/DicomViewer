package com.bit.dicomdw.dcm.colorstrategy;

import com.bit.dicomdw.dcm.color.MyColor;

public class MyNullColorStrategy extends MyColorStrategy {
    //直接返回原值
    @Override
    public void map(MyColor fromColor, MyColor toColor){
        toColor.set(fromColor);
    }
}
