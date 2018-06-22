
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public class Mains243DomParser {

    List<Movie> myMovies;
    List<Genre> myGenres;
    List<Genre> allGenres;
    List<MovieGenres> genres_in_movies;
    HashMap<String, Integer> allMoviesMap = new HashMap<>();
    Document dom;
    int maxGenId = 0;

    public Mains243DomParser() {
        //create a list to hold the employee objects
        myMovies = new ArrayList<>();
        myGenres = new ArrayList<>();
        allGenres = new ArrayList<>();
        genres_in_movies = new ArrayList<>();
    }

    public void runExample() {

        //parse the xml file and get the dom object
        parseXmlFile();

        

        //Iterate through the list and print the data
        
        
        try {
			loadData();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      //get each employee element and create a Employee object
        parseDocument();
        printData();
        System.out.println("max is " + maxGenId);

    }

    private void parseXmlFile() {
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom = db.parse("mains243.xml");

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
        NodeList nl = docEle.getElementsByTagName("film");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {

                //get the employee element
                Element el = (Element) nl.item(i);

                //get the Employee object
                //Movie e = getMovie(el);
                //Genre g = getGenre(el);
                Movie e = new Movie("atitle", 8080, "adirector");
                Genre g = new Genre("agenre", 999);
                search(e, g, el);

                //add it to list
                myMovies.add(e);
                //myGenres.add(g);
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
    private void search(Movie e, Genre g, Element ele)
    {
    	String textVal = "0";
    	String filmTitle = "NA";
    	int filmYear = 0;
        NodeList nt = ele.getElementsByTagName("t");
        if (nt != null && nt.getLength() > 0) {
            Element et = (Element) nt.item(0);
            if (et.getFirstChild()==null)
            {
            	System.out.println("NULL t found");
            	textVal = null;
            }
            else
            {
            	textVal = et.getFirstChild().getNodeValue();
            	e.setTitle(textVal);
            	filmTitle = textVal;
            }
        }
        textVal = "0";
        NodeList ny = ele.getElementsByTagName("year");
        if (ny != null && ny.getLength() > 0) {
            Element ey = (Element) ny.item(0);
            if (ey.getFirstChild()==null)
            {
            	System.out.println("NULL t found");
            	textVal = null;
            }
            else if (ey.getFirstChild().getNodeValue() == null)
            	textVal = "0";
            else if (ey.getFirstChild().getNodeValue().equals("n.a."))
            	textVal = "0";
            else if (!ey.getFirstChild().getNodeValue().matches("\\d+"))
            	textVal = "0";
            else
            {
            	textVal = ey.getFirstChild().getNodeValue();
            	e.setYear(Integer.parseInt(textVal));
            	filmYear = Integer.parseInt(textVal);
            }
        }
        //directors
        textVal = "0";
        NodeList nd = ele.getElementsByTagName("dirn");
        if (nd != null && nd.getLength() > 0) {
            Element ed = (Element) nd.item(0);
            if (ed.getFirstChild() == null)
            {
            	textVal = null;
            }
            else
            {
            	textVal = ed.getFirstChild().getNodeValue();
            	e.setDirector(textVal);
            }
        }
        //cats
        textVal = "0";
        NodeList ncats = ele.getElementsByTagName("cat");
        if (ncats != null && ncats.getLength() > 0) {
            for (int i = 0; i < ncats.getLength(); i++)
            {
            	Element el = (Element) ncats.item(i);
                if (el.getFirstChild() == null)
                {
                	textVal = null;
                }
                else if (el.getFirstChild().getNodeValue() == null)
                {
                	textVal = null;
                }
                else
                {
                			
                			String temp =  null;
                			
                			temp = el.getFirstChild().getNodeValue();
                			if(temp.equalsIgnoreCase("comd") || temp.equalsIgnoreCase("comdx") || temp.equalsIgnoreCase("cond")) {
            	        		temp = "Comedy";        		
                    		} else if(temp.equalsIgnoreCase("epic")) {
                    			temp = "Epic";
                    		} else if(temp.equalsIgnoreCase("s.f.") || temp.equalsIgnoreCase("Scfi")  || temp.equalsIgnoreCase("Sxfi")|| temp.equalsIgnoreCase("Scif")) {
                    			temp = "Sci-Fi";
                    		} else if(temp.equalsIgnoreCase("stage musical") || temp.equalsIgnoreCase("Muscl")) {
                    			temp = "Musical";
                    		} else if (temp.equalsIgnoreCase("Musc")|| temp.equalsIgnoreCase("Muusc")) {
                    			temp = "Music";
                    		}else if(temp.equalsIgnoreCase("myst") || temp.equalsIgnoreCase("mystp")) {
                    			temp = "Mystery";
                    		} else if(temp.equalsIgnoreCase("susp")) {
                    			temp = "Suspense";
                    		} else if(temp.equalsIgnoreCase("avga") || temp.equalsIgnoreCase("Avant Garde")) {
                    			temp = "Avant-Garde";
                    		} else if(temp.equalsIgnoreCase("dram") || temp.equalsIgnoreCase("draam") || temp.equalsIgnoreCase("dramn") || temp.equalsIgnoreCase("drama") || temp.equalsIgnoreCase("dramd") || temp.equalsIgnoreCase("Dram>")) {
                    			temp = "Drama";
                    		} else if(temp.equalsIgnoreCase("actn") || temp.equalsIgnoreCase("act") || temp.equalsIgnoreCase("axtn")) {
                    			temp = "Action";
                    		} else if(temp.equalsIgnoreCase("Hor") || temp.equalsIgnoreCase("Horr")) {
                    			temp = "Horror";
                    		} else if(temp.equalsIgnoreCase("Expm")) {
                    			temp = "Experimental";
                    		} else if(temp.equalsIgnoreCase("verite")) {
                    			temp = "Verite";
                    		} else if(temp.equalsIgnoreCase("Advt") || temp.equalsIgnoreCase("Adctx") || temp.equalsIgnoreCase("Adct")) {
                    			temp = "Adventure";
                    		} else if(temp.equalsIgnoreCase("Psyc")) {
                    			temp = "Psychological";
                    		} else if(temp.equalsIgnoreCase("porn") || temp.equalsIgnoreCase("porb") || temp.equalsIgnoreCase("kinky")) {
                    			temp = "Adult";
                    		} else if(temp.equalsIgnoreCase("fant")) {
                    			temp = "Fantasy";
                    		} else if(temp.equalsIgnoreCase("Hist")) {
                    			temp = "History"; 
                    		} else if(temp.equalsIgnoreCase("romt") || temp.equalsIgnoreCase("Romtx") || temp.equalsIgnoreCase("Ront")) {
                    			temp = "Romance";
                    		} else if(temp.equalsIgnoreCase("docu") || temp.equalsIgnoreCase("ducu") || temp.equalsIgnoreCase("dicu") || temp.equalsIgnoreCase("duco")) {
                    			temp = "Documentary";
                    		} else if(temp.equalsIgnoreCase("fam") || temp.equalsIgnoreCase("faml")) {
                    			temp = "Family";
                    		} else if(temp.equalsIgnoreCase("Cult")) {
                    			temp = "Cult";
                    		} else if(temp.equalsIgnoreCase("West1") || temp.equalsIgnoreCase("West")) {
                    			temp = "Western";
                    		} else if(temp.equalsIgnoreCase("sport") || temp.equalsIgnoreCase("sports")) {
                    			temp = "Sport";
                    		} else if(temp.equalsIgnoreCase("cart")) {
                    			temp = "Animation";
                    		} else if(temp.equalsIgnoreCase("biop") || temp.equalsIgnoreCase("biopp") || temp.equalsIgnoreCase("biog") || temp.equalsIgnoreCase("biob") || temp.equalsIgnoreCase("BioPx")|| temp.equalsIgnoreCase("bio") ) {
                    			temp = "Biography";
                    		} else if(temp.equalsIgnoreCase("noir")) {
                    			temp = "Noir";
                    		} else if(temp.equalsIgnoreCase("col TV") || temp.equalsIgnoreCase("bnw TV") || temp.equalsIgnoreCase("TV") || temp.equalsIgnoreCase("TVmini")) {
                    			temp = "TV";
                    		} else if(temp.equalsIgnoreCase("Surl") || temp.equalsIgnoreCase("Surreal") || temp.equalsIgnoreCase("Surr")) {
                    			temp = "Surreal";
                    		} else if(temp.equalsIgnoreCase("crim")) {
                    			temp = "Crime";
                    		} else if(temp.equalsIgnoreCase("Dram.Actn")) {
                    			temp = "Drama";
                    			//mult
                    		} else if(temp.equalsIgnoreCase("Psych Dram")) {
                    			temp = "Psychological";
                    			//mult
                    		} else if(temp.equalsIgnoreCase("Romt Dram")) {
                    			temp = "Romance";
                    			//mult
                    		} else if(temp.equalsIgnoreCase("Romt. Comd") || temp.equalsIgnoreCase("Romt Comd")) {
                    			temp = "Romance";
                    			//mult
                    		} else if(temp.equalsIgnoreCase("Docu Dram") || temp.equalsIgnoreCase("Dram Docu")) {
                    			temp = "Documentary";
                    			//mult
                    		} else if(temp.equalsIgnoreCase("Romt Actn") || temp.equalsIgnoreCase("RomtAdvt")) {
                    			temp = "Romance";
                    			//mult
                    		} else if(temp.equalsIgnoreCase("Comd West")) {
                    			temp = "Comedy";
                    			//mult
                    		} else if(temp.equalsIgnoreCase("Noir Comd Romt")) {
                    			temp = "Noir";
                    			//mult
                    		} else if(temp.equalsIgnoreCase("Comd Noir") || temp.equalsIgnoreCase("Noir Comd")) {
                    			temp = "Comedy";
                    			//mult
                    		} else if(temp.equalsIgnoreCase("Romt Fant")) {
                    			temp = "Romance";
                    			//mult
                    		} else if(temp.equalsIgnoreCase("Ctxx")) {
                    			System.out.println("The following genre is not accepted: " + temp);
                    			//mult
                    		} else {
                    			System.out.println("The following genre is not accepted: " + temp);
                    		}
                			System.out.println(temp);
                			Genre gen = new Genre(temp, 0);
                			MovieGenres mg = new MovieGenres(filmTitle);
                			
                			Iterator<Genre> ig = allGenres.iterator();
                	        while (ig.hasNext()) {
                	        	if (!ig.next().getName().equals(temp))
                	        	{
                	        		//mg.setGenre(ig.next().getId());
                	        		//genres_in_movies.add(i, new MovieGenres(filmTitle));
                	        		gen.setId(maxGenId+1);
                	        		maxGenId++;
                	        		myGenres.add(gen);
                	        		
                	        	}
                	        	/*gen.setId(maxGenId+1);
            	        		maxGenId++;
                	        	myGenres.add(gen);*/
                	        }	
                	        //myGenres.add(gen);

                }
            }
        }
    }
    private Movie getMovie(Element movieEl) {

        //for each <employee> element get text or int values of 
        //name ,id, age and name
        String title = getTextValue(movieEl, "t");
        int year = getIntValue(movieEl, "year");
        String director = getTextValue(movieEl, "dirn");

        //Create a new Employee with the value read from the xml nodes
        Movie m = new Movie(title, year, director);

        return m;
    }
    
    /*private Genre getGenre(Element genreEl)
    {
    	String name = getTextValue(genreEl, "cat");
    	Genre g = new Genre(name);
    	return g;
    }*/

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
            if (tagName == "dirn" && el.getFirstChild() == null)
            {
            	textVal = null;
            }
            else if (tagName == "t" && el.getFirstChild()==null)
            {
            	//System.out.println("NULL t found");
            	textVal = null;
            }
            else if (tagName == "year" && el.getFirstChild()==null)
            {
            	//System.out.println("NULL year found");
            	textVal = "0";
            }
            else if (tagName == "cat" && el.getFirstChild()==null)
            {
            	//System.out.println("NULL genre found");
            	textVal = null;
            }
            else if (tagName == "year" && el.getFirstChild().getNodeValue() == null)
            	textVal = "0";
            else if (tagName == "year" && el.getFirstChild().getNodeValue().equals("n.a."))
            	textVal = "0";
            else if (tagName == "year" && !el.getFirstChild().getNodeValue().matches("\\d+"))
            	textVal = "0";
            else if (tagName == "cat" && el.getFirstChild().getNodeValue() == null)
            	textVal = null;
            else
            {
            	
            	textVal = el.getFirstChild().getNodeValue();
            	if (tagName == "dirn")
            		System.out.println("reg val: " + textVal);
            }
        }
        if (tagName == "t")
        	System.out.println(textVal);
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

        /*Iterator<Movie> it = myMovies.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }*/
        
        
        
        /*Iterator<MovieGenres> ig = genres_in_movies.iterator();
        while (ig.hasNext()) {
            System.out.println(ig.next().toString());
        }*/
        /*Iterator<Genre> iga = allGenres.iterator();
        while (iga.hasNext()) {
            System.out.println(iga.next().toString());
        }*/
    	Iterator<Genre> iga = myGenres.iterator();
        while (iga.hasNext()) {
            System.out.println(iga.next().toString());
        }
        System.out.println("No of Movies '" + myMovies.size() + "'.");
    }
    
    /*private void load() {
    	HashMap<String, Integer> starMap = new HashMap<>();
    	//Iterator<Star> it = myStars.iterator();
    	for (int i = 0; i < myMovies.size(); ++i) {
    		starMap.put(myMovies.get(i).getTitle(), myMovies.get(i).getYear(),  myMovies.get(i).getDirector());
    	}
    	System.out.println(starMap);
    }*/
    
    private void loadData() throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
    	Connection conn = null;

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String jdbcURL="jdbc:mysql://localhost:3306/testxml";

        try {
            conn = DriverManager.getConnection(jdbcURL,"root", "gododgers1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        //initialize hashmaps
        Statement stmt = null;
        try {
        	stmt = conn.createStatement();
        	String sqlFindGenres = "SELECT id, name FROM genres";
        	ResultSet rs = stmt.executeQuery(sqlFindGenres);
        	while (rs.next())
        	{
        		String name = rs.getString("name");
        		int id = rs.getInt("id");
        		Genre gg = new Genre(name, id);
        		allGenres.add(gg);
        		//System.out.println(gg.toString());
        	}
        	
        	String sqlFindMax = "SELECT max(id) as id from genres";
        	rs = stmt.executeQuery(sqlFindMax);
        	rs.next();
        	maxGenId = rs.getInt("id");
        	//System.out.println("Max is: " + maxGenId);
        	rs.close();
        }
        catch (SQLException e)
        {
        	e.printStackTrace();
        }
        
        //insert genres_in_movies
        /*PreparedStatement psInsertRecord=null;
        String sqlInsertRecord=null;

        int[] iNoRows=null;
        
        sqlInsertRecord="insert into genres_in_movies (genreId, movieId) values(?,?)";
        try {
			conn.setAutoCommit(false);

            psInsertRecord=conn.prepareStatement(sqlInsertRecord);
            
            for(int i=0;i<genres_in_movies.size();i++)
            {
            	for (int j = 0; j < genres_in_movies.get(i).getGenres().size(); ++j)
            	{
            		psInsertRecord.setInt(1, genres_in_movies.get(i).getGenres().get(i));
            		psInsertRecord.setString(2, genres_in_movies.get(i).getMovieId());
            	}
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
        }*/
    }

    public static void main(String[] args) {
        //create an instance
    	Mains243DomParser dpe = new Mains243DomParser();

        //call run example
        dpe.runExample();
    }

}
