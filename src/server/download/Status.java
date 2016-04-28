package server.download;

/**
 *
 * @author agari
 */
public enum Status {

    APP(0),
    DELETED(1),
    DUPLICATED(2),
    PENDING(3),
    SOURCE(4),
    USER(5);

    private final int value;

    Status(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        switch (this) {
            case APP:
                return "APP";
            case DELETED:
                return "DELETED";
            case PENDING:
                return "PENDING";
            case SOURCE:
                return "SOURCE";
            case USER:
                return "USER";
            default:
                throw new IllegalArgumentException();
        }
    }
}
