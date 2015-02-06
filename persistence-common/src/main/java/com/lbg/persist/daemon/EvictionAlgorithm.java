package com.lbg.persist.daemon;

import java.util.List;

/**
 * an cache eviction algorithm.
 * 
 * @author C006011
 *
 * @param <R> the type of the run-time data kept for each page
 */
public interface EvictionAlgorithm {

	public int select(AlgorithmCallback daemon, List<Page> pages) throws PageException;

	public void release(int index);

	public void acquire(int index);

}
