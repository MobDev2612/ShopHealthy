package com.shopfitt.android.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.Font;
import com.shopfitt.android.Utils.FontView;
import com.shopfitt.android.Utils.Shopfitt;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment implements Response.ErrorListener, Response.Listener<String> {

    private View view;
    private Button fitYes, fitNo, crunchYes, crunchNo;
//    private int questionAnswered = 0;
    private FontView fitCartQuestion;
    private int requestId;
    private Context mContext;
    LinearLayout crunchQuestion;
    boolean fitCartAccess;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public QuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_question, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Crunchathon");
        initialiseComponents();
        getFitCartStatus(Config.customerID);
    }

    private void initialiseComponents() {
        crunchQuestion = (LinearLayout) view.findViewById(R.id.crunch_match_question);
        fitCartQuestion = (FontView) view.findViewById(R.id.question_fit_cart);
        fitYes = (Button) view.findViewById(R.id.question_fit_cart_yes);
        fitYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFitCartYes(Config.customerID);
            }
        });
        fitNo = (Button) view.findViewById(R.id.question_fit_cart_no);
        fitNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFitCartNo(Config.customerID);
            }
        });
        crunchYes = (Button) view.findViewById(R.id.question_crunch_match_yes);
        crunchNo = (Button) view.findViewById(R.id.question_crunch_match_no);

        fitYes.setTypeface(Font.getTypeface(mContext,Font.FONT_AWESOME));
        fitNo.setTypeface(Font.getTypeface(mContext,Font.FONT_AWESOME));
        crunchNo.setTypeface(Font.getTypeface(mContext, Font.FONT_AWESOME));
        crunchYes.setTypeface(Font.getTypeface(mContext,Font.FONT_AWESOME));

        crunchYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCrunchMatch();
            }
        });
        crunchNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showThankyou();
            }
        });
    }

    private void showThankyou() {
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
        StringRequest fetchLocations = new StringRequest("http://23.91.69.85:61090/ProductService.svc/checkFitCart/" + id,
                this, this);
        fetchLocations.setRetryPolicy(new RetryPolicy() {
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
        Shopfitt.getInstance().addToRequestQueue(fetchLocations, "locationapi");
    }

    private void setFitCartNo(String id) {
        fitCartAccess = false;
//        if (questionAnswered == 0) {
            requestId = 2;
            CommonMethods.showProgress(true, mContext);
            StringRequest fetchLocations = new StringRequest("http://23.91.69.85:61090/ProductService.svc/checkNoFitCart/" + id,
                    this, this);
            fetchLocations.setRetryPolicy(new RetryPolicy() {
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
            Shopfitt.getInstance().addToRequestQueue(fetchLocations, "locationapi");
//        }
    }


    private void setFitCartYes(String id) {
        fitCartAccess = true;
//        if (questionAnswered == 0) {
            requestId = 2;
            CommonMethods.showProgress(true, mContext);
            StringRequest fetchLocations = new StringRequest("http://23.91.69.85:61090/ProductService.svc/checkYesFitCart/" + id,
                    this, this);
            fetchLocations.setRetryPolicy(new RetryPolicy() {
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
            Shopfitt.getInstance().addToRequestQueue(fetchLocations, "locationapi");
//        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonMethods.showProgress(false, mContext);
        Toast.makeText(mContext, "Unable to fetch details", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(String s) {
        CommonMethods.showProgress(false, mContext);
        if (requestId == 1) {
            if (s.contains("1")) {
                fitCartQuestion.setVisibility(View.GONE);
                fitYes.setVisibility(View.GONE);
                fitNo.setVisibility(View.GONE);
                crunchQuestion.setVisibility(View.VISIBLE);
                fitCartAccess = true;
            } else if (s.contains("0")) {
                showThankyou();
            }
        }
        if (requestId == 2) {
//            questionAnswered = 1;
            if(s.contains("1")) {
                fitCartQuestion.setVisibility(View.GONE);
                fitYes.setVisibility(View.GONE);
                fitNo.setVisibility(View.GONE);
                if(fitCartAccess) {
                    crunchQuestion.setVisibility(View.VISIBLE);
                }  else {
                    showThankyou();
                }
            } else if (s.contains("0")){
                Toast.makeText(mContext, "Something went wrong.. please try later", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStop() {
        CommonMethods.showProgress(false,mContext);
        super.onStop();
    }
}
