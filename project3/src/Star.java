
public class Star {
	private String name;
	private int dateOfBirth;
	
	public Star(String name, int dateOfBirth)
	{
		this.name = name;
		this.dateOfBirth = dateOfBirth;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getDateOfBirth()
	{
		return dateOfBirth;
	}
	
	public void setDateOfBirth(int dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Star Details - ");
		sb.append("Name:" + getName());
		sb.append(", ");
		sb.append("DOB:" + getDateOfBirth());
		sb.append(".");
		
		return sb.toString();
	}
}
