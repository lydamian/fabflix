import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
public class CastsDOMParser {
	Document dom;
	HashMap<String, Integer> movieMap;
	HashMap<String, Integer> starMap;
	public CastsDOMParser() {
		movieMap = new HashMap<String, Integer>();
		starMap = new HashMap<String, Integer>();
	}
	public void run()  throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Connection conn = null;

		Class.forName("com.mysql.jdbc.Driver").newInstance();
        String jdbcURL="jdbc:mysql://localhost:3306/testxml";

        try {
            conn = DriverManager.getConnection(jdbcURL,"root", "gododgers1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
		conn.setAutoCommit(false);
		parseXmlFile();
		insertCastsData(conn);
	}
	
	private void parseXmlFile() {
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom = db.parse("casts124.xml");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
	
	private void insertCastsData(Connection conn)
	{
		try {
			String filmid = null;
			String actorStgNm = null;
			int starId;
			int movieId;
			int flag = 0;
			PreparedStatement ptinsertStarMovie = null;
			ptinsertStarMovie = (PreparedStatement) conn.prepareStatement("Insert into stars_in_movies (starId, movieId) Values (? ,?);");

			Element docElem = dom.getDocumentElement();
			NodeList dirfilmsList = docElem.getElementsByTagName("dirfilms");
			for (int i = 0; i < dirfilmsList.getLength(); i++) {
				Element dirfilmsElem = (Element) dirfilmsList.item(i);
				NodeList filmcList = dirfilmsElem.getElementsByTagName("filmc");
				if (filmcList != null) {
					for (int j = 0; j < filmcList.getLength(); j++) {
						Element filmcElem = (Element) filmcList.item(j);
						NodeList mList = filmcElem.getElementsByTagName("m");
						if (mList != null) {
							for (int k = 0; k < mList.getLength(); k++) {
								filmid = null;
								actorStgNm = null;
								flag = 0;
								// count3++;
								Element mElem = (Element) mList.item(k);
								// Get Film ID
								NodeList fList = mElem.getElementsByTagName("f");
								if (fList != null && fList.getLength() > 0) {
									try {
										filmid = fList.item(0).getFirstChild().getNodeValue();
										filmid = filmid.trim();
									} catch (Exception e1) {
										// do nothing
									}
								}

								NodeList aList = mElem.getElementsByTagName("a");
								if (aList != null) {
									try {
										actorStgNm = aList.item(0).getFirstChild().getNodeValue();
										actorStgNm = actorStgNm.trim();
										actorStgNm = actorStgNm.toLowerCase();
									} catch (Exception e2) {
										// do nothing
									}
								}

								if (actorStgNm != null && filmid != null) {

									if (!movieMap.containsKey(filmid)) {
										flag = 1;
									}

									if (!starMap.containsKey(actorStgNm)) {
										flag = 1;
									}

									if (flag == 0) {
										starId = starMap.get(actorStgNm);
										movieId = movieMap.get(filmid);
										ptinsertStarMovie.setInt(1, starId);
										ptinsertStarMovie.setInt(2, movieId);
										ptinsertStarMovie.addBatch();
									}
								}
							}
						}
					}
				}
			}
			ptinsertStarMovie.executeBatch();
			conn.commit();
			ptinsertStarMovie.close();
			// PreparedStatement temp = (PreparedStatement)
			// conn.prepareStatement("ALTER IGNORE TABLE stars_in_movies ADD
			// constraint myuniqueconstaint UNIQUE (star_id, movie_id);");
			// temp.executeUpdate();
			// temp.close();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public static void main(String args[])
	{
		CastsDOMParser dpe = new CastsDOMParser();
		try {
			dpe.run();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
