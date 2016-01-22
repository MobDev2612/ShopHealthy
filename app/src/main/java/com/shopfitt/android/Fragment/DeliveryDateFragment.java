package com.shopfitt.android.Fragment;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.SharedPreferences;
import com.shopfitt.android.Utils.Shopfitt;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryDateFragment extends Fragment implements Response.Listener<String>, Response.ErrorListener {

    private View view;
//    private EditText editText;
    private TextView textViewShowTime;
    private Button submit;
    SharedPreferences sharedPreferences;
    private Context mContext;
    ProgressBar mProgressBar;
    private CountDownTimer countDownTimer;
    private long totalTimeCountInMilliseconds;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    public DeliveryDateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_delivery_date, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialiseComponents();
    }

    private void initialiseComponents() {
        sharedPreferences = new SharedPreferences(mContext);
        textViewShowTime = (TextView) view.findViewById(R.id.tvTimeCount);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        submit = (Button) view.findViewById(R.id.delivery_date_submit);
        totalTimeCountInMilliseconds = 60 * 120 * 1000;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    if (sharedPreferences.getCustomerId().length() > 0) {
                        showThankyou();
//                    } else {
//                        getCustomerId();
//                    }
            }
        });
        ObjectAnimator animation = ObjectAnimator.ofInt (mProgressBar, "progress", 0, 500); // see this max value coming back here, we animale towards that value
        animation.setDuration (totalTimeCountInMilliseconds);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
        startTimer();

    }

    private void getCustomerId() {
        SharedPreferences sharedPreferences = new SharedPreferences(mContext);
        String userName = sharedPreferences.getLoginID("");
        StringRequest fetchLocations = new StringRequest("http://23.91.69.85:61090/ProductService.svc/getCustomerID/" + userName,
                this, this);
        Shopfitt.getInstance().addToRequestQueue(fetchLocations, "locationapi");
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(mContext, "Unable to fetch details", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(String s) {
        sharedPreferences.setCustomerID(s);
        Config.customerID = s;
        showThankyou();
    }

    private void showThankyou() {
        Fragment fragment = new QuestionFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onStop() {
        Log.e("test", "test1");
        super.onStop();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 500) {
            // 500 means, onTick function will be called at every 500
            // milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                //i++;
                //Setting the Progress Bar to decrease wih the timer
//                mProgressBar.setProgress((int) (leftTimeInMilliseconds / 1000));
                textViewShowTime.setText(String.format("%02d", seconds / 60)
                        + ":" + String.format("%02d", seconds % 60));
                // format the textview to show the easily readable format

            }

            @Override
            public void onFinish() {
                textViewShowTime.setText("Time up!");
                textViewShowTime.setVisibility(View.VISIBLE);
            }

        }.start();
    }
}
