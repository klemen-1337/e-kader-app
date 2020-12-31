package com.example.ekadri;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import android.os.Bundle;

public class AddZaposleni extends AppCompatActivity {

    private TextView status;
    private EditText name;
    private EditText surname;
    private EditText address;
    private EditText phone;
    private EditText date;
    private EditText gender;

    private RequestQueue requestQueue;
    private String url = "https://e-kadri-dev.azurewebsites.net/api/ZaposlenApi";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_zaposleni);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        status = (TextView) findViewById(R.id.status);
        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone);
        date = (EditText) findViewById(R.id.date);
        gender = (EditText) findViewById(R.id.gender);

    }

    @SuppressLint("SetTextI18n")
    public void addZaposleni(View view){

        this.status.setText("Pošiljam na " + url);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ime", name.getText());
            jsonObject.put("priimek", surname.getText());
            jsonObject.put("naslov", address.getText());
            jsonObject.put("telefon", phone.getText());
            jsonObject.put("datumRojstva", date.getText());
            jsonObject.put("spol", gender.getText());

            final String mRequest = jsonObject.toString();
            status.setText(mRequest);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                }
            }, error -> Log.e("LOG_VOLLEY", error.toString())
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return mRequest == null ? null : mRequest.getBytes(StandardCharsets.UTF_8);
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if(response != null){
                        responseString = String.valueOf(response.statusCode);
                        status.setText(responseString);
                    }
                    assert response != null;
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);

        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}