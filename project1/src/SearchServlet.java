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

import java.io.File;
import java.io.FileWriter;
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
@WebServlet(name = "SearchServlet", urlPatterns = "/search")
public class SearchServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	long TSstartTime = System.nanoTime();
	long TJstartTime;
	long TJendTime;
	long TJelapsedTime;

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
	                out.println("envCtx is NULL");

	            // Look up our data source
	            DataSource dataSource = (DataSource) envCtx.lookup("jdbc/TestDB");

	            // the following commented lines are direct connections without pooling
	            //Class.forName("org.gjt.mm.mysql.Driver");
	            //Class.forName("com.mysql.jdbc.Driver").newInstance();
	            //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

	            if (dataSource == null)
	                out.println("ds is null.");

	            System.out.println("Datasource is: " + dataSource);
	            System.out.println("envCtx is: " + envCtx);
	        	
	            // Get a connection from dataSource
	            Connection dbcon = dataSource.getConnection();
	            if (dbcon == null)
	                out.println("dbcon is null.");
	         
	            System.out.println("Search Servlet page called...");
	         
	            // get title(substring), year, director(substring) , stars name(substring) (get method)
	            String title;
	            String year;
	            String director;
	            String stars_name;
	         
	            
	            title = request.getParameter("title");
	            year = request.getParameter("year");
	            director = request.getParameter("directors");
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
	  
	          ResultSet rs;

	            //generate query
	            if(year.equals("")) {
	            	JsonArray jsonArray = new JsonArray();
	            	//System.out.println("if block entered...");
	            	/*query = "SELECT distinct movies.id as id, title, year, director, GROUP_CONCAT(distinct G1.name) as genres, GROUP_CONCAT(distinct S1.name ORDER BY S1.id) as stars, GROUP_CONCAT(distinct S1.id) as stars_id, rating\r\n" + 
		            		"FROM movies, stars_in_movies, stars as S1, genres as G1, genres_in_movies, ratings\r\n" + 
		            		"WHERE movies.id = stars_in_movies.movieId AND \r\n" + 
		            		"	stars_in_movies.starId = S1.id AND movies.id = genres_in_movies.movieId AND genres_in_movies.genreId = G1.id ANd\r\n" + 
		            		"    movies.id = ratings.movieId AND\r\n" + 
		            		"    movies.title like ? AND\r\n" + 
		            		"    movies.director like ? AND\r\n" +
		            		"    S1.name like? \r\n" +
		            		"GROUP BY title;";*/
	            	
	            	/*
	            	 * query = "SELECT distinct movies.id as id, title, year, director, GROUP_CONCAT(distinct G1.name) as genres, GROUP_CONCAT(distinct S1.name ORDER BY S1.id) as stars, GROUP_CONCAT(distinct S1.id) as stars_id, rating\r\n" + 
	            			"FROM movies, stars_in_movies, stars as S1, genres as G1, genres_in_movies, ratings\r\n" + 
	            			"WHERE movies.id = stars_in_movies.movieId AND\r\n" + 
	            			"	stars_in_movies.starId = S1.id AND movies.id = genres_in_movies.movieId AND genres_in_movies.genreId = G1.id ANd \r\n" + 
	            			"	movies.id = ratings.movieId AND\r\n" + 
	            			"	( match(movies.title) against (? in boolean mode) OR\r\n" + 
	            			"	ed(movies.title, ?) <= 3 ) AND\r\n" + 
	            			"	movies.director like ? AND\r\n" + 
	            			"	S1.name like ? \r\n" + 
	            			"GROUP BY id, title, year, director, rating\r\n" + 
	            			"order by (ed( ? ,movies.title)) asc \r\n" +
	            			"LIMIT 1000;";
	            	*/
	            	TJstartTime = System.nanoTime();
	            	 query = "SELECT distinct movies.id as id, title, year, director, GROUP_CONCAT(distinct G1.name) as genres, GROUP_CONCAT(distinct S1.name ORDER BY S1.id) as stars, GROUP_CONCAT(distinct S1.id) as stars_id, rating\r\n" + 
		            			"FROM movies, stars_in_movies, stars as S1, genres as G1, genres_in_movies, ratings\r\n" + 
		            			"WHERE movies.id = stars_in_movies.movieId AND\r\n" + 
		            			"	stars_in_movies.starId = S1.id AND movies.id = genres_in_movies.movieId AND genres_in_movies.genreId = G1.id ANd \r\n" + 
		            			"	movies.id = ratings.movieId AND\r\n" + 
		            			"	 match(movies.title) against (? in boolean mode) AND\r\n" + 
		            			"	movies.director like ? AND\r\n" + 
		            			"	S1.name like ? \r\n" + 
		            			"GROUP BY id, title, year, director, rating\r\n" + 
		            			"LIMIT 1000;";
	            	
	            	// Declare our statement
	    			PreparedStatement statement = dbcon.prepareStatement(query);

	    			// Set the parameter represented by "?" in the query to the id we get from url,
	    			// num 1 indicates the first "?" in the query
	    			//statement.setString(1,"%" + title + "%");
	    			String[] search_terms = title.split("\\s+");
	    			String search_expression = "+" + search_terms[0] + "*";
	    			for (int i = 1 ; i < search_terms.length; ++i)
	    			{
	    				search_expression = search_expression + " +" + search_terms[i] + "*";
	    			}
	    			System.out.println("expression is: " + search_expression);
	    			statement.setString(1, search_expression);
	    			statement.setString(2, "%" + director + "%");
	    			statement.setString(3,"%" + stars_name + "%");

	    			// Perform the query
	    			rs = statement.executeQuery();
	    			TJendTime = System.nanoTime();
	    			TJelapsedTime = TJendTime - TJstartTime;
	    			System.out.println("TJ time: " + TJelapsedTime);
	    			
	    			 // Iterate through each row of rs
		            while (rs.next()) {
		                String id = rs.getString("id");
		                String movie_title = rs.getString("title");
		                int movie_year = rs.getInt("year");
		                String movie_director = rs.getString("director");
		                String list_of_genres = rs.getString("genres");
		                String list_of_stars = rs.getString("stars");
		                String rating = rs.getString("rating");
		                String stars_id = rs.getString("stars_id");
		                

		                // Create a JsonObject based on the data we retrieve from rs
		                JsonObject jsonObject = new JsonObject();
		                jsonObject.addProperty("id", id);
		                jsonObject.addProperty("title", movie_title);
		                jsonObject.addProperty("year", movie_year);
		                jsonObject.addProperty("director", movie_director);
		                jsonObject.addProperty("genres", list_of_genres);
		                jsonObject.addProperty("stars", list_of_stars);
		                jsonObject.addProperty("rating", rating);
		                jsonObject.addProperty("stars_id", stars_id);

		                jsonArray.add(jsonObject);
		            }
		            
		            // write JSON string to output
		            out.write(jsonArray.toString());
		            System.out.println(jsonArray.toString());
		            // set response status to 200 (OK)
		            response.setStatus(200);
		            System.out.println("Success!!!");
		            rs.close();
		            dbcon.close();
	    			statement.close();
	    			

	            }
	            else {
	            	JsonArray jsonArray = new JsonArray();
	            	//System.out.println("else block entered...");
	            	query = "SELECT distinct movies.id as id, title, year, director, GROUP_CONCAT(distinct G1.name) as genres, GROUP_CONCAT(distinct S1.name ORDER BY S1.id) as stars, GROUP_CONCAT(distinct S1.id) as stars_id, rating\r\n" + 
		            		"FROM movies, stars_in_movies, stars as S1, genres as G1, genres_in_movies, ratings\r\n" + 
		            		"WHERE movies.id = stars_in_movies.movieId AND \r\n" + 
		            		"	stars_in_movies.starId = S1.id AND movies.id = genres_in_movies.movieId AND genres_in_movies.genreId = G1.id ANd\r\n" + 
		            		"    movies.id = ratings.movieId AND\r\n" + 
		            		"    movies.year = ? AND\r\n" +
		            		"    movies.title like ? AND\r\n" + 
		            		"    movies.director like ? AND\r\n" + 
		            		"    S1.name like ? \r\n" + 
		            		"GROUP BY title;";
	            	
	            	// Declare our statement
	    			PreparedStatement statement = dbcon.prepareStatement(query);

	    			// Set the parameter represented by "?" in the query to the id we get from url,
	    			// num 1 indicates the first "?" in the query
	    			statement.setString(1,year);
	    			statement.setString(2,"%" + title + "%");
	    			statement.setString(3, "%" + director + "%");
	    			statement.setString(4,"%" + stars_name + "%");
	    			

	    			// Perform the query
	    			rs = statement.executeQuery();
	    			 // Iterate through each row of rs
		            while (rs.next()) {
		                String id = rs.getString("id");
		                String movie_title = rs.getString("title");
		                int movie_year = rs.getInt("year");
		                String movie_director = rs.getString("director");
		                String list_of_genres = rs.getString("genres");
		                String list_of_stars = rs.getString("stars");
		                String rating = rs.getString("rating");
		                String stars_id = rs.getString("stars_id");
		                

		                // Create a JsonObject based on the data we retrieve from rs
		                JsonObject jsonObject = new JsonObject();
		                jsonObject.addProperty("id", id);
		                jsonObject.addProperty("title", movie_title);
		                jsonObject.addProperty("year", movie_year);
		                jsonObject.addProperty("director", movie_director);
		                jsonObject.addProperty("genres", list_of_genres);
		                jsonObject.addProperty("stars", list_of_stars);
		                jsonObject.addProperty("rating", rating);
		                jsonObject.addProperty("stars_id", stars_id);

		                jsonArray.add(jsonObject);
		            }
		            
		            // write JSON string to output
		            out.write(jsonArray.toString());
		            System.out.println(jsonArray.toString());
		            // set response status to 200 (OK)
		            response.setStatus(200);
		            System.out.println("Success!!!");
		            rs.close();
		            dbcon.close();
	    			statement.close();

	            }

	            

	           
	        } catch (Exception e) {
	        	
				// write error message JSON object to output
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("errorMessage", e.getMessage());
				out.write(jsonObject.toString());

				// set reponse status to 500 (Internal Server Error)
				response.setStatus(500);

	        }
	        long TSendTime = System.nanoTime();
		    long TSelapsedTime = TSendTime - TSstartTime;
		    
		    System.out.println("TS time: " + TSelapsedTime);
		    
		    String contextPath = request.getServletContext().getRealPath("/");

		    String xmlFilePath=contextPath + "log.txt";

		    System.out.println(xmlFilePath);

		    File myfile = new File(xmlFilePath);

		    if (!myfile.exists())
		    {
		    	myfile.createNewFile();
		    }
		    FileWriter writer = new FileWriter(myfile, true); 
		    writer.write(TSelapsedTime + "," + TJelapsedTime + "\n");
		    writer.flush();
		      writer.close();
	        out.close();
	    }
	    

}

	
	
	
	
	
	
	
	

