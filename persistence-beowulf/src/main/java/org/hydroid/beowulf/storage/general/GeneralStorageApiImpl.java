package org.hydroid.beowulf.storage.general;

import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.space.ResponsibilityChain;
import org.hydroid.beowulf.space.SpaceRequest;
import org.hydroid.beowulf.space.SpaceRequestImpl;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.hydroid.beowulf.storage.general.visitor.AddVisitor;
import org.hydroid.beowulf.storage.general.visitor.CopyVisitor;
import org.hydroid.beowulf.storage.general.visitor.FetchVisitor;
import org.hydroid.beowulf.storage.general.visitor.ReanimateVisitor;
import org.hydroid.beowulf.storage.general.visitor.RemoveVisitor;
import org.hydroid.beowulf.storage.general.visitor.ReviveVisitor;
import org.hydroid.beowulf.storage.general.visitor.StoreVisitor;
import org.hydroid.file.PhysicalResourceException;

public class GeneralStorageApiImpl implements GeneralStorageApi
{
	private final LocatorFactory locatorFactory;
	//private SlotFinder slotFinder;
	private final ResponsibilityChain chain;
	//private ScatteringWriter writer;
	private final SlotSkin slotSkin;
	private final Sizing sizing;
	private int transactionId;
	private int spaceRequestId = 1;
	
	public GeneralStorageApiImpl(ApiContext context, Sizing sizing,	SlotFinder slotFinder, int transactionId) throws PhysicalResourceException
	{
		this.sizing = sizing;
		this.transactionId = transactionId;
		this.locatorFactory = context.getLocatorFactory();

		chain = context.getChain();

		//writer = new ScatteringWriter(sizing, locatorFactory);
		slotSkin = new SlotSkinImpl(sizing, slotFinder);
	}

	public void add(byte[] bytes) throws PhysicalResourceException
	{
		AddObjectRequest addRequest = new AddObjectRequest(this.transactionId, bytes, false);
		SpaceRequest request = new SpaceRequestImpl(addRequest,	getNextSpaceRequestId(), sizing);
		chain.handleRequest(request);

		// writer.write(bytes, request);
		AddVisitor visitor = new AddVisitor(bytes, request, locatorFactory);
		slotSkin.accept(visitor);
	}

	public long store(byte[] bytes) throws PhysicalResourceException
	{
		AddObjectRequest addRequest = new AddObjectRequest(this.transactionId, bytes, true);
		SpaceRequest request = new SpaceRequestImpl(addRequest, getNextSpaceRequestId(), sizing);
		chain.handleRequest(request);

		// writer.write(bytes, request);
		// return request.getLocator();
		StoreVisitor visitor = new StoreVisitor(bytes, request, locatorFactory);
		slotSkin.accept(visitor);
		return visitor.getLocator();
	}

	public byte[] copy(long locator) throws PhysicalResourceException
	{
		CopyVisitor visitor = new CopyVisitor(locator);
		slotSkin.accept(visitor);
		return visitor.getObjectData();
	}

	public void duplicate(long locator) throws PhysicalResourceException
	{

		// DuplicateReaderVisitor reader = new DuplicateReaderVisitor();
		// slotSkin.traverse(locator, reader);
		// byte[] bytes = reader.getObjectData();
		//
		// AddObjectRequest addRequest = new
		// AddObjectRequest(this.transactionId, bytes);
		// SpaceRequest request = new SpaceRequest(addRequest,
		// getNextSpaceRequestId(), sizing);
		// chain.handleRequest(request);
		//
		// DuplicateWriterVisitor writer = new DuplicateWriterVisitor(bytes,
		// request);
		// slotSkin.traverse(locator, writer);
	}

	public void remove(long locator) throws PhysicalResourceException
	{
		RemoveVisitor visitor = new RemoveVisitor(locator);
		slotSkin.accept(visitor);
	}

	public byte[] fetch(long locator) throws PhysicalResourceException
	{
		FetchVisitor visitor = new FetchVisitor(locator);
		slotSkin.accept(visitor);
		return visitor.getObjectData();
	}

	public void replace(long locator, byte[] replacementBytes) throws PhysicalResourceException
	{
		throw new PhysicalResourceException("not supported");
	}

	public byte[] substitute(long locator, byte[] replacementBytes)	throws PhysicalResourceException
	{
		throw new PhysicalResourceException("not supported");
	}

	public void revive(long locator) throws PhysicalResourceException
	{
		ReviveVisitor visitor = new ReviveVisitor(locator);
		slotSkin.accept(visitor);
	}

	public byte[] reanimate(long locator) throws PhysicalResourceException
	{
		ReanimateVisitor visitor = new ReanimateVisitor(locator);
		slotSkin.accept(visitor);
		return visitor.getObjectData();
	}

	public void redirect(long locator) throws PhysicalResourceException
	{
		throw new PhysicalResourceException("not supported");
	}

	public byte[] revert(long locator) throws PhysicalResourceException
	{
		throw new PhysicalResourceException("not supported");
	}

	public void flush() throws PhysicalResourceException
	{
		throw new PhysicalResourceException("not supported");
	}

	public void close() throws PhysicalResourceException
	{
		throw new PhysicalResourceException("not supported");
	}
 
	private int getNextSpaceRequestId()
	{
		return spaceRequestId++;
	}

}
