DROP PROCEDURE IF EXISTS add_movie;

-- Change DELIMITER to $$
DELIMITER $$


CREATE PROCEDURE add_movie(IN movieIds varchar(10), IN movieTitle varchar(100), IN movieYear int(11),
IN director varchar(100),IN starName varchar(100), IN genreName varchar(32))
BEGIN 
	/*local variables*/
	DECLARE genreIds int(11);
    DECLARE starIds VARCHAR(10);
    
	INSERT INTO movies(id, title, director, year) VALUES (movieIds, movieTitle, director, movieYear);

	/*link corresponding stars, assume already created*/
	SELECT id from stars WHERE stars.name = starName LIMIT 1 INTO starIds;
	INSERT INTO stars_in_movies(starId, movieId) VALUES(starIds, movieIds);
    INSERT INTO ratings(movieId, rating, numVotes) VALUES (movieIds, 0, 0);
        
    /*link corresponding genres, assume already created*/
	SELECT id from genres WHERE genres.name like genreName LIMIT 1 INTO genreIds;
	INSERT INTO genres_in_movies(genreId, movieId) VALUES(genreIds, movieIds);

END
$$

-- Change back DELIMITER TO ;
DELIMITER ;