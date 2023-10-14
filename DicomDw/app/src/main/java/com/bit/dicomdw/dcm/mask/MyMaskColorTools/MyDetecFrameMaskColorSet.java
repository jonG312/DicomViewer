package com.bit.dicomdw.dcm.mask.MyMaskColorTools;

import com.bit.dicomdw.dcm.color.MyColor;
import com.bit.dicomdw.dcm.color.MyColorHandler;
import com.bit.dicomdw.dcm.color.MyRainbowColorGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyDetecFrameMaskColorSet {
    private HashMap<String, Integer> tagToColor=new HashMap<String, Integer>();
    private List<MyColor> colors=new ArrayList<MyColor>();

    public int getColorCnt() {
        return colors.size();
    }

    public MyColor getColorByIndex(int index){
        // 越界返回null
        if(index<0 || index>colors.size())return null;
        return colors.get(index);
    }

    /**
     * 根据标签获取颜色索引，不存在则返回null
     */
    public Integer getColorIndexByTag(String tag) {
        Integer c_index = tagToColor.get(tag);
        if (null == c_index) return null;
        else {
            return new Integer(c_index);
        }
    }

    /**添加新标签，返回新生成的颜色*/
    public Integer addNewTag(String tag){
        MyColor new_color=new MyColor();
        boolean found = false;
        int max_loop_cnt = 20;//最多max_loop_cnt轮检测，如果还没找到用最后一次的,有小概率冲突
        double min_color_aberration = 20;//最小色差
        for (int turn = 0; turn < max_loop_cnt; turn++) {
            new_color = MyRainbowColorGenerator.getRandomColor();
            found=true;
            for (int i = 0; i < this.colors.size(); i++) {
                if (MyColorHandler.colorAberration(new_color, colors.get(i)) < min_color_aberration) {
                    found=false;
                    break;
                }
            }
            if(found)break;
        }
        tagToColor.put(tag,this.colors.size());
        this.colors.add(new_color);
        return new Integer(this.colors.size()-1);
    }

    /**
     * 根据标签获取颜色索引，不存在则新建颜色再返回结果
     */
    public Integer getColorIndexByTag_force(String tag) {
        Integer c_index = tagToColor.get(tag);
        if (null == c_index) {
            return addNewTag(tag);
        }else{
            return new Integer(c_index);
        }
    }
}
