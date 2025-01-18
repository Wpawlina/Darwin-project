package darwin;

public interface ToFileWriter {
    void writeLine(String[] args);
    void appendLine(String[] args);
    String[] readLine();
    void clear();
}
