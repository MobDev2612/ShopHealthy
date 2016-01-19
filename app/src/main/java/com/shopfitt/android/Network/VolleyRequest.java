package com.shopfitt.android.Network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class VolleyRequest<T> extends Request<T> {
    private static final Gson GSON_VERIFY = new Gson();
    private static final String TAG = "VolleyRequest";

    private final Class<T> clazz;
    private final Response.Listener<T> listener;
    String resCodeStr;
    private Map<String, String> headers;
    private JSONObject parameters = null;
    private Gson gson;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url     URL of the request to make
     * @param clazz   Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     */
    public VolleyRequest(int method, String url, Class<T> clazz, Map<String, String> headers,
                         Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
        gson = new Gson();
    }

    public VolleyRequest(int method, String url, Class<T> clazz, Map<String, String> headers,
                         Response.Listener<T> listener, Response.ErrorListener errorListener, JSONObject parameters) {
        this(method, url, clazz, headers, listener, errorListener);
        this.parameters = parameters;
        gson = new Gson();
    }


    /**
     * Gets header to send header in volley request
     *
     * @return Map<StringString>
     * @throws AuthFailureError
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Content-Type", "application/json");
        return headers;
    }

    /**
     * Get Content type to send in Volley request
     *
     * @return String content type
     */
    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    /**
     * Converts JSONObject into bytes[] array
     *
     * @return byte[] of object
     * @throws AuthFailureError
     */
    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return parameters.toString().getBytes(getParamsEncoding());
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.toString(), e);
        }
        return null;
    }

    /**
     * Deceivers volley request response
     *
     * @param response The parsed response returned by
     */
    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    /**
     * Parses the network response in to json
     *
     * @param response Response from the network
     * @return Response<T> response object
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.e("Response",json);
            if (isJSONValid(json)) {
                if (json.length() > 1) {
                    return Response.success(
                            gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
                } else {
                    this.resCodeStr = json;
                    return (Response<T>) Response.success(clazz, HttpHeaderParser.parseCacheHeaders(response));
                }
            } else {
                throw new JsonSyntaxException("Object cannot be casted");
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage(), e);
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            Log.e(TAG, e.getMessage(), e);
            return Response.error(new VolleyError(response));
        }
    }

    /**
     * Returns response code
     *
     * @return String code
     */
    public String getResponseCode() {
        return resCodeStr;
    }

    /**
     * Gets priority
     *
     * @return Priority object
     */
    @Override
    public Priority getPriority() {
        return Priority.IMMEDIATE;
    }


    /**
     * Returns flag about is json valid or invalid
     *
     * @param responseString json string object
     * @return flag true if json is valid, false otherwise
     */
    private boolean isJSONValid(String responseString) {
        if (responseString != null && responseString.length() > 0) {
            try {
                new JSONObject(responseString);
            } catch (JSONException ex) {
                Log.e(TAG, ex.toString(), ex);
                try {
                    new JSONArray(responseString);
                } catch (JSONException ex1) {
                    Log.e(TAG, ex1.toString(), ex1);
                    return false;
                }
            }
            try {
                GSON_VERIFY.fromJson(responseString, this.clazz);
                return true;
            } catch (com.google.gson.JsonSyntaxException ex) {
                Log.e(TAG, ex.toString(), ex);
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Sets retry policy
     *
     * @param retryPolicy RetryPolicy
     */
    @Override
    public Request<T> setRetryPolicy(RetryPolicy retryPolicy) {
        super.setRetryPolicy(new DefaultRetryPolicy(60000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return this;
    }
}