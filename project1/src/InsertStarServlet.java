

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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class InsertStarServlet
 */
@WebServlet("/InsertStarServlet")
public class InsertStarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertStarServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json"); // Response mime type
        
        System.out.println("doGet in InsertStarServlet called...");
        /*assume data is valid handle handle invalid data server-side*/
    	
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
     
            System.out.println("InsertStarServlet page called...");
         
            // get title(substring), year, director(substring) , stars name(substring) (get method)
            String starName;
            String birthYear;       
            starName = request.getParameter("star_name");
            birthYear = request.getParameter("birth_year");
            
            System.out.println("star_name is: " + starName);
            System.out.println("birth_year is: " + birthYear);
         	
          //find max id
            int idMax = 0;
            Statement stmt = null;
            try {
            	stmt = dbcon.createStatement();
            	String sqlFindMaxId = "SELECT id FROM stars";
            	ResultSet rs = stmt.executeQuery(sqlFindMaxId);
            	while (rs.next())
            	{
            		String id = rs.getString("id");
            		int idTrunc = Integer.parseInt(id.replaceAll("[^\\d]", ""));
            		System.out.println("ID is: " + id + "num is: " + idTrunc);
            		if (idTrunc > idMax)
            			idMax = idTrunc;
            	}
            	rs.close();
            }
            catch (SQLException e)
            {
            	e.printStackTrace();
            }
            idMax++;
            String s = Integer.toString(idMax);
            StringBuilder sb = new StringBuilder(s);
            sb.insert(0, "nm");
            s = sb.toString();
            System.out.println("Max id is: " + idMax + " str is: " + s);

            
      		//generate query
      		String query = "INSERT INTO stars(id, name, birthYear) VALUES(?,?,?);";
      		
        	// Declare our statement
			PreparedStatement statement = dbcon.prepareStatement(query);

			// Set the parameter represented by "?" in the query to the id we get from url,
			// num 1 indicates the first "?" in the query
			statement.setString(1, s);
			statement.setString(2,starName);
			statement.setString(3, birthYear);
			
			try {
			// Perform the query
			statement.executeUpdate();
			}catch(SQLException z){
				z.printStackTrace();
			}
			
            // Create a JsonObject based on the data we retrieve from rs
			JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");
            response.getWriter().write(responseJsonObject.toString());
            
            // set response status to 200 (OK)
            response.setStatus(200);
            System.out.println("Success!!!");
            dbcon.close();
			statement.close();
			stmt.close();
        } catch (Exception e) {
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
