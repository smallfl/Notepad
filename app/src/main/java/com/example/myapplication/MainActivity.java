package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DataBase.MyDBOpenHelper;
import com.example.myapplication.User.Login;
import com.example.myapplication.User.Register;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //给按钮添加监听事件
        findViewById(R.id.login_button).setOnClickListener(this);
        findViewById(R.id.Register_button).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_button){
            //记得在AndroidManifest.xml中注册Login_Activity.class
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        } else if (v.getId() == R.id.Register_button) {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
        }
    }
}