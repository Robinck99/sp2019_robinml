package com.ck.corp.opticalattendence.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ck.corp.opticalattendence.R;
import com.ck.corp.opticalattendence.Utils.Config;
import com.ck.corp.opticalattendence.Utils.PrefrenceManager;

public class Student extends Activity {

    private Button changeStudentData;
    private Button attendenceStaus;
    private EditText studentName;
    private EditText studentRollno;

    private String sName = "nun";
    private String sRollno = "1170..";

    private String IP_Address = "192.168.1.102";

    private String TAG = "Record_Server";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_student_view);

        initVew();
    }

    private void initVew() {
        studentName       = this.findViewById(R.id.name_of_student);
        studentRollno     = this.findViewById(R.id.rollno_of_student);
        changeStudentData = this.findViewById(R.id.student_data_save_button);
        attendenceStaus   = this.findViewById(R.id.attendence_status_student);

        String s = PrefrenceManager.getString(getApplicationContext(), Config.KEY_IP_ADDRESS);
        if (s != null) {
            IP_Address = s;
        }

        String name = PrefrenceManager.getString(getApplicationContext(), Config.KEY_STUDENT_NAME);
        if (name != null) { sName = name; }
        studentName.setHint(sName);
        String rollno = PrefrenceManager.getString(getApplicationContext(), Config.KEY_STUDENT_ROLLNO);
        if (rollno != null) { sRollno = rollno; }
        studentRollno.setHint(sRollno);

        getAttendenceStatus();

        onClick();
    }

    private void onClick() {
        changeStudentData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sName = studentName.getText().toString();
                sRollno = studentRollno.getText().toString();
                PrefrenceManager.setString(getApplicationContext(), Config.KEY_STUDENT_NAME, sName);
                PrefrenceManager.setString(getApplicationContext(), Config.KEY_STUDENT_ROLLNO, sRollno);
                studentName.setHint(sName);
                studentRollno.setHint(sRollno);
            }
        });

        attendenceStaus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAttendenceStatus();
            }
        });
    }

    private void getAttendenceStatus() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://" + IP_Address + ":8000/get_attendence_student_record/" + sRollno;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response ." + response + ".");
                response = response.trim();
                if (response.equals("\"1\"")) {
                    attendenceStaus.setText("P");
                    attendenceStaus.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.color_present));
                } else {
                    attendenceStaus.setText("A");
                    attendenceStaus.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.color_absent));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error " + error);
            }
        });
        queue.add(stringRequest);
    }


}
