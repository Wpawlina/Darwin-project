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

    public StartButtonState getNextState() {
        return switch (this) {
            case START -> STOP;
            case STOP -> START;
            case ENDED -> ENDED;

        };
    }
}
