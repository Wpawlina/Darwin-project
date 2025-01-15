package darwin.presenter;

public enum StartButtonState {
    START("Start"),
    STOP("Stop"),
    ENDED("Close");

    private final String text;

    StartButtonState(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
