package com.shopfitt.android.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.shopfitt.android.R;
import com.shopfitt.android.Utils.Config;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrunchFragmentTwo extends Fragment {

    private View view;
    private EditText editText;
    private TextView textView;

    public CrunchFragmentTwo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_crunch_fragment_two, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] message = new String[] {"There is always a Next time.","Secret to getting ahead is to get started.","A legend in anything, was once a beginner.","It always seems impossible until it is done.",
                "Success is, going from failure to failure without loss of enthusiasm.","Never Give up","Improvise,Adapt,Overcome !","Anybody can dance if they find the music they love.",
                "If it was easy, it wouldn’t be worth doing it.","If you think you can or you can’t, you’ll be right both the times.","No one’s perfect, that’s why pencils have erasers."};
        editText = (EditText) view.findViewById(R.id.crunch_message_input);
        textView= (TextView) view.findViewById(R.id.crunch_message);
        textView.setText(message[randInt(1,10)]);
        if (Config.crunchWon){
            textView.setVisibility(View.GONE);
        }else {
            editText.setVisibility(View.GONE);
        }
        final Handler handler = new Handler();
        final Runnable r = new Runnable()
        {
            public void run()
            {
                showThankyou();
            }
        };
        handler.postDelayed(r, 30000);
    }

    private void showThankyou() {
        Fragment fragment = new ThankQFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

}
