package de.skuzzle.test.snapshots;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * This is a framework exception that can be thrown when implementing certain extension
 * points.
 *
 * @author Simon Taddiken
 */
@API(status = Status.MAINTAINED)
public class SnapshotException extends RuntimeException {

    private static final long serialVersionUID = -5942910480826497997L;

    public SnapshotException() {
        super();
    }

    public SnapshotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SnapshotException(String message, Throwable cause) {
        super(message, cause);
    }

    public SnapshotException(String message) {
        super(message);
    }

    public SnapshotException(Throwable cause) {
        super(cause);
    }

}
