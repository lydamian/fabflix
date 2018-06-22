

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

// this annotation maps this Java Servlet Class to a URL
@WebServlet("/MovieServlet")
public class MovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
	
    public MovieServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // change this to your own mysql username and password
        //String loginUser = "root";
        //String loginPasswd = "gododgers1";
        //String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		
        // set response mime type
        response.setContentType("text/html"); 

        // get the printwriter for writing response
        PrintWriter out = response.getWriter();

        out.println("<!doctype html>\r\n"
        		+ "<html lang=\"en\">\r\n");
        out.println("<head><title>Fabflix</title>\r\n"
        		+ "<meta charset=\"utf-8\">\r\n"
        		+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\r\n"
        		+ "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\r\n"
        		+ ""
        		+ "</head>");
        
        
        try {
        		Class.forName("com.mysql.jdbc.Driver").newInstance();
        		// create database connection
        		//Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        		Connection connection = dataSource.getConnection();
        		// declare statement
        		Statement statement = connection.createStatement();
        		// prepare query
        		String query = "SELECT * FROM(\r\n" + 
        				"SELECT DISTINCT title, year, director, rating, GROUP_CONCAT(DISTINCT S1.name) as stars, GROUP_CONCAT(DISTINCT G1.name) as genres\r\n" + 
        				"FROM (SELECT title, year, director, rating, id\r\n" + 
        				"	FROM movies, ratings\r\n" + 
        				"	WHERE movies.id = ratings.movieId\r\n" + 
        				"        ORDER BY rating DESC\r\n" + 
        				"        LIMIT 20) as M1 LEFT JOIN (SELECT name, movieId\r\n" + 
        				"									FROM stars, stars_in_movies\r\n" + 
        				"									WHERE stars.id = stars_in_movies.starId) as S1 ON M1.id = S1.movieId LEFT JOIN (SELECT name, movieId\r\n" + 
        				"									FROM genres_in_movies JOIN genres ON genres.id = genres_in_movies.genreId) as G1\r\n" + 
        				"									ON M1.id = G1.movieId\r\n" + 
        				"		GROUP BY title) as A1 \r\n" + 
        				"ORDER BY rating desc;";
        		// execute query
        		ResultSet resultSet = statement.executeQuery(query);

        		out.println("<body>");
        		out.println("<nav class=\"navbar navbar-expand-lg navbar-light bg-light\">\r\n" + 
        				"  <a class=\"navbar-brand\" href=\"#\">Fablix</a>\r\n" + 
        				"  <button class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarNav\" aria-controls=\"navbarNav\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\r\n" + 
        				"    <span class=\"navbar-toggler-icon\"></span>\r\n" + 
        				"  </button>\r\n" + 
        				"  <div class=\"collapse navbar-collapse\" id=\"navbarNav\">\r\n" + 
        				"    <ul class=\"navbar-nav\">\r\n" + 
        				"      <li class=\"nav-item\">\r\n" + 
        				"        <a class=\"nav-link\" href=\"/project1/index.html\">Home <span class=\"sr-only\">(current)</span></a>\r\n" + 
        				"      </li>\r\n" + 
        				"      <li class=\"nav-item\">\r\n" + 
        				"        <a class=\"nav-link\" href=\"#\">Top 20</a>\r\n" + 
        				"      </li>\r\n" + 
        				"      <li class=\"nav-item\">\r\n" + 
        				"        <a class=\"nav-link\" href=\"/project1/search.html\">Advanced Search</a>\r\n" + 
        				"      </li>\r\n" + 
        				"      <li class=\"nav-item\">\r\n" + 
        				"        <a class=\"nav-link\" href=\"/project1/login.html\">Login</a>\r\n" + 
        				"      </li>\r\n" + 
        				"    </ul>\r\n" + 
        				"  </div>\r\n" + 
        				"</nav>");
        		out.println("<h1>MovieDB TOP 20 MOVIES</h1>");
        		
        		out.println("<table class=\"table table-striped\">");
        		
        		// add table header row
        		out.println("<thead>");
        		out.println("<tr>");
        		out.println("<th scope=\"col\">Ranking</th>");
        		out.println("<th scope=\"col\">Title</th>");
        		out.println("<th scope=\"col\">Director</th>");
        		out.println("<th scope=\"col\">Year</th>");
        		out.println("<th scope=\"col\">Rating</th>");
        		out.println("<th scope=\"col\">Stars</th>");
        		out.println("<th scope=\"col\">Genres</th>");
        		out.println("</tr>");
        		out.println("</thead>");
        		
        		out.println("<tbody>");
        		
        		/*db line number counter */
        		int index = 1;
        		
        		// add a row for every star result
        		while (resultSet.next()) {
        		
        			String title = resultSet.getString("title");
        			String year = resultSet.getString("year");
        			String director = resultSet.getString("director");
        			String rating = resultSet.getString("rating");
        			String stars = resultSet.getString("stars");
        			String genres = resultSet.getString("genres");
        			
        			out.println("<tr>");
        			out.println("<td>" + index + "</td>");
        			out.println("<td>" + title + "</td>");
        			out.println("<td>" + director + "</td>");
        			out.println("<td>" + year + "</td>");
        			out.println("<td>" + rating + "</td>");
        			out.println("<td>" + stars + "</td>");
        			out.println("<td>" + genres + "</td>");
        			out.println("</tr>");
        			
        			/*line number counter increment*/
        			index++;
        		}
        		out.println("</tbody>");
        		out.println("</table>");
        		
        		resultSet.close();
        		statement.close();
        		connection.close();
        		
        } catch (Exception e) {
        		/*
        		 * After you deploy the WAR file through tomcat manager webpage,
        		 *   there's no console to see the print messages.
        		 * Tomcat append all the print messages to the file: tomcat_directory/logs/catalina.out
        		 * 
        		 * To view the last n lines (for example, 100 lines) of messages you can use:
        		 *   tail -100 catalina.out
        		 * This can help you debug your program after deploying it on AWS.
        		 */
        		e.printStackTrace();
        		
        		out.println("<body>");
        		out.println("<p>");
        		out.println("Exception in doGet: " + e.getMessage());
        		out.println("</p>");
        		out.print("</body>");
        }
        
        out.println("</html>");
        out.close();
        
	}


}

