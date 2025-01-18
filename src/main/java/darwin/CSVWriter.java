package darwin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class CSVWriter implements FileWriter {
    public File csvFile;
    public PrintWriter writer;
    public Scanner reader;

    public CSVWriter(File csvFile){
        try {
            writer = new PrintWriter(csvFile);
            reader = new Scanner(csvFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeLine(String[] args) {
        this.writer.write(csvConvert(args));
        this.writer.close();
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
        return String.join(",", data);
    }
    private String[] stringConvert(String csv){
        return csv.split(",");
    }
}

