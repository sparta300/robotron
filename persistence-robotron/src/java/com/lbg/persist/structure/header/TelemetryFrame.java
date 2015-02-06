package com.lbg.persist.structure.header;

import static com.lbg.persist.structure.StructureType.FRAME_MAIN;
import static com.lbg.persist.structure.StructureType.RELATIONSHIP_SET;
import static com.lbg.persist.structure.StructureType.TELEMETRY_FRAME;
import static com.lbg.persist.structure.StructureType.TIME_CODE;

import java.nio.ByteBuffer;

import com.lbg.persist.structure.CompositeStructure;
import com.lbg.persist.structure.CompositeStructureBuilder;
import com.lbg.persist.structure.raw.Header;

/**
 * a composite structure that helps you to pick out the various components of a telemetry frame.
 * 
 * @author C006011
 */
public class TelemetryFrame extends AbstractStructureWithHeader
{
	private final TimeCode timeCode;
	private final FrameMain frameInformation;
	private final RelationshipSet relationships;
 
	public TelemetryFrame(CompositeStructure composite)
	{
		super(composite.getByteBuffer(), composite.getHeader());
		this.timeCode = (TimeCode) composite.getComponent(0);
		this.frameInformation = (FrameMain) composite.getComponent(1);
		this.relationships = (RelationshipSet) composite.getComponent(2);
	}

	public static TelemetryFrame forge(ByteBuffer bb, Header header)
	{
		final int startPos = bb.position();
		final CompositeStructureBuilder builder = new CompositeStructureBuilder(bb, TELEMETRY_FRAME, startPos, header);
		builder.addComponent(TIME_CODE, TimeCode.forge(bb, header));
		builder.addComponent(FRAME_MAIN, FrameMain.forge(bb, header));
		builder.addComponent(RELATIONSHIP_SET, RelationshipSet.forge(bb, header)); 
		return new TelemetryFrame((CompositeStructure) builder.build());
	}

	public TimeCode getTimeCode()
	{
		return timeCode;
	}

	public FrameMain getFrameInformation()
	{
		return frameInformation;
	}

	public RelationshipSet getRelationshipSet()
	{
		return relationships;
	}
	
	public void reset()
	{
		timeCode.reset();
		frameInformation.reset();
		relationships.reset();
	}
}
