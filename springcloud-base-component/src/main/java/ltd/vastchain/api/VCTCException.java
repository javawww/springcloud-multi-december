package ltd.vastchain.api;

public class VCTCException extends Exception {
    private String errorType;

    public VCTCException(String errorType, String msg) {
        super(errorType + ": " + msg);
    }

    public String getErrorType() {
        return errorType;
    }
}
