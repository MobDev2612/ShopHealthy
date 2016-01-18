package com.shopfitt.android.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shopfitt.android.Activity.HomeActivity;
import com.shopfitt.android.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThankQFragment extends Fragment {


    public ThankQFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_thank_q, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        final Handler handler = new Handler();
        final Runnable r = new Runnable()
        {
            public void run()
            {
                Intent intent = new Intent(getActivity(),HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        };
        handler.postDelayed(r, 30000);
    }
}
