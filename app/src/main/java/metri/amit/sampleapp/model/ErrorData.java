package metri.amit.sampleapp.model;

/**
 * Created by amitmetri on 04,April,2021
 */
public class ErrorData {
    private String errorCode;
    private String errorMessage;

    public ErrorData(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
