package com.example.myapplication.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DataBase.CreateSalt;
import com.example.myapplication.DataBase.MyDBOpenHelper;
import com.example.myapplication.R;

public class Register extends AppCompatActivity {
    //注册页面
    private EditText RegisterUsername;
    private EditText RegisterPassword;
    private EditText RegisterConfirmPassword;
    private Button buttonRegister;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        RegisterUsername = findViewById(R.id.editTextRegisterUsername);
        RegisterPassword = findViewById(R.id.editTextRegisterPassword);
        RegisterConfirmPassword = findViewById(R.id.editTextRegisterConfirmPassword);
        buttonRegister = findViewById(R.id.button_Register);

        //登录注册事件监听处理
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = RegisterUsername.getText().toString().trim();
                String password = RegisterPassword.getText().toString().trim();
                String confirmPassword = RegisterConfirmPassword.getText().toString().trim();

                CreateSalt createSalt = new CreateSalt();
                //生成一个随机盐值
                String salt = createSalt.generateSalt();
                // 将密码和盐值进行哈希
                String hashedPassword = createSalt.hashPassword(password, salt);

                // 检查两次输入的密码是否一致
                if (password.equals(confirmPassword)) {
                    // 进行注册操作，例如发送网络请求
                    MyDBOpenHelper dbHelper = new MyDBOpenHelper(Register.this);
                    boolean isRegistered = dbHelper.registerUser(username, hashedPassword, salt);

                    if (isRegistered) {
                        Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                        // 跳转到登录页面
                        Intent intent = new Intent(Register.this, Login.class);
                        startActivity(intent);
                        finish(); // 结束当前活动，防止用户返回注册页面
                    } else {
                        Toast.makeText(Register.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Register.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
