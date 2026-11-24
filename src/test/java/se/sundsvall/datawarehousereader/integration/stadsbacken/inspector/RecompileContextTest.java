package se.sundsvall.datawarehousereader.integration.stadsbacken.inspector;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecompileContextTest {

	@AfterEach
	void tearDown() {
		RecompileContext.disable();
	}

	@Test
	void defaultsToFalse() {
		assertThat(RecompileContext.isEnabled()).isFalse();
		assertThat(RecompileContext.isHashJoinEnabled()).isFalse();
	}

	@Test
	void enableThenDisable() {
		RecompileContext.enable(false);
		assertThat(RecompileContext.isEnabled()).isTrue();
		assertThat(RecompileContext.isHashJoinEnabled()).isFalse();

		RecompileContext.disable();
		assertThat(RecompileContext.isEnabled()).isFalse();

		// idempotent
		RecompileContext.disable();
		assertThat(RecompileContext.isEnabled()).isFalse();
	}

	@Test
	void enableWithHashJoinEnablesBoth() {
		RecompileContext.enable(true);

		assertThat(RecompileContext.isEnabled()).isTrue();
		assertThat(RecompileContext.isHashJoinEnabled()).isTrue();
	}

	@Test
	void disableClearsBothRecompileAndHashJoin() {
		RecompileContext.enable(true);

		RecompileContext.disable();

		assertThat(RecompileContext.isEnabled()).isFalse();
		assertThat(RecompileContext.isHashJoinEnabled()).isFalse();
	}

	@Test
	void threadLocalIsolation() throws InterruptedException {
		RecompileContext.enable(true);
		assertThat(RecompileContext.isEnabled()).isTrue();

		final var latch = new CountDownLatch(1);
		final boolean[] otherThread = new boolean[2];

		new Thread(() -> {
			otherThread[0] = RecompileContext.isEnabled();
			otherThread[1] = RecompileContext.isHashJoinEnabled();
			latch.countDown();
		}).start();

		assertThat(latch.await(2, TimeUnit.SECONDS)).isTrue();
		assertThat(otherThread[0]).isFalse();
		assertThat(otherThread[1]).isFalse();
		assertThat(RecompileContext.isEnabled()).isTrue();
		assertThat(RecompileContext.isHashJoinEnabled()).isTrue();
	}
}
