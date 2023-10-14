package com.bit.dicomdw.dcm.mask;

import com.bit.dicomdw.dcm.color.MyColor;
import com.bit.dicomdw.dcm.img.MyImg;
import com.bit.dicomdw.dcm.mask.MyMaskColorTools.MyDetecFrameMaskColorSet;

public class MyDetecFrameMask extends MyMask {

    private int[][] mask;
    private boolean[][] valid;
    private int width;
    private int height;
    private MyDetecFrameMaskColorSet colorSet;
    private MyColor _tmp_rgb;

    public MyDetecFrameMask(MyImg img,MyDetecFrameMaskColorSet colorSet) {
        setImg(img);
        setColorSet(colorSet);
    }
    public MyDetecFrameMask(int w,int h,MyDetecFrameMaskColorSet colorSet){
        init(w,h);
        setColorSet(colorSet);
    }
    public void init(int w,int h){
        this.width = w;
        this.height = h;
        this.mask = new int[height][width];
        this.valid = new boolean[height][width];
    }
    public void setImg(MyImg img){
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.mask = new int[height][width];
        this.valid = new boolean[height][width];
    }
    public void setColorSet(MyDetecFrameMaskColorSet colorSet){
        this.colorSet = colorSet;
    }
    public void clearMask(){
        for(int y=0;y<height;y++){
            for(int x=0;x<width;x++){
                this.valid[y][x]=false;
            }
        }
    }

    public void drawFrame(String tag, int startX, int startY, int width, int height, int lineWidth) {
        Integer color_index = colorSet.getColorIndexByTag_force(tag);
        int ci = color_index.intValue();
        int x_begin, y_begin, fw, fh, x_end, y_end;
        for (int layer = 0; layer < lineWidth; layer++) {
            x_begin = startX + layer;
            y_begin = startY + layer;
            fw = width - layer * 2;
            fh = height - layer * 2;
            x_end = x_begin + fw;
            y_end = y_begin + fh;
            if(fw<=0||fh<=0)break;
            for (int x = x_begin; x < x_end; x++) {
                setColor(x, y_begin, ci);
                setColor(x, y_end - 1, ci);
            }
            for (int y = y_begin; y < y_end; y++) {
                setColor(x_begin,y,ci);
                setColor(x_end-1,y,ci);
            }
        }
    }

    @Override
    public boolean getPixelColor(int x, int y, MyColor color) {
        if (!valid[y][x]) return false;
        _tmp_rgb=colorSet.getColorByIndex(mask[y][x]);
        if(_tmp_rgb==null)return false;
        color.set(_tmp_rgb);
        return true;
    }

    private boolean inRange(int x, int y) {
        if (x < 0 || y < 0 || x >= this.width || y >= this.height)
            return false;
        else
            return true;
    }

    private void setColor(int x, int y, int colorIndex) {
        this.valid[y][x] = true;
        this.mask[y][x] = colorIndex;
    }
}
