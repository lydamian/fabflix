import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MovieGenres {
	private String movieId;
	private List<Integer> genres = new ArrayList<>();
	
	public MovieGenres(String movieId)
	{
		this.movieId = movieId;
	}
	
	
	public String getMovieId()
	{
		return movieId;
	}
	
	public void setMovieId(String movieId)
	{
		this.movieId = movieId;
	}
	
	public List<Integer> getGenres()
	{
		return genres;
	}
	
	public void setGenre(int id)
	{
		genres.add(id);
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("GIM Details - ");
		sb.append("mid:" + getMovieId());
		sb.append(", ");
		sb.append("Genres:" + getGenres());
		sb.append(".");
		
		return sb.toString();
	}
	
}
