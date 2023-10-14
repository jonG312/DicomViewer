package com.bit.dicomdw.dcm;

/**
 * 用于表示坐标和向量
 * 注意使用的x和y都是 public double
 */
public final class MyPair {
    public double x,y;

    public MyPair(double x, double y){
        this.x=x;
        this.y=y;
    }
    public MyPair(int x, int y){
        this.x=(double)x;
        this.y=(double)y;
    }
    public MyPair(MyPair other){
        this.x=other.x;
        this.y=other.y;
    }

    public MyPair add(MyPair other){
        return new MyPair(
          x+other.x,
          y+other.y
        );
    }
    public MyPair minus(MyPair other){
        return new MyPair(
                x-other.x,
                y-other.y
        );
    }
    public MyPair getNegtive(){
        return new MyPair(-x,-y);
    }

    public double getLen(){
        return (double)Math.sqrt(x*x+y*y);
    }

    public MyPair valMul(double val){
        return new MyPair(x*val, y*val);
    }

    public double dotMul(MyPair other){
        return x*other.x+y*other.y;
    }

    public double crossMul(MyPair other){
        return x*other.y-y*other.x;
    }

    public double calcuVecAngleRadians(MyPair otherVec){
        double denominator=this.getLen()*otherVec.getLen();
        if(0!=denominator){
            return (double)Math.acos(this.dotMul(otherVec)/denominator);
        }else{
            return 0.0;
        }
    }
}
