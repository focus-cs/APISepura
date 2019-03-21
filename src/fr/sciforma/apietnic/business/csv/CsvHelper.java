package fr.sciforma.apietnic.business.csv;

import java.util.List;

public interface CsvHelper<T> {

    String START_HEADER = "**Start**";
    String FINISH_HEADER = "**Finish**";

    void addLine(String line);
    List<String> getHeaderAsList();
    String getHeaderAsString();
}
