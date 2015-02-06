package com.lbg.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.utility.Characters;
import com.lbg.utility.LineListener;

/**
 * a helper class for various operations involving files and the file system.
 * 
 * @author C006011
 */
public class FileHelper
{	
	private static final Logger log = LoggerFactory.getLogger(FileHelper.class);
	private static final int DEFAULT_BUFFER_SIZE = 4 * 1024;
	
	/**
	 * read the contents of a file into an array of strings.
	 */
	public static List<String> readLines(File file) throws IOException
	{
		return readLines(new BufferedReader(new FileReader(file)));
	}
	
	/**
	 * read the contents from a {@link BufferedReader} into an array of strings.
	 * The given reader is closed when the content is exhausted.
	 */
	public static List<String> readLines(BufferedReader reader) throws IOException
	{
		String line = null;
		final List<String> lines = new ArrayList<String>();
		
		try
		{
			do
			{
				line = reader.readLine();
				
				if (line != null)
				{
					lines.add(line.trim());
				}
			}
			while (line != null);
		}
		finally
		{
			reader.close();
		}
		
		return lines;
	}
		
	/**
	 * read the contents of a file into a map.
	 * The file must contain lines in <code>KEY->VALUE</code> format.
	 */
	public static Map<String, String> loadMap(File file) throws IOException
	{
		// load as a set to remove any duplicates
		log.info("loading map from " + file.getName());
		final Set<String> set = loadSet(file);
		
		// load into a tree map so that the entries are sorted in lexographical order
		final Map<String, String> map = new TreeMap<String, String>();
		
		for (String entry : set)
		{
			final int index = entry.indexOf(Characters.MAP_TO);
			
			if (index == -1)
			{
				if (!(entry.trim().length() == 0 || entry.startsWith(Characters.HASH_STRING))) 
				{
					log.error("ignoring bad line in map file: [" + entry + "]");
				}
				
				continue;
			}
			
			final String key = entry.substring(0, index).trim();
			final String value = entry.substring(index + 2).trim().intern();
			
			// check for clobbering the value for an existing key - basically a difference of opinion on the value
			final String oldValue = map.put(key, value);
						
			if (oldValue != null)
			{
				log.error("value replaced for existing key '" + key + "': " + oldValue + Characters.MAP_TO + value);
			}
		}
		
		return map;
	}
	
	/**
	 * load the contents of the file into a map of properties.  
	 * The properties are interpreted using the same logic as the {@link Properties} class but are 
	 * converted into a map with string keys and string values.
	 */
	public static Map<String, String> loadPropertyMap(String fileName) throws IOException
	{
		log.info("loading property map from " + fileName);
		final Map<String, String> map = new HashMap<String, String>();
		final Properties properties = new Properties();
		properties.load(new FileReader(new File(fileName)));
		
		for (Entry<Object, Object> entry : properties.entrySet())
		{
			final String key = (String)entry.getKey();
			final String value = (String)entry.getValue();
			final String oldValue = map.put(key, value);
			
			if (oldValue != null && !oldValue.equals(value))
			{
				log.error("duplicate entry: " + key + "=" + value);
			}
		}

		return map;
	}

	/**
	 * read the contents of a file into a set.
	 * The set is sorted using the natural sort order of the lines.  Since it is a set,
	 * any duplicate lines will effectively disappear.  Empty lines also disappear.
	 */
	public static Set<String> loadSet(File file) throws IOException
	{
		final BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		final Set<String> set = new TreeSet<String>();
		
		try
		{
			do
			{
				line = reader.readLine();
				
				if (line != null)
				{
					final String trimmedLine = line.trim();
					
					// ignore empty lines
					if (trimmedLine.length() > 0)
					{
						set.add(trimmedLine);	
					}					
				}
			}
			while (line != null);
		}
		finally
		{
			reader.close();
		}
		
		return set;
	}
	
	public static Set<String> loadSet(String fileName) throws IOException
	{
		return loadSet(new File(fileName));
	}
			
	/**
	 * read the contents of a file into a list of strings.
	 * Each line will be passed to the given line listener too.
	 */
	public static List<String> loadLines(String fileName, LineListener listener) throws IOException
	{
		return loadLines(new File(fileName), listener);
	}
	
	/**
	 * read the contents of a file into a list of strings. 
	 */
	public static List<String> loadLines(File file) throws IOException
	{
		return loadLines(file, new LineListener() 
			{	
				@Override
				public void onLine(String line)
				{

				}				
			});
	}
	
