package com.lbg.persist;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.io.StreamCorruptedException;

import org.hydroid.file.PhysicalResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * an object serialiser that
 * 
 * @author C006011
 */
public class BufferBasedObjectSerialiser implements ObjectSerialiser {
	private static final Logger log = LoggerFactory.getLogger(BufferBasedObjectSerialiser.class);

	private final int bufferSize;

	/**
	 * added for phoenix 2022
	 */
	public BufferBasedObjectSerialiser() {
		this.bufferSize = 4096;
	}

	public BufferBasedObjectSerialiser(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	@Override
	public byte[] write(Serializable object) throws PhysicalResourceException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream(bufferSize);
		final ObjectOutputStream oos;

		try {
			oos = new ObjectOutputStream(baos);
		} catch (IOException ioe) {
			throw new PhysicalResourceException("problem creating object output stream", ioe);
		}

		try {
			oos.writeObject(object);
		} catch (IOException ioe) {
			throw new PhysicalResourceException("problem writing object to output stream", ioe);
		}

		final byte[] data = baos.toByteArray();
		log.debug("dataLength=" + data.length);
		return data;
	}

	@Override
	public <T> T read(byte[] data) throws PhysicalResourceException {
		return read(data, 0, data.length);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T read(byte[] data, int offset, int length) throws PhysicalResourceException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(data, offset, length);
		final ObjectInputStream ois;

		try {
			ois = new ObjectInputStream(bais);
		} catch (StreamCorruptedException sce) {
			throw new PhysicalResourceException("stream corruption detected on object input stream construction", sce);
		} catch (IOException ioe) {
			throw new PhysicalResourceException("I/O exception occurred on object input stream construction", ioe);
		}

		T object = null;

		try {
			object = (T) ois.readObject();
		} catch (ClassNotFoundException cnfe) {
			throw new PhysicalResourceException("class not found during object read", cnfe);
		} catch (OptionalDataException ode) {
			throw new PhysicalResourceException("optional data problem during object read", ode);
		} catch (IOException ioe) {
			throw new PhysicalResourceException("I/O exception during object read", ioe);
		}

		log.debug("read in instance of " + object.getClass().getName());
		return object;
	}
}
