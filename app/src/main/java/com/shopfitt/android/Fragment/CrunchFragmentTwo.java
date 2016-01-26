package com.shopfitt.android.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.shopfitt.android.Network.CustomVolleyRequest;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.Font;
import com.shopfitt.android.Utils.Shopfitt;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrunchFragmentTwo extends Fragment implements Response.ErrorListener, Response.Listener {

    private View view;
    private EditText editText;
    private TextView textView;
    private Context mContext;
    private Button submitButton;
    private boolean executed = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

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
        getActivity().setTitle("Crunchaton");
        String[] message = new String[]{"There is always a Next time.", "Secret to getting ahead is to get started.", "A legend in anything, was once a beginner.", "It always seems impossible until it is done.",
                "Success is, going from failure to failure without loss of enthusiasm.", "Never Give up", "Improvise,Adapt,Overcome !", "Anybody can dance if they find the music they love.",
                "If it was easy, it wouldn’t be worth doing it.", "If you think you can or you can’t, you’ll be right both the times.", "No one’s perfect, that’s why pencils have erasers."};
        editText = (EditText) view.findViewById(R.id.crunch_message_input);
        editText.setTypeface(Font.getTypeface(mContext,Font.FONTAWESOME));
        textView = (TextView) view.findViewById(R.id.crunch_message);
        submitButton = (Button) view.findViewById(R.id.submit_message);
        submitButton.setTypeface(Font.getTypeface(mContext,Font.FONTAWESOME));
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMessage();
            }
        });
        textView.setText(message[randInt(1, 10)]);
        if (Config.crunchWon) {
            textView.setText("Winner's Gyan , You got 30 sec");
        } else {
            editText.setVisibility(View.GONE);
        }
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                redirect();
            }
        };
        int time;
        if (Config.crunchWon){
            time = 30000;
        } else {
            time = 10000;
        }
        handler.postDelayed(r, time);
    }

    private void redirect() {
        if (!Config.crunchWon) {
            showThankyou();
        } else {
            postMessage();
        }
    }

    private void postMessage() {
        if(!executed) {
            CommonMethods.showProgress(true, mContext);
            executed = true;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("comparerid", Config.comparerID.replaceAll("\"", ""));
                jsonObject.put("message", editText.getText().toString() + "");
                CustomVolleyRequest<String> volleyRequest = new CustomVolleyRequest<String>(Request.Method.POST, "http://23.91.69.85:61090/ProductService.svc/WinnersMessage/", String.class, jsonObject,
                        this, this);
                volleyRequest.setRetryPolicy(new RetryPolicy() {
                    @Override
                    public int getCurrentTimeout() {
                        return 30000;
                    }

                    @Override
                    public int getCurrentRetryCount() {
                        return 0;
                    }

                    @Override
                    public void retry(VolleyError volleyError) throws VolleyError {

                    }
                });
                Shopfitt.getInstance().addToRequestQueue(volleyRequest, "verifyotpapi");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonMethods.showProgress(false,mContext);
//        executed = false;
        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_LONG).show();
        showThankyou();
    }

    @Override
    public void onResponse(Object o) {
        CommonMethods.showProgress(false,mContext);
        showThankyou();
    }

    @Override
    public void onStop() {
        Log.e("test", "test1");
        CommonMethods.showProgress(false,mContext);
        super.onStop();
    }
}
