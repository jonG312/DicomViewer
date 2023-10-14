package com.bit.dicomdw;

import android.os.Environment;
import android.util.Log;

public class PathParser {
    public static final String pathParse(String raw_path){
        String ans="";
        Log.d("before",raw_path);
        String SDPATH = Environment.getExternalStorageDirectory().getPath() + "/";
        String path=raw_path.substring(raw_path.lastIndexOf(":")+1);
        if(SDPATH.length()<=path.length() && SDPATH.equals(path.substring(0,SDPATH.length()))){
            ans=path;
        }else{
            ans=SDPATH+path;
        }
        Log.d("path",ans);
        return ans;
    }
}
