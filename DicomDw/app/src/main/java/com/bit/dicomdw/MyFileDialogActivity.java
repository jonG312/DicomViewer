package com.bit.dicomdw;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

class MyFileDialogActivity extends Activity {
    String DefaultFilePath;
    ArrayList<File> FileList = new ArrayList<File>();
    File FileNow;
    /**更改于20190314*/
    //String Ext;
    String[] Exts;

    public final static int RESULT_CODE = 4;

    protected void RefreshFileList(){
        //将这些文件名加入listview
        this.FileList.clear();
        File[] TempFiles = this.FileNow.listFiles();
        if (TempFiles != null){
            for (int i = 0;i < TempFiles.length;i ++){
                if (TempFiles[i].isDirectory()){
                    insert_file(TempFiles[i]);
//                    this.FileList.add(TempFiles[i]);
                }
                else{
                    /**更改于20190314*/
//                    if (TempFiles[i].getName().endsWith(this.Ext)){
//                        insert_file(TempFiles[i]);
////                        this.FileList.add(TempFiles[i]);
//                    }
                    for(int j=0;j<this.Exts.length;j++){
                        if (TempFiles[i].getName().endsWith(this.Exts[j])){
                            insert_file(TempFiles[i]);
                            break;
                        }
                    }
                }
            }
            FileList.sort(new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    //文件夹在前,文件在后
                    //按名排序,小写字母按照大写算
                    if(o1.isDirectory()==o2.isDirectory()){
                        return o1.getName().compareToIgnoreCase(o2.getName());
                    }else{
                        if(o1.isDirectory()){
                            return -1;
                        }else{
                            return 1;
                        }
                    }
                }
            });
            //赋值给listView
            String[] TempStrArr = new String[this.FileList.size()];
            for (int i = 0;i < TempStrArr.length;i ++){
                TempStrArr[i] = this.FileList.get(i).isDirectory() ? "[" + this.FileList.get(i).getName() + "]" : this.FileList.get(i).getName();
            }
            ListView mListView = (ListView)findViewById(R.id.FileList);
            mListView.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, TempStrArr));
        }
        else{
            Toast.makeText(this,"权限不够！", Toast.LENGTH_LONG).show();
            if (this.FileNow.getParentFile() != null){
                this.FileNow = this.FileNow.getParentFile();
            }
            else{
                this.FileNow = new File(DefaultFilePath);
            }
            this.RefreshFileList();
        }
    }

    protected void insert_file(File file){
        //安卓隐藏文件名开头为".",忽略
        if('.'==file.getName().charAt(0)){
            return;
        }
        this.FileList.add(file);
    }
}
