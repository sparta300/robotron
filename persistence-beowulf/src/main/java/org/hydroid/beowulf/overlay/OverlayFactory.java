package org.hydroid.beowulf.overlay;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.hydroid.beowulf.model.list.overlay.SinglyLinkedListHead;
import org.hydroid.beowulf.model.list.overlay.SinglyLinkedListSegment;
import org.hydroid.beowulf.storage.LocatorFactory;

public class OverlayFactory {
	public OverlayFactory(final boolean create, final LocatorFactory locatorFactory){
		super();
		isCreator = create;
		
		map.put("md", new Command() {
			public MetaData execute(CommandContext ctx) {
				if (create) {
					return new MetaData(ctx.getByteBuffer(), locatorFactory, create);
				} else {
					return new MetaData(ctx.getByteBuffer(), locatorFactory);
				}
			}
		});
		
		map.put("sz", new Command() {
			public Sizing execute(CommandContext ctx) {
				if (create) {
					return new Sizing(ctx.getByteBuffer(), locatorFactory, create);
				} else {
					return new Sizing(ctx.getByteBuffer(), locatorFactory);
				}
			}	
		});	
		
		map.put("ro", new Command() {
			public RepositoryOverhead execute(CommandContext ctx) {
				if (create) {
					return new RepositoryOverhead(ctx.getByteBuffer(), locatorFactory, create);
				} else {
					return new RepositoryOverhead(ctx.getByteBuffer(), locatorFactory);
				}
			}
		});		
		
		map.put("bo", new Command() {
			public BlockOverhead execute(CommandContext ctx) {
				if (create) {
					return new BlockOverhead(ctx.getByteBuffer(), locatorFactory, create);
				} else {
					return new BlockOverhead(ctx.getByteBuffer(), locatorFactory);
				}
			}
		});	
		
		map.put("sp", new Command() {
			public Sandpit execute(CommandContext ctx) {
				if (create) {
					return new Sandpit(ctx.getByteBuffer(), locatorFactory, create);
				} else {
					return new Sandpit(ctx.getByteBuffer(), locatorFactory);
				}
			}
		});	
		
		map.put("so", new Command() {
			public SlotOverhead execute(CommandContext ctx) {
				if (create) {
					return new SlotOverhead(ctx.getByteBuffer(), locatorFactory, create);	
				} else {
					return new SlotOverhead(ctx.getByteBuffer(), locatorFactory);
				}
				
			}
		});	
		
		map.put("fsl", new Command() {
			public FreeSlotList execute(CommandContext ctx) {
				return new FreeSlotList(ctx.getByteBuffer(), locatorFactory, create);
			}
		});	
		
		map.put("flr", new Command() {
			public FreeListRuntime execute(CommandContext ctx) {
				return new FreeListRuntime(ctx.getByteBuffer(), locatorFactory, create);
			}
		});	
		
		map.put("fl-256", new Command() {
			public FreeList256 execute(CommandContext ctx) {
				return new FreeList256(ctx.getByteBuffer(), OverlayFactory.this, locatorFactory);
			}
		});		
		
		map.put("ll-seg1", new Command() {
			public SinglyLinkedListSegment execute(CommandContext ctx) {
				return new SinglyLinkedListSegment(ctx.getByteBuffer(), locatorFactory, create);
			}
		});		
		
		// linked list with head/tail/length
		map.put("ll-htl", new Command() {
			public SinglyLinkedListHead execute(CommandContext ctx) {
				return new SinglyLinkedListHead(ctx.getByteBuffer(), locatorFactory, create);
			}
		});			
	}


	@SuppressWarnings("unchecked")
	public <T extends Overlay> T make(String code, ByteBuffer bb) {
		Command command = map.get(code);
		CommandContext ctx = new CommandContext(bb);
				
		if (command == null) {
			throw new IllegalArgumentException(String.format("unknown overlay code '%s'", code));
		}
		
		return (T) command.execute(ctx);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Overlay> T make(int componentId, ByteBuffer bb) {
		StoreComponent component = StoreComponent.forId(componentId);
		
		if (component == null) {
			throw new IllegalArgumentException(String.format("unknown store component ID '%d'", componentId));
		}
		
		String key = component.key();
		Command command = map.get(key);
				
		if (command == null) {
			throw new IllegalArgumentException(String.format("unknown store component key '%s'", key));
		}
		
		CommandContext ctx = new CommandContext(bb);
		return (T) command.execute(ctx);
	}
	
	public boolean isCreator() { return isCreator ; }
	
	private final boolean isCreator;
	private Map<String, Command> map = new HashMap<String, Command>();
	
	private interface Command {
		Overlay execute(CommandContext context);
	}
	
	private class CommandContext {
		private CommandContext(ByteBuffer bb) {
			this.bb = bb;
		}
		
		private ByteBuffer getByteBuffer() {
			return bb;
		}
		
		private ByteBuffer bb;
	}
}
