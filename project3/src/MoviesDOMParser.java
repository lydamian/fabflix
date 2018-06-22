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

public class MoviesDOMParser {
	Document dom;
	HashMap<String, Integer> movie_map;
	HashMap<String, Integer> star_map;
	int movieid = 0;
	int genreid = 0;
	public MoviesDOMParser() {
		movie_map = new HashMap<String, Integer>();
		star_map = new HashMap<String, Integer>();
	}
	public void run()  throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Connection conn = null;

		Class.forName("com.mysql.jdbc.Driver").newInstance();
        String jdbcURL="jdbc:mysql://localhost:3306/moviedb";

        try {
            conn = DriverManager.getConnection(jdbcURL,"root", "gododgers1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
		conn.setAutoCommit(false);
		long startTime = System.currentTimeMillis();
		parseXmlFile("mains243.xml");
		insertMovieData(conn);
		parseXmlFile("actors63.xml");
		insertStarData(conn);
		parseXmlFile("casts124.xml");
		insertCastsData(conn);
		long endTime = System.currentTimeMillis();

		System.out.println("That took " + (endTime - startTime) + " milliseconds");
	}
	
	private void parseXmlFile(String sourceXML) {
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom = db.parse(sourceXML);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
	
	public String cleanGenres(String temp)
	{
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
			System.out.println("Invalid genre: " + temp);
			//mult
		} else {
			System.out.println("Invalid genre: " + temp);
		}
		return temp;
	}
	
	public void insertMovieData(Connection conn) throws SQLException {

		try {
			String fid = "None";
			String movieTitle = null;
			String movieDirector = null;
			int movieYear = 0;
			/*int movieid = 0;
			int genreid = 0;*/

			Statement getMaxId = null;
			getMaxId = conn.createStatement();
			ResultSet maxR = getMaxId.executeQuery("select max(id) as id from genres");
			maxR.next();
			genreid = maxR.getInt("id");
			getMaxId.close();
			
			Statement getMaxMId = null;
			getMaxMId = conn.createStatement();
			ResultSet maxRM = getMaxMId.executeQuery("select max(id) as id from movies");
			maxRM.next();
			String tmpId = maxRM.getString(1);
			movieid = Integer.parseInt(tmpId.replaceAll("[^\\d]", ""));
			getMaxMId.close();
					
			PreparedStatement ptInsertMovie = null;
			PreparedStatement ptInsertGenre = null;
			PreparedStatement checkGenre = null;
			PreparedStatement ptGenreMovie = null;
			PreparedStatement insertRatings = null;
			ptInsertMovie = (PreparedStatement) conn.prepareStatement("INSERT INTO movies (id, title, year, director) Values (? ,?, ?, ?);");
			ptInsertGenre = (PreparedStatement) conn.prepareStatement("Insert into genres (id, name) Values (?, ?);");
			checkGenre = (PreparedStatement) conn.prepareStatement("Select id from genres where name = ?;");
			ptGenreMovie = (PreparedStatement) conn.prepareStatement("INSERT INTO genres_in_movies (genreId, movieId) Values (?, ?);");
			insertRatings = (PreparedStatement) conn.prepareStatement("INSERT into ratings (movieId, rating, numVotes) Values (?, 0.0, 0);");

					Element docEle = dom.getDocumentElement();
					NodeList films = docEle.getElementsByTagName("films");
					if (films.getLength() > 0 && films != null) {
						for (int j = 0; j < films.getLength(); j++) {
							Element filmElem = (Element) films.item(j);
							NodeList filmList = filmElem.getElementsByTagName("film");

							if (filmList.getLength() > 0 && filmList != null) {
								for (int k = 0; k < filmList.getLength(); k++) {

									// Get the Film ID from XML
									Element film = (Element) filmList.item(k);
									NodeList idList = film.getElementsByTagName("fid");
									try {
										fid = idList.item(0).getFirstChild().getNodeValue();
										fid = fid.trim();
									} catch (Exception e) {
										try {
											idList = film.getElementsByTagName("filmed");
											fid = idList.item(0).getFirstChild().getNodeValue();
										} catch (Exception e1) {
											System.out.println("Movie FID not Found");
										}
									}

									// Get Movie Title from XML
									NodeList titleList = film.getElementsByTagName("t");
									try {
										movieTitle = titleList.item(0).getFirstChild().getNodeValue();
									} catch (Exception titleError) {
										// use default movie title
										movieTitle = "None";
									}

									// Get Movie Year from XML
									NodeList yearList = film.getElementsByTagName("year");
									try {
										movieYear = Integer.parseInt(yearList.item(0).getFirstChild().getNodeValue());
									} catch (Exception e) {
										// use default movie id
										movieYear = 0;
									}

									// Get Movie Director from XML
									NodeList dirsList = film.getElementsByTagName("dirs");
									try {
										Element dirs = (Element) dirsList.item(0);
										NodeList dirLst = dirs.getElementsByTagName("dir");
										Element dir = (Element) dirLst.item(0);
										NodeList dirnLst = dir.getElementsByTagName("dirn");
										movieDirector = dirnLst.item(0).getFirstChild().getNodeValue();
									} catch (Exception dirFail) {
										// use deafult director name
										movieDirector = "None";
									}

									movieid++;
									movie_map.put(fid, movieid);

									if (movieTitle == null)
										movieTitle = "None";

									if (movieDirector == null)
										movieDirector = "None";

									movieTitle = movieTitle.trim();
									movieDirector = movieDirector.trim();
									ptInsertMovie.setString(1, "tt" + movieid);
									ptInsertMovie.setString(2, movieTitle);
									ptInsertMovie.setInt(3, movieYear);
									ptInsertMovie.setString(4, movieDirector);
									ptInsertMovie.addBatch();
									
									//ratings
									insertRatings.setString(1, "tt" + movieid);
									insertRatings.addBatch();
									
									insertGenres(film, checkGenre, ptInsertGenre, ptGenreMovie);

								}
							}
						}
			}

			ptInsertMovie.executeBatch();
			insertRatings.executeBatch();
			ptGenreMovie.executeBatch();
			conn.commit();
			ptInsertMovie.close();
			insertRatings.close();
			ptInsertGenre.close();
			ptGenreMovie.close();
		} catch (SQLException sqlExp) {
			System.out.println(sqlExp.getMessage());
		}
	}
	
	private void insertGenres(Element film, PreparedStatement allGenres, PreparedStatement ptInsertGenre, PreparedStatement ptGenreMovie) throws SQLException
	{
		NodeList catsNode = film.getElementsByTagName("cats");
		if (catsNode != null && catsNode.getLength() > 0) {
			Element cats = (Element) catsNode.item(0);
			NodeList catNodes = cats.getElementsByTagName("cat");
			if (catNodes != null) {
				for (int m = 0; m < catNodes.getLength(); m++) {
					NodeList catList = catNodes.item(m).getChildNodes();
					if (catList != null && catList.getLength() > 0) {
						String genre_name = catList.item(0).getNodeValue();
						if (genre_name != null) {
							genre_name = genre_name.trim();
							genre_name = cleanGenres(genre_name);
							allGenres.setString(1, genre_name);
							ResultSet genreData = allGenres.executeQuery();

							if (genreData.next()) {
								int genId = genreData.getInt(1);
								ptGenreMovie.setInt(1, genId);
							} else {
								genreid++;
								ptInsertGenre.setInt(1, genreid);
								ptInsertGenre.setString(2, genre_name);
								ptInsertGenre.executeUpdate();
								ptGenreMovie.setInt(1, genreid);
							}
							ptGenreMovie.setString(2, "tt" + movieid);
							ptGenreMovie.addBatch();
						}
					}
				}
			}
		}
	}
	/*STARS--------------------------------------------*/
	
	public void insertStarData(Connection conn) throws SQLException {
		try {
			String stageName = null;
			int date = 0;
			String starid = null;
			int staridnum = 0;

			PreparedStatement ptGetMaxId = null;
			ptGetMaxId = (PreparedStatement) conn.prepareStatement("select max(id) from stars;");
			ResultSet maxRes = ptGetMaxId.executeQuery();
			if (maxRes.next()) {
				
				starid = maxRes.getString(1);
				staridnum = Integer.parseInt(starid.replaceAll("[^\\d]", ""));
			}
			ptGetMaxId.close();

			PreparedStatement ptInsertStar = null;
			ptInsertStar = (PreparedStatement) conn
					.prepareStatement("Insert into stars (id, name, birthYear) Values (? ,?, ?);");

			Element docElem = dom.getDocumentElement();
			NodeList actorLst = docElem.getElementsByTagName("actor");
			for (int i = 0; i < actorLst.getLength(); i++) {
				stageName = null;
				date = 0;

				Element actorElem = (Element) actorLst.item(i);
				// Get actor Stagename
				NodeList stageNameLst = actorElem.getElementsByTagName("stagename");
				try {
					stageName = stageNameLst.item(0).getFirstChild().getNodeValue();
					stageName = stageName.trim();
					stageName = stageName.toLowerCase();
				} catch (Exception e1) {
					// do nothing
				}
				

				// Get Actor dob
				NodeList dobList = actorElem.getElementsByTagName("dob");
				try {
					date = Integer.parseInt(dobList.item(0).getFirstChild().getNodeValue());
				} catch (Exception e1) {
					// do nothing
				}

				staridnum++;
				
				
				ptInsertStar.setString(1, "nm" + staridnum);
				ptInsertStar.setString(2, stageName);
				ptInsertStar.setInt(3, date);
				ptInsertStar.addBatch();

				if (stageName != null) {
					star_map.put(stageName, staridnum);
				}
			}

			ptInsertStar.executeBatch();
			conn.commit();
			ptInsertStar.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/* STARS IN MOVIES */
	
	private void insertCastsData(Connection conn)
	{
		try {
			String filmid = null;
			String actorstageName = null;
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
								actorstageName = null;
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
										actorstageName = aList.item(0).getFirstChild().getNodeValue();
										actorstageName = actorstageName.trim();
										actorstageName = actorstageName.toLowerCase();
									} catch (Exception e2) {
										// do nothing
									}
								}

								if (actorstageName != null && filmid != null) {

									if (!movie_map.containsKey(filmid)) {
										flag = 1;
									}

									if (!star_map.containsKey(actorstageName)) {
										flag = 1;
									}

									if (flag == 0) {
										starId = star_map.get(actorstageName);
										movieId = movie_map.get(filmid);
										ptinsertStarMovie.setString(1, "nm" + starId);
										ptinsertStarMovie.setString(2, "tt" + movieId);
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
		MoviesDOMParser dpe = new MoviesDOMParser();
		try {
			dpe.run();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
