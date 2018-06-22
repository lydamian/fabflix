import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;

	
	
/**
 * A servlet that takes input from a html <form> and talks to MySQL moviedb,
 * generates output as a json
*/
// Declaring a WebServlet called SearchServlet, which maps to url "/form"
@WebServlet(name = "Results", urlPatterns = "/results")
public class Results extends HttpServlet{
	private static final long serialVersionUID = 1L;

	    /**
	     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	     */
	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	        response.setContentType("application/json"); // Response mime type
	        	
	        // Output stream to STDOUT
	        PrintWriter out = response.getWriter();

	        try {
	        	Context initCtx = new InitialContext();

	            Context envCtx = (Context) initCtx.lookup("java:comp/env");
	            if (envCtx == null)
	                System.out.println("envCtx is NULL");

	            // Look up our data source
	            DataSource dataSource = (DataSource) envCtx.lookup("jdbc/TestDB");

	            // the following commented lines are direct connections without pooling
	            //Class.forName("org.gjt.mm.mysql.Driver");
	            //Class.forName("com.mysql.jdbc.Driver").newInstance();
	            //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

	            if (dataSource == null)
	                System.out.println("ds is null."); 	
	        	
	            // Get a connection from dataSource
	            Connection dbcon = dataSource.getConnection();

	            // Declare our statement
	            Statement statement = dbcon.createStatement();
	            
	            System.out.println("Results Servlet page called...");
	         
	            // get title(substring), year, director(substring) , stars name(substring) (get method)
	            String title;
	            String year;
	            String director;
	            String stars_name;
	            
	            title = request.getParameter("title");
	            year = request.getParameter("year");
	            director = request.getParameter("director");
	            stars_name = request.getParameter("stars_name");
	            
	            if(title == null) {
	            	title = "%";
	            }
	            if(director == null) {
	            	director = "%";
	            }
	            if(stars_name == null || stars_name.equals(null)) {
	            	stars_name = "%";
	            }
	            
	            System.out.println("Title is: " + title);
	            System.out.println("year is: " + year);
	            System.out.println("director is: " + director);
	            System.out.println("stars_name is: " + stars_name);
	            
	            
	
	            String query;

/*
	            query = "SELECT distinct movies.id as id, title, year, director, GROUP_CONCAT(distinct G1.name) as genres, GROUP_CONCAT(distinct S1.name) as stars, rating\r\n" + 
	            		"FROM movies, stars_in_movies, stars as S1, genres as G1, genres_in_movies, ratings\r\n" + 
	            		"WHERE movies.id = stars_in_movies.movieId AND \r\n" + 
	            		"	stars_in_movies.starId = S1.id AND movies.id = genres_in_movies.movieId AND genres_in_movies.genreId = G1.id ANd\r\n" + 
	            		"    movies.id = ratings.movieId AND\r\n" + 
	            		"    movies.title like \"%home%\" AND\r\n" + 
	            		"    movies.director like \"%Danae%\" AND\r\n" + 
	            		"    S1.name like \"%%%\" \r\n" + 
	            		"GROUP BY title;";
*/
	          

	            //generate query
	            if(year.equals("")) {
	            	//System.out.println("if block entered...");
	            	query = "SELECT distinct movies.id as id, title, year, director, GROUP_CONCAT(distinct G1.name) as genres, GROUP_CONCAT(distinct S1.name) as stars, rating\r\n" + 
		            		"FROM movies, stars_in_movies, stars as S1, genres as G1, genres_in_movies, ratings\r\n" + 
		            		"WHERE movies.id = stars_in_movies.movieId AND \r\n" + 
		            		"	stars_in_movies.starId = S1.id AND movies.id = genres_in_movies.movieId AND genres_in_movies.genreId = G1.id ANd\r\n" + 
		            		"    movies.id = ratings.movieId AND\r\n" + 
		            		"    movies.title like " + "\"%" + title+"%\"" + "AND\r\n" + 
		            		"    movies.director like " + "\"%"+director+"%\"" + "AND\r\n" +
		            		"    S1.name like " + "\"%" +stars_name + "%\"\r\n" +
		            		"GROUP BY title;";

	            }
	            else {
	            	//System.out.println("else block entered...");
	            	query = "SELECT distinct movies.id as id, title, year, director, GROUP_CONCAT(distinct G1.name) as genres, GROUP_CONCAT(distinct S1.name) as stars, rating\r\n" + 
		            		"FROM movies, stars_in_movies, stars as S1, genres as G1, genres_in_movies, ratings\r\n" + 
		            		"WHERE movies.id = stars_in_movies.movieId AND \r\n" + 
		            		"	stars_in_movies.starId = S1.id AND movies.id = genres_in_movies.movieId AND genres_in_movies.genreId = G1.id ANd\r\n" + 
		            		"    movies.id = ratings.movieId AND\r\n" + 
		            		"    movies.year = " + Integer.parseInt(year) + " AND\r\n" +
		            		"    movies.title like " + "\"%" + title + "%\"" + " AND\r\n" + 
		            		"    movies.director like " + "\"%" + director + "%\"" + " AND\r\n" + 
		            		"    S1.name like " + "\"%" + stars_name + "%\"\r\n" + 
		            		"GROUP BY title;";
	            	           	
	            }


	            // Perform the query
	            ResultSet rs = statement.executeQuery(query);

	            JsonArray jsonArray = new JsonArray();

	            // Iterate through each row of rs
	            while (rs.next()) {
	                String id = rs.getString("id");
	                String movie_title = rs.getString("title");
	                int movie_year = rs.getInt("year");
	                String movie_director = rs.getString("director");
	                String list_of_genres = rs.getString("genres");
	                String list_of_stars = rs.getString("stars");
	                String rating = rs.getString("rating");
	                

	                // Create a JsonObject based on the data we retrieve from rs
	                JsonObject jsonObject = new JsonObject();
	                jsonObject.addProperty("id", id);
	                jsonObject.addProperty("title", movie_title);
	                jsonObject.addProperty("year", movie_year);
	                jsonObject.addProperty("director", movie_director);
	                jsonObject.addProperty("genres", list_of_genres);
	                jsonObject.addProperty("stars", list_of_stars);
	                jsonObject.addProperty("rating", rating);

	                jsonArray.add(jsonObject);
	            }
	            
	            // write JSON string to output
	            out.write(jsonArray.toString());
	            //System.out.println(jsonArray.toString());
	            // set response status to 200 (OK)
	            response.setStatus(200);

	            rs.close();
	            statement.close();
	            dbcon.close();
	        } catch (Exception e) {
	        	
				// write error message JSON object to output
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("errorMessage", e.getMessage());
				out.write(jsonObject.toString());

				// set reponse status to 500 (Internal Server Error)
				response.setStatus(500);

	        }
	        out.close();
	    }

}

	
	
	
	
	
	
	
	

