import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.jasypt.util.password.StrongPasswordEncryptor;

import java.io.IOException;
import java.io.PrintWriter;

//
@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);

        try {
        	RecaptchaVerifyUtils.verify(gRecaptchaResponse);
        }
        catch (Exception e)
        {
        	out.println("<p>" + e.getMessage() + "</p>");
        	out.close();
        	return;
        }

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

            Connection connection = dataSource.getConnection();
            if (connection == null)
                out.println("dbcon is null.");
         
            System.out.println("Datasource is: " + dataSource);
            System.out.println("envCtx is: " + envCtx);
            
            
        	Statement statement = connection.createStatement();
        	String query = "SELECT email, password, id FROM customers";
        	ResultSet resultSet = statement.executeQuery(query);
        	
        	int userTrue = 0;
        	int passTrue = 0;
        	
        	int credentialsFound = 0;
        	boolean success = false;
        	StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        	//String encryptedPassword = passwordEncryptor.encryptPassword(userPassword);

        	while (resultSet.next())
        	{
        		String rsUserName = resultSet.getString("email");
            	String rsPassWord = resultSet.getString("password");
            	
            	int rsId = resultSet.getInt("id");
            	if (username.equals(rsUserName) && (passwordEncryptor.checkPassword(password, rsPassWord))) {
    	            // Login success:
            		credentialsFound = 1;
    	            // set this user into the session
    	            request.getSession().setAttribute("user", new User(username, rsId));
    	
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

			// set reponse status to 500 (Internal Server Error)
			response.setStatus(500);

        }
    }
}
