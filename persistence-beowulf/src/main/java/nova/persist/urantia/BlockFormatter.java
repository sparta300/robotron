package nova.persist.urantia;

@FunctionalInterface
public interface BlockFormatter {
	void format(FormatterContext context);
}
