package com.shopfitt.android.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shopfitt.android.Network.VolleyRequest;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.datamodels.Crunch;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrunchathonTableFragment extends Fragment {
    private View view;
    private Context mContext;
    int requestId = 0;
    double selfpercent, opponentpercent;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public CrunchathonTableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_crunchathon_tableragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSelfOrderExtra();
        getOpponentOrderExtra();
        getSelfFinalPercent();
        getOpponentFinalPercent();
    }

    private void getSelfOrderExtra() {
        VolleyRequest<Crunch> fetchLocations = new VolleyRequest<Crunch>(Request.Method.GET,
                "http://23.91.69.85:61090/ProductService.svc/getOrderExtraDetails/" + Config.orderId, Crunch.class, null,
                new Response.Listener<Crunch>() {
                    @Override
                    public void onResponse(Crunch crunch) {
                        ((TextView) view.findViewById(R.id.your_calories)).setText(crunch.getCalories());
                        ((TextView) view.findViewById(R.id.your_fat)).setText(crunch.getFat());
                        ((TextView) view.findViewById(R.id.your_sodium)).setText(crunch.getSodium());
                        ((TextView) view.findViewById(R.id.your_sugar)).setText(crunch.getSugar());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(mContext, "Please try later", Toast.LENGTH_SHORT).show();
            }
        });
        Shopfitt.getInstance().addToRequestQueue(fetchLocations, "locationapi");
    }

    private void getOpponentOrderExtra() {
        VolleyRequest<Crunch> fetchLocations = new VolleyRequest<Crunch>(Request.Method.GET,
                "http://23.91.69.85:61090/ProductService.svc/getOrderExtraDetails/" + Config.comparerOrderID, Crunch.class, null,
                new Response.Listener<Crunch>() {
                    @Override
                    public void onResponse(Crunch crunch) {
//                        Crunch crunch = crunch1[0];
                        ((TextView) view.findViewById(R.id.opponent_calories)).setText(crunch.getCalories());
                        ((TextView) view.findViewById(R.id.opponent_fat)).setText(crunch.getFat());
                        ((TextView) view.findViewById(R.id.opponent_sodium)).setText(crunch.getSodium());
                        ((TextView) view.findViewById(R.id.opponent_sugar)).setText(crunch.getSugar());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(mContext, "Please try later", Toast.LENGTH_SHORT).show();
            }
        });
        Shopfitt.getInstance().addToRequestQueue(fetchLocations, "locationapi");
    }

    private void getSelfFinalPercent() {
        VolleyRequest<String> fetchLocations = new VolleyRequest<String>(Request.Method.GET,
                "http://23.91.69.85:61090/ProductService.svc/returnFinalPercentage/" + Config.orderId, String.class, null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String crunch) {
                        requestId++;
                        ((TextView) view.findViewById(R.id.your_final_percent)).setText(crunch);
                        selfpercent = Double.parseDouble(crunch);
                        decideWinner();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                decideWinner();
                requestId++;
                selfpercent = 0.0;
                Toast.makeText(mContext, "Please try later", Toast.LENGTH_SHORT).show();
                decideWinner();
            }
        });
        Shopfitt.getInstance().addToRequestQueue(fetchLocations, "locationapi");
    }

    private void getOpponentFinalPercent() {
        VolleyRequest<String> fetchLocations = new VolleyRequest<String>(Request.Method.GET,
                "http://23.91.69.85:61090/ProductService.svc/returnFinalPercentage/" + Config.comparerOrderID, String.class, null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String crunch) {
                        requestId++;
                        ((TextView) view.findViewById(R.id.opponent_final_percent)).setText(crunch);
                        opponentpercent = Double.parseDouble(crunch);
                        decideWinner();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                requestId++;
                opponentpercent = 0.0;
                Toast.makeText(mContext, "Please try later", Toast.LENGTH_SHORT).show();
                decideWinner();
            }
        });
        Shopfitt.getInstance().addToRequestQueue(fetchLocations, "locationapi");
    }

    private void decideWinner() {
        String message = "";
        if (requestId == 2) {
            if (selfpercent < opponentpercent) {
                Config.crunchWon = true;
                double diff = selfpercent - opponentpercent;
                if (diff <= 3.0) {
                    message = "You Win! CLOSE CALL! 50 CRUNCHES WON.";
                }
                if (diff > 3.0 && diff <= 5.0) {
                    message = "You Win! Awesomeness! 50 CRUNCHES WON";
                }
                if (diff > 5.0) {
                    message = "You Win! Sublime! 50 CRUNCHES WON";
                }
//                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(mContext)
                        .setTitle(getResources().getString(R.string.app_name))
                        .setMessage(message)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showThankyou();
                            }
                        })
                        .show();
            } else {
                Config.crunchWon = false;
                showThankyou();
            }
//            showThankyou();
        }
    }

    private void showThankyou() {
        final Handler handler = new Handler();
        final Runnable r = new Runnable()
        {
            public void run()
            {
                Fragment fragment = new CrunchFragmentTwo();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
            }
        };
        handler.postDelayed(r, 15000);
    }
}
