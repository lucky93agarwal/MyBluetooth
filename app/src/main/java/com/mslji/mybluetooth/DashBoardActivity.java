package com.mslji.mybluetooth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.mslji.mybluetooth.Adapter.HomeRandomTwoAdapter;
import com.mslji.mybluetooth.Database.FirstTableData;
import com.mslji.mybluetooth.Database.MyDbHandler;
import com.mslji.mybluetooth.Database.SessionManager;
import com.mslji.mybluetooth.Database.Temp;
import com.mslji.mybluetooth.Model.ProductModeltwo;
import com.mslji.mybluetooth.R;
import com.mslji.mybluetooth.databinding.ActivityIDBinding;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class DashBoardActivity extends AppCompatActivity {
    public MyDbHandler myDbHandler;



    public List<ProductModeltwo> productModelsthree = new ArrayList<>();
    public ProductModeltwo getproductmodelthree;

    public RecyclerView.LayoutManager mLayoutManagersecthree;
    public HomeRandomTwoAdapter adaptersecthree;

    SessionManager sessionManager;

    ActivityIDBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIDBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sessionManager = new SessionManager(this);






        binding.progress.setVisibility(View.VISIBLE);










        JSONAsyncTask getRequest = new JSONAsyncTask();
        getRequest.execute("");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("Reportdataknoe","Handler data =    "+String.valueOf(datanew));
                if(datanew.equalsIgnoreCase("0")){
                    Log.i("Reportdataknoe","Handler data if =    "+String.valueOf(datanew));


                    binding.progress.setVisibility(View.GONE);
                    binding.nodataiv.setVisibility(View.VISIBLE);
                }


            }
        }, 6000);
    }
    public String datanew = "";
    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet httppost = new HttpGet("https://ggeova9x0d.execute-api.us-east-2.amazonaws.com/live/form?DeviceID="+sessionManager.getAppId());
                Log.i("dashboardnewdata","url =    "+"https://ggeova9x0d.execute-api.us-east-2.amazonaws.com/live/form?DeviceID="+sessionManager.getAppId());
//                HttpGet httppost = new HttpGet("https://ggeova9x0d.execute-api.us-east-2.amazonaws.com/live/form?DeviceID=CP002");
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();
                Log.i("dashboardnewdata","status =    "+String.valueOf(status));
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    Log.i("dashboardnewdata","data =    "+String.valueOf(data));
                    if (productModelsthree.size() > 0) {
                        productModelsthree.clear();
                    }
                    JSONArray jsonArray = new JSONArray(data);
                    if (jsonArray.length() ==0){


                        datanew = "0";
                    }
                    Log.i("dashboardnewdata","jsonArray =    "+String.valueOf(jsonArray));
                    for (int i=0;i<jsonArray.length();i++) {
                        Log.i("dashboardnewdata","DataUpload =    "+jsonArray.getJSONObject(i).getString("DataUpload"));
                        getproductmodelthree = new ProductModeltwo();
                        getproductmodelthree.setDate(jsonArray.getJSONObject(i).getString("DataUpload"));
                        getproductmodelthree.setId(jsonArray.getJSONObject(i).getString("PatientID"));
                        getproductmodelthree.setNumbers(jsonArray.getJSONObject(i).getString("Result"));
                        getproductmodelthree.setDataTaken(jsonArray.getJSONObject(i).getString("DataTaken"));

                        productModelsthree.add(getproductmodelthree);
                    }

//                    JSONObject jsono = new JSONObject(data);
                    Log.i("datahhhhgffcffccf","Result =    "+data.toString());
                    return true;
                }


            } catch (IOException e) {
                e.printStackTrace();
                Log.i("dashboardnewdata","IOException =    "+e.getMessage().toString());
            }
            catch (JSONException e) {

                e.printStackTrace();
                Log.i("dashboardnewdata","JSONException =    "+e.getMessage().toString());
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            binding.progress.setVisibility(View.GONE);

            Log.i("dashboardnewdata","DataUpload =    "+result.toString());
            adaptersecthree = new HomeRandomTwoAdapter(DashBoardActivity.this, productModelsthree);
            final GridLayoutManager layoutManager = new GridLayoutManager(DashBoardActivity.this, 3);
            mLayoutManagersecthree = new LinearLayoutManager(DashBoardActivity.this, LinearLayoutManager.VERTICAL, false);
            binding.recyclerViewMyteamTwo.setLayoutManager(layoutManager);
            binding.recyclerViewMyteamTwo.setAdapter(adaptersecthree);
            adaptersecthree.notifyDataSetChanged();
        }
    }
}