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
	}

	@Test
	void enableThenDisable() {
		RecompileContext.enable();
		assertThat(RecompileContext.isEnabled()).isTrue();

		RecompileContext.disable();
		assertThat(RecompileContext.isEnabled()).isFalse();

		// idempotent
		RecompileContext.disable();
		assertThat(RecompileContext.isEnabled()).isFalse();
	}

	@Test
	void threadLocalIsolation() throws InterruptedException {
		RecompileContext.enable();
		assertThat(RecompileContext.isEnabled()).isTrue();

		final var latch = new CountDownLatch(1);
		final boolean[] otherThread = new boolean[1];

		new Thread(() -> {
			otherThread[0] = RecompileContext.isEnabled();
			latch.countDown();
		}).start();

		assertThat(latch.await(2, TimeUnit.SECONDS)).isTrue();
		assertThat(otherThread[0]).isFalse();
		assertThat(RecompileContext.isEnabled()).isTrue();
	}
}
