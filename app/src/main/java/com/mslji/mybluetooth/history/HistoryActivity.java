package com.mslji.mybluetooth.history;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mslji.mybluetooth.Adapter.HistoryAdapter;
import com.mslji.mybluetooth.Database.RefUrl;
import com.mslji.mybluetooth.Database.SessionManager;
import com.mslji.mybluetooth.Model.GetHistoryData;
import com.mslji.mybluetooth.R;
import com.mslji.mybluetooth.SendNewDataActivity;
import com.mslji.mybluetooth.databinding.ActivityHistoryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    public ArrayList<GetHistoryData> productList = new ArrayList<>();
    public GetHistoryData request;

    public HistoryAdapter adapter;
    SessionManager sessionManager;
    public RecyclerView.LayoutManager mLayoutManager;

    ActivityHistoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        sessionManager = new SessionManager(this);

        onClick();


        API();



    }

    public void onClick(){
        binding.backiv.setOnClickListener(v -> {
            onBackPressed();
        });
    }



    private void API(){
        HttpGetRequest getRequest = new HttpGetRequest();
        getRequest.execute("");
    }

    public class HttpGetRequest extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;
        String inputLine;
        @Override
        protected String doInBackground(String... params){
//            String stringUrl = "https://53qngtooj6.execute-api.us-east-2.amazonaws.com/configuration/config-history?DeviceID="+sessionManager.getAppId();
            String stringUrl = "https://53qngtooj6.execute-api.us-east-2.amazonaws.com/configuration/config-history?DeviceID=CP002";
            String result;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url

                connection.connect();
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                result = stringBuilder.toString();
            }   catch(IOException e){
                e.printStackTrace();
                result = null;
            }
            return result;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Log.i("checkdatanew","result =  "+result);

            if(!result.isEmpty()) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    if (productList.size() > 0) {
                        productList.clear();
                    }
                    if(jsonArray.length() >0){
                        binding.nodataiv.setVisibility(View.GONE);
                        binding.recyclerview.setVisibility(View.VISIBLE);
                    }else {
                        binding.nodataiv.setVisibility(View.VISIBLE);
                        binding.recyclerview.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        request = new GetHistoryData();
                        request.setNewtime(jsonArray.getJSONObject(i).getString("id"));
                        request.setOnetv(jsonArray.getJSONObject(i).getString("TH_1"));
                        request.setTwotv(jsonArray.getJSONObject(i).getString("TH_2"));
                        request.setThreetv(jsonArray.getJSONObject(i).getString("TH_3"));
                        request.setId(sessionManager.getAppId());
                        productList.add(request);
                    }
                    adapter = new HistoryAdapter(HistoryActivity.this, productList);
                    binding.recyclerview.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
                    binding.recyclerview.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(HistoryActivity.this, "data not found", Toast.LENGTH_SHORT).show();
            }

        }
    }


}