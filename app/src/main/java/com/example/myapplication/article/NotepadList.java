package com.example.myapplication.article;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DataBase.MyDBOpenHelper;
import com.example.myapplication.DataBase.Note;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class NotepadList extends Activity {

    private static final int REQUEST_CODE_ADD = 1; // 添加便签请求码
    private static final int RESULT_CODE_SAVE = 2; // 保存便签结果码

    ListView listView; // 显示便签的列表视图
    List<Note> list; // 存储便签数据的列表
    MyDBOpenHelper mSQLiteHelper; // 数据库帮助类，用于数据库操作
    NotepadAdapter adapter; // 适配器，用于将数据绑定到 ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepadlist);
        //setContentView(R.layout.userinfo);

        // 获取 ListView 和添加按钮的引用
        listView = findViewById(R.id.listview);
        ImageView add = findViewById(R.id.add_note);
        ImageView user = findViewById(R.id.note_user);

        // 为添加按钮设置点击事件监听器
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击添加按钮时，启动 NotepadInfo 进行记录添加
                Intent intent = new Intent(NotepadList.this, NotepadInfo.class);
                startActivityForResult(intent, REQUEST_CODE_ADD);
            }
        });

        // 为用户按钮设置点击事件监听器
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击用户按钮时，启动 UserInfo 页面
                Intent intent = new Intent(NotepadList.this, UserInfo.class);
                startActivity(intent);
            }
        });

        // 初始化数据
        initData();
    }

    protected void initData() {
        // 创建数据库帮助类实例
        mSQLiteHelper = new MyDBOpenHelper(this);
        // 查询并显示数据
        showQueryData();

        if (list == null) {
            list = new ArrayList<>();
        }

        // 设置列表项点击事件监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 单击列表项时启动 NotepadInfo 进行记录编辑
                Note notepadBean = list.get(position);
                Intent intent = new Intent(NotepadList.this, NotepadInfo.class);
                intent.putExtra("id", notepadBean.getId());
                intent.putExtra("time", notepadBean.getNotepadTime()); // 记录的时间
                intent.putExtra("content", notepadBean.getNotepadContent()); // 记录的内容
                startActivityForResult(intent, REQUEST_CODE_ADD);
            }
        });

        // 设置列表项长按事件监听器
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                // 长按列表项时弹出删除确认对话框
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(NotepadList.this)
                        .setMessage("是否删除此事件？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 确定删除时从数据库中删除记录，并更新列表
                                Note notepadBean = list.get(position);
                                if (mSQLiteHelper.deleteNoteData(notepadBean.getId())) {
                                    list.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(NotepadList.this, "删除成功", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 取消删除时关闭对话框
                                dialog.dismiss();
                            }
                        });
                dialog = builder.create();
                dialog.show();
                return true;
            }
        });
    }

    private void showQueryData() {
        // 显示进度对话框
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载...");
        progressDialog.show();

        // 在后台线程中查询数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (list != null) {
                    list.clear();
                }
                // 从数据库中查询数据（保存的便签）
                list = mSQLiteHelper.queryNotes();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 如果列表为空，显示空数据提示
                        if (list.isEmpty()) {
                            Toast.makeText(NotepadList.this, "暂无便签", Toast.LENGTH_SHORT).show();
                        }
                        // 创建适配器实例，并将其设置为 ListView 的适配器
                        adapter = new NotepadAdapter(NotepadList.this, list);
                        listView.setAdapter(adapter);
                        progressDialog.dismiss();
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 如果返回的请求码和结果码匹配，则重新查询数据
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_CODE_SAVE) {
            showQueryData();
        }
    }
}


