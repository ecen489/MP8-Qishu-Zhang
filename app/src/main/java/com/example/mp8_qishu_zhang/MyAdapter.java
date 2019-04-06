package com.example.mp8_qishu_zhang;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<String> classList;
    private List<String> gradeList;
    private List<String> studentList;

    public MyAdapter(List<String> courseNames, List<String> grades, List<String> studentNames)
    {
        classList = courseNames;
        gradeList = grades;
        studentList = studentNames;
    }


    public void delete(int position)
    {
        classList.remove(position);
        gradeList.remove(position);
        studentList.remove(position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final String course = classList.get(position);
        final String grade = gradeList.get(position);
        final String name = studentList.get(position);
        holder.Course.setText(name + ", " + course);
        holder.Course.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                delete(position);
            }
        });

        holder.Grade.setText(grade);

    }

    @Override
    public int getItemCount()
    {
        return classList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public View layout;
        public TextView Course;
        public TextView Grade;

        public ViewHolder(View view)
        {
            super(view);
            layout = view;
            Course = view.findViewById(R.id.course_disp);
            Grade = view.findViewById(R.id.grade_disp);
        }
    }
}
