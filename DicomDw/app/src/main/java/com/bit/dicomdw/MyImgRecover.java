package com.bit.dicomdw;

import com.bit.dicomdw.dcm.color.MyColor;
import com.bit.dicomdw.dcm.img.MyImg;
import com.bit.dicomdw.dcm.img.MyRealImg;
import com.bit.dicomdw.dcm.transform.MyTransform;

public class MyImgRecover implements MyTransform {
    private static final int[] dx={-1,0,1,-1,0,1,-1,0,1};
    private static final int[] dy={-1,-1,-1,0,0,0,1,1,1};
    private static final double R=0.5;

    public MyImg work(MyImg img){
        //图像边缘不考虑了
        int w=img.getWidth();
        int h=img.getHeight();
        MyRealImg ans=new MyRealImg(w,h);
        float[][][] color_map=new float[h][w][3];//这里为了效率,倒过来
        float[][][] color_map_2=new float[h][w][3];
        smooth(img,color_map_2);
        smooth(color_map_2,color_map,w,h);
        smooth(color_map,color_map_2,w,h);
        smooth(color_map_2,color_map,w,h);
        MyColor c=new MyColor();
        for(int y=0;y<h;y++){
            for(int x=0;x<w;x++){
                img.getPixelColor(x,y,c);
                ans.setColor(x,y,
                        (int)(R*c.r+(1-R)*color_map[y][x][0]),
                        (int)(R*c.g+(1-R)*color_map[y][x][1]),
                        (int)(R*c.b+(1-R)*color_map[y][x][2])
                        );
            }
        }
        return ans;
    }
    private void smooth(float[][][] img,float[][][] color_map,int w,int h){
        float[][] mini=new float[9][3];
        for(int y=1;y<h-1;y++){
            for(int x=1;x<w-1;x++){
                for(int k=0;k<9;k++){
                    mini[k][0]=img[y+dy[k]][x+dx[k]][0];
                    mini[k][1]=img[y+dy[k]][x+dx[k]][1];
                    mini[k][2]=img[y+dy[k]][x+dx[k]][2];
                }
                average(mini,color_map[y][x]);
            }
        }
        for(int y=0;y<h;y++){
            color_map[y][0]=color_map[y][1];
            color_map[y][w-1]=color_map[y][w-2];
        }
        for(int x=0;x<w;x++){
            color_map[0][x]=color_map[1][x];
            color_map[h-1][x]=color_map[h-2][x];
        }
    }
    private void smooth(MyImg img,float[][][] color_map){
        int w=img.getWidth();
        int h=img.getHeight();
        MyColor[] mini=new MyColor[9];
        for(int i=0;i<9;i++)mini[i]=new MyColor(0,0,0);
        for(int y=1;y<h-1;y++){
            for(int x=1;x<w-1;x++){
                for(int k=0;k<9;k++)
                    img.getPixelColor(x+dx[k],y+dy[k],mini[k]);
                average(mini,color_map[y][x]);
            }
        }
        for(int y=0;y<h;y++){
            color_map[y][0]=color_map[y][1];
            color_map[y][w-1]=color_map[y][w-2];
        }
        for(int x=0;x<w;x++){
            color_map[0][x]=color_map[1][x];
            color_map[h-1][x]=color_map[h-2][x];
        }
    }

    private static final void average(float[][] colors,float[] ret){
        float sum=0;
        for(int i=0;i<9;i++)
            sum+=colors[i][0];
        ret[0]=sum/9;
        sum=0;
        for(int i=0;i<9;i++)
            sum+=colors[i][1];
        ret[1]=sum/9;
        sum=0;
        for(int i=0;i<9;i++)
            sum+=colors[i][2];
        ret[2]=sum/9;
    }

    private static final void average(MyColor[] colors,float[] ret){
        float sum=0;
        for(int i=0;i<9;i++)
            sum+=colors[i].r;
        ret[0]=sum/9;
        sum=0;
        for(int i=0;i<9;i++)
            sum+=colors[i].g;
        ret[1]=sum/9;
        sum=0;
        for(int i=0;i<9;i++)
            sum+=colors[i].b;
        ret[2]=sum/9;
    }
}
