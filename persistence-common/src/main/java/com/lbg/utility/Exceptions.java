package com.lbg.utility;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Exceptions
{

	private Exceptions()
	{
		super();
	}

	public static String asString(Throwable t)
	{
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);		
		t.printStackTrace(pw);		
		pw.close();
		return sw.toString();
	}
}
