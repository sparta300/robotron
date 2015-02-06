package org.hydroid.beowulf.overlay;

import static org.hydroid.beowulf.BeowulfConstants.UNSET_LOCATOR;

import java.nio.ByteBuffer;

import org.hydroid.beowulf.storage.LocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.pointer.LongPointer;

public class Sandpit extends AbstractOverlay {
	public Sandpit(ByteBuffer bb, LocatorFactory locatorFactory) {
    	super(bb, locatorFactory);
    	linkedListLocator = new LongPointer(bb);
        listLocator = new LongPointer(bb);
    	bagLocator = new LongPointer(bb);
    	nameSpaceLocator = new LongPointer(bb);
    	binaryTreeLocator = new LongPointer(bb);
    	bPlusTreeLocator = new LongPointer(bb);
    	diaryLocator = new LongPointer(bb);
    	telemetryLocator = new LongPointer(bb);
    	hashMapLocator = new LongPointer(bb);
    	sourceRepositoryLocator = new LongPointer(bb);
        markEnd();
     
        logger.debug(toString()); 
	}
	
	public Sandpit(ByteBuffer bb, LocatorFactory locatorFactory, boolean reset) {
    	this(bb, locatorFactory);
    	
    	if (reset) {
    		reset();
    	}
	}
	
	@Override
	public void reset() {
		ByteBuffer bb = getByteBuffer();
		start();
		bb.putLong(UNSET_LOCATOR);
		bb.putLong(UNSET_LOCATOR);
		bb.putLong(UNSET_LOCATOR);
		bb.putLong(UNSET_LOCATOR);
		bb.putLong(UNSET_LOCATOR);
		bb.putLong(UNSET_LOCATOR);
		bb.putLong(UNSET_LOCATOR);
		bb.putLong(UNSET_LOCATOR);
		bb.putLong(UNSET_LOCATOR);
		bb.putLong(UNSET_LOCATOR);
	}
	
	public long getLinkedListLocator() {
		return linkedListLocator.get();
	}

	public void setLinkedListLocator(long linkedListLocator) {
		this.linkedListLocator.set(linkedListLocator);
	}

	public long getListLocator() {
		return listLocator.get();
	}

	public void setListLocator(long listLocator) {
		this.listLocator.set(listLocator);
	}

	public long getBagLocator() {
		return bagLocator.get();
	}

	public void setBagLocator(long bagLocator) {
		this.bagLocator.set(bagLocator);
	}

	public long getNameSpaceLocator() {
		return nameSpaceLocator.get();
	}

	public void setNameSpaceLocator(long nameSpaceLocator) {
		this.nameSpaceLocator.set(nameSpaceLocator);
	}

	public long getBinaryTreeLocator() {
		return binaryTreeLocator.get();
	}

	public void setBinaryTreeLocator(long binaryTreeLocator) {
		this.binaryTreeLocator.set(binaryTreeLocator);
	}

	public long getbPlusTreeLocator() {
		return bPlusTreeLocator.get();		
	}

	public void setbPlusTreeLocator(long bPlusTreeLocator) {
		this.bPlusTreeLocator.set(bPlusTreeLocator);
	}

	public long getDiaryLocator() {
		return diaryLocator.get();
	}

	public void setDiaryLocator(long diaryLocator) {
		this.diaryLocator.set(diaryLocator);
	}

	public long getTelemetryLocator() {
		return telemetryLocator.get();
	}

	public void setTelemetryLocator(long telemetryLocator) {
		this.telemetryLocator.set(telemetryLocator);
	}

	public long getHashMapLocator() {
		return hashMapLocator.get();
	}

	public void setHashMapLocator(long hashMapLocator) {
		this.hashMapLocator.set(hashMapLocator);
	}

	public long getSourceRepositoryLocator() {
		return sourceRepositoryLocator.get();
	}

	public void setSourceRepositoryLocator(long sourceRepositoryLocator) {
		this.sourceRepositoryLocator.set(sourceRepositoryLocator);
	}
	
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		stringify(sb);
		return sb.toString();
	}
 
	private void stringify(StringBuilder sb) {
		append(sb, "lli", linkedListLocator.get());
		append(sb, "lis", listLocator.get());
		append(sb, "bag", bagLocator.get());
		append(sb, "ns", nameSpaceLocator.get());
		append(sb, "bt", binaryTreeLocator.get());
		append(sb, "b+t", bPlusTreeLocator.get());
		append(sb, "dia", diaryLocator.get());
		append(sb, "tel", telemetryLocator.get());
		append(sb, "map", hashMapLocator.get());
		append(sb, "src", sourceRepositoryLocator.get());	
	}

	private void append(StringBuilder sb, String code, long locator) {
		sb.append(code).append("=");
		
		if (locator == UNSET_LOCATOR) {
			sb.append("?");
		} else {
			sb.append(locator);
		}
		
		sb.append(" ");
	}

	private LongPointer linkedListLocator;
	private LongPointer listLocator;
	private LongPointer bagLocator;
	private LongPointer nameSpaceLocator;
	private LongPointer binaryTreeLocator;
	private LongPointer bPlusTreeLocator;
	private LongPointer diaryLocator;
	private LongPointer telemetryLocator;
	private LongPointer hashMapLocator;
	private LongPointer sourceRepositoryLocator;
	
    private static final Logger logger = LoggerFactory.getLogger(Sandpit.class);	
}
