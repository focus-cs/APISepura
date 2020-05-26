package fr.sciforma.apisepura.business.csv;

import fr.sciforma.apisepura.business.model.AggregatedTimesheet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.SortedMap;

public interface CsvHelper {

    SortedMap<String, SortedMap<LocalDateTime, List<AggregatedTimesheet>>> parseFiles();
    void renameFiles();

}
