package com.example.ekadri;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddZaposleni extends AppCompatActivity {

    private TextView status;
    private EditText oddelek;
    private EditText lokacija;
    private EditText NazivDelovnegaMesta;


    private RequestQueue requestQueue;
    private String url = "https://e-kadri-dev.azurewebsites.net/api/DelovnaMestaApi";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_zaposleni);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        status = (TextView) findViewById(R.id.status);
        oddelek = (EditText) findViewById(R.id.oddelek);
        lokacija = (EditText) findViewById(R.id.lokacija);
        NazivDelovnegaMesta = (EditText) findViewById(R.id.NazivDelovnegaMesta);
        

    }

    @SuppressLint("SetTextI18n")
    public void addZaposleni(View view){

        this.status.setText("Pošiljam na " + url);

        try {
            Random rn = new Random();
            int id=rn.nextInt(30000);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("delovnaMestaId", id);
            jsonObject.put("oddelek", oddelek.getText());
            jsonObject.put("lokacija", lokacija.getText());
            jsonObject.put("nazivDelovnegaMesta", NazivDelovnegaMesta.getText());

            

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
                public Map<String,String> getHeaders() throws AuthFailureError
                {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("ApiKey","PetZaDvjst");
                    params.put("Content-Type","application/json");
                    return params;
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