	public static List<String> loadLines(File file, LineListener listener) throws IOException
	{
		if (listener == null)
		{
			throw new IllegalArgumentException("listener cannot be null");
		}
		
		final BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		final List<String> list = new ArrayList<String>();
		
		try
		{
			do
			{
				line = reader.readLine();
				
				if (line != null)
				{
					listener.onLine(line);
					list.add(line);
				}
			}
			while (line != null);
		}
		finally
		{
			reader.close();
		}
		
		return list;
	}
	
	/**
	 * create an empty file.
	 * The empty file is usually used as some kind of marker.  
	 * The behaviour is equivalent the the unix <code>touch</code> command.
	 * 
	 * @param file the file to create
	 * @throws IOException when the file could not be created or closed
	 */
	public static void createEmptyFile(File file) throws IOException
	{
		final OutputStream stream = createOutputStream(file);
		stream.close();
	}
	
	/**
	 * create a print writer for an output file, ready for writing.
	 * 
	 * @param file the file to create (path relative to the working directory)
	 * @return a print writer
	 * 
	 * @throws IOException
	 */	
	public static PrintWriter createOutputFileWriter(File file) throws IOException
	{
		ensureParentDirectories(file);
		return new PrintWriter(createOutputStream(file));
	}
	
	public static OutputStream createOutputStream(File file) throws IOException
	{
		ensureParentDirectories(file);
		return new BufferedOutputStream(new FileOutputStream(file));
	}
	
	public static InputStream createInputStream(File file) throws IOException
	{
		return new BufferedInputStream(new FileInputStream(file));
	}	

