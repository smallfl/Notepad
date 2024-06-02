package com.example.myapplication.article;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myapplication.DataBase.DBUtils;
import com.example.myapplication.DataBase.MyDBOpenHelper;
import com.example.myapplication.R;

public class NotepadInfo extends Activity implements View.OnClickListener {
    ImageView note_back; // 返回按钮
    TextView note_time; // 显示便签时间的 TextView
    EditText content; // 输入便签内容的 EditText
    ImageView delete; // 删除按钮
    ImageView note_save; // 保存按钮
    MyDBOpenHelper mSQLiteHelper; // 数据库帮助类实例
    TextView noteName; // 显示便签名称的 TextView
    String id; // 当前便签的 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepadinfo);

        // 获取界面上的控件引用
        note_back = findViewById(R.id.note_back);
        note_time = findViewById(R.id.tv_time);
        content = findViewById(R.id.note_content);
        delete = findViewById(R.id.delete);
        note_save = findViewById(R.id.note_save);
        noteName = findViewById(R.id.note_name);

        // 设置点击事件监听器
        note_back.setOnClickListener(this);
        delete.setOnClickListener(this);
        note_save.setOnClickListener(this);

        // 初始化数据
        initData();
    }

    protected void initData() {
        // 创建数据库帮助类实例
        mSQLiteHelper = new MyDBOpenHelper(this);

        // 设置默认标题为“添加记录”
        noteName.setText("添加记录");

        // 获取传递过来的 Intent
        Intent intent = getIntent();
        if (intent != null) {
            // 获取便签 ID
            id = intent.getStringExtra("id");
            if (id != null) {
                // 如果 ID 不为空，说明是修改记录
                noteName.setText("修改记录");
                content.setText(intent.getStringExtra("content"));
                note_time.setText(intent.getStringExtra("time"));
                note_time.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.note_back) {
            // 返回按钮点击事件，关闭当前 Activity
            finish();
        } else if (id == R.id.delete) {
            // 删除按钮点击事件，清空便签内容
            content.setText("");
        } else if (id == R.id.note_save) {
            // 保存按钮点击事件
            String noteContent = content.getText().toString().trim();
            if (this.id != null) {
                // 修改操作
                if (noteContent.length() > 0) {
                    if (mSQLiteHelper.updateNoteData(this.id, noteContent, DBUtils.getTime())) {
                        showToast("修改成功");
                        setResult(2);
                        finish();
                    } else {
                        showToast("修改失败");
                    }
                } else {
                    showToast("修改内容不能为空!");
                }
            } else {
                // 添加操作
                if (noteContent.length() > 0) {
                    if (mSQLiteHelper.insertNoteData(noteContent, DBUtils.getTime())) {
                        showToast("保存成功");
                        setResult(2);
                        finish();
                    } else {
                        showToast("保存失败");
                    }
                } else {
                    showToast("内容不能为空!");
                }
            }
        }
    }


    // 显示 Toast 提示信息
    public void showToast(String message) {
        Toast.makeText(NotepadInfo.this, message, Toast.LENGTH_SHORT).show();
    }
}



