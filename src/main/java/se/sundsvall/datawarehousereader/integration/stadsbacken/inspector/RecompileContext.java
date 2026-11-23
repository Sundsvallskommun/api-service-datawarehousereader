package se.sundsvall.datawarehousereader.integration.stadsbacken.inspector;

public final class RecompileContext {

	private static final ThreadLocal<Boolean> RECOMPILE_ENABLED = new ThreadLocal<>();
	private static final ThreadLocal<Boolean> HASH_JOIN_ENABLED = new ThreadLocal<>();

	private RecompileContext() {}

	public static void enable(final boolean hashJoin) {
		RECOMPILE_ENABLED.set(true);
		HASH_JOIN_ENABLED.set(hashJoin);
	}

	public static void disable() {
		RECOMPILE_ENABLED.remove();
		HASH_JOIN_ENABLED.remove();
	}

	public static boolean isEnabled() {
		return Boolean.TRUE.equals(RECOMPILE_ENABLED.get());
	}

	public static boolean isHashJoinEnabled() {
		return Boolean.TRUE.equals(HASH_JOIN_ENABLED.get());
	}
}
