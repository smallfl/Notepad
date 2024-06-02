package com.example.myapplication.article;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.DataBase.Note;
import com.example.myapplication.R;

import java.util.List;

public class NotepadAdapter extends BaseAdapter {
    // 用于将 XML 布局文件转换为 View 对象
    private LayoutInflater layoutInflater;
    // 数据源，保存 NotepadBean 对象的列表
    private List<Note> list;

    // 构造方法，接收上下文和数据源列表
    public NotepadAdapter(Context context, List<Note> list) {
        // 初始化 LayoutInflater
        this.layoutInflater = LayoutInflater.from(context);
        // 初始化数据源
        this.list = list;
    }

    // 返回数据源的大小
    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    // 返回指定位置的数据项
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    // 返回数据项的 ID，这里使用数据项的位置作为 ID
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 返回指定位置的列表项视图
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        // 如果 convertView 为 null，说明需要创建新的视图
        if (convertView == null) {
            // 将 XML 布局文件转换为 View 对象
            convertView = layoutInflater.inflate(R.layout.article_added, null);
            // 创建 ViewHolder 并初始化
            viewHolder = new ViewHolder(convertView);
            // 将 ViewHolder 设置为标签，方便复用
            convertView.setTag(viewHolder);
        } else {
            // 如果 convertView 不为 null，直接复用之前的 ViewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 获取当前数据项
        Note noteInfo = (Note) getItem(position);
        // 设置数据到视图中的控件
        viewHolder.tvNoteoadContent.setText(noteInfo.getNotepadContent());
        viewHolder.tvNotepadTime.setText(noteInfo.getNotepadTime());

        // 返回列表项视图
        return convertView;
    }

    // ViewHolder 静态内部类，用于持有视图中的子视图
    class ViewHolder {
        TextView tvNoteoadContent;
        TextView tvNotepadTime;

        // 构造方法，通过 findViewById 找到控件
        public ViewHolder(View view) {
            tvNoteoadContent = view.findViewById(R.id.item_content);
            tvNotepadTime = view.findViewById(R.id.item_time);
        }
    }
}

