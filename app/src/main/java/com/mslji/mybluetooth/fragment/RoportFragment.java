package com.mslji.mybluetooth.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mslji.mybluetooth.Database.SessionManager;
import com.mslji.mybluetooth.R;
import com.mslji.mybluetooth.databinding.FragmentRoportBinding;
import com.mslji.mybluetooth.zoom.ZoomImageActivity;

import java.io.IOException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;


public class RoportFragment extends Fragment {


    public RoportFragment() {
        // Required empty public constructor
    }


    FragmentRoportBinding binding;
    public String data="";
    public SessionManager sessionManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentRoportBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getActivity());
        runAPI();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("Reportdataknoe","Handler data =    "+String.valueOf(data));
                if(data.equalsIgnoreCase("0")){
                    Log.i("Reportdataknoe","Handler data if =    "+String.valueOf(data));


                    binding.oneimageiv.setVisibility(View.GONE);
                    binding.nodataiv.setVisibility(View.VISIBLE);
                    binding.shareiv.setVisibility(View.GONE);
                }else {
                    Log.i("Reportdataknoe","Handler data else =    "+String.valueOf(data));
                    binding.imagell.setVisibility(View.VISIBLE);
                    Glide.with(getActivity())
                            .load(data)
                            .centerCrop()
                            .into(binding.oneimageiv);
                }


            }
        }, 6000);


        onClick();
        return binding.getRoot();
    }

    public void onClick(){
        binding.shareiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Create an ACTION_SEND Intent*/
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                /*This will be the actual content you wish you share.*/
                String shareBody = "Here is the share content body";
                /*The type of the content is text, obviously.*/
                intent.setType("text/plain");
                /*Applying information Subject and Body.*/
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Report");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, data);
                /*Fire!*/
                startActivity(Intent.createChooser(intent, "Report"));
            }
        });
        binding.oneimageiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ZoomImageActivity.class);
                intent.putExtra("url",data);
                getActivity().startActivity(intent);
            }
        });
    }

    public void runAPI(){

        JSONAsyncTaskNew getRequest = new JSONAsyncTaskNew();
        getRequest.execute("");

    }


    class JSONAsyncTaskNew extends AsyncTask<String, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet httppost = new HttpGet("https://ggeova9x0d.execute-api.us-east-2.amazonaws.com/live/report?DeviceID="+sessionManager.getAppId()+"&PatientID="+sessionManager.getTId());
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);
                Log.i("datahhhhgffcffccf","url =    "+"https://ggeova9x0d.execute-api.us-east-2.amazonaws.com/live/report?DeviceID="+sessionManager.getAppId()+"&PatientID="+sessionManager.getTId());
                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();
                Log.i("datahhhhgffcffccf","status =    "+String.valueOf(status));
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    data = EntityUtils.toString(entity);
                    Log.i("Reportdataknoe","data =    "+String.valueOf(data));



//                    JSONObject jsono = new JSONObject(data);
                    Log.i("Reportdataknoe","Result =    "+data.toString());
                    return true;
                }


            } catch (IOException e) {
                e.printStackTrace();
                Log.i("Reportdataknoe","IOException =    "+e.getMessage().toString());
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

        }
    }
}