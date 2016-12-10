package comm.error;

/**
 * Created by Domino on 2016-12-10.
 */
public class PortNotFoundException extends Exception {

    public PortNotFoundException() {
        super();
    }

    public PortNotFoundException(String message) {
        super(message);
    }

    public PortNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PortNotFoundException(Throwable cause) {
        super(cause);
    }

    protected PortNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
