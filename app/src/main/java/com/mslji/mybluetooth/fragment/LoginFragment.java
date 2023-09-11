package com.mslji.mybluetooth.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mslji.mybluetooth.R;
import com.mslji.mybluetooth.databinding.FragmentLoginBinding;


public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }

    FragmentLoginBinding binding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=  FragmentLoginBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }
}