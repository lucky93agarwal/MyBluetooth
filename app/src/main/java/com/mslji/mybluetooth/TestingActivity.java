package com.mslji.mybluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.mslji.mybluetooth.Database.MyDbHandler;
import com.mslji.mybluetooth.Database.RefUrl;
import com.mslji.mybluetooth.Database.Temp;
import com.mslji.mybluetooth.Model.GetAllData;
import android.text.method.ScrollingMovementMethod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TestingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_form);
//        HttpGetRequest getRequest = new HttpGetRequest();
//        getRequest.execute("");
    }


//    public class HttpGetRequest extends AsyncTask<String, Void, String> {
//        public static final String REQUEST_METHOD = "POST";
//        public static final int READ_TIMEOUT = 15000;
//        public static final int CONNECTION_TIMEOUT = 15000;
//        @Override
//        protected String doInBackground(String... params){
//            String stringUrl = RefUrl.BaseUrl;
//            String result;
//            String inputLine="Lucky agarwal 1";
//
//            try {
//                //Create a URL object holding our url
//                URL myUrl = new URL(stringUrl);
//                //Create a connection
//                HttpURLConnection connection =(HttpURLConnection)
//                        myUrl.openConnection();
//                //Set methods and timeouts
//                connection.setRequestMethod(REQUEST_METHOD);
//                connection.setReadTimeout(READ_TIMEOUT);
//                connection.setConnectTimeout(CONNECTION_TIMEOUT);
//
//
////                String Scan_date=mIncomingText.getText().toString();
//                String Scan_date= "Lucky Agarwal 3";
//
//
//                OutputStream outputStream = connection.getOutputStream();
//                BufferedWriter bufferedReader = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
//                bufferedReader.write(Scan_date);
//                bufferedReader.flush();
//                bufferedReader.close();
//                outputStream.close();
//
//
//
//                //Connect to our url
//                connection.connect();
//                //Create a new InputStreamReader
//                InputStreamReader streamReader = new
//                        InputStreamReader(connection.getInputStream());
//                //Create a new buffered reader and String Builder
//                BufferedReader reader = new BufferedReader(streamReader);
//                StringBuilder stringBuilder = new StringBuilder();
//                //Check if the line we are reading is not null
//                while((inputLine = reader.readLine()) != null){
//                    stringBuilder.append(inputLine);
//                }
//                //Close our InputStream and Buffered reader
//                reader.close();
//                streamReader.close();
//                //Set our result equal to our stringBuilder
//                result = stringBuilder.toString();
//            }
//            catch(IOException e){
//                Log.d("datahhhhgffcffccf","Error   =    "+e.getMessage());
//                e.printStackTrace();
//                result = null;
//            }
//            return result;
//        }
//        protected void onPostExecute(String result){
//            super.onPostExecute(result);
//                   Log.d("datahhhhgffcffccf","Result =    "+result);
//
//        }
//    }
}