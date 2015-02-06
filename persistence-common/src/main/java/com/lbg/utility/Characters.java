package com.lbg.utility;

/**
 * basic utility class for methods and constants related to characters.
 * 
 * @author C006011
 */
public class Characters
{
	public static final String COMMA_STRING = ",";
	public static final String EOL = System.getProperty("line.separator");
	public static final String MAP_TO = "->";
	public static final String BULLET_POINT = " - ";	
	public static final String EXPLANATION = ": ";
	public static final String EMPTY_STRING = "";
	public static final String HASH_STRING = "#";
	public static final String EQUALS_STRING = "=";
	public static final String WHITESPACE_REGEX = "\\s";
	public static final String DOT_REGEX = "\\.";
	public static final String MINUS_D = "-D";
	public static final String OPEN_PARAMETER = "${";
	public static final String CLOSE_PARAMETER = "}";
	public static final String DOT_STRING = ".";
	public static final char COMMA = ',';
	public static final char QUOTE = '\'';
	public static final char DOT = '.';
	public static final char UNDERSCORE = '_';
	public static final char SPACE = ' ';
	public static final char COLON = ':';
	public static final char AT = '@';
	public static final char MINUS = '-';
	public static final char EQUALS = '=';
	public static final char FORWARD_SLASH = '/';
	public static final char BACK_SLASH = '\\';
	public static final char QUESTION_MARK = '?';
	public static final char ADDRESS = '&';
	

	public static String stripChars(char charToRemove, String inputString)
	{
		final int length = inputString.length();
		final StringBuilder sb = new StringBuilder(length);
		int index = 0;

		while (index < length)
		{
			final char c = inputString.charAt(index);
			
			if (c != charToRemove)
			{
				sb.append(c);
			}
			
			index++;
		}
		
		return sb.toString();
	}
}
