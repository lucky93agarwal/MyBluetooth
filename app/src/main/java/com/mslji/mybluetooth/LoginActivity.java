package com.mslji.mybluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    public Button btnSubmit;
    public EditText etpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);


        etpassword = (EditText)findViewById(R.id.etaddres);
        btnSubmit = (Button)findViewById(R.id.btnsub);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etpassword.getText().length()==0){
                    Toast.makeText(LoginActivity.this, "Please Enter your device address", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(LoginActivity.this, SendActivity.class);
                    intent.putExtra(SendActivity.EXTRAS_DEVICE_NAME, "Bio");
                    intent.putExtra(SendActivity.EXTRAS_DEVICE_ADDRESS, etpassword.getText().toString().trim());
                    startActivity(intent);
                    finish();
                }

            }
        });

    }
}