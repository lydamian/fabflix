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
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "SingleStarServlet", urlPatterns = "/single-star")
public class SingleStarServlet extends HttpServlet {
	private static final long serialVersionUID = 2L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json"); // Response mime type

		// Retrieve parameter id from url request.
		String id = request.getParameter("id");

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

			// Construct a query with parameter represented by "?"
			String query = "SELECT distinct s.id as s_id, name, birthyear, GROUP_CONCAT(distinct m.title order by m.id) as movies, GROUP_CONCAT(distinct m.id) as movie_ids\r\n" + 
					"from stars as s, stars_in_movies as sim, movies as m\r\n" + 
					"where m.id = sim.movieId and sim.starId = s.id and s.id = ?;";

			// Declare our statement
			PreparedStatement statement = dbcon.prepareStatement(query);

			// Set the parameter represented by "?" in the query to the id we get from url,
			// num 1 indicates the first "?" in the query
			statement.setString(1, id);

			// Perform the query
			ResultSet rs = statement.executeQuery();

			JsonArray jsonArray = new JsonArray();

			// Iterate through each row of rs
			while (rs.next()) {

				String starId = rs.getString("s_id"); 
				String starName = rs.getString("name"); 
				int birthYear = rs.getInt("birthyear");
				String movies = rs.getString("movies");
				String movie_ids = rs.getString("movie_ids");

				// Create a JsonObject based on the data we retrieve from rs

				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", starId);
				jsonObject.addProperty("star_name", starName);
				jsonObject.addProperty("birth_year", birthYear);
				jsonObject.addProperty("movies", movies);
				jsonObject.addProperty("movie_ids", movie_ids);

				jsonArray.add(jsonObject);
			}
			
            // write JSON string to output
            out.write(jsonArray.toString());
            System.out.println(jsonArray.toString());
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
