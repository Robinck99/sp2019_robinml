package com.ck.corp.opticalattendence.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ck.corp.opticalattendence.R;
import com.ck.corp.opticalattendence.Utils.Config;
import com.ck.corp.opticalattendence.Utils.PrefrenceManager;

public class LoginType extends Activity {

    private Button adminLogin;
    private Button studentLogin;
    private Button ipSaveBtn;
    private EditText ipAddressView;


    private String IP_Address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_type);

        initView();
    }

    private void initView() {
        adminLogin   = this.findViewById(R.id.admin_button);
        studentLogin = this.findViewById(R.id.student_button);
        ipAddressView = this.findViewById(R.id.ip_address);
        ipSaveBtn = this.findViewById(R.id.ip_save_button);

        String s = PrefrenceManager.getString(getApplicationContext(), Config.KEY_IP_ADDRESS);
        if (s != null) {
            IP_Address = s;
            ipAddressView.setHint(IP_Address);
        }
        onClick();
    }

    private void onClick() {
        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Admin.class));
            }
        });

        studentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Student.class));
            }
        });

        ipSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IP_Address = ipAddressView.getText().toString();
                PrefrenceManager.setString(getApplicationContext(), Config.KEY_IP_ADDRESS, IP_Address);
            }
        });
    }
}
