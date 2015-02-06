/**
 * File		: $Source:$
 * Author	: $Author:$
 * Revision	: $Revision:$
 * Date 	: $Date:$
 */
package org.hydroid.beowulf.storage.general;

/**
 * @author smiley
 */
public class AddObjectRequest  {
	/**
	 * @param transactionId
	 * @param eventCode
	 * @param byteBuffer
	 */
	public AddObjectRequest(int transactionId, byte[] bytes, boolean requiresResponse) {
		this.tx = transactionId;
		this.bytes = bytes;
		this.byteCount = bytes.length;
		this.requiresResponse = requiresResponse;
	}

	public int getByteCount() {	return byteCount; }
	public byte[] getBytes() {return bytes; }
	public boolean requiresResponse() { return requiresResponse; }
	public int getTransactionId() { return tx; }
	
	private int tx;
	private boolean requiresResponse;
	private int byteCount;
	private byte[] bytes;
}
