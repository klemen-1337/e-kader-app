package com.example.ekadri;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private TextView zaposleni;
    private String url = "https://e-kadri-dev.azurewebsites.net/api/ZaposlenApi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        zaposleni = (TextView) findViewById(R.id.zaposleni);
    }

    public void prikaziZaposlene(View view){
        if(view != null){
            JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener);
            requestQueue.add(request);
        }
    }

    private Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            ArrayList<String> data = new ArrayList<>();

            for(int i = 0; i < response.length(); i++){

                try {
                    JSONObject object = response.getJSONObject(i);
                    String ime = object.getString("ime");
                    String priimek = object.getString("priimek");

                    data.add(ime + " " + priimek);

                }catch (JSONException e){
                    e.printStackTrace();
                    return;
                }
            }

            for(String row : data){

                String current = zaposleni.getText().toString();
                zaposleni.setText(current + "\n\n" + row);

            }

        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };

}