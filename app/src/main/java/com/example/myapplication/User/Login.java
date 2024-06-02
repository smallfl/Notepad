package com.example.myapplication.User;


import android.content.Intent;
import android.database.Cursor;

import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DataBase.CreateSalt;
import com.example.myapplication.DataBase.DBUtils;
import com.example.myapplication.DataBase.MyDBOpenHelper;
import com.example.myapplication.R;
import com.example.myapplication.article.NotepadList;


public class Login extends AppCompatActivity {
    // 登录页面的界面元素
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // 初始化界面元素
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.login_button);

        // 设置按钮的点击监听器
        buttonLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        // 获取用户输入
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // 检查输入合法性（可选）
        if (username.isEmpty()) {
            editTextUsername.setError("用户名不能为空");
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("密码不能为空");
            return;
        }

        // 执行登录逻辑
        loginUser(username, password);
    }

    private void loginUser(String username, String password) {
        MyDBOpenHelper dbHelper = new MyDBOpenHelper(Login.this);
        try (Cursor cursor = dbHelper.getUser(username)) {
            if (cursor != null && cursor.moveToFirst()) {
                int saltIndex = cursor.getColumnIndex(DBUtils.SALT);
                int passwordIndex = cursor.getColumnIndex(DBUtils.PASSWORD);
                if (saltIndex >= 0 && passwordIndex >= 0) {
                    String storedSalt = cursor.getString(saltIndex);
                    String storedHashedPassword = cursor.getString(passwordIndex);

                    // 假设 CreateSalt 是您的自定义类，负责生成和验证密码哈希
                    CreateSalt createSalt = new CreateSalt();
                    String hashedPassword = createSalt.hashPassword(password, storedSalt);

                    if (storedHashedPassword.equals(hashedPassword)) {
                        Toast.makeText(Login.this, "登录成功", Toast.LENGTH_SHORT).show();
                        //在这里添加登录成功后的代码，例如跳转到另一个Activity
                        Intent intent = new Intent(Login.this, NotepadList.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Login.this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "数据库错误", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Login.this, "用户名不存在", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "登录时发生错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

