package se.sundsvall.datawarehousereader.integration.stadsbacken.inspector;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;

@Aspect
@Component
public class RecompileAspect {

	@Around("@within(WithRecompile) || @annotation(WithRecompile)")
	public Object applyRecompile(ProceedingJoinPoint joinPoint) throws Throwable {
		final var method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		final var annotation = ofNullable(method.getAnnotation(WithRecompile.class))
			.orElseGet(() -> joinPoint.getTarget().getClass().getAnnotation(WithRecompile.class));
		try {
			RecompileContext.enable(annotation.hashJoin());
			return joinPoint.proceed();
		} finally {
			RecompileContext.disable();
		}
	}
}
