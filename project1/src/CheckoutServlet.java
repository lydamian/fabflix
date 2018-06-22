

import java.io.IOException;
import java.sql.Connection;
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

/**
 * Servlet implementation class CheckoutServlet
 */
@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckoutServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */  
    
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	 System.out.println("CheckoutServlet page called...");
    	 HttpServletRequest httpRequest = (HttpServletRequest) request;
    	/*form input*/
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String creditCardNumber = request.getParameter("credit_card_number");
        String cardExpirationDate = request.getParameter("card_exp_date");
        
        System.out.println("firstName is: " + firstName);
        System.out.println("lastName is: " + lastName);
        System.out.println("credCardNumber is: " + creditCardNumber);
        System.out.println("cardExpirationDate is: " + cardExpirationDate);
        
       /*query results from checkout page input*/
        String queryFirstName = null;
    	String queryLastName = null;
    	String queryCCID = null;
    	String queryExpDate = null;

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

            System.out.println("Datasource is: " + dataSource);
            System.out.println("envCtx is: " + envCtx);

        	Connection connection = dataSource.getConnection();
        	Statement statement = connection.createStatement();
        	Statement saleStatement = connection.createStatement();
        	
        	
        	System.out.println("TRY BLOCK ENTERED.... in checkoutServlet");
        	
        	String query = "SELECT customers.firstName as first, customers.lastName as last, ccId, expiration as exp\r\n" + 
        			"FROM customers, creditcards\r\n" + 
        			"WHERE customers.ccId = creditcards.id\r\n"; 
        			
        	ResultSet resultSet = statement.executeQuery(query);
        	
        	int credentialsFound = 0;
        	while (resultSet.next())
        	{
        		queryFirstName = resultSet.getString("first");
            	queryLastName = resultSet.getString("last");
            	queryCCID = resultSet.getString("ccId");
            	queryExpDate = resultSet.getString("exp");
            	
            	
            	//System.out.println("query result for queryFirstName is: " + queryFirstName);
            	
            	
            	
            	
            	if (firstName.equals(queryFirstName) && lastName.equals(queryLastName) && creditCardNumber.equals(queryCCID) && cardExpirationDate.equals(queryExpDate)) {
    	            // Login success:
            		credentialsFound = 1;
            		
            		System.out.println("CheckoutServlet was successful!...");
            		
            		
            		/* Inserting Sales information into the database */
            		/*String insertQuery = "";
            		
            		ResultSet saleResultSet = saleStatement.executeQuery(insertQuery);*/
            		
    	
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
	            /*
	            if (!firstName.equals("anteater")) {
	                responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
	            } else if (!password.equals("123456")) {
	                responseJsonObject.addProperty("message", "incorrect password");
	            }
	            */
	            if(!firstName.equals(queryFirstName)){
        			responseJsonObject.addProperty("message", firstName + " doesnt exist");
        		}
        		else if(!lastName.equals(queryLastName)) {
        			responseJsonObject.addProperty("message", lastName + " doesnt exist");
        		}
        		else if(!creditCardNumber.equals(queryCCID)) {
        			responseJsonObject.addProperty("message", creditCardNumber + " is incorrect");
        		}
        		else if(!cardExpirationDate.equals(queryExpDate)) {
        			responseJsonObject.addProperty("message", cardExpirationDate + " is incorrect");
        		}
	            response.getWriter().write(responseJsonObject.toString());
        	}
        	if (credentialsFound == 1)
        	{
        		System.out.println("in info block");
        		User thisUser = (User) httpRequest.getSession().getAttribute("user");
        		System.out.println(thisUser.getId());
        		
        		//JsonObject shoppingCart =  (JsonObject) httpRequest.getSession().getAttribute("shopping-cart-obj");
        		String[] movieId1 = request.getParameterValues("movieIds");
        		String[] movieIds = movieId1[0].split(",");
        		//System.out.println(thisUser.getId() + ", " + movieId1[0]);
        		for (int i = 0 ; i < movieIds.length; i++)
        		{
        			//System.out.println(thisUser.getId() + ", " + movieIds[i]);
        			try {
        			String insertQuery = "INSERT INTO sales " + "VALUES (NULL, "+  thisUser.getId() + ", '" +  movieIds[i] + "', CURDATE());";
        			saleStatement.executeUpdate(insertQuery);
        			}
        			catch (SQLException e)
        			{
        				e.printStackTrace();
        			}
        		}
        		//System.out.println(movieId1[1]);
        		
        	}
        	
	        resultSet.close();
    		statement.close();
    		saleStatement.close();
    		connection.close();
        } catch (Exception e) {

			// set reponse status to 500 (Internal Server Error)
			response.setStatus(500);

        }
    }
	
	
	
	
	
	
	
	

}
