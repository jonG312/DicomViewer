package com.bit.dicomdw.dcm;

//这个矩阵表示变换的时候
//认为需要变换的向量为列向量

/**
 * MyMatrix是一个矩阵类
 * 内部数据使用行优先存储
 * (即展开为一维为 m00,m01,m02,m10,m11,m12,m20,m21,m22)
 * 里面实现了一些必要的矩阵运算:
 *
 * 矩阵乘法: matrixMul
 * 矩阵求逆: inverse
 * 矩阵求行列式: det
 * 矩阵乘坐标: posMul
 *      需要注意的是,这里的坐标是通过MyPair传入的
 *      我选取的坐标格式是列向量 [x y 1]^T
 *      也就是说进行变换的时候使用的是左乘矩阵
 * 以及设置矩阵为单位矩阵: setIdentity
 */
public final class MyMatrix {
    //定义了部分特殊矩阵类型
    public enum SpecialMatirxType{
        IDENTITY,//单位矩阵
        ZERO,//全0矩阵
        ONE//全1矩阵
    }
    //矩阵数据
    public double[][] value=new double[3][3];

    //内部使用的精度值
    private static final double EPS=(double)1e-9;

    //默认初始化为单位矩阵
    public MyMatrix(){
        this(SpecialMatirxType.IDENTITY);
    }
    //按照传入参数创建特殊矩阵
    //参数类型为 MyMatrix.SpecialMatrixType
    public MyMatrix(SpecialMatirxType type){
        switch (type){
            case ZERO:
                value[0][0]=value[0][1]=value[0][2]=0;
                value[1][0]=value[1][1]=value[1][2]=0;
                value[2][0]=value[2][1]=value[2][2]=0;
                break;
            case ONE:
                value[0][0]=value[0][1]=value[0][2]=1;
                value[1][0]=value[1][1]=value[1][2]=1;
                value[2][0]=value[2][1]=value[2][2]=1;
                break;
            case IDENTITY:
                value[0][0]=value[1][1]=value[2][2]=1;
                value[0][1]=value[0][2]=0;
                value[1][0]=value[1][2]=0;
                value[2][0]=value[2][1]=0;
                //默认也是单位矩阵,所以这里不用break
            default:
                break;
        }
    }

    //复制别的矩阵
    public MyMatrix(MyMatrix other){
        this.setMatrix(other);
    }

    //从一维数组形式数据建立矩阵
    public MyMatrix(double data[]){
        int len=data.length;
        for(int i=0;i<9;i++){
            if(i>=len)
                value[i/3][i%3]=0;
            else
                value[i/3][i%3]=data[i];
        }
    }

    //复制别的矩阵
    public void setMatrix(MyMatrix other){
        for(int i=0;i<3;i++){
            //for(int j=0;j<3;j++){
            //    value[i][j]=other.value[i][j];
            //}
            System.arraycopy(
                    other.value[i],0,
                    value[i],0,
                    3);
        }
    }

    //矩阵乘法
    public MyMatrix matrixMul(MyMatrix other){
        MyMatrix mat=new MyMatrix(SpecialMatirxType.ZERO);
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                for(int k=0;k<3;k++)
                    mat.value[i][j]+=this.value[i][k]*other.value[k][j];
        return mat;
    }

    //矩阵行列式
    public double det(){
        return value[0][0]*(value[1][1]*value[2][2]-value[1][2]*value[2][1])
                - value[0][1]*(value[1][0]*value[2][2]-value[1][2]*value[2][0])
                + value[0][2]*(value[1][0]*value[2][1]-value[1][1]*value[2][0]);
    }

    //矩阵求逆
    //如果行列式为0,返回null
    public MyMatrix inverse(){
        MyMatrix mat=new MyMatrix();
        double detValue=this.det();
        if(detValue>-EPS && detValue<EPS)
            return null;
        else{
            mat.value[0][0]=(value[1][1]*value[2][2]-value[1][2]*value[2][1])/detValue;
            mat.value[1][0]=-(value[1][0]*value[2][2]-value[1][2]*value[2][0])/detValue;
            mat.value[2][0]=(value[1][0]*value[2][1]-value[1][1]*value[2][0])/detValue;

            mat.value[0][1]=-(value[0][1]*value[2][2]-value[0][2]*value[2][1])/detValue;
            mat.value[1][1]=(value[0][0]*value[2][2]-value[0][2]*value[2][0])/detValue;
            mat.value[2][1]=-(value[0][0]*value[2][1]-value[0][1]*value[2][0])/detValue;

            mat.value[0][2]=(value[0][1]*value[1][2]-value[0][2]*value[1][1])/detValue;
            mat.value[1][2]=-(value[0][0]*value[1][2]-value[0][2]*value[1][0])/detValue;
            mat.value[2][2]=(value[0][0]*value[1][1]-value[0][1]*value[1][0])/detValue;
            return mat;
        }
    }

    //矩阵乘坐标
    public MyPair posMul(MyPair pos){
        double _x,_y;
        _x=(value[0][0]*pos.x+value[0][1]*pos.y+value[0][2])/value[2][2];
        _y=(value[1][0]*pos.x+value[1][1]*pos.y+value[1][2])/value[2][2];
        //暂时不考虑切变
        return new MyPair(_x,_y);
    }

    public MyMatrix valMul(double value){
        MyMatrix mat=new MyMatrix();
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                mat.value[i][j]=value*this.value[i][j];
            }
        }
        return mat;
    }

    public void posMul(double x,double y,double[] ret){
        ret[0]=value[0][0]*x+value[0][1]*y+value[0][2];
        ret[1]=value[1][0]*x+value[1][1]*y+value[1][2];
    }

    public void posMul(int x,int y,double[] ret){
        ret[0]=value[0][0]*x+value[0][1]*y+value[0][2];
        ret[1]=value[1][0]*x+value[1][1]*y+value[1][2];
    }

    //根据vec设置平移矩阵
    public void setTrans(MyPair vec){
        this.setIdentity();
        value[0][2]=vec.x;
        value[1][2]=vec.y;
    }

    public void setRotate_degree(double angle_degree){
        angle_degree=MyMathHelper.angle_loop(angle_degree);
        double angle_radian = (double)((angle_degree * Math.PI)/180.0f);
        setRotate_radian(angle_radian);
    }

    //根据angle设置旋转矩阵(注意,转轴为原点)
    //弧度制
    //方向则要看选用的时左手系还是右手系
    //(图片的为左手系---->对应顺时针为正)
    public void setRotate_radian(double angle_radian){
        this.setIdentity();
        double _c=(double)Math.cos((double)angle_radian);
        double _s=(double)Math.sin((double)angle_radian);
        value[0][0]=_c;
        value[1][0]=_s;
        value[0][1]=-_s;
        value[1][1]=_c;
    }

    //根据angle设置旋转矩阵(注意,转轴为原点)
    public void setRotate(double angle_degree){
        setRotate_degree(angle_degree);
    }


    //根据scale_rate设置放大矩阵(注意,放缩中心为原点,且是在原图大小基础上进行)
    //注意value[2][2]必须保持为1
    public void setScale(double scale_rate){
        this.setIdentity();
        value[0][0]=value[1][1]=scale_rate;
    }

    //设置单位矩阵
    public void setIdentity(){
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                value[i][j]=0;
        value[0][0]=value[1][1]=value[2][2]=1;
    }

}
