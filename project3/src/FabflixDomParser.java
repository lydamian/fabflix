
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;*/
import java.sql.*;

public class FabflixDomParser {

    List<Star> myStars;
    Document dom;

    public FabflixDomParser() {
        //create a list to hold the employee objects
        myStars = new ArrayList<>();
    }

    public void runExample() {

        //parse the xml file and get the dom object
        parseXmlFile();

        //get each employee element and create a Employee object
        parseDocument();

        //Iterate through the list and print the data
        printData();
        
        try {
			load();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    private void parseXmlFile() {
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom = db.parse("actors63.xml");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void parseDocument() {
        //get the root elememt
        Element docEle = dom.getDocumentElement();

        //get a nodelist of <employee> elements
        NodeList nl = docEle.getElementsByTagName("actor");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {

                //get the employee element
                Element el = (Element) nl.item(i);

                //get the Employee object
                Star e = getStar(el);

                //add it to list
                myStars.add(e);
            }
        }
    }

    /**
     * I take an employee element and read the values in, create
     * an Employee object and return it
     * 
     * @param empEl
     * @return
     */
    private Star getStar(Element starEl) {

        //for each <employee> element get text or int values of 
        //name ,id, age and name
        String name = getTextValue(starEl, "stagename");
        int dob = getIntValue(starEl, "dob");

        //Create a new Employee with the value read from the xml nodes
        Star s = new Star(name, dob);

        return s;
    }

    /**
     * I take a xml element and the tag name, look for the tag and get
     * the text content
     * i.e for <employee><name>John</name></employee> xml snippet if
     * the Element points to employee node and tagName is name I will return John
     * 
     * @param ele
     * @param tagName
     * @return
     */
    private String getTextValue(Element ele, String tagName) {
        String textVal = "0";
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            if (tagName == "dob" && el.getFirstChild()==null)
            {
            	//System.out.println("NULL val found");
            	textVal = "0";
            }
            else if (tagName == "dob" && el.getFirstChild().getNodeValue() == null)
            	textVal = "0";
            else if (tagName == "dob" && el.getFirstChild().getNodeValue().equals("n.a."))
            	textVal = "0";
            else if (tagName == "dob" && !el.getFirstChild().getNodeValue().matches("\\d+"))
            	textVal = "0";
            else
            {
            	//System.out.println("reg val");
            	textVal = el.getFirstChild().getNodeValue();
            }
        }
        //if (tagName == "stagename")
        	//System.out.println(textVal);
        return textVal;
    }

    /**
     * Calls getTextValue and returns a int value
     * 
     * @param ele
     * @param tagName
     * @return
     */
    private int getIntValue(Element ele, String tagName) {
        //in production application you would catch the exception
    	return Integer.parseInt(getTextValue(ele, tagName));
    	//String x = getTextValue(ele, tagName);
    	//int y = Integer.parseInt(x);
    	//if (x.equals("n.a."))
    		//System.out.println("dob str: " + x + " int: " + y);
    	//return 0;
    }

    /**
     * Iterate through the list and print the
     * content to console
     */
    private void printData() {

        

        Iterator<Star> it = myStars.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
        System.out.println("No of Stars '" + myStars.size() + "'.");
    }

    private void load() throws InstantiationException, IllegalAccessException, ClassNotFoundException{
    	HashMap<String, Integer> starMap = new HashMap<>();
    	int idMax = 0;
    	//Iterator<Star> it = myStars.iterator();
    	for (int i = 0; i < myStars.size(); ++i) {
    		starMap.put(myStars.get(i).getName(), myStars.get(i).getDateOfBirth());
    	}
    	System.out.println(starMap);
    	
    	Connection conn = null;

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String jdbcURL="jdbc:mysql://localhost:3306/testxml";

        try {
            conn = DriverManager.getConnection(jdbcURL,"root", "gododgers1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        //find max id
        Statement stmt = null;
        try {
        	stmt = conn.createStatement();
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
        String s = Integer.toString(idMax);
        StringBuilder sb = new StringBuilder(s);
        sb.insert(0, "nm");
        s = sb.toString();
        System.out.println("Max id is: " + idMax + " str is: " + s);

        //batch insert
        PreparedStatement psInsertRecord=null;
        String sqlInsertRecord=null;

        int[] iNoRows=null;
        
        sqlInsertRecord="insert into stars (id, name, birthYear) values(?,?,?)";
        try {
			conn.setAutoCommit(false);

            psInsertRecord=conn.prepareStatement(sqlInsertRecord);
            
            for(int i=0;i<myStars.size();i++)
            {
            	int curId = idMax + i + 1;
            	String curIdStr = Integer.toString(curId);
            	StringBuilder sa = new StringBuilder(curIdStr);
            	sa.insert(0, "nm");
            	curIdStr = sa.toString();
            	psInsertRecord.setString(1, curIdStr);
                psInsertRecord.setString(2, myStars.get(i).getName());
                psInsertRecord.setInt(3, myStars.get(i).getDateOfBirth());
                psInsertRecord.addBatch();
            }

			iNoRows=psInsertRecord.executeBatch();
			conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if(psInsertRecord!=null) psInsertRecord.close();
            if(conn!=null) conn.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        //create an instance
        FabflixDomParser dpe = new FabflixDomParser();

        //call run example
        dpe.runExample();
        
        
    }

}
