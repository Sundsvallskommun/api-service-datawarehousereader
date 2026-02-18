package se.sundsvall.datawarehousereader.integration.stadsbacken.inspector;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RecompileAspectTest {

	private static TestService proxy(final TestService target) {
		final var factory = new AspectJProxyFactory(target);
		factory.addAspect(new RecompileAspect());
		return factory.getProxy();
	}

	@AfterEach
	void cleanup() {
		RecompileContext.disable();
	}

	@Test
	void enablesDuringInvocationAndDisablesAfterReturn() {
		final var target = new TestService();
		final var proxy = proxy(target);

		assertThat(RecompileContext.isEnabled()).isFalse();
		final String out = proxy.ok();

		assertThat(out).isEqualTo("ok");
		assertThat(target.enabledInside).isTrue();
		assertThat(RecompileContext.isEnabled()).isFalse(); // no leak
	}

	@Test
	void disablesAfterExceptionToo() {
		final var target = new TestService();
		final var proxy = proxy(target);

		assertThatThrownBy(proxy::boom)
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("boom");

		assertThat(target.enabledInside).isTrue();
		assertThat(RecompileContext.isEnabled()).isFalse(); // still no leak
	}

	@Test
	void directUnitTestOfAdvice_callsProceedAndCleansUp() throws Throwable {
		final var aspect = new RecompileAspect();
		final var pjp = mock(ProceedingJoinPoint.class);

		when(pjp.proceed()).thenAnswer(inv -> {
			assertThat(RecompileContext.isEnabled()).isTrue();
			return 123;
		});

		final var result = aspect.applyRecompile(pjp);

		assertThat(result).isEqualTo(123);
		verify(pjp, times(1)).proceed();
		assertThat(RecompileContext.isEnabled()).isFalse();
	}

	static class TestService {
		volatile boolean enabledInside;

		@WithRecompile
		String ok() {
			enabledInside = RecompileContext.isEnabled();
			return "ok";
		}

		@WithRecompile
		void boom() {
			enabledInside = RecompileContext.isEnabled();
			throw new IllegalStateException("boom");
		}
	}
}
