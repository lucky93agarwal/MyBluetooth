package com.mslji.mybluetooth.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mslji.mybluetooth.Adapter.HomeRandomTwoAdapter;
import com.mslji.mybluetooth.DashBoardActivity;
import com.mslji.mybluetooth.Database.RefUrl;

import com.mslji.mybluetooth.Database.SessionManager;
import com.mslji.mybluetooth.Model.ProductModeltwo;
import com.mslji.mybluetooth.R;
import com.mslji.mybluetooth.Room.FormClass;
import com.mslji.mybluetooth.Room.MyDatabase;
import com.mslji.mybluetooth.SendActivity;
import com.mslji.mybluetooth.zoom.ZoomImageActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FormFragment extends Fragment {

    private MyDatabase myDatabase;
    String reserver = "";
    //// progress bar
    public AVLoadingIndicatorView progressbar;
    Integer SELECT_FILE = 0;
    public EditText etcerebo_scan_date, etcerebo_scan_time, etrecord_id, ethospital_patient_id, etpatient_name, etageet, etif_yes_et, etany_othere_et;
    byte[] CDRIVES;
    public String encoded = "null";
    public String oneencoded = "null";
    public String twoencoded = "null";



    public SessionManager sessionManager;


    public EditText etif_no_where_did_you_go_first, etdate_of_symptoms, etgcs_score, etany_other_remarks_et, etblood_pressure_et;

    public static final String ALLOW_KEY = "ALLOWED";
    private static final int CAMERA_REQUEST = 1888;
    int REQUEST_CAMERA = 1;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public RadioGroup radioSexGroup, radioSkinColorGroup, radioHairdensityGroup, radiomethod_of_reaching_hospitalrgGroup, radiotype_of_accidentrbtnGroup,
            radiogeographical_location_of_accidentGroup, radiopart_of_head_that_was_affectedGroup, radiohospital_after_injuryGroup,
            radiosymptomsGroup, radiogcs_report_uploaded_rgbtnGroup, radioct_scan_uploadedbtnGroup, radiois_a_high_blood_pressure_patientbtnGroup,
            hair_Colour_rg;

    public RadioButton radioSexButton, radioSkinColorButton, radioHairdensityButton, radiomethod_of_reaching_hospitalrgButton, radiotype_of_accidentrbtnButton,
            radiogeographical_location_of_accidentButton, radiopart_of_head_that_was_affectedButton, radiohospital_after_injuryButton,
            radiosymptomsButton, radiogcs_report_uploaded_rgbtnButton, radioct_scan_uploadedbtnButton, radiois_a_high_blood_pressure_patientbtnButton,
            haircolorrb;

    public int iclick = 0;


    public AppCompatButton btnSubmit,btnSaveDraft;


    public String number, id, date;
    public static final String CAMERA_PREF = "camera_pref";


    public EditText etdate_of_accidentet, ettime_of_accidentet, etdate_of_admissionet, ettime_of_admissionet;


    public LinearLayout mPatient_Face_Photo_Linear_layout, mPatient_Face_Photo_Top_Linear_layout;
    public TextView mPatient_Face_Photo_TV, mPatient_Face_Photo_Top_TV;
    public ImageView mPatient_Face_Photo_IV, mPatient_Face_Photo_Top_IV;

    String[] appPermissions = {Manifest.permission.INTERNET,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private static final int PERMISSION_REQUEST_CODE = 1240;


    private String one_image_path, sec_image_path;
    public FormFragment() {
        // Required empty public constructor
    }
    public LinearLayout llonenewll,llimagell;
    public ImageView ivone,ivtwo;
    public View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_form, container, false);

        sessionManager = new SessionManager(getActivity());
        initViews(view);

        databaseSetup();
        getData();
        setData();
        onClick(view);




        if (checkAndRequestPermissions()) {
            // All permission are granted already. Proceed ahead
        }




        JSONAsyncTaskNew getRequest = new JSONAsyncTaskNew();
        getRequest.execute("");




        runAPI();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(data.equalsIgnoreCase("0")){
                    llimagell.setVisibility(View.GONE);
                    llonenewll.setVisibility(View.VISIBLE);
                }else {
                    String[] separated = data.split(",");
                    ImageOne = separated[0];
                    ImageOne = ImageOne.substring(1);
                    ImageTwo = separated[1];
                    ImageTwo = ImageTwo.substring(0, ImageTwo.length() - 1);
                    Log.i("datahhhhgffcffccf","ImageOne =    "+String.valueOf(ImageOne));
                    Log.i("datahhhhgffcffccf","ImageTwo =    "+String.valueOf(ImageTwo));

                    llimagell.setVisibility(View.VISIBLE);
                    llonenewll.setVisibility(View.GONE);
                    Glide.with(getActivity())
                            .load(ImageOne)
                            .centerCrop()
                            .into(ivone);
                    Glide.with(getActivity())
                            .load(ImageTwo)
                            .centerCrop()
                            .into(ivtwo);
                }


            }
        }, 6000);


        return view;
    }

    public String data="";
    public void runAPI(){

        JSONAsyncTask getRequest = new JSONAsyncTask();
        getRequest.execute("");

    }
    public void databaseSetup(){
        myDatabase = Room.databaseBuilder(getActivity(),  MyDatabase.class, "FormDB").allowMainThreadQueries().build();
    }
    private void initViews(final View view){
        llimagell = (LinearLayout)view.findViewById(R.id.imagell);
        llonenewll = (LinearLayout)view.findViewById(R.id.onenewll);
        ivone = (ImageView) view.findViewById(R.id.oneimageiv);
        ivtwo = (ImageView) view.findViewById(R.id.twoimageiv);
        llimagell.setVisibility(View.GONE);


        etdate_of_accidentet = (EditText) view.findViewById(R.id.etdate_of_accident);
        ettime_of_accidentet = (EditText) view.findViewById(R.id.ettime_of_accident);
        etdate_of_admissionet = (EditText) view.findViewById(R.id.etdate_of_admission);
        ettime_of_admissionet = (EditText) view.findViewById(R.id.ettime_of_admission);


        mPatient_Face_Photo_Linear_layout = (LinearLayout) view.findViewById(R.id.patient_ll);
        mPatient_Face_Photo_Top_Linear_layout = (LinearLayout) view.findViewById(R.id.patient_top_view_ll);

        mPatient_Face_Photo_TV = (TextView) view.findViewById(R.id.patient_tv);
        mPatient_Face_Photo_Top_TV = (TextView) view.findViewById(R.id.patient_top_view_tv);


        mPatient_Face_Photo_IV = (ImageView) view.findViewById(R.id.patient_iv);
        mPatient_Face_Photo_Top_IV = (ImageView) view.findViewById(R.id.patient_top_view_iv);






        // progress bar
        progressbar = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        etif_no_where_did_you_go_first = view.findViewById(R.id.etif_no_where_did_you_go_firstet);
        etdate_of_symptoms = view.findViewById(R.id.etdate_of_symptomset);


        etgcs_score = view.findViewById(R.id.etgcs_scoreet);
        etany_other_remarks_et = view.findViewById(R.id.any_other_remarks_et);

        etblood_pressure_et = view.findViewById(R.id.blood_pressure_et);

//        radiogcs_report_uploaded_rgbtnGroup=(RadioGroup)view.findViewById(R.id.gcs_report_uploaded_rgbtn);


        radioct_scan_uploadedbtnGroup = (RadioGroup) view.findViewById(R.id.ct_scan_uploadedbtn);

        hair_Colour_rg = (RadioGroup) view.findViewById(R.id.hairColour);
        radioSexGroup = (RadioGroup) view.findViewById(R.id.radioSex);
        radioSkinColorGroup = (RadioGroup) view.findViewById(R.id.skincolorrg);
        radioHairdensityGroup = (RadioGroup) view.findViewById(R.id.hairdensity);

        radiomethod_of_reaching_hospitalrgGroup = (RadioGroup) view.findViewById(R.id.method_of_reaching_hospitalrg);
        radiotype_of_accidentrbtnGroup = (RadioGroup) view.findViewById(R.id.type_of_accidentrbtn);

        radiogeographical_location_of_accidentGroup = (RadioGroup) view.findViewById(R.id.geographical_location_of_accidentrbtn);

        radiopart_of_head_that_was_affectedGroup = (RadioGroup) view.findViewById(R.id.part_of_head_that_was_affectedrbtn);

        radiohospital_after_injuryGroup = (RadioGroup) view.findViewById(R.id.hospital_after_injuryrbtn);


        radiosymptomsGroup = (RadioGroup) view.findViewById(R.id.symptomsrbtn);


        radiois_a_high_blood_pressure_patientbtnGroup = (RadioGroup) view.findViewById(R.id.is_a_high_blood_pressure_patientbtn);


        etany_othere_et = (EditText) view.findViewById(R.id.any_othere_et);


        etif_yes_et = (EditText) view.findViewById(R.id.if_yes_et);

        etageet = (EditText) view.findViewById(R.id.ageet);
        btnSubmit = (AppCompatButton) view.findViewById(R.id.button);
        etcerebo_scan_date = view.findViewById(R.id.cerebo_scan_dateet);
        etcerebo_scan_date.setText(date);
        etcerebo_scan_date.setEnabled(false);
        etcerebo_scan_time = view.findViewById(R.id.cerebo_scan_dateetet);
        etrecord_id = view.findViewById(R.id.record_idet);

        ethospital_patient_id = view.findViewById(R.id.hospital_patient_idet);
        etpatient_name = view.findViewById(R.id.patient_nameet);

        btnSaveDraft = (AppCompatButton)view.findViewById(R.id.button_save);
        btnSubmit = (AppCompatButton) view.findViewById(R.id.button);
    }
    public void getData(){


        id = sessionManager.getTId();
        date = sessionManager.getTdate();
        taken = sessionManager.getTTaken();
        Log.i("karnikalucky","form id =  "+id );
        Log.i("karnikalucky","form date =  "+date);
        Log.i("karnikalucky","form taken =  "+taken);
    }

    public void setData(){
        etrecord_id.setText(id);
        ethospital_patient_id.setText(id);
        Log.e("checkidnumber","no   =    "+id);
        etcerebo_scan_date.setText(date);
        etrecord_id.setEnabled(false);
        Boolean CheckDb = myDatabase.dao().isRowIsExist(Integer.valueOf(id));
        if(CheckDb){
            List<FormClass> formclass = myDatabase.dao().getFormClass(Integer.valueOf(id));

            etcerebo_scan_date.setText(formclass.get(0).getScan_date());
            etcerebo_scan_time.setText(formclass.get(0).getScan_time());
            etrecord_id.setText(formclass.get(0).getRecord_id());

            ethospital_patient_id.setText(formclass.get(0).getHospital_patient_id());
            etpatient_name.setText(formclass.get(0).getPatient_name());



            int hair_Colour_child = hair_Colour_rg.getChildCount();

            for(int x=0; x<hair_Colour_child; x++){
                RadioButton btn = (RadioButton) hair_Colour_rg.getChildAt(x);

                if(formclass.get(0).getHairCoor().equalsIgnoreCase(btn.getText().toString())){

                    btn.setChecked(true);
                }
            }

            int sex_child = radioSexGroup.getChildCount();

            for(int x=0; x<sex_child; x++){
                RadioButton btn = (RadioButton) radioSexGroup.getChildAt(x);

                if(formclass.get(0).getSex().equalsIgnoreCase(btn.getText().toString())){

                    btn.setChecked(true);
                }
            }




            etageet.setText(formclass.get(0).getAge());


            int skin_color_child = radioSkinColorGroup.getChildCount();
            for(int x=0; x<skin_color_child; x++){
                RadioButton btn = (RadioButton) radioSkinColorGroup.getChildAt(x);

                if(formclass.get(0).getSkinColor().equalsIgnoreCase(btn.getText().toString())){

                    btn.setChecked(true);
                }
            }



            int Hairdensity_child = radioHairdensityGroup.getChildCount();
            for(int x=0; x<Hairdensity_child; x++){
                RadioButton btn = (RadioButton) radioHairdensityGroup.getChildAt(x);

                if(formclass.get(0).getHairdensity().equalsIgnoreCase(btn.getText().toString())){

                    btn.setChecked(true);
                }
            }





            int method_of_reaching_hospitalrg_child = radiomethod_of_reaching_hospitalrgGroup.getChildCount();
            for(int x=0; x<method_of_reaching_hospitalrg_child; x++){
                RadioButton btn = (RadioButton) radiomethod_of_reaching_hospitalrgGroup.getChildAt(x);

                if(formclass.get(0).getMethod_of_reaching_hospitalrg().equalsIgnoreCase(btn.getText().toString())){

                    btn.setChecked(true);
                }
            }









            int type_of_accidentr_child = radiotype_of_accidentrbtnGroup.getChildCount();
            for(int x=0; x<type_of_accidentr_child; x++){
                RadioButton btn = (RadioButton) radiotype_of_accidentrbtnGroup.getChildAt(x);

                if(formclass.get(0).getType_of_accidentrbtn().equalsIgnoreCase(btn.getText().toString())){

                    btn.setChecked(true);
                }
            }









            int geographical_location_of_accident_child = radiogeographical_location_of_accidentGroup.getChildCount();
            for(int x=0; x<geographical_location_of_accident_child; x++){
                RadioButton btn = (RadioButton) radiogeographical_location_of_accidentGroup.getChildAt(x);

                if(formclass.get(0).getGeographical_location_of_accident().equalsIgnoreCase(btn.getText().toString())){

                    btn.setChecked(true);
                }
            }





            int part_of_head_that_was_affected_child = radiopart_of_head_that_was_affectedGroup.getChildCount();
            for(int x=0; x<part_of_head_that_was_affected_child; x++){
                RadioButton btn = (RadioButton) radiopart_of_head_that_was_affectedGroup.getChildAt(x);

                if(formclass.get(0).getPart_of_head_that_was_affected().equalsIgnoreCase(btn.getText().toString())){

                    btn.setChecked(true);
                }
            }








            int hospital_after_injury_child = radiohospital_after_injuryGroup.getChildCount();
            for(int x=0; x<hospital_after_injury_child; x++){
                RadioButton btn = (RadioButton) radiohospital_after_injuryGroup.getChildAt(x);

                if(formclass.get(0).getHospital_after_injury().equalsIgnoreCase(btn.getText().toString())){

                    btn.setChecked(true);
                }
            }



            etif_no_where_did_you_go_first.setText(formclass.get(0).getIf_no_where_did_you_go_first());


            etdate_of_symptoms.setText(formclass.get(0).getDate_of_symptoms());

            etdate_of_accidentet.setText(formclass.get(0).getDate_of_accident());
            ettime_of_accidentet.setText(formclass.get(0).getTime_of_accident());


            etdate_of_admissionet.setText(formclass.get(0).getDate_of_admission());
            ettime_of_admissionet.setText(formclass.get(0).getTime_of_admission());






            int symptoms_child = radiosymptomsGroup.getChildCount();
            for(int x=0; x<symptoms_child; x++){
                RadioButton btn = (RadioButton) radiosymptomsGroup.getChildAt(x);

                if(formclass.get(0).getSymptoms().equalsIgnoreCase(btn.getText().toString())){

                    btn.setChecked(true);
                }
            }

            etgcs_score.setText(formclass.get(0).getGcs_score());









            int ct_scan_uploaded_child = radioct_scan_uploadedbtnGroup.getChildCount();
            for(int x=0; x<ct_scan_uploaded_child; x++){
                RadioButton btn = (RadioButton) radioct_scan_uploadedbtnGroup.getChildAt(x);

                if(formclass.get(0).getCt_scan_uploaded().equalsIgnoreCase(btn.getText().toString())){

                    btn.setChecked(true);
                }
            }


            etany_other_remarks_et.setText(formclass.get(0).getAny_other_remarks());

            etblood_pressure_et.setText(formclass.get(0).getBlood_pressure());








            int is_a_high_blood_pressure_child = radiois_a_high_blood_pressure_patientbtnGroup.getChildCount();
            for(int x=0; x<is_a_high_blood_pressure_child; x++){
                RadioButton btn = (RadioButton) radiois_a_high_blood_pressure_patientbtnGroup.getChildAt(x);

                if(formclass.get(0).getIs_a_high_blood_pressure_patient().equalsIgnoreCase(btn.getText().toString())){

                    btn.setChecked(true);
                }
            }



            etif_yes_et.setText(formclass.get(0).getIf_yes());
            etany_othere_et.setText(formclass.get(0).getAny_othere());

//            if(formclass.get(0).getPatient_Hair_Photo().equalsIgnoreCase("NA")){
//
//            }else {
//                Log.i("checkdataindatabase", "one image path == " + formclass.get(0).getPatient_Hair_Photo());
//                Glide.with(getActivity()).load(formclass.get(0).getPatient_Hair_Photo()).placeholder(R.drawable.uploadimg).into(mPatient_Face_Photo_IV);
//            }
//
//            if(formclass.get(0).getPatient_Hair_Photo_Two().equalsIgnoreCase("NA")){
//
//            }else {
//                Log.i("checkdataindatabase", "two image path == " + formclass.get(0).getPatient_Hair_Photo_Two());
//                Glide.with(getActivity()).load(formclass.get(0).getPatient_Hair_Photo_Two()).placeholder(R.drawable.uploadimg).into(mPatient_Face_Photo_Top_IV);
//            }


        }







    }
    public String RadioSkinColorButton,If_yes_et,Any_othere_et,Is_a_high_blood_pressure_patientbtnButton,Blood_pressure_et,Any_other_remarks_et,Ct_scan_uploadedbtnButton,Gcs_score,RadiosymptomsButton,Etdate_of_symptoms, Etif_no_where_did_you_go_first,RadioHairdensityButton,Radiohospital_after_injuryButton,Radiomethod_of_reaching_hospitalrgButton,Radiotype_of_accidentrbtnButton, Radiogeographical_location_of_accidentButton,Radiopart_of_head_that_was_affectedButton;

    public void onClick(final View view){
        ivone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ZoomImageActivity.class);
                intent.putExtra("url",ImageOne);
                getActivity().startActivity(intent);
            }
        });
        ivtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ZoomImageActivity.class);
                intent.putExtra("url",ImageTwo);
                getActivity().startActivity(intent);
            }
        });
        btnSaveDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int haircolorid = hair_Colour_rg.getCheckedRadioButtonId();
                haircolorrb = (RadioButton) view.findViewById(haircolorid);

                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                radioSexButton = (RadioButton) view.findViewById(selectedId);


                int selectedSkinColorId = radioSkinColorGroup.getCheckedRadioButtonId();
                radioSkinColorButton = (RadioButton) view.findViewById(selectedSkinColorId);


                int selectedHairdensityId = radioHairdensityGroup.getCheckedRadioButtonId();
                radioHairdensityButton = (RadioButton) view.findViewById(selectedHairdensityId);


                int selectedmethod_of_reaching_hospitalrgId = radiomethod_of_reaching_hospitalrgGroup.getCheckedRadioButtonId();
                radiomethod_of_reaching_hospitalrgButton = (RadioButton) view.findViewById(selectedmethod_of_reaching_hospitalrgId);


                int selectedtype_of_accidentrId = radiotype_of_accidentrbtnGroup.getCheckedRadioButtonId();
                radiotype_of_accidentrbtnButton = (RadioButton) view.findViewById(selectedtype_of_accidentrId);


                int selectedgeographical_location_of_accidentId = radiogeographical_location_of_accidentGroup.getCheckedRadioButtonId();
                radiogeographical_location_of_accidentButton = (RadioButton) view.findViewById(selectedgeographical_location_of_accidentId);


                int selectedpart_of_head_that_was_affectedId = radiopart_of_head_that_was_affectedGroup.getCheckedRadioButtonId();
                radiopart_of_head_that_was_affectedButton = (RadioButton) view.findViewById(selectedpart_of_head_that_was_affectedId);


                int selectedradiohospital_after_injuryGroupId = radiohospital_after_injuryGroup.getCheckedRadioButtonId();
                radiohospital_after_injuryButton = (RadioButton) view.findViewById(selectedradiohospital_after_injuryGroupId);


                int selectedsymptomsId = radiosymptomsGroup.getCheckedRadioButtonId();
                radiosymptomsButton = (RadioButton) view.findViewById(selectedsymptomsId);


