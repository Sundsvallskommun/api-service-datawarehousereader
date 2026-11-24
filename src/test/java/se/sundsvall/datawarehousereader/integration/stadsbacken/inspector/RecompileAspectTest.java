package se.sundsvall.datawarehousereader.integration.stadsbacken.inspector;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class RecompileAspectTest {

	private static <T> T proxy(final T target) {
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
		assertThat(target.hashJoinInside).isFalse();
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
		assertThat(target.hashJoinInside).isTrue();
		assertThat(RecompileContext.isEnabled()).isFalse(); // still no leak
		assertThat(RecompileContext.isHashJoinEnabled()).isFalse();
	}

	@Test
	void directUnitTestOfAdvice_callsProceedAndCleansUp() throws Throwable {
		final var aspect = new RecompileAspect();
		final var pjp = mock(ProceedingJoinPoint.class);
		final var signature = mock(MethodSignature.class);
		when(pjp.getSignature()).thenReturn(signature);
		when(signature.getMethod()).thenReturn(TestService.class.getDeclaredMethod("ok"));

		when(pjp.proceed()).thenAnswer(inv -> {
			assertThat(RecompileContext.isEnabled()).isTrue();
			return 123;
		});

		final var result = aspect.applyRecompile(pjp);

		assertThat(result).isEqualTo(123);
		verify(pjp, times(1)).proceed();
		verify(pjp).getSignature();
		verify(signature).getMethod();
		verifyNoMoreInteractions(pjp, signature);
		assertThat(RecompileContext.isEnabled()).isFalse();
	}

	@Test
	void hashJoinIsEnabledDuringInvocationWhenChosenAndDisabledAfterReturn() {
		final var target = new TestService();
		final var proxy = proxy(target);

		final var out = proxy.hashJoin();

		assertThat(out).isEqualTo("hash");
		assertThat(target.enabledInside).isTrue();
		assertThat(target.hashJoinInside).isTrue();
		assertThat(RecompileContext.isEnabled()).isFalse();
		assertThat(RecompileContext.isHashJoinEnabled()).isFalse();
	}

	@Test
	void methodChoiceOverridesTypeChoice() {
		final var target = new TypeAnnotatedService();
		final var proxy = proxy(target);

		proxy.methodWithHashJoin();

		assertThat(target.enabledInside).isTrue();
		assertThat(target.hashJoinInside).isTrue();
	}

	@Test
	void typeChoiceAppliesToMethodWithoutOwnAnnotation() {
		final var target = new HashJoinTypeService();
		final var proxy = proxy(target);

		proxy.plain();

		assertThat(target.enabledInside).isTrue();
		assertThat(target.hashJoinInside).isTrue();
	}

	@Test
	void typeChoiceOnSuperclassAppliesToInheritedMethodOfSubclass() {
		final var target = new HashJoinTypeSubService();
		final var proxy = proxy(target);

		proxy.plain();

		assertThat(target.enabledInside).isTrue();
		assertThat(target.hashJoinInside).isTrue();
	}

	static class HashJoinTypeSubService extends HashJoinTypeService {
	}

	@WithRecompile(hashJoin = true)
	static class HashJoinTypeService {
		volatile boolean enabledInside;
		volatile boolean hashJoinInside;

		void plain() {
			enabledInside = RecompileContext.isEnabled();
			hashJoinInside = RecompileContext.isHashJoinEnabled();
		}
	}

	@WithRecompile(hashJoin = false)
	static class TypeAnnotatedService {
		volatile boolean enabledInside;
		volatile boolean hashJoinInside;

		@WithRecompile(hashJoin = true)
		void methodWithHashJoin() {
			enabledInside = RecompileContext.isEnabled();
			hashJoinInside = RecompileContext.isHashJoinEnabled();
		}
	}

	static class TestService {
		volatile boolean enabledInside;
		volatile boolean hashJoinInside;

		@WithRecompile(hashJoin = true)
		String hashJoin() {
			enabledInside = RecompileContext.isEnabled();
			hashJoinInside = RecompileContext.isHashJoinEnabled();
			return "hash";
		}

		@WithRecompile
		String ok() {
			enabledInside = RecompileContext.isEnabled();
			hashJoinInside = RecompileContext.isHashJoinEnabled();
			return "ok";
		}

		@WithRecompile(hashJoin = true)
		void boom() {
			enabledInside = RecompileContext.isEnabled();
			hashJoinInside = RecompileContext.isHashJoinEnabled();
			throw new IllegalStateException("boom");
		}
	}
}
