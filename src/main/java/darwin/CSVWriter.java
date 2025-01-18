package darwin;

import java.io.*;
import java.util.Scanner;

public class CSVWriter implements ToFileWriter {
    public File csvFile;
    public PrintWriter writer;
    public PrintWriter appender;
    public Scanner reader;

    public CSVWriter(File csvFile){
        try {
            writer = new PrintWriter(csvFile);
            appender = new PrintWriter(new FileWriter(csvFile, true));
            reader = new Scanner(csvFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeLine(String[] args) {
        this.writer.write(csvConvert(args));
        this.writer.close();
    }

    @Override
    public void appendLine(String[] args){
        this.appender.println(csvConvert(args));
    }

    @Override
    public String[] readLine() {
        if (reader.hasNextLine()){
            return stringConvert(reader.nextLine());
        }
        else return new String[] {};
    }

    @Override
    public void clear() {
        this.writer.print("");
    }

    private String csvConvert(String[] data){
        return String.join(";", data);
    }
    private String[] stringConvert(String csv){
        return csv.split(";");
    }
}

