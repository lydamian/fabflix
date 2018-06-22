package edu.uci.ics.fabflixmobile;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SearchResultList extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ArrayList<Movie> arrayMovieData = new ArrayList<Movie>();
    //create own class
    class Movie{
        public String toString(){
            return (title +"/" + year + "/" + director + "/" + list_of_genres + "/" + list_of_stars);
        }

        String title;
        String year;
        String director;
        String list_of_genres;
        String list_of_stars;
    }

    static ArrayList<String> resultRow;
    FancyAdapter aa = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_list);

        System.out.println("SearchResultList Activity Page called");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String results = bundle.get("search_results").toString();
        System.out.println("SearchResultList got  " + results);

        // onclick listener to get position of movie

        // parse info and display on omvielist then we need to do pagination which idk how.
        try {
            JSONArray jsonArr = new JSONArray(results);
            System.out.println("jsonArr is: " + jsonArr.toString());

            for(int i = 0; i < jsonArr.length(); i++){
                JSONObject c = jsonArr.getJSONObject(i);

                Movie resultRow = new Movie();
                //set that person's attribute
                resultRow.title = c.getString("title");
                resultRow.year = c.getString("year");
                resultRow.director = c.getString("director");
                resultRow.list_of_genres = c.getString("stars");
                resultRow.list_of_stars = c.getString("genres");
                //this is our arrayList object, we add our Person object to it
                arrayMovieData.add(resultRow);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListView myListView = (ListView)findViewById(R.id.search_result_ListView);
        myListView.setOnItemClickListener(this);

        //we initialize our fancy adapter object, we already declared it above
        //in the class definition
        aa=new FancyAdapter();

        // here we set the adapter, this turns it on
        myListView.setAdapter(aa);

    }

    class FancyAdapter extends ArrayAdapter<Movie> {
        FancyAdapter() {
            super(SearchResultList.this, android.R.layout.simple_list_item_1, arrayMovieData);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.row, null);

                //here is something new.  we are using a class called a view holder
                holder = new ViewHolder(convertView);
                //we are using that class to cache the result of the findViewById function
                //which we then store in a tag on the view
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.populateFrom(arrayMovieData.get(position));

            return (convertView);
        }

        class ViewHolder {
            public TextView title = null;
            public TextView year = null;
            public TextView director = null;
            public TextView stars = null;
            public TextView genres = null;

            ViewHolder(View row) {
                title = (TextView)row.findViewById(R.id.title_view);
                year = (TextView) row.findViewById(R.id.year_view);
                director = (TextView) row.findViewById(R.id.director_view);
                stars = (TextView) row.findViewById(R.id.stars_view);
                genres = (TextView) row.findViewById(R.id.genres_view);
            }

            //notice we had to change our populate from to take an argument of type person
            void populateFrom(Movie r) {
                title.setText(r.title);
                year.setText(r.year);
                director.setText(r.director);
                genres.setText(r.list_of_genres);
                stars.setText(r.list_of_stars);
            }
        }
    }

    //create a onclick function for the button that will redirect to the SingleMoviepage...
    public void goToSingleMovie(View view){
        System.out.println("goToSingleMovie function called...");
        TextView myButton = (TextView)view;
        String myValue = myButton.getText().toString();

        Intent goToSearch = new Intent(getApplicationContext(), SingleMoviePage.class);
        goToSearch.putExtra("title", myValue);
        startActivity(goToSearch);

    }


    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        Log.i("position", "clicked on position" + position);
        Movie myMovie = arrayMovieData.get(position);
        System.out.println(myMovie.toString());

        Intent movie_intent = new Intent(getApplicationContext(), SingleMoviePage.class);
        movie_intent.putExtra("info", myMovie.toString());

        movie_intent.putExtra("position", position);
        startActivity(movie_intent);
    }




}
