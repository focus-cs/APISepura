package fr.sciforma.apietnic.business.csv;

import java.util.List;

public interface CsvHelper<T> {

    String START_HEADER = "**Start**";
    String FINISH_HEADER = "**Finish**";

    void addLine(String line);
    void flush();
    List<String> getHeaderAsList();
    String getHeaderAsString();
    String getFilename();
}
