package com.bit.dicomdw;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;

public class OpenFileDialogActivity extends MyFileDialogActivity {
    String FileChosenName;

    public final static int RESULT_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_file_dialog);
        FileChosenName=null;

        //获取参数
        Intent intent = getIntent();
        DefaultFilePath = intent.getStringExtra("DefaultFilePath");
//        DefaultFileName = intent.getStringExtra("DefaultFileName");

        //this.Ext = intent.getStringExtra("Ext");
        String exts=intent.getStringExtra("Ext");
        this.Exts=exts.split(" ");

        this.FileNow = new File(DefaultFilePath);
        this.RefreshFileList();
        //
        EditText EditFileName = (EditText)findViewById(R.id.editFileName);
//        EditFileName.setText(DefaultFileName);
        //设置ListView单击事件
        ListView mListView = (ListView)findViewById(R.id.FileList);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (FileList.get(arg2).isDirectory()){
                    FileNow = FileList.get(arg2);
                    RefreshFileList();
                }
                else{
                    FileChosenName=FileList.get(arg2).getName();
                    choosed();
                }
            }

        });
    }


    public void Return (View srcView){
        if (this.FileNow.getParentFile() != null){
            this.FileNow = this.FileNow.getParentFile();
            this.RefreshFileList();
        }
    }

    public void Cancel(View srcView){
        Intent intent = new Intent();
        intent.putExtra("FilePathName","");
        setResult(OpenFileDialogActivity.RESULT_CODE, intent);
        this.finish();
    }

    private void choosed(){
        Intent intent = new Intent();
        intent.putExtra("FilePathName",this.FileNow.getAbsolutePath() + "/" + FileChosenName);
        setResult(OpenFileDialogActivity.RESULT_CODE, intent);
        this.finish();
    }
}
