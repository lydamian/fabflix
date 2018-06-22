

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.jasypt.util.password.StrongPasswordEncryptor;

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

/**
 * Servlet implementation class EmployeeLoginServlet
 */
@WebServlet(name = "EmployeeLoginServlet", urlPatterns = "/EmployeeLoginServlet")
public class EmployeeLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeLoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doPost in EmployeeLoginServlet called");
		String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
        	System.out.println("Try block in EmployeeLoginServlet called");
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

        	Connection connection = dataSource.getConnection();
        	Statement statement = connection.createStatement();
        	String query = "SELECT email, password, fullname FROM employees";
        	ResultSet resultSet = statement.executeQuery(query);
        	
        	int userTrue = 0;
        	int passTrue = 0;
        	 
        	int credentialsFound = 0;
        	StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        	
        	while (resultSet.next())
        	{
        		String rsUserName = resultSet.getString("email");
            	String rsPassWord = resultSet.getString("password");

            	if (username.equals(rsUserName) && (passwordEncryptor.checkPassword(password, rsPassWord))) {
    	            // Login success:
            		credentialsFound = 1;
            		
    	            // set this user into the session
            		 request.getSession().setAttribute("user", new User(username, 1));
            		 
    	            JsonObject responseJsonObject = new JsonObject();
    	            responseJsonObject.addProperty("status", "success");
    	            responseJsonObject.addProperty("message", "success");
    	
    	            response.getWriter().write(responseJsonObject.toString());
            	}
        	}
        	if (credentialsFound == 0)
        	{
	            // Login fail
	            JsonObject responseJsonObject = new JsonObject();
	            responseJsonObject.addProperty("status", "fail");
	            if (userTrue == 0) {
	            	
	                responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
	            } else if (passTrue == 0) {
	                responseJsonObject.addProperty("message", "incorrect password");
	            }
	            response.getWriter().write(responseJsonObject.toString());
        	}
	        resultSet.close();
    		statement.close();
    		connection.close();
        } catch (Exception e) {
        	System.out.println("Exception caught in doPost in EmployeeLoginServlet");
			// set reponse status to 500 (Internal Server Error)
			response.setStatus(500);

        }
	}

}
