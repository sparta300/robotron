package org.hydroid.beowulf.overlay;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hydroid.beowulf.storage.LocatorFactory;


public class CompositeOverlay extends AbstractOverlay
{
	private static final String EOL = System.getProperty("line.separator");

	private final Map<String, Overlay> lookUp = new HashMap<String, Overlay>();
	private final List<Overlay> overlays = new ArrayList<Overlay>();

	public CompositeOverlay(ByteBuffer bb, List<String> overlayTypes, OverlayFactory factory, LocatorFactory locatorFactory)
	{
		super(bb, locatorFactory);

		for (String type : overlayTypes)
		{
			try
			{
				add(bb, type, factory);
			}
			catch (IllegalArgumentException iae)
			{
				// if the overlay code isn't in the factory just blow up,
				// otherwise see if it is our magic code to back up
				// a number of bytes, which is handy for free list management
				if (type.startsWith("back-"))
				{
					int backUpBytes = Integer.valueOf(type.substring(type.indexOf("-") + 1));
					bb.position(bb.position() - backUpBytes);
				}
				else
				{
					throw iae;
				}
			}
		}
				
		markEnd();
	}

	public void reset()
	{
		start();

		for (Overlay o : overlays)
		{
			o.reset();
		}
	}

	public int add(ByteBuffer bb, String type, OverlayFactory factory)
	{
		final Overlay overlay = factory.make(type, bb);
		overlays.add(overlay);
		lookUp.put(type, overlay);
		return overlays.size() - 1;
	}

	public void remove(int index)
	{
		overlays.remove(index);
	}

	public String toString()
	{
		final StringBuilder sb = new StringBuilder();

		for (Overlay o : overlays)
		{
			sb.append(o.toString());
		}

		return sb.toString();
	}

	public String report()
	{
		final StringBuilder sb = new StringBuilder();

		for (Overlay o : overlays)
		{
			sb.append(o.report());
			sb.append(" ").append(o.toString());
			sb.append(EOL);
		}

		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public <T extends Overlay> T getComponent(int index)
	{
		return (T) overlays.get(index);
	}

	@SuppressWarnings("unchecked")
	public <T extends Overlay> T getComponent(String type)
	{
		return (T) lookUp.get(type);
	}

	public Iterable<Overlay> iterate()
	{
		return Collections.unmodifiableList(overlays);
	}
}
