package de.skuzzle.test.snapshots;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RUNTIME)
@Target({ TYPE, METHOD })
@ExtendWith(SnapshotExtension.class)
public @interface SnapshotAssertions {
    String value() default "";
}
