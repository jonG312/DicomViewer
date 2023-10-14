package com.bit.dicomdw.dcm.color;

public class MyColorHandler {
    static public final MyColor white=new MyColor(255,255,255);
    static public final MyColor black=new MyColor(0,0,0);

    static public double colorAberration(MyColor c1, MyColor c2) {
        //double wr = 3, wg = 4, wb = 2;
        return Math.sqrt(3 * (c1.r - c2.r) * (c1.r - c2.r) +
                4 * (c1.g - c2.g) * (c1.g - c2.g) +
                2 * (c1.b - c2.b) * (c1.b - c2.b)
        );
    }
}
