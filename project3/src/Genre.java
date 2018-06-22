
public class Genre {
	private int id;
	private String name;
	
	public Genre(String name, int id)
	{
		this.id = id;
		this.name = name;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Genre Details - ");
		sb.append("Name:" + getName());
		sb.append(", ");
		sb.append("id:" + getId());
		sb.append(".");
		
		return sb.toString();
	}
}
