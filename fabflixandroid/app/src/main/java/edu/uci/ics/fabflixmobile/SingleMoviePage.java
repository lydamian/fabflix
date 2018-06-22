package edu.uci.ics.fabflixmobile;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SingleMoviePage extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie_page);
        System.out.println("SingleMovieActivity Page called");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String results = bundle.get("info").toString();
        System.out.println("SingleMoviePage got  " + results);

        String parsed[] = results.split("/");

        String title = "Title: " + parsed[0];
        String year = "Year " + parsed[1];
        String director = "Director" + parsed[2];
        String genre = "Genre: " +parsed[4];
        String stars = "Stars: " +  parsed[3];


        System.out.println("CHECKING SINGLE MOVIE RESULTS");
        System.out.println(title + "///" +  year + "///" + director+ "///" + genre + "///" + stars);

        TextView myText1 = (TextView)findViewById(R.id.myText1);
        TextView myText2 = (TextView)findViewById(R.id.myText2);
        TextView myText3 = (TextView)findViewById(R.id.myText3);
        TextView myText4 = (TextView)findViewById(R.id.myText4);
        TextView myText5 = (TextView)findViewById(R.id.myText5);


        myText1.setText(title);
        myText2.setText(year);
        myText3.setText(director);
        myText4.setText(genre);
        myText5.setText(stars);



        //get id

        // search for single movie and display.
    }
}
