
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

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

// server endpoint URL
@WebServlet("/movie-suggestion")
public class MovieSuggestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public MovieSuggestion() {
        super();
    }

    
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.setContentType("application/json"); // Response mime type
		// Output stream to STDOUT
        PrintWriter out = response.getWriter();
		try {
			System.out.println("Movie suggestion servlet called!");
			
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
				
				// setup the response json arrray
				JsonArray jsonArray = new JsonArray();
				
				// get the query string from parameter
				String sug_query = request.getParameter("query");
				
				// return the empty json array if query is null or empty
				if (sug_query == null || sug_query.trim().isEmpty()) {
					out.write(jsonArray.toString());
					return;
				}	
	            // Get a connection from dataSource
	            Connection dbcon = dataSource.getConnection();
	     
	            System.out.println("Search Servlet page called...");
	         
	            
	            System.out.println("Sug query is: " + sug_query);
	            
	            String query;
	  
	          ResultSet rs;

	            	//System.out.println("if block entered...");
	            	query= "SELECT distinct movies.id as id, title\r\n" + 
	            			"FROM movies\r\n" + 
	            			"WHERE MATCH(title) AGAINST (? in boolean mode)\r\n" + 
	            			"LIMIT 10;";

	            	
	            	// Declare our statement
	    			PreparedStatement statement = dbcon.prepareStatement(query);

	    			// Set the parameter represented by "?" in the query to the id we get from url,
	    			// num 1 indicates the first "?" in the query
	    			String[] search_terms = sug_query.split("\\s+");
	    			String search_expression = "+" + search_terms[0] + "*";
	    			for (int i = 1 ; i < search_terms.length; ++i)
	    			{
	    				search_expression = search_expression + " +" + search_terms[i] + "*";
	    			}
	    			System.out.println("expression is: " + search_expression);
	    			statement.setString(1, search_expression);
	    		
	    			// Perform the query
	    			rs = statement.executeQuery();
	    			
	    			 // Iterate through each row of rs
		            while (rs.next()) {
		                String id = rs.getString("id");
		                String movie_title = rs.getString("title");

		                jsonArray.add(generateJsonObject(id, movie_title));
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

	            

		           
	        } catch (Exception e) {
	        	
				System.out.println("SQL error in movie suggestion!");
				// set reponse status to 500 (Internal Server Error)
				response.setStatus(500);

	        }
			
			return;
		} catch (Exception e) {
			System.out.println(e);
			response.sendError(500, e.getMessage());
		}
	}
	

	private static JsonObject generateJsonObject(String movieID, String movieTitle) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("value", movieTitle);
		
		JsonObject additionalDataJsonObject = new JsonObject();
		additionalDataJsonObject.addProperty("category", "movie");
		additionalDataJsonObject.addProperty("id", movieID);
		additionalDataJsonObject.addProperty("title", movieTitle);
		
		jsonObject.add("data", additionalDataJsonObject);
		return jsonObject;
	}


}