//                int selectedradiogcs_report_uploaded_rgbtnGroupId=radiogcs_report_uploaded_rgbtnGroup.getCheckedRadioButtonId();
//                radiogcs_report_uploaded_rgbtnButton=(RadioButton)view.findViewById(selectedradiogcs_report_uploaded_rgbtnGroupId);


                int selectedradioct_scan_uploadedbtnGroupId = radioct_scan_uploadedbtnGroup.getCheckedRadioButtonId();
                radioct_scan_uploadedbtnButton = (RadioButton) view.findViewById(selectedradioct_scan_uploadedbtnGroupId);


                int selectedradiois_a_high_blood_pressure_patientbtnGroupId = radiois_a_high_blood_pressure_patientbtnGroup.getCheckedRadioButtonId();
                radiois_a_high_blood_pressure_patientbtnButton = (RadioButton) view.findViewById(selectedradiois_a_high_blood_pressure_patientbtnGroupId);

//                Toast.makeText(getActivity(), radiois_a_high_blood_pressure_patientbtnButton.getText(), Toast.LENGTH_SHORT).show();
                if(etcerebo_scan_date.getText().length() == 0){
                    String Scan_date = "";
                }else {
                    String Scan_date = etcerebo_scan_date.getText().toString();
                }

                String Scan_time = etcerebo_scan_time.getText().toString();
                String Record_id = etrecord_id.getText().toString();
                String Ethospital_patient_id = ethospital_patient_id.getText().toString();
                String Etpatient_name = etpatient_name.getText().toString();
                String RadioSexButton = radioSexButton.getText().toString();
                String HairColor = haircolorrb.getText().toString();
                String Etageet = etageet.getText().toString();
                if(selectedSkinColorId == -1){
                    RadioSkinColorButton = "";
                }else {
                    RadioSkinColorButton = radioSkinColorButton.getText().toString();
                }

                if(selectedHairdensityId ==-1){
                    RadioHairdensityButton = "";
                }else {
                    RadioHairdensityButton = radioHairdensityButton.getText().toString();
                }


                if(selectedmethod_of_reaching_hospitalrgId ==-1){
                    Radiomethod_of_reaching_hospitalrgButton = "";
                }else {
                    Radiomethod_of_reaching_hospitalrgButton = radiomethod_of_reaching_hospitalrgButton.getText().toString();
                }
                if(selectedtype_of_accidentrId ==-1){
                    Radiotype_of_accidentrbtnButton = "";
                }else {
                    Radiotype_of_accidentrbtnButton = radiotype_of_accidentrbtnButton.getText().toString();
                }
                if(selectedgeographical_location_of_accidentId ==-1){
                    Radiogeographical_location_of_accidentButton = "";
                }else {
                    Radiogeographical_location_of_accidentButton = radiogeographical_location_of_accidentButton.getText().toString();
                }
                if(selectedpart_of_head_that_was_affectedId ==-1){
                    Radiopart_of_head_that_was_affectedButton = "";
                }else {
                    Radiopart_of_head_that_was_affectedButton = radiopart_of_head_that_was_affectedButton.getText().toString();
                }
                if(selectedradiohospital_after_injuryGroupId ==-1){
                    Radiohospital_after_injuryButton = "";
                }else {
                    Radiohospital_after_injuryButton = radiohospital_after_injuryButton.getText().toString();
                }
                if(etif_no_where_did_you_go_first.getText().length() ==0){
                    Etif_no_where_did_you_go_first = "";
                }else {
                    Etif_no_where_did_you_go_first = etif_no_where_did_you_go_first.getText().toString();
                }

                if(etdate_of_symptoms.getText().length() ==0){
                    Etdate_of_symptoms = "";
                }else {
                    Etdate_of_symptoms = etdate_of_symptoms.getText().toString();
                };
                if(selectedsymptomsId ==-1){
                    RadiosymptomsButton = "";
                }else {
                    RadiosymptomsButton = radiosymptomsButton.getText().toString();
                }
                if(etgcs_score.getText().length() ==0){
                    Gcs_score = "";
                }else {
                    Gcs_score = etgcs_score.getText().toString();
                }
