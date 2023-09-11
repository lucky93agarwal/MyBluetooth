package com.mslji.mybluetooth.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mslji.mybluetooth.R;
import com.mslji.mybluetooth.databinding.FragmentSplashBinding;


public class SplashFragment extends Fragment {



    public SplashFragment() {
        // Required empty public constructor
    }
    FragmentSplashBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  FragmentSplashBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }
}