package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RedActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red);

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_red, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

        public void connectToTomcat(View view) {

            // get user input
            EditText login_username = (EditText) findViewById(R.id.login_username);
            String username = login_username.getText().toString();

            EditText login_password = (EditText) findViewById(R.id.login_password);
            String password = login_password.getText().toString();

            System.out.println("login_username is: " + username);
            System.out.println("login_passowrd is: " + password);
            // send username and password to server to validate
            //redirect if(correct) to fabflix_android landing page

            // Post request form data
        final Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);

        // no user is logged in, so we must connect to the server

        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        final StringRequest loginRequest = new StringRequest(Request.Method.POST, "http://10.0.2.2:8080/project1/api/android-login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("loginRequest function called..");
                        Log.d("response", response);
                        String myResponse = response.toString();
                            // redirect to search activity

                        String myStatus = response;
                        System.out.println("mystatus is: " +  myStatus);

                        if(myStatus.toLowerCase().contains("success")){
                            System.out.println("SUCESS");

                            Intent goToSearch = new Intent(getApplicationContext(), SearchActivity.class);
                            startActivity(goToSearch);
                        }
                        else
                        {
                            ((TextView) findViewById(R.id.http_response1)).setText("FAILURE");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("security.error", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }  // HTTP POST Form Data
        };
        queue.add(loginRequest);
    }
}
