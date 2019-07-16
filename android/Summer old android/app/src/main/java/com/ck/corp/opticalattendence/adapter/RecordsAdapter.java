package com.ck.corp.opticalattendence.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ck.corp.opticalattendence.R;
import com.ck.corp.opticalattendence.models.Records;

import java.util.List;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.MyViewHolder> {

    private List<Records> data;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView studentName, studentRollno, studentAttend;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.item_student_name);
            studentRollno = itemView.findViewById(R.id.item_student_rollno);
            studentAttend = itemView.findViewById(R.id.item_student_attend);
        }
    }


    public RecordsAdapter(Context c,List<Records> data) {
        this.context = c;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_record_display, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Records records = data.get(i);
        myViewHolder.studentName.setText(records.getName());
        myViewHolder.studentRollno.setText(records.getRollno());
        String a = records.getAttend();
        if (a == null) {
            myViewHolder.studentAttend.setVisibility(View.GONE);
        } else {
            myViewHolder.studentAttend.setVisibility(View.VISIBLE);
            if (a.equals("1")) {
                myViewHolder.studentAttend.setText("P");
                myViewHolder.studentAttend.setBackgroundTintList(context.getResources().getColorStateList(R.color.color_present));
            } else {
                myViewHolder.studentAttend.setText("A");
                myViewHolder.studentAttend.setBackgroundTintList(context.getResources().getColorStateList(R.color.color_absent));
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
