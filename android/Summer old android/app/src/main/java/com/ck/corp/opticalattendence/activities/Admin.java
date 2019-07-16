package com.ck.corp.opticalattendence.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ck.corp.opticalattendence.R;
import com.ck.corp.opticalattendence.Utils.Config;
import com.ck.corp.opticalattendence.Utils.PrefrenceManager;
import com.ck.corp.opticalattendence.adapter.RecordsAdapter;
import com.ck.corp.opticalattendence.models.Records;

import java.util.ArrayList;
import java.util.List;

public class Admin extends Activity {

    private RecyclerView recordView;
    private RecordsAdapter recordsAdapter;

    private List<Records> data = new ArrayList<>();

    private String IP_Address = "192.168.1.102";

    private String TAG = "Record_Server";

    private Button getStudentRecords;
    private Button getStudentAttendenceRecords;
    private EditText dateOfRecord;

    private Button addNewStudent;
    private Button removeStudent;
    private Button updateStudent;

    private Button dataProcessingBtn;
    private LinearLayout nameLayout;
    private LinearLayout rollnoLayout;

    private EditText studentNameEditTxt;
    private EditText studentRollnoEditTxt;

    private boolean modeAdd = false;
    private boolean modeRemove = false;
    private boolean modeUpdate = false;

    private int modeOfWork = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_admin_view);
        String s = PrefrenceManager.getString(getApplicationContext(), Config.KEY_IP_ADDRESS);
        if (s != null) {
            IP_Address = s;
        }
        initView();
    }

    private void initView() {
        recordView = this.findViewById(R.id.record_data_display);
        getStudentRecords = this.findViewById(R.id.admin_get_student_records);
        getStudentAttendenceRecords = this.findViewById(R.id.admin_get_student_attendence_records);
        dateOfRecord = this.findViewById(R.id.admin_date_of_record);
        addNewStudent = this.findViewById(R.id.add_student_record);
        removeStudent = this.findViewById(R.id.remove_student_record);
        updateStudent = this.findViewById(R.id.update_student_record);
        dataProcessingBtn = this.findViewById(R.id.data_processing_button);

        studentNameEditTxt = this.findViewById(R.id.name_of_student);
        studentRollnoEditTxt = this.findViewById(R.id.rollno_of_student);

        nameLayout = this.findViewById(R.id.layout_name);
        rollnoLayout = this.findViewById(R.id.layout_rollno);

        refreshRecyclerView();
        onClick();
    }


    private void onClick() {
        getStudentRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStudentRecord();
            }
        });
        getStudentAttendenceRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = "2019_06_24";
                String s = dateOfRecord.getText().toString();
                if (s.length() == 10) {
                    date = s;
                }
                getStudentAttendenceRecord(date);
            }
        });

        addNewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!modeAdd) {
                    miniDialog(1);
                } else {
                    miniDialog(0);
                }
            }
        });
        removeStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!modeRemove) {
                    miniDialog(2);
                } else {
                    miniDialog(0);
                }
            }
        });
        updateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!modeUpdate) {
                    miniDialog(3);
                } else {
                    miniDialog(0);
                }
            }
        });

        dataProcessingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                String rollno;
                switch (modeOfWork) {
                    case 1:
                        name = studentNameEditTxt.getText().toString();
                        rollno = studentRollnoEditTxt.getText().toString();
                        addStudentRecord(name, rollno);
                        getStudentRecord();
                        break;
                    case 2:
                        rollno = studentRollnoEditTxt.getText().toString();
                        removeStudentRecord(rollno);
                        getStudentRecord();
                        break;
                    case 3:
                        name = studentNameEditTxt.getText().toString();
                        rollno = studentRollnoEditTxt.getText().toString();
                        updateStudentRecord(name, rollno);
                        getStudentRecord();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void miniDialog(int mode) {
        modeOfWork = mode;
        modeAdd = false;
        modeRemove = false;
        modeUpdate = false;
        dataProcessingBtn.setVisibility(View.VISIBLE);
        rollnoLayout.setVisibility(View.VISIBLE);
        switch (mode) {
            case 1:
                // add
                nameLayout.setVisibility(View.VISIBLE);
                dataProcessingBtn.setText("ADD");
                modeAdd = true;
                break;
            case 2:
                // remove
                nameLayout.setVisibility(View.GONE);
                dataProcessingBtn.setText("Remove");
                modeRemove = true;
                break;
            case 3:
                // update
                nameLayout.setVisibility(View.VISIBLE);
                dataProcessingBtn.setText("Modify");
                modeUpdate = true;
                break;
            default:
                nameLayout.setVisibility(View.GONE);
                rollnoLayout.setVisibility(View.GONE);
                dataProcessingBtn.setVisibility(View.GONE);
                break;
        }
    }

    private void refreshRecyclerView() {
        recordsAdapter = new RecordsAdapter(getApplicationContext(), data);
        recordView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recordView.setItemAnimator(new DefaultItemAnimator());
        recordView.setAdapter(recordsAdapter);
    }

    private void getStudentRecord() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://" + IP_Address + ":8000/get_student_record";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.trim();
                Log.e(TAG, "Response ." + response + ".");
                data = converterDataParser(response, 2);
                refreshRecyclerView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error " + error);
            }
        });
        queue.add(stringRequest);
    }

    private void getStudentAttendenceRecord(String date) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://" + IP_Address + ":8000/get_attendence_record/" + date;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.trim();
                Log.e(TAG, "Response ." + response + ".");
                data = converterDataParser(response, 3);
                refreshRecyclerView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error " + error);
            }
        });
        queue.add(stringRequest);
    }

    private List<Records> converterDataParser(String response, int entry) {
        response = response.replace("[","");
        response = response.replace("]","");
        response = response.replace("\"","");
        String[] entries = response.split(",");
        int count = 0;
        List<Records> records = new ArrayList<>();
        while (count < entries.length) {
            if (entry == 2) {
                records.add(new Records(entries[count], entries[count + 1]));
            } else {
                records.add(new Records(entries[count], entries[count + 1], entries[count + 2]));
            }
            count += entry;
        }
        return records;
    }

    private void addStudentRecord(String name,String rollno) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://" + IP_Address + ":8000/add_student_record/" + name + "_" + rollno;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.trim();
                Log.e(TAG, "Response ." + response + ".");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error " + error);
            }
        });
        queue.add(stringRequest);
    }

    private void removeStudentRecord(String rollno) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://" + IP_Address + ":8000/delete_student_record/" + rollno;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.trim();
                Log.e(TAG, "Response ." + response + ".");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error " + error);
            }
        });
        queue.add(stringRequest);
    }

    private void updateStudentRecord(String name,String rollno) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://" + IP_Address + ":8000/update_student_record/" + name + "_" + rollno;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.trim();
                Log.e(TAG, "Response ." + response + ".");
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
