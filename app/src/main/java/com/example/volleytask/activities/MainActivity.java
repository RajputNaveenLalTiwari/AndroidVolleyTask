package com.example.volleytask.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.volleytask.R;
import com.example.volleytask.models.Feeds;
import com.example.volleytask.singletonclasses.VolleySingleton;
import com.example.volleytask.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private Context context = null;
    private RequestQueue requestQueue = null;
    private JsonArrayRequest jsonArrayRequest = null;
    private List<Feeds> feedsList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init()
    {
        context = this;
        feedsList = new ArrayList<>();
        requestQueue = VolleySingleton.getVolleySingletonInstance().getRequestQueue();
//        jsonArrayRequest = getJsonArrayRequest();
//        requestQueue.add(jsonArrayRequest);

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try
        {
//            jsonObject.put("id",106);
            jsonObject.put("name","Samsung Galaxy S Advance");
            jsonObject.put("description","Gingerbread Version Device");
            jsonObject.put("price",44);
            jsonArray.put(jsonObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.POST,
                "http://dev.superman-academy.com/api.php",
                jsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(context,response.toString(),Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        int id = 107;
        String url = String.format("%s/%d","http://dev.superman-academy.com/api.php",id);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(stringRequest);
    }

    private JsonArrayRequest getJsonArrayRequest()
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Utilities.GET_REQUEST_URL,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        parseJsonArray(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        handleVolleyError(error);
                    }
                });
        return jsonArrayRequest;
    }

    private void parseJsonArray(JSONArray response)
    {
        for (int i=0; i<response.length(); i++)
        {
            try
            {
                JSONObject root = response.getJSONObject(i);
                JSONObject parent_one = root.getJSONObject("address");
                JSONObject parent_two = root.getJSONObject("company");
                JSONObject parent_one_child_one = parent_one.getJSONObject("geo");

                Feeds feeds = new Feeds();
                Feeds.Address address = feeds.new Address();
                Feeds.Company company = feeds.new Company();
                Feeds.Address.Geo geo = address.new Geo();

                //  root items
                feeds.id        = root.getInt("id");
                feeds.name      = root.getString("name");
                feeds.username  = root.getString("username");
                feeds.email     = root.getString("email");

                    //  parent_one items
                    address.street = parent_one.getString("street");
                    address.suite = parent_one.getString("suite");
                    address.city = parent_one.getString("city");
                    address.zipcode = parent_one.getString("zipcode");
                    feeds.address = address;

                        //  parent_one_child_one items
                        geo.lat = parent_one_child_one.getString("lat");
                        geo.lng = parent_one_child_one.getString("lng");
                        feeds.geo = geo;

                feeds.phone     = root.getString("phone");
                feeds.website   = root.getString("website");

                    //  parent_two items
                    company.name = parent_two.getString("name");
                    company.catchPhrase = parent_two.getString("catchPhrase");
                    company.bs = parent_two.getString("bs");
                    feeds.company = company;

                feedsList.add(feeds);
            }
            catch (JSONException e)
            {
                Log.e(TAG,"Error In JSONObject"+e.getMessage());
            }
        }

        for (Feeds feeds:feedsList)
        {
            Log.i(TAG,feeds.company.name);
        }
    }

    private void handleVolleyError(VolleyError error)
    {
        if (error instanceof AuthFailureError || error instanceof TimeoutError)
        {
            Toast.makeText(context,"AuthFailureError/TimeoutError",Toast.LENGTH_LONG).show();
        }
        else if (error instanceof NoConnectionError)
        {
            Toast.makeText(context,"NoConnectionError",Toast.LENGTH_LONG).show();
        }
        else if (error instanceof NetworkError)
        {
            Toast.makeText(context,"NetworkError",Toast.LENGTH_LONG).show();
        }
        else if (error instanceof ServerError)
        {
            Toast.makeText(context,"ServerError",Toast.LENGTH_LONG).show();
        }
        else if (error instanceof ParseError)
        {
            Toast.makeText(context,"ParseError",Toast.LENGTH_LONG).show();
        }
    }
}
