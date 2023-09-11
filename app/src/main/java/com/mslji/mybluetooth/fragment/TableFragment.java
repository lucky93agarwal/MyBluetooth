package com.mslji.mybluetooth.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mslji.mybluetooth.R;

import static android.content.Context.MODE_PRIVATE;


public class TableFragment extends Fragment {



    public TableFragment() {
        // Required empty public constructor
    }

    public CardView tvleft_frontal, tvright_frontal;
    public CardView tvleft_par, tvright_par;


    public CardView tvleft_temp, tvright_temp;


    public CardView tvleft_occ, tvright_occ;
    public TextView tvid, tvdate;
    public String number,id,date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view;
        view =  inflater.inflate(R.layout.fragment_table, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("datanew",MODE_PRIVATE);
        number= sharedPreferences.getString("num","");
        id= sharedPreferences.getString("id","");
        date= sharedPreferences.getString("date","");


        tvid = (TextView)view.findViewById(R.id.idtv);
        tvdate = (TextView)view.findViewById(R.id.datetv);

        tvid.setText(id);
        tvdate.setText(date);

        Log.d("karnikalucky","TableFragment number =  "+number);
        Log.d("karnikalucky","ID =  "+id);
        Log.d("karnikalucky","DATE =  "+date);
        Log.d("karnikalucky","number 0 =  "+number.charAt(0));
        Log.d("karnikalucky","number 1 =  "+number.charAt(1));





        tvleft_frontal = (CardView)view.findViewById(R.id.left_f_tv);

        if(String.valueOf(number.charAt(0)).equalsIgnoreCase("1")){
            tvleft_frontal.setCardBackgroundColor(Color.parseColor("#008000"));
        }else if(String.valueOf(number.charAt(0)).equalsIgnoreCase("2")){
            tvleft_frontal.setCardBackgroundColor(Color.parseColor("#ff0000"));
        }else if(String.valueOf(number.charAt(0)).equalsIgnoreCase("3")){
            tvleft_frontal.setCardBackgroundColor(Color.parseColor("#000000"));
        }else {
            tvleft_frontal.setCardBackgroundColor(Color.parseColor("#ffffff"));
        }



        tvright_frontal = (CardView)view.findViewById(R.id.right_f_tv);



        if(String.valueOf(number.charAt(1)).equalsIgnoreCase("1")){
            tvright_frontal.setCardBackgroundColor(Color.parseColor("#008000"));
        }else if(String.valueOf(number.charAt(1)).equalsIgnoreCase("2")){
            tvright_frontal.setCardBackgroundColor(Color.parseColor("#ff0000"));
        }else if(String.valueOf(number.charAt(1)).equalsIgnoreCase("3")){
            tvright_frontal.setCardBackgroundColor(Color.parseColor("#000000"));
        }else {
            tvright_frontal.setCardBackgroundColor(Color.parseColor("#ffffff"));
        }

        tvleft_temp = (CardView) view.findViewById(R.id.left_t_tv);


        if(String.valueOf(number.charAt(2)).equalsIgnoreCase("1")){
            tvleft_temp.setCardBackgroundColor(Color.parseColor("#008000"));
        }else if(String.valueOf(number.charAt(2)).equalsIgnoreCase("2")){
            tvleft_temp.setCardBackgroundColor(Color.parseColor("#ff0000"));
        }else if(String.valueOf(number.charAt(2)).equalsIgnoreCase("3")){
            tvleft_temp.setCardBackgroundColor(Color.parseColor("#000000"));
        }else {
            tvleft_temp.setCardBackgroundColor(Color.parseColor("#ffffff"));
        }




        tvright_temp = (CardView) view.findViewById(R.id.right_t_tv);


        if(String.valueOf(number.charAt(3)).equalsIgnoreCase("1")){
            tvright_temp.setCardBackgroundColor(Color.parseColor("#008000"));
        }else if(String.valueOf(number.charAt(3)).equalsIgnoreCase("2")){
            tvright_temp.setCardBackgroundColor(Color.parseColor("#ff0000"));
        }else if(String.valueOf(number.charAt(3)).equalsIgnoreCase("3")){
            tvright_temp.setCardBackgroundColor(Color.parseColor("#000000"));
        }else {
            tvright_temp.setCardBackgroundColor(Color.parseColor("#ffffff"));
        }

        tvleft_par = (CardView)view.findViewById(R.id.left_p_tv);



        if(String.valueOf(number.charAt(4)).equalsIgnoreCase("1")){
            tvleft_par.setCardBackgroundColor(Color.parseColor("#008000"));
        }else if(String.valueOf(number.charAt(4)).equalsIgnoreCase("2")){
            tvleft_par.setCardBackgroundColor(Color.parseColor("#ff0000"));
        }else if(String.valueOf(number.charAt(4)).equalsIgnoreCase("3")){
            tvleft_par.setCardBackgroundColor(Color.parseColor("#000000"));
        }else {
            tvleft_par.setCardBackgroundColor(Color.parseColor("#ffffff"));
        }



        tvright_par = (CardView) view.findViewById(R.id.right_p_tv);


        if(String.valueOf(number.charAt(5)).equalsIgnoreCase("1")){
            tvright_par.setCardBackgroundColor(Color.parseColor("#008000"));
        }else if(String.valueOf(number.charAt(5)).equalsIgnoreCase("2")){
            tvright_par.setCardBackgroundColor(Color.parseColor("#ff0000"));
        }else if(String.valueOf(number.charAt(5)).equalsIgnoreCase("3")){
            tvright_par.setCardBackgroundColor(Color.parseColor("#000000"));
        }else {
            tvright_par.setCardBackgroundColor(Color.parseColor("#ffffff"));
        }

        tvleft_occ = (CardView) view.findViewById(R.id.left_o_tv);


        if(String.valueOf(number.charAt(6)).equalsIgnoreCase("1")){
            tvleft_occ.setCardBackgroundColor(Color.parseColor("#008000"));
        }else if(String.valueOf(number.charAt(6)).equalsIgnoreCase("2")){
            tvleft_occ.setCardBackgroundColor(Color.parseColor("#ff0000"));
        }else if(String.valueOf(number.charAt(6)).equalsIgnoreCase("3")){
            tvleft_occ.setCardBackgroundColor(Color.parseColor("#000000"));
        }else {
            tvleft_occ.setCardBackgroundColor(Color.parseColor("#ffffff"));
        }

        tvright_occ = (CardView) view.findViewById(R.id.right_o_tv);


        if(String.valueOf(number.charAt(7)).equalsIgnoreCase("1")){
            tvright_occ.setCardBackgroundColor(Color.parseColor("#008000"));
        }else if(String.valueOf(number.charAt(7)).equalsIgnoreCase("2")){
            tvright_occ.setCardBackgroundColor(Color.parseColor("#ff0000"));
        }else if(String.valueOf(number.charAt(7)).equalsIgnoreCase("3")){
            tvright_occ.setCardBackgroundColor(Color.parseColor("#000000"));
        }else {
            tvright_occ.setCardBackgroundColor(Color.parseColor("#ffffff"));
        }

        return view;
    }
}