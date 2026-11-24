package se.sundsvall.datawarehousereader.integration.stadsbacken.inspector;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

@Aspect
@Component
public class RecompileAspect {

	@Around("@within(WithRecompile) || @annotation(WithRecompile)")
	public Object applyRecompile(ProceedingJoinPoint joinPoint) throws Throwable {
		final var targetClass = AopUtils.getTargetClass(joinPoint.getTarget());
		final var method = AopUtils.getMostSpecificMethod(((MethodSignature) joinPoint.getSignature()).getMethod(), targetClass);
		final var hashJoin = ofNullable(findAnnotation(method, WithRecompile.class))
			.or(() -> ofNullable(findAnnotation(targetClass, WithRecompile.class)))
			.map(WithRecompile::hashJoin)
			.orElse(false);
		try {
			RecompileContext.enable(hashJoin);
			return joinPoint.proceed();
		} finally {
			RecompileContext.disable();
		}
	}
}
