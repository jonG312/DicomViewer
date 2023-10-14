package com.bit.dicomdw.menu_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bit.dicomdw.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_activity);
        String info_name,info_sex,info_date,info_type,info_area,info_age;
        TextView editText_name,editText_sex,editText_date,editText_type,editText_area,editText_age;
        editText_name=(TextView) findViewById(R.id.info_name);
        editText_age=(TextView)findViewById(R.id.info_age);
        editText_area=(TextView)findViewById(R.id.info_check_area);
        editText_date=(TextView)findViewById(R.id.info_check_time);
        editText_sex=(TextView)findViewById(R.id.info_sex);
        editText_type=(TextView)findViewById(R.id.info_check_type);

        Intent intent=getIntent();
        info_name=intent.getStringExtra("info_name");
        info_sex=intent.getStringExtra("info_sex");
        Log.d("sex",info_sex);
        info_age=intent.getStringExtra("info_age");
        info_area=intent.getStringExtra("info_area");
        info_date=intent.getStringExtra("info_date");
        info_type=intent.getStringExtra("info_type");

        editText_name.setText("患者姓名: "+info_name);
        editText_age.setText("患者年龄: "+info_age);
        editText_area.setText("检查部位: "+info_area);
        editText_date.setText("检查日期: "+info_date);
        editText_type.setText("检查类型: "+info_type);
        editText_sex.setText("患者性别: "+info_sex);

        Button back=findViewById(R.id.info_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
