package se.sundsvall.datawarehousereader.integration.stadsbacken.inspector;

public class RecompileContext {

	private static final ThreadLocal<Boolean> RECOMPILE_ENABLED = new ThreadLocal<>();

	public static void enable() {
		RECOMPILE_ENABLED.set(true);
	}

	public static void disable() {
		RECOMPILE_ENABLED.remove();
	}

	public static boolean isEnabled() {
		return Boolean.TRUE.equals(RECOMPILE_ENABLED.get());
	}
}
