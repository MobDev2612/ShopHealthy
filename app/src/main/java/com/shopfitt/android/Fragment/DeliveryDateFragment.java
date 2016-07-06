package com.shopfitt.android.Fragment;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shopfitt.android.Activity.HomeActivity;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.Font;
import com.shopfitt.android.Utils.FontView;
import com.shopfitt.android.Utils.SharedPreferences;
import com.shopfitt.android.Utils.Shopfitt;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryDateFragment extends Fragment  implements Response.ErrorListener, Response.Listener<String> {


    private View view;
    private FontView textViewShowTime;
    private Button backButton;
    SharedPreferences sharedPreferences;
    private Context mContext;
    ProgressBar mProgressBar;
    private CountDownTimer countDownTimer;
    private long totalTimeCountInMilliseconds;
    boolean fitCartAccess;
    private int requestId;

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
        textViewShowTime = (FontView) view.findViewById(R.id.tvTimeCount);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        Button submit = (Button) view.findViewById(R.id.delivery_date_submit);
        submit.setTypeface(Font.getTypeface(mContext, Font.FONT_AWESOME));
        backButton = (Button) view.findViewById(R.id.delivery_date_no);
        backButton.setTypeface(Font.getTypeface(mContext, Font.FONT_AWESOME));
        if (Config.foodItems > 0) {
            ((FontView) view.findViewById(R.id.crunch_match_text)).setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);
        } else {
            ((FontView) view.findViewById(R.id.crunch_match_text)).setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
            backButton.setVisibility(View.VISIBLE);
            backButton.setText("Continue Shopping");
        }

        totalTimeCountInMilliseconds = 60 * 120 * 1000;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextScreen();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });
        ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", 0, 500); // see this max value coming back here, we animale towards that value
        animation.setDuration(totalTimeCountInMilliseconds);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
        startTimer();
    }

    private void goHome(){
        Config.foodItems =0;
        Intent intent = new Intent(mContext,HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }

    private void showNextScreen() {
        Config.foodItems =0;
//        Fragment fragment = new QuestionFragment();
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container, fragment);
//        fragmentTransaction.commit();
        getFitCartStatus(Config.customerID);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 500) {
            // 500 means, onTick function will be called at every 500
            // milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                int hours = (int) seconds / 3600;
                int minutes = (int) ((seconds - (3600 * hours)) / 60);
                //i++;
                //Setting the Progress Bar to decrease wih the timer
//                mProgressBar.setProgress((int) (leftTimeInMilliseconds / 1000));
                textViewShowTime.setText(String.format("%01d", hours) + ":" + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds % 60));
                // format the textview to show the easily readable format

            }

            @Override
            public void onFinish() {
                textViewShowTime.setText("Time up!");
                textViewShowTime.setVisibility(View.VISIBLE);
                goHome();
            }

        }.start();
    }

    private void showThankYou() {
        Fragment fragment = new ThankQFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    private void doCrunchMatch() {
        Fragment fragment = new CrunchFragmentOne();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }


    private void getFitCartStatus(String id) {
        requestId = 1;
        CommonMethods.showProgress(true, mContext);
        StringRequest getFitCartStatus = new StringRequest("http://23.91.69.85:61090/ProductService.svc/checkFitCart/" + id,
                this, this);
        getFitCartStatus.setRetryPolicy(new RetryPolicy() {
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
        Shopfitt.getInstance().addToRequestQueue(getFitCartStatus, "fitCartStatus");
    }

    private void setFitCartNo(String id) {
        fitCartAccess = false;
        requestId = 2;
        CommonMethods.showProgress(true, mContext);
        StringRequest setFitCart= new StringRequest("http://23.91.69.85:61090/ProductService.svc/checkNoFitCart/" + id,
                this, this);
        setFitCart.setRetryPolicy(new RetryPolicy() {
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
        Shopfitt.getInstance().addToRequestQueue(setFitCart, "setFitCart");
    }


    private void setFitCartYes(String id) {
        fitCartAccess = true;
        requestId = 2;
        CommonMethods.showProgress(true, mContext);
        StringRequest setFitCartStatus = new StringRequest("http://23.91.69.85:61090/ProductService.svc/checkYesFitCart/" + id,
                this, this);
        setFitCartStatus.setRetryPolicy(new RetryPolicy() {
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
        Shopfitt.getInstance().addToRequestQueue(setFitCartStatus, "setFitCart");
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonMethods.showProgress(false, mContext);
        Toast.makeText(mContext, "Unable to fetch Crunchathon details", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(String s) {
        CommonMethods.showProgress(false, mContext);
        if (requestId == 1) {
            if (s.contains("1")) {
               showSecondQuestion();
                fitCartAccess = true;
            } else if (s.contains("0")) {
                showFirstQuestion();
            }
        }
        if (requestId == 2) {
            if(s.contains("1")) {
                if(fitCartAccess) {
                    showSecondQuestion();
                }  else {
                    showThankYou();
                }
            } else if (s.contains("0")){
                Toast.makeText(mContext, "Something went wrong.. please try later", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showSecondQuestion(){
        new AlertDialog.Builder(mContext)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage("Do you want to have a Crunch Match ?")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doCrunchMatch();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showThankYou();
                    }
                })
                .show();
    }

    private void showFirstQuestion(){
        new AlertDialog.Builder(mContext)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage("Allow access to Fitt Cart ?")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setFitCartYes(Config.customerID);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setFitCartNo(Config.customerID);
                    }
                })
                .show();
    }

    @Override
    public void onStop() {
        CommonMethods.showProgress(false,mContext);
        super.onStop();
    }
}