	/**
	 * makes sure all the parent directories to a file exist, because {@link File#mkdirs()} never seems to work.
	 * 
	 * @param file the target file
	 * @return true if all parent directories already existed or did not exist and were successfully created, else false
	 */
	public static boolean ensureParentDirectories(File file)	
	{
		final List<File> parents = new ArrayList<File>();
		File parent = file.getParentFile();
		
		while (parent != null)
		{
			parents.add(parent);
			parent = parent.getParentFile();
		}
		
		Collections.reverse(parents);
		
		for (File directory : parents)
		{
			if (!directory.exists())
			{
				boolean success = directory.mkdir();
				
				if (!success)
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * update the contents of a text file from the given list of lines.
	 * The target file name is taken as paramount.  The safe file is the file that contains the old data contents.
	 * If the safe file is created successfully, the safe file is put into the given list, so that it can be cleaned
	 * up later if needs be.
	 *  
	 * @param newContents the new content lines of the updated version of the file
	 * @param targetFile the file that will be written with the new contents, or left intact if the safe file cannot be created
	 * @param safeFilesHolder a out parameter to which the name of the safe file is added if it was successfully created
	 * @return true if the update was successful
	 */
	public static boolean updateFileNoClobber(List<String> newContents, File targetFile, List<File> safeFilesHolder)
	{
		log.info("attempting to safely update text file " + targetFile.getName());
		final File safeFile = FileHelper.avoidClobber(targetFile);
		
		if (safeFile != null)
		{
			log.info("target file exists, so renaming existing file to " + safeFile.getName());
			boolean success = targetFile.renameTo(safeFile);
			
			if (!success)
			{
				return false;
			}
			else
			{
				safeFilesHolder.add(safeFile);
			}
		}			
		
		try
		{
			log.info("writing new file contents");
			FileHelper.writeLines(newContents, targetFile);
			return true;
		}
		catch (IOException e)
		{
			log.error("could not update text file", e);
			return false;
		}		
	}

	/**
	 * avoid clobbering an existing file.
	 * The clobber avoidance style assumes that the target file name is significant and will be used even if the
	 * target already exists.  The idea is that we move the existing file to another safer name that does not exist
	 * and then create the target file with the intended name.  The naming suggestion scheme is simple.
	 * If a file does not have a suffix, such as <tt>readme</tt>, then the first suggestion will be 
	 * <tt>readme_1</tt>.  If that exists, then <tt>readme_2</tt> is tried and so on until an unused number is found.  It works
	 * more or less in the same way if the file has a suffix, except that the suffix is maintained, so <tt>readme.txt</tt> would
	 * give you the sequence <tt>readme_1.txt</tt>, <tt>readme_2.txt</tt>, <tt>readme_3.txt</tt> and so on.
	 * Internally the file name pattern contains an asterisk (*) which is then substituted for the current value of an integer
	 * counter, so that the patterns in the two examples above would be <tt>readme_*</tt> and <tt>readme_*.txt</tt>.
	 * 
	 * @param targetFile the target file name you want to avoid clobbering
	 * @return null if there no risk of clobbering, otherwise a suggestion for the name of a file that does not exist
	 */
	public static File avoidClobber(File targetFile)
	{
		boolean exists = targetFile.exists();
		
		if (!exists)
		{
			return null;
		}
		
		final String fileName = targetFile.getName();
		final int dotIndex = fileName.lastIndexOf(Characters.DOT);
		int counter = 1;
		String fileNamePattern;
		final File parent = targetFile.getParentFile();
		String suffix = "";
		
		if (dotIndex == -1)
		{
			fileNamePattern = fileName + "_*";			
		}
		else
		{
			suffix = fileName.substring(dotIndex);
			fileNamePattern = fileName.substring(0, dotIndex) + "_*" + suffix;
		}
		
		File newFile = null;
		
		while (exists)
		{
			final String suggestion = fileNamePattern.replace("*", String.valueOf(counter));
			newFile = new File(parent, suggestion);
			exists = newFile.exists();			
			counter++;
		}
		
		return newFile;
	}
	
	public static String getClobberMessage(File targetFile, File toFile)
	{
		final StringBuilder sb = new StringBuilder(200);
		sb.append("clobber avoidance failed, could not rename the target file: ");
		sb.append(targetFile.getAbsolutePath());
		sb.append(" -> ");
		sb.append(toFile.getAbsolutePath());
		return sb.toString();
	}
	 
	public static void writeMap(Map<String, String> map, File file) throws IOException
	{
		log.info("attempting to write map (size " + map.size() + ") from memory to disc: " + file.getAbsolutePath());
		final PrintWriter pw = createOutputFileWriter(file);
		
		for (Entry<String, String> entry : map.entrySet())
		{
			pw.println(entry.getKey() + Characters.MAP_TO + entry.getValue());
		}
		
		pw.close();
	}
	
	public static void writeLines(List<String> lines, File file) throws IOException
	{
		log.info("attempting to write lines (size " + lines.size() + ") from memory to disc: " + file.getAbsolutePath());
		final PrintWriter pw = createOutputFileWriter(file);
		
		for (String line : lines)
		{
			pw.println(line);
		}
		
		pw.close();
	}
	
	public static void writeSet(Set<String> set, File file) throws IOException
	{
		log.info("attempting to write lines (size " + set.size() + ") from memory to disc: " + file.getAbsolutePath());
		final PrintWriter pw = createOutputFileWriter(file);	
		
		for (String entry : set)
		{
			pw.println(entry);
		}
		
		pw.close();
	}

	public static String deriveFileNameForEnv(String fileName, String environment)
	{
		final String[] parts = fileName.split("\\.");
		
		if (parts.length != 2)
		{
			throw new IllegalArgumentException("cannot derive environment-specific file name (no dot suffix): " + fileName);
		}
		
		final String stem = parts[0];
		final String suffix = parts[1];
				
		if (environment.equals("prod") || environment.equals("test"))
		{
			return fileName;
		}
		else
		{
			return stem + Characters.UNDERSCORE + environment + Characters.DOT + suffix;
		}	
	}
	
	public static void copyBinaryFile(File from, File to, List<File> safeFiles) throws IOException
	{
		FileHelper.ensureParentDirectories(to);
		log.info("copying " + from.getName());
		
		final File safeFile = FileHelper.avoidClobber(to);
		
		if (safeFile != null)
		{
			log.info("file exists, so saving old version to " + safeFile.getName());
			boolean success = to.renameTo(safeFile);
			
			if (!success)
			{
				log.error("copy file failed");
				safeFiles.add(from);
				return;
			}
		}
		
		final InputStream in = new FileInputStream(from);
		final OutputStream out = new FileOutputStream(to);
		
		copyBinaryStream(in, out);
	}
		
	public static long copyBinaryStream(InputStream input, OutputStream output) throws IOException
	{
        final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
	    long count = 0;
	    int n = 0;
	  
	    while (-1 != (n = input.read(buffer))) 
	    {
		    output.write(buffer, 0, n);
		    count += n;
	    }
	  
	    return count;
	 }	
}