//                String Gcs_report_uploaded_rgbtnButton = radiogcs_report_uploaded_rgbtnButton.toString();
                if(selectedradioct_scan_uploadedbtnGroupId ==-1){
                    Ct_scan_uploadedbtnButton = "";
                }else {
                    Ct_scan_uploadedbtnButton = radioct_scan_uploadedbtnButton.getText().toString();
                }
                if(etany_other_remarks_et.getText().length() ==0){
                    Any_other_remarks_et = "";
                }else {
                    Any_other_remarks_et = etany_other_remarks_et.getText().toString();
                }
                if(etblood_pressure_et.getText().length() ==0){
                    Blood_pressure_et = "";
                }else {
                    Blood_pressure_et = etblood_pressure_et.getText().toString();
                }
                if(selectedradiois_a_high_blood_pressure_patientbtnGroupId ==-1){
                    Is_a_high_blood_pressure_patientbtnButton = "";
                }else {
                    Is_a_high_blood_pressure_patientbtnButton = radiois_a_high_blood_pressure_patientbtnButton.getText().toString();
                }
                if(etany_othere_et.getText().length() ==0){
                    Any_othere_et = "";
                }else {
                    Any_othere_et = etany_othere_et.getText().toString();
                }
                if(etif_yes_et.getText().length() ==0){
                    If_yes_et = "";
                }else {
                    If_yes_et = etif_yes_et.getText().toString();
                }


                FormClass formClass = new FormClass(Ethospital_patient_id,
                        "Scan_date",
                        Scan_time,
                        "101",
                        etdate_of_accidentet.getText().toString(),
                        ettime_of_accidentet.getText().toString(),
                        etdate_of_admissionet.getText().toString(),
                        ettime_of_admissionet.getText().toString(),
                        Etpatient_name,
                        RadioSexButton,
                        Etageet,
                        RadioSkinColorButton,
                        RadioHairdensityButton,
                        HairColor,
                        Radiomethod_of_reaching_hospitalrgButton,
                        Radiotype_of_accidentrbtnButton,
                        Radiogeographical_location_of_accidentButton,
                        Radiopart_of_head_that_was_affectedButton,
                        Radiohospital_after_injuryButton,
                        Etif_no_where_did_you_go_first,
                        Etdate_of_symptoms,
                        RadiosymptomsButton,
                        Gcs_score,
                        Ct_scan_uploadedbtnButton,
                        Any_other_remarks_et,
                        Blood_pressure_et,
                        Is_a_high_blood_pressure_patientbtnButton,
                        Any_othere_et,
                        If_yes_et,
                        one_image_path,
                        sec_image_path
                );
                Log.e("checkidnumber"," 786 no   =    "+ethospital_patient_id.getText().toString());

                Boolean CheckDb = myDatabase.dao().isRowIsExist(Integer.valueOf(ethospital_patient_id.getText().toString()));
                if(CheckDb){

                    Log.i("checkdataindatabase","true");
                    myDatabase.dao().deleteform(Integer.valueOf(ethospital_patient_id.getText().toString()));
                    myDatabase.dao().formInsertation(formClass);
                    Toast.makeText(getActivity(), "From update successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Log.i("checkdataindatabase","false");
                    Toast.makeText(getActivity(), "From insert successfully", Toast.LENGTH_SHORT).show();
                    myDatabase.dao().formInsertation(formClass);
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int haircolorid = hair_Colour_rg.getCheckedRadioButtonId();
                haircolorrb = (RadioButton) view.findViewById(haircolorid);

                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                radioSexButton = (RadioButton) view.findViewById(selectedId);


                int selectedSkinColorId = radioSkinColorGroup.getCheckedRadioButtonId();
                radioSkinColorButton = (RadioButton) view.findViewById(selectedSkinColorId);


                int selectedHairdensityId = radioHairdensityGroup.getCheckedRadioButtonId();
                radioHairdensityButton = (RadioButton) view.findViewById(selectedHairdensityId);


                int selectedmethod_of_reaching_hospitalrgId = radiomethod_of_reaching_hospitalrgGroup.getCheckedRadioButtonId();
                radiomethod_of_reaching_hospitalrgButton = (RadioButton) view.findViewById(selectedmethod_of_reaching_hospitalrgId);


                int selectedtype_of_accidentrId = radiotype_of_accidentrbtnGroup.getCheckedRadioButtonId();
                radiotype_of_accidentrbtnButton = (RadioButton) view.findViewById(selectedtype_of_accidentrId);


                int selectedgeographical_location_of_accidentId = radiogeographical_location_of_accidentGroup.getCheckedRadioButtonId();
                radiogeographical_location_of_accidentButton = (RadioButton) view.findViewById(selectedgeographical_location_of_accidentId);


                int selectedpart_of_head_that_was_affectedId = radiopart_of_head_that_was_affectedGroup.getCheckedRadioButtonId();
                radiopart_of_head_that_was_affectedButton = (RadioButton) view.findViewById(selectedpart_of_head_that_was_affectedId);


                int selectedradiohospital_after_injuryGroupId = radiohospital_after_injuryGroup.getCheckedRadioButtonId();
                radiohospital_after_injuryButton = (RadioButton) view.findViewById(selectedradiohospital_after_injuryGroupId);


                int selectedsymptomsId = radiosymptomsGroup.getCheckedRadioButtonId();
                radiosymptomsButton = (RadioButton) view.findViewById(selectedsymptomsId);


//                int selectedradiogcs_report_uploaded_rgbtnGroupId=radiogcs_report_uploaded_rgbtnGroup.getCheckedRadioButtonId();
//                radiogcs_report_uploaded_rgbtnButton=(RadioButton)view.findViewById(selectedradiogcs_report_uploaded_rgbtnGroupId);


                int selectedradioct_scan_uploadedbtnGroupId = radioct_scan_uploadedbtnGroup.getCheckedRadioButtonId();
                radioct_scan_uploadedbtnButton = (RadioButton) view.findViewById(selectedradioct_scan_uploadedbtnGroupId);


                int selectedradiois_a_high_blood_pressure_patientbtnGroupId = radiois_a_high_blood_pressure_patientbtnGroup.getCheckedRadioButtonId();
                radiois_a_high_blood_pressure_patientbtnButton = (RadioButton) view.findViewById(selectedradiois_a_high_blood_pressure_patientbtnGroupId);








                Boolean CheckDb = myDatabase.dao().isRowIsExist(Integer.valueOf(ethospital_patient_id.getText().toString()));
                if(CheckDb){

                    Log.i("checkdataindatabase","true");
                    myDatabase.dao().deleteform(Integer.valueOf(ethospital_patient_id.getText().toString()));
                }

                date_of_accident = etdate_of_accidentet.getText().toString();
                time_of_accident = ettime_of_accidentet.getText().toString();
                date_of_admission = etdate_of_admissionet.getText().toString();
                time_of_admission = ettime_of_admissionet.getText().toString();

                Login login = new Login();
                login.execute();
            }
        });







        // LinearLayout
        mPatient_Face_Photo_Top_Linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iclick = 2;
                bottompopup(v);
            }
        });

        mPatient_Face_Photo_Linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iclick = 1;
                bottompopup(v);
            }
        });


        // ImageView

        mPatient_Face_Photo_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iclick = 1;
                bottompopup(v);
            }
        });

        mPatient_Face_Photo_Top_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iclick = 2;
                bottompopup(v);
            }
        });

        // TextView

        mPatient_Face_Photo_Top_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iclick = 2;
                bottompopup(v);
            }
        });


        mPatient_Face_Photo_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iclick = 1;
                bottompopup(v);
            }
        });




    }
    public String date_of_accident,time_of_accident,date_of_admission,time_of_admission;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("WalletNewYUYU", "Camera == " + data);
        if (resultCode == Activity.RESULT_OK) {
            Log.d("WalletNewYUYU", "Camera 2 == " + data);
            if (requestCode == REQUEST_CAMERA) {
                Log.d("WalletNewYUYU", "Camera 3 == " + data);
                Bundle bundle = data.getExtras();

                Log.d("WalletNewYUYU", "Camera 4 == " + bundle.toString());
//                if(bundle !=null){
                final Bitmap bmp = (Bitmap) bundle.get("data");

                Log.d("WalletNewYUYU", "Camera 5 == " + bmp.toString());


                //    uploadtitle.setText("screenshot upload successfully");

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                CDRIVES = byteArrayOutputStream.toByteArray();
                encoded = Base64.encodeToString(CDRIVES, Base64.DEFAULT);

                if (iclick == 1) {
                    one_image_path = "NA";
                    oneencoded = encoded;
                    mPatient_Face_Photo_IV.setImageBitmap(bmp);

                } else if (iclick == 2) {
                    sec_image_path = "NA";
                    twoencoded = encoded;
                    mPatient_Face_Photo_Top_IV.setImageBitmap(bmp);
                }




            } else if (requestCode == SELECT_FILE) {

                Uri selectedImageUri = data.getData();


                Bitmap selectedImage = null;
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                CDRIVES = byteArrayOutputStream.toByteArray();
                encoded = Base64.encodeToString(CDRIVES, Base64.DEFAULT);


                if (iclick == 1) {
                    one_image_path = selectedImageUri.getPath();
                    oneencoded = encoded;
                    mPatient_Face_Photo_IV.setImageURI(selectedImageUri);

                } else if (iclick == 2) {
                    twoencoded = encoded;
                    sec_image_path = selectedImageUri.getPath();
                    mPatient_Face_Photo_Top_IV.setImageURI(selectedImageUri);
                }


            } else {
                Toast.makeText(getActivity(), "Technical problem please try again", Toast.LENGTH_SHORT).show();
            }


        } else {
            Toast.makeText(getActivity(), "Technical problem please try again", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkAndRequestPermissions() {
        // Check which Permissions are granted (Check करें कि कौन सी Permissions Granted है।)
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String perm : appPermissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(perm);

//                Log.d("CheckPermissionLucky","Yes");
            }
        }
        // Ask for non-granted permissions
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(),
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSION_REQUEST_CODE);
//            Log.d("CheckPermissionLucky","No");
            return false;
        }
        ;
        return true;
    }







    public static Boolean getFromPref(Context context, String key) {
        SharedPreferences myPrefs = context.getSharedPreferences
                (CAMERA_PREF, Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }
    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startInstalledAppDetailsActivity(getActivity());

                    }
                });
        alertDialog.show();
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getActivity().finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);

                    }
                });
        alertDialog.show();
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    public void bottompopup(final View view) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheem);
        View bottomSheetView = LayoutInflater.from(getActivity()).inflate(R.layout.bottomsheetlayout, (LinearLayout) view.findViewById(R.id.bottomSheetContainer));
        ImageView ivaddcam = (ImageView) bottomSheetView.findViewById(R.id.addcamiv);
        TextView tvaddcam = (TextView) bottomSheetView.findViewById(R.id.addcamtv);


        tvaddcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (getFromPref(getActivity(), ALLOW_KEY)) {

                        showSettingsAlert();

                    } else if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.CAMERA)) {
                            showAlert();
                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    }
                } else {
                    bottomSheetDialog.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                }
            }
        });

        ivaddcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (getFromPref(getActivity(), ALLOW_KEY)) {

                        showSettingsAlert();

                    } else if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.CAMERA)) {
                            showAlert();
                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    }
                } else {
                    bottomSheetDialog.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                    //openCamera();

                }
            }
        });


        ImageView ivaddgalliv = (ImageView) bottomSheetView.findViewById(R.id.addgalliv);
        TextView tvaddgalltv = (TextView) bottomSheetView.findViewById(R.id.addgalltv);
        ivaddgalliv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_FILE);
            }
        });
        tvaddgalltv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_FILE);
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }


    public String ImageOne, ImageTwo;


    class JSONAsyncTaskNew extends AsyncTask<String, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet httppost = new HttpGet("https://ggeova9x0d.execute-api.us-east-2.amazonaws.com/live/form?DeviceID="+sessionManager.getAppId()+"&PatientID="+id);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();
                Log.i("datahhhhgffcffccf","status =    "+String.valueOf(status));
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    data = EntityUtils.toString(entity);
                    Log.i("datahhhhgffcffccf","data =    "+String.valueOf(data));



