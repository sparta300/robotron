package nova.persist.urantia;

import static org.hydroid.beowulf.BeowulfConstants.UNSET_POSITION;
import static org.hydroid.beowulf.BeowulfConstants.UNSET_SIZE;

import java.nio.ByteBuffer;

import com.lbg.persist.SafeCast;
import com.mfdev.utility.ProgrammingError;

public class PositionHelper {
	private final long startPosition;
	private final ByteBuffer bb;
	private long endPosition = UNSET_POSITION;
	private int size = UNSET_SIZE;

	public PositionHelper(ByteBuffer bb) {
		this.bb = bb;
		startPosition = bb.position();
	}

	public void markEnd() {
		endPosition = bb.position();
		size = SafeCast.fromLongToInt(endPosition - startPosition);
	}

	public void start() {
		// set buffer position to be at the start position
		bb.position(SafeCast.fromLongToInt(startPosition));
	}

	public void end() {
		if (endPosition == UNSET_POSITION) {
			throw new ProgrammingError("cannot move to the end when endPosition is not set, need to call markEnd() first");
		}

		// set buffer position to be at the end position
		bb.position(SafeCast.fromLongToInt(endPosition));
	}

	public int size() {
		if (size == UNSET_SIZE) {
			throw new ProgrammingError("cannot access size when endPosition is not set, need to call markEnd() first");
		}
		
		return size;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("start=%4d ", startPosition));
		sb.append(endPosition == UNSET_POSITION ? "UNSET" : String.format("end=%4d ", endPosition));
		sb.append(size == UNSET_SIZE ? "UNSET" : String.format("size=%4d ", size));
		return sb.toString();
	}
}
