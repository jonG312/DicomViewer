package com.bit.dicomdw.menu_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bit.dicomdw.R;

public class ScaleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale);
        Button sure=findViewById(R.id.scale_sure);
        //这个intent用来传递数据

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("qaqa","ddd");
                Intent intent=new Intent();
                EditText editText=(EditText)findViewById(R.id.scale);
                Log.d("qaqa","1");
                String str_scale=editText.getText().toString();
                Log.d("qaqa","2");
                intent.putExtra("scale",str_scale);
                Log.d("qaqa","3");
                setResult(RESULT_OK,intent);
                finish();

            }
        });

        Button cancel=findViewById(R.id.scale_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
