package com.mslji.mybluetooth.Popup;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mslji.mybluetooth.Database.SessionManager;
import com.mslji.mybluetooth.DeviceScanActivity;
import com.mslji.mybluetooth.R;
import com.mslji.mybluetooth.SendActivity;
import com.mslji.mybluetooth.first.FirstPageActivity;

public class firstlogo {
    public Dialog epicDialog;
    private final Context _context;
    public EditText etpass, etid;
    public TextView btnsubmit;
    public String Studentid;
    public String Mssage, Tutorid;



    SessionManager sessionManager;
    public firstlogo(Context context) {
        this._context = context;

        epicDialog = new Dialog(context); // for popup Dialog
        sessionManager = new SessionManager(context);
    }


    public void addpopup() {
        epicDialog.setContentView(R.layout.messageloginlayout);



        etpass = (EditText) epicDialog.findViewById(R.id.passwordet);
        etid = (EditText) epicDialog.findViewById(R.id.idet);

        btnsubmit = (TextView) epicDialog.findViewById(R.id.sendbtn);


//        btnsubmit.setOnClickListener(View.OnClickListener);

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("logindata", "etmsg 11 = =" + etid.getText().toString());
                if (etid.getText().length() == 0) {
                    Log.d("logindata", "data check = 0");
                    Toast.makeText(_context, "Enter your ID", Toast.LENGTH_SHORT).show();

                } else
                if (etpass.getText().length() == 0) {
                    Log.d("logindata", "data check = 0");
                    Toast.makeText(_context, "Enter your Password", Toast.LENGTH_SHORT).show();

                } else if (etpass.getText().toString().equalsIgnoreCase("cerebo")) {
                    Log.d("logindata", "data check = 1");
                    Log.d("logindata", "etmsg = =" + etid.getText().toString());
                    sessionManager.setAppId(etid.getText().toString());
                    Intent intent = new Intent(_context, FirstPageActivity.class);
                    _context.startActivity(intent);
                }


            }
        });

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // popup background transparent
        epicDialog.show();

    }
}
