package com.lbg.utility.json;

/**
 * Represents a member of a JSON object, i.e. a pair of name and value.
 */
public class Member
{
	private final String name;
	private final JsonValue value;

	Member(String name, JsonValue value)
	{
		this.name = name;
		this.value = value;
	}

	/**
	 * Returns the name of this member.
	 *
	 * @return the name of this member, never <code>null</code>
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Returns the value of this member.
	 *
	 * @return the value of this member, never <code>null</code>
	 */
	public JsonValue getValue()
	{
		return value;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result = 31 * result + name.hashCode();
		result = 31 * result + value.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		
		final Member other = (Member) obj;
		return name.equals(other.name) && value.equals(other.value);
	}

}
