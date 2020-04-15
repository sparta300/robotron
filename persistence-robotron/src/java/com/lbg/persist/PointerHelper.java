package com.lbg.persist;

import static com.mfdev.utility.Characters.ADDRESS;
import static com.mfdev.utility.Characters.QUESTION_MARK;
import static com.mfdev.utility.Characters.SPACE;

import javax.inject.Inject;

import com.lbg.persist.pointer.LongPointer;

/**
 * a basic helper class for actions surrounding the use of pointers/addresses.
 * 
 * An address is basically a Java long but is encoded in such a way that you can address any object in the file via a set of
 * increasingly specific numbers.  The least specific is just a block ID.  More specific than that is a block ID and a component
 * ID.  The block reader is responsible for assigning component IDs as it reads in a block.  The most specific address is a 
 * block ID together with a component ID and finally a final index number.  If the component itself is made up of smaller
 * components that can be individually addresses, such as the elements of list, then you can provide an index ID.
 * 
 * @author C006011
 */
public class PointerHelper
{
	private final Swizzler swizzler;
	
	@Inject
	public PointerHelper(Swizzler swizzler)
	{
		this.swizzler = swizzler;
	}

	public void appendAddress(StringBuilder sb, String name, LongPointer pointer, boolean spaceBefore, boolean spaceAfter)
	{
		appendPointer(sb, name, Unset.ADDRESS, pointer, spaceBefore, spaceAfter);
	}
	
	public void appendPointer(StringBuilder sb, String name, long unsetValue, LongPointer pointer, boolean spaceBefore, boolean spaceAfter)
	{
		if (spaceBefore)
		{
			sb.append(SPACE);
		}

		sb.append(name).append(ADDRESS);
		long locator = pointer.get();

		if (locator == unsetValue)
		{
			sb.append(QUESTION_MARK);
		}
		else
		{
			sb.append(swizzler.make(locator).asAddress());
		}

		if (spaceAfter)
		{
			sb.append(SPACE);
		}
	}
}
