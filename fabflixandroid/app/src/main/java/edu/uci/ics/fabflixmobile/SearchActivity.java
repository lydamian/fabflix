package edu.uci.ics.fabflixmobile;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class SearchActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("SearchActivity Activity called...");
        setContentView(R.layout.activity_search);
    }

    public void searchBackend(View view){
        System.out.println("searchBackendFunction called...");

        // get the result of user input
        EditText searchEditText = (EditText) findViewById(R.id.search_input_EditText);
        String searchValue = searchEditText.getText().toString();
        System.out.println("search value is: " + searchValue);

        //String url = "http://10.0.2.2:8080/project1/search?title="+searchValue+"&year=#&date=#&star=#";
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority("10.0.2.2:8080")
                .appendPath("project1")
                .appendPath("search")
                .appendQueryParameter("title",searchValue)
                .appendQueryParameter("year","")
                .appendQueryParameter("directors","")
                .appendQueryParameter("stars_name","");
        String myUrl = builder.build().toString();

        System.out.println(myUrl);


        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        final StringRequest searchRequest = new StringRequest(Request.Method.GET, myUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("searchRequest function called..");
                        Log.d("response", response);
                        System.out.println("response is: " + response);

                        // make response into json object
                        try {
                            JSONArray jsonObj = new JSONArray(response);
                            System.out.println("the parsed object is" + jsonObj.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // pass json object to another activity
                        Intent goToSearch = new Intent(getApplicationContext(), SearchResultList.class);
                        goToSearch.putExtra("search_results", response);
                        startActivity(goToSearch);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("security.error", error.toString());
                    }
                } ) {
        };
        queue.add(searchRequest);

        System.out.println("test");


    }
}