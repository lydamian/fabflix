

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.google.gson.JsonObject;
import com.mysql.jdbc.CallableStatement;

/**
 * Servlet implementation class AddMovieServlet
 */
@WebServlet("/AddMovieServlet")
public class AddMovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddMovieServlet() {
        super();
        // TODO Auto-generated constructor stub
    } 
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doGet in AddMovieServlet called...");	
	
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        ResultSet rs = null;
        
		 try {
			 	System.out.println("try block in AddMovieServlet page called...");
			 	
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

	            Connection dbcon = dataSource.getConnection();
	            if (dbcon == null)
	                out.println("dbcon is null.");
	            
	            String movieYear;
	            String movieDirector;
	            String movieId;
	            String movieTitle;
	            String starName;
	            String genreName;
	            String genreId = null;
	            String starId = null;
	            
	            // get movie title, movie year, movie director, star name, genre name
	            movieTitle = request.getParameter("movie_title");
	            movieYear = request.getParameter("movie_year");
	            movieDirector = request.getParameter("movie_director");
	            starName = request.getParameter("star_name");
	            genreName = request.getParameter("genre_name");
	        
	            System.out.println("movie title is: " + movieTitle );
	            System.out.println("movie year is: " + movieYear);
	            System.out.println("movie director is: " + movieDirector);
	            System.out.println("star name: " + starName);
	            System.out.println("genre name is: " + genreName);
	         	
	            // ====================== Logic Start =======================================================
	            
	            /* =============== CHECK IF THE MOVIE EXISTS ================================================*/
	            //generate query
	      		String query = "SELECT * FROM movies WHERE title =  ? AND year = ? and director = ?;";
	      		
	        	// Declare our statement
				PreparedStatement statement = dbcon.prepareStatement(query);

				// Set the parameter represented by "?" in the query to the id we get from url,
				statement.setString(1, movieTitle);
				statement.setString(2, movieYear);
				statement.setString(3, movieDirector);
				
				try {
				// Perform the query
				rs = statement.executeQuery();
				}catch(SQLException z){
					z.printStackTrace();
				}
				System.out.println(rs);
				
				if(rs.next() != false) { //means that movie is empty
					System.out.println("Movie previously existed");
					JsonObject jsonError = new JsonObject();
					jsonError.addProperty("status", "duplicate");
					out.write(jsonError.toString());
				}
				else {
					System.out.println("Movie has not previously existed");	
					
					/*create a new movie id*/
					// ---------------   find max id --------------------------------------------
		            int idMax = 0;
		            Statement stmt = null;
		            try {
		            	stmt = dbcon.createStatement();
		            	String sqlFindMaxId = "SELECT id FROM movies";
		            	ResultSet movieIdResult = stmt.executeQuery(sqlFindMaxId);
		            	while (movieIdResult.next())
		            	{
		            		String id = movieIdResult.getString("id");
		            		int idTrunc = Integer.parseInt(id.replaceAll("[^\\d]", ""));
		            		//System.out.println("ID is: " + id + "num is: " + idTrunc);
		            		if (idTrunc > idMax)
		            			idMax = idTrunc;
		            	}
		            	movieIdResult.close();
		            }
		            catch (SQLException e)
		            {
		            	e.printStackTrace();
		            }
		            idMax++;
		            String s = Integer.toString(idMax);
		            StringBuilder sb = new StringBuilder(s);
		            sb.insert(0, "tt");
		            s = sb.toString();
		           System.out.println("Max id is: " + idMax + " str is: " + s);
		            movieId = s;
		            System.out.println("New Movie Id is: " + movieId);
		            /* ----------------------------------------------------------------------*/
					
					
					/*-------------------------- check if the star exist - do nothing ---------*/
		            String starQuery = "SELECT * FROM stars WHERE name = ?;";
		            // Declare our statement
					PreparedStatement starStatement = dbcon.prepareStatement(starQuery);
	
					// Set the parameter represented by "?" in the query to the id we get from url,
					starStatement.setString(1, starName);
					
					try {
					// Perform the query
					rs = starStatement.executeQuery();
					}catch(SQLException p){
						p.printStackTrace();
					}
					System.out.println(rs);
		            
					if(rs.next() == false) {
						System.out.println("No Duplicate Found for stars, create a new star");
						/*get new star ID*/
						 // ---------------   find max id --------------------------------------------
			            int starMaxId = 0;
			            Statement starIdStatement = null;
			            try {
			            	starIdStatement = dbcon.createStatement();
			            	String starIdQuery = "SELECT id FROM stars";
			            	ResultSet starResultSet = starIdStatement.executeQuery(starIdQuery);
			            	while (starResultSet.next())
			            	{
			            		String id = starResultSet.getString("id");
			            		int idTrunc = Integer.parseInt(id.replaceAll("[^\\d]", ""));
			            		//System.out.println("ID is: " + id + "num is: " + idTrunc);
			            		if (idTrunc > starMaxId)
			            			starMaxId = idTrunc;
			            	}
			            	starResultSet.close();
			            }
			            catch (SQLException e)
			            {
			            	e.printStackTrace();
			            }
			            starMaxId++;
			            String newStarId = Integer.toString(starMaxId);
			            StringBuilder fb = new StringBuilder(newStarId);
			            fb.insert(0, "nm");
			            newStarId = fb.toString();
			            System.out.println("Max id is: " + starMaxId + " str is: " + newStarId);
			            starId = newStarId;
			            /* ----------------------------------------------------------------------*/

						System.out.println("About to create a new star");
						/*create a new star*/
						String insertStar = "INSERT INTO stars(id,name,birthYear) VALUES(?,?,?);";
						
						PreparedStatement newStarStatement = dbcon.prepareStatement(insertStar);
						
						newStarStatement.setString(1,starId);
						newStarStatement.setString(2, starName);
						newStarStatement.setInt(3, 0);
						
						newStarStatement.executeUpdate();
					}//else do nothing
	
					/*---------------check if the genre exist - do nothing--------------------- */
		            String genreQuery = "SELECT * FROM genres WHERE name = ?;";
		            // Declare our statement
					PreparedStatement genreStatement = dbcon.prepareStatement(genreQuery);
	
					// Set the parameter represented by "?" in the query to the id we get from url,
					genreStatement.setString(1, genreName);
					
					try {
					// Perform the query
					rs = genreStatement.executeQuery();
					}catch(SQLException p){
						p.printStackTrace();
					}
					System.out.println(rs);

					if(rs.next() == false) {
						/*find new genre Id*/
						System.out.println("the genre did not previously exist");
						 // ---------------   find max id --------------------------------------------
			            int idMax1 = 0;
			            Statement stmt1 = null;
			            try {
			            	stmt1 = dbcon.createStatement();
			            	String sqlFindMaxId = "SELECT id FROM genres";
			            	ResultSet rs1 = stmt1.executeQuery(sqlFindMaxId);
			            	while (rs1.next())
			            	{
			            		String id = rs1.getString("id");
			            		int idTrunc = Integer.parseInt(id.replaceAll("[^\\d]", ""));
			            		//System.out.println("ID is: " + id + "num is: " + idTrunc);
			            		if (idTrunc > idMax)
			            			idMax1 = idTrunc;
			            	}
			            	rs1.close();
			            }
			            catch (SQLException e)
			            {
			            	e.printStackTrace();
			            }
			            idMax1++;
			            String s1 = Integer.toString(idMax1);
			            StringBuilder sb1 = new StringBuilder(s1);
			            sb1.insert(0, "");
			            s1 = sb1.toString();
			            System.out.println("Max id is: " + idMax1 + " str is: " + s1);
			            genreId = s1;
			            /* ----------------------------------------------------------------------*/
			            
						/*create a new genre*/System.out.println("About to create a new genre");
						String insertGenre = "INSERT INTO genres(id, name) VALUES(?,?);";
						
						PreparedStatement newGenreStatement = dbcon.prepareStatement(insertGenre);
						
						newGenreStatement.setString(1, genreId);
						newGenreStatement.setString(2, genreName);
						
						try {
							newGenreStatement.executeUpdate();
						}catch(SQLException g) {
							g.printStackTrace();
						}
					}
					else {
						System.out.println("genre has previously existed");
					}
				/* add the new movie */
					System.out.println("Adding the new movie");
					System.out.println("movieId is: " + movieId);
					System.out.println("Movie title is: " + movieTitle);
					System.out.println("Movie Year is: " + movieYear);
					System.out.println("movieDirector is: " + movieDirector);
					System.out.println("starName is: " + starName);
					System.out.println("genreName is: " + genreName);
					System.out.println("starId:" + starId);
					System.out.println("genreId is: " + genreId);
					
				String addMovieQuery = "{CALL add_movie(?,?,?,?,?,?)}";
				
				// Declare our statement
				java.sql.CallableStatement insertMovieStatement = dbcon.prepareCall(addMovieQuery);

				// Set the parameter represented by "?" in the query to the id we get from url,
				// num 1 indicates the first "?" in the query
				insertMovieStatement.setString(1, movieId);
				insertMovieStatement.setString(2, movieTitle);
				insertMovieStatement.setString(3, movieYear);
				insertMovieStatement.setString(4, movieDirector);
				insertMovieStatement.setString(5, starName);
				insertMovieStatement.setString(6, genreName);
				
				try {
				// Perform the query
				insertMovieStatement.executeUpdate();
				}catch(SQLException z){
					z.printStackTrace();
				}
				System.out.println("There is Success");
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("status", "success");
				out.write(jsonObject.toString());

				}

	}catch (Exception e) {
    	System.out.println("Exception thrown in insert_star_servlet" + e);
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
