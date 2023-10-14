package com.bit.dicomdw;


import com.bit.dicomdw.dcm.MyMatrix;
import com.bit.dicomdw.dcm.MyPair;

public class MatrixFactory {
    //平移变换矩阵
    public static MyMatrix getTransformationMatrix(MyPair begin,MyPair end)
    {
        MyMatrix myMatrix = new MyMatrix();
        myMatrix.value[0][0]=1;
        myMatrix.value[0][1]=0;
        myMatrix.value[0][2]=end.x-begin.x;
        myMatrix.value[1][0]=0;
        myMatrix.value[1][1]=1;
        myMatrix.value[1][2]=end.y-begin.y;
        myMatrix.value[2][0]=0;
        myMatrix.value[2][1]=0;
        myMatrix.value[2][2]=1;
        return myMatrix;
    }
    //放缩旋转变换矩阵
    public static MyMatrix getTransformationMatrix(MyPair leftBegin,MyPair rightBegin,MyPair leftEnd,MyPair rightEnd)
    {
        MyPair vecBegin=rightBegin.minus(leftBegin);
        MyPair vecEnd=rightEnd.minus(leftEnd);

        //两个平移矩阵
        MyMatrix toOrigTransMat=new MyMatrix();
        toOrigTransMat.setTrans(leftBegin.getNegtive());
        MyMatrix toEndTransMat=new MyMatrix();
        toEndTransMat.setTrans(leftEnd);


        double rotateDirec=vecBegin.crossMul(vecEnd)>0?1f:-1f;
        double rotateAngleRadians=vecBegin.calcuVecAngleRadians(vecEnd);
        rotateAngleRadians*=rotateDirec;

        MyMatrix rotateMat=new MyMatrix();
        rotateMat.setRotate_radian(rotateAngleRadians);

        //计算放缩矩阵
        double denominator=vecBegin.getLen();
        if(denominator>-1 && denominator<1)
            return new MyMatrix(MyMatrix.SpecialMatirxType.IDENTITY);

        double scaleRate=vecEnd.getLen()/denominator;
        MyMatrix scaleMat=new MyMatrix();
        scaleMat.setScale(scaleRate);

        //按照实际变换顺序相乘(先变换的在右边)
        MyMatrix mat;
        mat=toOrigTransMat;
        mat=rotateMat.matrixMul(mat);
        mat=scaleMat.matrixMul(mat);
        mat=toEndTransMat.matrixMul(mat);

        return mat;
    }

    //传入窗口尺寸和图片尺寸,返回初始化矩阵
    public static MyMatrix getInitMatrix(int window_width,int window_height,int img_width,int img_height){
        double scale_rate=Math.min((double)window_width/img_width,(double)window_height/img_height);
        MyMatrix mat=new MyMatrix();
        mat.setScale(scale_rate);
        MyPair window_center=new MyPair(window_width/2.0,window_height/2.0);
        MyPair new_img_center=new MyPair(img_width/2.0*scale_rate,img_height/2.0*scale_rate);
        MyPair trans_vec=window_center.minus(new_img_center);
        MyMatrix trans_mat=new MyMatrix();
        trans_mat.setTrans(trans_vec);
        mat=trans_mat.matrixMul(mat);
        return mat;
    }
}
