package de.skuzzle.test.snapshots;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.MAINTAINED)
public class SnapshotException extends Exception {

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
