package com.bit.dicomdw.menu_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bit.dicomdw.R;

public class TuneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tune);
        Button sure=findViewById(R.id.tune_sure);
        //这个intent用来传递数据

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                EditText editText_one=(EditText)findViewById(R.id.tune_center);
                EditText editText_two=(EditText)findViewById(R.id.tune_radius);

                String str_center=editText_one.getText().toString();
                String str_radius=editText_two.getText().toString();

                intent.putExtra("center",str_center);
                intent.putExtra("radius",str_radius);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        Button cancel=findViewById(R.id.tune_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
