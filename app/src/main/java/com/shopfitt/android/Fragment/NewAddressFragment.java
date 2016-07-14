package com.shopfitt.android.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shopfitt.android.R;
import com.shopfitt.android.Utils.Font;
import com.shopfitt.android.datamodels.CustomerAddress;

public class NewAddressFragment extends Fragment {

    private TextInputEditText address1, address2, area, city, landmark, pinCode, name, mobile;
    private View view;
    private OnFragmentInteractionListener mListener;
    private Context mContext;

    public NewAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_address, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button completeOrder = (Button) view.findViewById(R.id.delivery_order_complete);
        address1 = (TextInputEditText) view.findViewById(R.id.delivery_address_line_1);
        address2 = (TextInputEditText) view.findViewById(R.id.delivery_address_line_2);
        area = (TextInputEditText) view.findViewById(R.id.delivery_area);
        city = (TextInputEditText) view.findViewById(R.id.delivery_city);
        landmark = (TextInputEditText) view.findViewById(R.id.delivery_land_mark);
        pinCode = (TextInputEditText) view.findViewById(R.id.delivery_pin_code);
        name = (TextInputEditText) view.findViewById(R.id.delivery_name);
        mobile = (TextInputEditText) view.findViewById(R.id.delivery_mobile_number);

        completeOrder.setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));
        address1.setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));
        address2.setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));
        area.setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));
        city.setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));
        landmark.setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));
        pinCode.setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));
        name.setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));
        mobile.setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));

        ((TextInputLayout) view.findViewById(R.id.delivery_area_layout)).setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));
        ((TextInputLayout) view.findViewById(R.id.delivery_address_line_1_layout)).setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));
        ((TextInputLayout) view.findViewById(R.id.delivery_address_line_2_layout)).setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));
        ((TextInputLayout) view.findViewById(R.id.delivery_city_layout)).setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));
        ((TextInputLayout) view.findViewById(R.id.delivery_land_mark_layout)).setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));
        ((TextInputLayout) view.findViewById(R.id.delivery_pin_code_layout)).setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));
        ((TextInputLayout) view.findViewById(R.id.delivery_name_layout)).setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));
        ((TextInputLayout) view.findViewById(R.id.delivery_mobile_number_layout)).setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));


        completeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean all = true;
                try {
                    String areaName = area.getText().toString();
                    String addressText1 = address1.getText().toString();
                    String addressText2 = address2.getText().toString();
                    String cityName = city.getText().toString();
                    String landMark = landmark.getText().toString();
                    String pinCodeText = pinCode.getText().toString();
                    String nameText = name.getText().toString();
                    String mobileText = mobile.getText().toString();

                    if (areaName.isEmpty()) {
                        ((TextInputLayout) view.findViewById(R.id.delivery_area_layout)).setError(getResources().getString(R.string.error_field_required));
                        all = false;
                    }
                    if (addressText1.isEmpty()) {
                        all = false;
                        ((TextInputLayout) view.findViewById(R.id.delivery_address_line_1_layout)).setError(getResources().getString(R.string.error_field_required));
                    }
                    if (addressText2.isEmpty()) {
                        all = false;
                        ((TextInputLayout) view.findViewById(R.id.delivery_address_line_2_layout)).setError(getResources().getString(R.string.error_field_required));
                    }
                    if (cityName.isEmpty()) {
                        all = false;
                        ((TextInputLayout) view.findViewById(R.id.delivery_city_layout)).setError(getResources().getString(R.string.error_field_required));
                    }
                    if (landMark.isEmpty()) {
                        all = false;
                        ((TextInputLayout) view.findViewById(R.id.delivery_land_mark_layout)).setError(getResources().getString(R.string.error_field_required));
                    }
                    if (pinCodeText.isEmpty()) {
                        all = false;
                        ((TextInputLayout) view.findViewById(R.id.delivery_pin_code_layout)).setError(getResources().getString(R.string.error_field_required));
                    }
                    if (nameText.isEmpty()) {
                        all = false;
                        ((TextInputLayout) view.findViewById(R.id.delivery_name_layout)).setError(getResources().getString(R.string.error_field_required));
                    }
                    if (mobileText.isEmpty()) {
                        all = false;
                        ((TextInputLayout) view.findViewById(R.id.delivery_mobile_number_layout)).setError(getResources().getString(R.string.error_field_required));
                    }

                    if (all) {
                        CustomerAddress customerAddress = new CustomerAddress(addressText1, addressText2
                                , cityName, landMark, mobileText, pinCodeText, nameText, "", areaName);
                        onButtonPressed(customerAddress);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void onButtonPressed(CustomerAddress customerAddress) {
        if (mListener != null) {
            mListener.onFragmentInteraction(customerAddress);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(CustomerAddress customerAddress);
    }
}
