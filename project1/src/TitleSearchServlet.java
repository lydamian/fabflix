 

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class TitleSearchServlet
 */
@WebServlet("/TitleSearchServlet")
public class TitleSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TitleSearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

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
     
            System.out.println("TitleSearchServlet called...");
         
            // get genre
            String title;
            title = request.getParameter("title");
            
            if(title == null) {
            	out.println("ERROR, title FIELD IS EMPTY IN GENRESEARCHSERVLET....");
            }
            else {
            	System.out.println("Title is: " + title);
            }
            
            String query;
  
            //generate query
            	query = "SELECT distinct movies.id as id, title, year, director, GROUP_CONCAT(distinct G1.name) as genres, GROUP_CONCAT(distinct S1.name ORDER BY S1.id) as stars, GROUP_CONCAT(distinct S1.id) as stars_id, rating\r\n" + 
	            		"FROM movies, stars_in_movies, stars as S1, genres as G1, genres_in_movies, ratings\r\n" + 
	            		"WHERE movies.id = stars_in_movies.movieId AND \r\n" + 
	            		"	stars_in_movies.starId = S1.id AND movies.id = genres_in_movies.movieId AND genres_in_movies.genreId = G1.id ANd\r\n" + 
	            		"    movies.id = ratings.movieId AND\r\n" + 
	            		"    movies.title like ? \r\n" + 
	            		"GROUP BY title;";


        	// Declare our statement
			PreparedStatement statement = dbcon.prepareStatement(query);

			// Set the parameter represented by "?" in the query to the id we get from url,
			// num 1 indicates the first "?" in the query
			statement.setString(1,title + "%");

			// Perform the query
			ResultSet rs = statement.executeQuery();

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
