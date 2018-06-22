/*creating index's for fabflix website */


CREATE INDEX movieSearchIndex
ON movies(title, year , director); 

CREATE INDEX starSearchIndex
ON stars(name);

CREATE INDEX genreNameSearchIndex
ON genres(name);