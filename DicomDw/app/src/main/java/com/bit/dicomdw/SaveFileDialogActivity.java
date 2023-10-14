package com.bit.dicomdw;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;


public class SaveFileDialogActivity extends com.bit.dicomdw.MyFileDialogActivity {

    String DefaultFileName;

    public final static int RESULT_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_file_dialog);

        //获取参数
        Intent intent = getIntent();
        DefaultFilePath = intent.getStringExtra("DefaultFilePath");
        DefaultFileName = intent.getStringExtra("DefaultFileName");
        //this.Ext = intent.getStringExtra("Ext");
        String exts=intent.getStringExtra("Ext");
        this.Exts=exts.split(" ");

        //Toast.makeText(this,DefaultFilePath + "," + DefaultFileName, Toast.LENGTH_LONG).show();
        //Toast.makeText(this,(new File(DefaultFilePath)).toString(), Toast.LENGTH_LONG).show();
        //

        this.FileNow = new File(DefaultFilePath);
        this.RefreshFileList();
        //
        EditText EditFileName = (EditText)findViewById(R.id.editFileName);
        EditFileName.setText(DefaultFileName);
        //设置ListView单击事件
        ListView mListView = (ListView)findViewById(R.id.FileList);
        mListView.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (FileList.get(arg2).isDirectory()){
                    FileNow = FileList.get(arg2);
                    RefreshFileList();
                }
                else{
                    EditText EditFileName = (EditText)findViewById(R.id.editFileName);
                    EditFileName.setText(FileList.get(arg2).getName());
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
        this.finish();
    }

    public void Enter (View srcView){
        Intent intent = new Intent();
        EditText EditFileName = (EditText)findViewById(R.id.editFileName);
        intent.putExtra("FilePathName",this.FileNow.getAbsolutePath() + "/" + EditFileName.getText());
        setResult(SaveFileDialogActivity.RESULT_CODE, intent);
        this.finish();
    }
}

