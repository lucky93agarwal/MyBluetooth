package com.mslji.mybluetooth.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mslji.mybluetooth.IDActivity;
import com.mslji.mybluetooth.Model.ProductModeltwo;
import com.mslji.mybluetooth.R;
import com.mslji.mybluetooth.TestDetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class HomeRandomTwoAdapter extends RecyclerView.Adapter<HomeRandomTwoAdapter.ViewHolder> {
    public static Context mcontext;

    public boolean run = false;
    public static List<ProductModeltwo> productList;
    public static boolean runtimer = true;
    public Context context;
    ViewHolder view;

    public ProductModeltwo getselfdata;
    String Contest = "";



    ViewHolder viewHolder;

    public HomeRandomTwoAdapter(Context context, List<ProductModeltwo> productList) {
        super();
        this.context = context;
        this.productList = productList;
        mcontext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.horizontallayout, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        getselfdata = productList.get(i);
        view = viewHolder;



        viewHolder.tvname.setText(productList.get(i).getId());
        viewHolder.tvdate.setText("("+productList.get(i).getDate()+")");





    }

    @Override
    public int getItemCount() {


        return productList.size();


    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView tvname,tvdate;

        public LinearLayout mlinear;

        public String apiDetails = "";
        public ViewHolder(final View itemView) {
            super(itemView);



            tvname = (TextView) itemView.findViewById(R.id.numtv);//
            tvdate = (TextView) itemView.findViewById(R.id.datetv);
            mlinear = (LinearLayout) itemView.findViewById(R.id.linearone);//




            mlinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


//                    JSONAsyncTask getRequest = new JSONAsyncTask();
//                    getRequest.execute("");
                    Intent intent = new Intent(mcontext, TestDetailsActivity.class);
                    intent.putExtra("id",productList.get(getAdapterPosition()).getId());
                    intent.putExtra("date",productList.get(getAdapterPosition()).getDate());
                    intent.putExtra("num",productList.get(getAdapterPosition()).getNumbers());
                    intent.putExtra("taken",productList.get(getAdapterPosition()).getDataTaken());
                    Log.i("karnikalucky","HomeRandomTwoAdapter number =  "+productList.get(getAdapterPosition()));
                    Log.i("karnikalucky","HomeRandomTwoAdapter date =  "+productList.get(getAdapterPosition()).getDate());
                    mcontext.startActivity(intent);


                }
            });








//
//            etproductno = (EditText) itemView.findViewById(R.id.productno);
//            spscheme = (Spinner) itemView.findViewById(R.id.scheme);
//            spunit = (Spinner) itemView.findViewById(R.id.unit);
//            tvid = (TextView)itemView.findViewById(R.id.btnupdates);
//            tvcategoryId = (TextView)itemView.findViewById(R.id.btnsaves);
//            tvrupees = (TextView)itemView.findViewById(R.id.btnproductrupees);


//            imageView = itemView.findViewById(R.id.image);
//            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);


//            etproductno.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    productList.get(i).setCompleteMasalaSchemeData(spscheme.getSelectedItem().toString());
//                    productList.get(i).setCompleteMasalaUnitData(spunit.getSelectedItem().toString());
//                    int variable = i;
//                }
//            });
//            spscheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    productList.get(i).setCompleteMasalaSchemeDataPosition(spscheme.getSelectedItemPosition());
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });




//            etproductno.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
////                    getada.add(productList.get(i).getName());
//                    productList.get(i).setCompleteMasalaProducQuantity(charSequence.toString());
////
////                    productList.get(i).setCompleteMasalaSchemeData(spscheme.getSelectedItem().toString());
////                    productList.get(i).setCompleteMasalaUnitData(spunit.getSelectedItem().toString());
//                    productList.get(i).setCompleteMasalaProductPrice(Double.parseDouble(tvrupees.getText().toString()));
//                    productList.get(i).setCompleteMasalaProductName(tvproductname.getText().toString());
//
//                    productList.get(i).setProductId(tvid.getText().toString());
//                    productList.get(i).setCompleteMasalaCategoryId(tvcategoryId.getText().toString());
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                }
//            });



        }


        class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Boolean doInBackground(String... urls) {
                try {

                    //------------------>>
                    HttpGet httppost = new HttpGet("https://ggeova9x0d.execute-api.us-east-2.amazonaws.com/live/interrupt?DeviceID=123456");
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpResponse response = httpclient.execute(httppost);

                    // StatusLine stat = response.getStatusLine();
                    int status = response.getStatusLine().getStatusCode();
                    Log.d("checknewdatanewlucky","before  status =    "+String.valueOf(status));
                    if (status == 200) {
                        HttpEntity entity = response.getEntity();
                        String data = EntityUtils.toString(entity);

//                    JSONArray jsonArray = new JSONArray(data);
//                    for(int i=0;i<jsonArray.length();i++){
//
//                    }
                        apiDetails = data;
//                    JSONObject jsono = new JSONObject(data);
                        Log.d("checknewdatanewlucky","before  Result =    "+data.toString());
                        return true;
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("checknewdatanewlucky","IOException =    "+e.getMessage().toString());
                }
//            catch (JSONException e) {
//
//                e.printStackTrace();
//                Log.i("datahhhhgffcffccf","JSONException =    "+e.getMessage().toString());
//            }
                return false;
            }

            protected void onPostExecute(Boolean result) {

                if(result){
                }

            }
        }
    }

}