//                    JSONObject jsono = new JSONObject(data);
                    Log.i("datahhhhgffcffccf","Result =    "+data.toString());
                    return true;
                }


            } catch (IOException e) {
                e.printStackTrace();
                Log.i("datahhhhgffcffccf","IOException =    "+e.getMessage().toString());
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

        }
    }

    public String taken;

    public class Login extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "POST";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;
        @Override
        protected String doInBackground(String... params){
            String stringUrl = "https://ggeova9x0d.execute-api.us-east-2.amazonaws.com/live/form";
            Log.d("checknewdatanewlucky","Run API stringUrl ="+stringUrl);
            String result;
            String inputLine="";

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
//
                final JSONObject jsonObject = new JSONObject();

                try {
                    String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
//
                    jsonObject.put("PatientID",id);
                    jsonObject.put("DataTaken",taken);
                    jsonObject.put("DeviceID",sessionManager.getAppId());





                    String Scan_date = etcerebo_scan_date.getText().toString();
                    String Scan_time = etcerebo_scan_time.getText().toString();
                    String Record_id = etrecord_id.getText().toString();
                    String Ethospital_patient_id = ethospital_patient_id.getText().toString();
                    String Etpatient_name = etpatient_name.getText().toString();
                    String RadioSexButton = radioSexButton.getText().toString();
                    String HairColor = haircolorrb.getText().toString();
                    String Etageet = etageet.getText().toString();
                    String RadioSkinColorButton = radioSkinColorButton.getText().toString();

                    String RadioHairdensityButton = radioHairdensityButton.getText().toString();


                    String Radiomethod_of_reaching_hospitalrgButton = radiomethod_of_reaching_hospitalrgButton.getText().toString();
                    String Radiotype_of_accidentrbtnButton = radiotype_of_accidentrbtnButton.getText().toString();
                    String Radiogeographical_location_of_accidentButton = radiogeographical_location_of_accidentButton.getText().toString();
                    String Radiopart_of_head_that_was_affectedButton = radiopart_of_head_that_was_affectedButton.getText().toString();
                    String Radiohospital_after_injuryButton = radiohospital_after_injuryButton.getText().toString();
                    String Etif_no_where_did_you_go_first = etif_no_where_did_you_go_first.getText().toString();
                    String Etdate_of_symptoms = etdate_of_symptoms.getText().toString();
                    String RadiosymptomsButton = radiosymptomsButton.getText().toString();
                    String Gcs_score = etgcs_score.getText().toString();
//                String Gcs_report_uploaded_rgbtnButton = radiogcs_report_uploaded_rgbtnButton.toString();
                    String Ct_scan_uploadedbtnButton = radioct_scan_uploadedbtnButton.getText().toString();
                    String Any_other_remarks_et = etany_other_remarks_et.getText().toString();
                    String Blood_pressure_et = etblood_pressure_et.getText().toString();
                    String Is_a_high_blood_pressure_patientbtnButton = radiois_a_high_blood_pressure_patientbtnButton.getText().toString();
                    String Any_othere_et = etany_othere_et.getText().toString();
                    String If_yes_et = etif_yes_et.getText().toString();
                    String password = "etpassword.getText().toString()";
                    String riferalID = "sharedPreferences.getString(";

                    Log.d("CheckDatariferal", "Mobile = " + Scan_date);
                    Log.d("CheckDatariferal", "Password = " + password);
                    Log.d("CheckDatariferal", "riferalID = " + riferalID);
                    @SuppressLint("WrongThread")
                    HttpPost httpPost = new HttpPost("https://ggeova9x0d.execute-api.us-east-2.amazonaws.com/live/form");
                    JSONObject postData = new JSONObject();

                    postData.put("Scan_date", Scan_date);
                    postData.put("Scan_time", Scan_time);
                    postData.put("Record_id", Record_id);
                    Log.d("CheckData", "Scan_date = " + Scan_date.toString());
                    Log.d("CheckData", "Scan_time = " + Scan_time.toString());
                    Log.d("CheckData", "Record_id = " + Record_id.toString());


//                    postData.put("date_of_accident", date_of_accident);
//                    postData.put("time_of_accident", time_of_accident);
//                    postData.put("date_of_admission", date_of_admission);
//                    postData.put("time_of_admission", time_of_admission);
                    postData.put("date_of_accident", "12/06/2021");
                    postData.put("time_of_accident", "12:00:PM");
                    postData.put("date_of_admission", "12/06/2021");
                    postData.put("time_of_admission", "12:00:PM");

                    Log.d("CheckData", "date_of_accident = " + "12/06/2021");
                    Log.d("CheckData", "time_of_accident = " +"12:00:PM");
                    Log.d("CheckData", "date_of_admission = " + "12/06/2021");
                    Log.d("CheckData", "time_of_admission = " + "12:00:PM");


                    postData.put("hospital_patient_id", Ethospital_patient_id);
                    postData.put("patient_name", Etpatient_name);
                    postData.put("Sex", radioSexButton.getText().toString());


                    Log.d("CheckData", "hospital_patient_id = " + Ethospital_patient_id.toString());
                    Log.d("CheckData", "patient_name = " + Etpatient_name.toString());
                    Log.d("CheckData", "Sex = " + RadioSexButton.toString());


                    postData.put("age", Etageet);
                    postData.put("SkinColor", RadioSkinColorButton);
                    postData.put("Hairdensity", RadioHairdensityButton);

                    Log.d("CheckData", "age = " + Etageet.toString());
                    Log.d("CheckData", "SkinColor = " + RadioSkinColorButton.toString());
                    Log.d("CheckData", "Hairdensity = " + RadioHairdensityButton.toString());


                    postData.put("HairCoor", HairColor);
                    Log.d("CheckData", "HairCoor = " + HairColor.toString());

                    postData.put("method_of_reaching_hospitalrg", Radiomethod_of_reaching_hospitalrgButton);
                    postData.put("skin_color", RadioSkinColorButton);
                    postData.put("type_of_accidentrbtn", Radiotype_of_accidentrbtnButton);

                    Log.d("CheckData", "method_of_reaching_hospitalrg = " + Radiomethod_of_reaching_hospitalrgButton.toString());
                    Log.d("CheckData", "skin_color = " + RadioSkinColorButton.toString());
                    Log.d("CheckData", "type_of_accidentrbtn = " + Radiotype_of_accidentrbtnButton.toString());


                    postData.put("geographical_location_of_accident", Radiogeographical_location_of_accidentButton);
                    postData.put("part_of_head_that_was_affected", Radiopart_of_head_that_was_affectedButton);
                    postData.put("hospital_after_injury", Radiohospital_after_injuryButton);

                    Log.d("CheckData", "geographical_location_of_accident = " + Radiogeographical_location_of_accidentButton.toString());
                    Log.d("CheckData", "geographical_location_of_accident = " + Radiopart_of_head_that_was_affectedButton.toString());
                    Log.d("CheckData", "hospital_after_injury = " + Radiohospital_after_injuryButton.toString());


                    postData.put("if_no_where_did_you_go_first", Etif_no_where_did_you_go_first);
                    postData.put("date_of_symptoms", Etdate_of_symptoms);
                    postData.put("symptoms", RadiosymptomsButton);

                    Log.d("CheckData", "if_no_where_did_you_go_first = " + Etif_no_where_did_you_go_first.toString());
                    Log.d("CheckData", "date_of_symptoms = " + Etdate_of_symptoms.toString());
                    Log.d("CheckData", "symptoms = " + RadiosymptomsButton.toString());


                    postData.put("gcs_score", Gcs_score);
//                postData.put("gcs_report_uploaded",Gcs_report_uploaded_rgbtnButton);
                    postData.put("ct_scan_uploaded", Ct_scan_uploadedbtnButton);


                    Log.d("CheckData", "gcs_score = " + Gcs_score.toString());
                    Log.d("CheckData", "ct_scan_uploaded = " + Ct_scan_uploadedbtnButton.toString());


                    postData.put("any_other_remarks", Any_other_remarks_et);
                    postData.put("blood_pressure", Blood_pressure_et);
                    postData.put("is_a_high_blood_pressure_patient", Is_a_high_blood_pressure_patientbtnButton);


                    Log.d("CheckData", "any_other_remarks = " + Any_other_remarks_et.toString());
                    Log.d("CheckData", "blood_pressure = " + Blood_pressure_et.toString());

                    Log.d("CheckData", "is_a_high_blood_pressure_patient = " + Is_a_high_blood_pressure_patientbtnButton.toString());


                    postData.put("any_othere", Any_othere_et);
                    postData.put("if_yes", If_yes_et);
                    Log.d("CheckData", "any_othere = " + Any_othere_et.toString());
                    Log.d("CheckData", "if_yes = " + If_yes_et.toString());

                    postData.put("Patient_Hair_Photo", oneencoded);
                    postData.put("Patient_Hair_Photo_Two", twoencoded);
                    jsonObject.put("Data", postData);

                    Log.d("CheckData", "Patient_Hair_Photo = " + oneencoded.toString());
                    Log.d("CheckData", "Patient_Hair_Photo_Two = " + twoencoded.toString());

                }catch (JSONException e){
                    e.printStackTrace();
                }

//                String Scan_date=mIncomingText.getText().toString();

//                String Scan_date= "Lucky Agarwal";
                Log.d("CheckData","print json = "+String.valueOf(jsonObject));
//                Log.d("checknewdatanewlucky","print get alll json = "+String.valueOf(Scan_date));

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter bufferedReader = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                bufferedReader.write(String.valueOf(jsonObject));
//                bufferedReader.write(Scan_date);
                bufferedReader.flush();
                bufferedReader.close();
                outputStream.close();



                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
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
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            }
            catch(IOException e){
                e.printStackTrace();
                Log.d("CheckData","Message =    "+e.getMessage());
                result = null;
            }
            return result;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Log.d("CheckData","Result =    "+result);


            Toast.makeText(getActivity(), "Saved Successfully", Toast.LENGTH_SHORT).show();

        }
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
                HttpGet httppost = new HttpGet("https://ggeova9x0d.execute-api.us-east-2.amazonaws.com/live/form?DeviceID="+sessionManager.getAppId()+"&PatientID="+id);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);
                Log.d("checknewdatanewlucky","before  url =    "+"https://ggeova9x0d.execute-api.us-east-2.amazonaws.com/live/form?DeviceID="+sessionManager.getAppId()+"&PatientID="+id);
                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();
                Log.d("checknewdatanewlucky","before  status =    "+String.valueOf(status));
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

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