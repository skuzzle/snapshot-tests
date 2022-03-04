package de.skuzzle.test.snapshots.directoryparams;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.params.provider.ArgumentsSource;

@Retention(RUNTIME)
@Target({ TYPE, FIELD, METHOD, CONSTRUCTOR })
@Documented
@ArgumentsSource(DirectoriesFromArgumentsProvider.class)
@API(status = Status.EXPERIMENTAL, since = "1.2.0")
public @interface DirectoriesFrom {

    /**
     * The directory, relative to src/main/resources, from which to list the directories.
     */
    String directory();

    Class<? extends PathFilter> filter() default PathFilterAll.class;
}
