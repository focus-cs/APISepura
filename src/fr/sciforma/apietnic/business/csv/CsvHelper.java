package fr.sciforma.apietnic.business.csv;

import java.util.List;

public interface CsvHelper {

    void addLine(String line);
    void flush();
    List<String> getHeaderAsList();
    String getHeaderAsString();
    String getFilename();
}
