package se.sundsvall.datawarehousereader.integration.stadsbacken.inspector;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RecompileAspect {

	@Around("@within(WithRecompile) || @annotation(WithRecompile)")
	public Object applyRecompile(ProceedingJoinPoint joinPoint) throws Throwable {
		try {
			RecompileContext.enable();
			return joinPoint.proceed();
		} finally {
			RecompileContext.disable();
		}
	}
}
