package org.hydroid.beowulf.overlay;

public interface RootOverlay {
	public MetaData getMetaData();
	public Sizing getSizing();
	public RepositoryOverhead getRepositoryOverhead();
	public Sandpit getSandpit();
	public BlockOverhead getBlockOverhead();
	public FreeSlotList getFreeSlotList();
}
