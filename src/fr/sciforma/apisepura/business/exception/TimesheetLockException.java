package fr.sciforma.apisepura.business.exception;

public class TimesheetLockException extends Exception {

    public TimesheetLockException(String message) {
        super(message);
    }

    public TimesheetLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimesheetLockException(Throwable cause) {
        super(cause);
    }
}
