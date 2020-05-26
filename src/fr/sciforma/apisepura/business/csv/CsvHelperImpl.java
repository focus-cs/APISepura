package fr.sciforma.apisepura.business.csv;

import fr.sciforma.apisepura.business.exception.CsvException;
import fr.sciforma.apisepura.business.model.AggregatedTimesheet;
import fr.sciforma.apisepura.business.model.SepuraTimesheet;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CsvHelperImpl implements CsvHelper {

    private static final String FILE_PROCESSED_SUFFIX = "_processed";

    @Value("${csv.delimiter}")
    protected String delimiter;
    @Value("${csv.path.input}")
    private String inputPath;
    @Value("${csv.date.format}")
    private String dateFormat;

    private DateTimeFormatter dateTimeFormatter;

    private List<Path> filesToTreat;

    @Override
    public SortedMap<String, SortedMap<LocalDateTime, List<AggregatedTimesheet>>> parseFiles() {

        dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);

        SortedMap<String, SortedMap<LocalDateTime, List<AggregatedTimesheet>>> aggregatedTimesheets = new TreeMap<>();

        try (Stream<Path> paths = Files.list(Paths.get(inputPath))) {

            filesToTreat = paths.filter(path -> !path.toString().endsWith(FILE_PROCESSED_SUFFIX + ".csv") && path.toString().endsWith(".csv")).collect(Collectors.toList());

            Logger.info(filesToTreat.size() + " CSV file(s) found for processing");

            for (Path path : filesToTreat) {

                try {
                    SortedMap<String, SortedMap<LocalDateTime, List<SepuraTimesheet>>> parsedTimesheets = parseFile(path);

                    for (Map.Entry<String, SortedMap<LocalDateTime, List<SepuraTimesheet>>> userEntry : parsedTimesheets.entrySet()) {

                        if (!aggregatedTimesheets.containsKey(userEntry.getKey())) {
                            aggregatedTimesheets.put(userEntry.getKey(), new TreeMap<>());
                        }

                        for (Map.Entry<LocalDateTime, List<SepuraTimesheet>> dateEntry : userEntry.getValue().entrySet()) {

                            if (!aggregatedTimesheets.get(userEntry.getKey()).containsKey(dateEntry.getKey())) {
                                aggregatedTimesheets.get(userEntry.getKey()).put(dateEntry.getKey(), new ArrayList<>());
                            }

                            for (SepuraTimesheet sepuraTimesheet : dateEntry.getValue()) {

                                AggregatedTimesheet aggregatedTimesheet = AggregatedTimesheet.builder()
                                        .parentSdcr(sepuraTimesheet.getParentSdcr())
                                        .projectCode(sepuraTimesheet.getProjectCode())
                                        .userName(sepuraTimesheet.getUserName())
                                        .startDate(toStartOfWeek(sepuraTimesheet.getStartDate()))
                                        .build();

                                aggregatedTimesheet.addDatedData(toUtcDate(sepuraTimesheet.getStartDate()), toUtcDate(sepuraTimesheet.getEndDate()), sepuraTimesheet.getTimeSpent().doubleValue());

                                if (aggregatedTimesheets.get(userEntry.getKey()).get(dateEntry.getKey()).contains(aggregatedTimesheet)) {
                                    int index = aggregatedTimesheets.get(userEntry.getKey()).get(dateEntry.getKey()).indexOf(aggregatedTimesheet);
                                    aggregatedTimesheets.get(userEntry.getKey()).get(dateEntry.getKey()).get(index).addDatedDatas(aggregatedTimesheet.getDatedDataList());
                                } else {
                                    aggregatedTimesheets.get(userEntry.getKey()).get(dateEntry.getKey()).add(aggregatedTimesheet);
                                }
                            }

//                            sepuraTimesheets.get(userEntry.getKey()).get(dateEntry.getKey()).addAll(dateEntry.getValue());

                        }
                    }

                } catch (CsvException e) {
                    Logger.error("Failed to read CSV file " + path.toString(), e);
                }

            }

        } catch (IOException e) {
            Logger.error("Failed to read CSV files from path " + inputPath, e);
        }

        return aggregatedTimesheets;
    }

    @Override
    public void renameFiles() {

        for (Path path : filesToTreat) {
            String filename = path.getFileName().toString();

            filename = filename.replace(".csv", "_processed.csv");

            try {
                Files.move(path, path.resolveSibling(filename), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                Logger.error("Failed to rename file " + filename);
            }
        }

    }

    private SortedMap<String, SortedMap<LocalDateTime, List<SepuraTimesheet>>> parseFile(Path path) throws CsvException {

        List<SepuraTimesheet> timesheets = new ArrayList<>();

        try (Stream<String> stream = Files.lines(path).skip(1)) {

            timesheets = stream.map(line -> {
                try {
                    return parseLine(line);
                } catch (CsvException e) {
                    throw new RuntimeException(e);
                }
            })
                    .collect(Collectors.toList());

        } catch (IOException e) {
            Logger.error("Failed to read file " + path.toString());
        }

        SortedMap<String, SortedMap<LocalDateTime, List<SepuraTimesheet>>> timesheetMap = new TreeMap<>();

        if (!timesheets.isEmpty()) {

            for (SepuraTimesheet timesheet : timesheets) {

                if (timesheet.getUserName() != null && !timesheet.getUserName().isEmpty()) {

                    if (!timesheetMap.containsKey(timesheet.getUserName())) {
                        timesheetMap.put(timesheet.getUserName(), new TreeMap<>());
                    }

                    if (!timesheetMap.get(timesheet.getUserName()).containsKey(toStartOfWeek(timesheet.getStartDate()))) {
                        timesheetMap.get(timesheet.getUserName()).put(toStartOfWeek(timesheet.getStartDate()), new ArrayList<>());
                    }

                    timesheetMap.get(timesheet.getUserName()).get(toStartOfWeek(timesheet.getStartDate())).add(timesheet);
                }

            }

        }

        return timesheetMap;
    }

    private SepuraTimesheet parseLine(String line) throws CsvException {

        String[] splittedLine = line.split(delimiter);

        if (splittedLine.length == 7) {

            LocalDateTime startDate = null;
            LocalDateTime endDate = null;

            if (splittedLine[4].length() > 0) {

                try {

                    startDate = LocalDateTime.parse(splittedLine[4], dateTimeFormatter).withHour(8).withMinute(0).withSecond(0);
                    endDate = startDate.plusHours(10);

                } catch (DateTimeParseException e) {
                    throw new CsvException("Failed to parse date : " + splittedLine[4], e);
                }

            }

            String projectCode = null;
            String[] splittedProjectCode = splittedLine[2].replace("\"", "").split(" - ");
            if (splittedProjectCode.length > 0) {
                projectCode = splittedProjectCode[0];
            }

            return SepuraTimesheet.builder()
                    .parentSdcr(splittedLine[1].replace("\"", ""))
                    .projectCode(projectCode)
                    .startDate(startDate)
                    .endDate(endDate)
                    .userName(splittedLine[5].replace("\"", ""))
                    .timeSpent(new BigDecimal(splittedLine[6]))
                    .build();
        }

        throw new CsvException("CSV line malformed : " + "[" + line + "]");

    }

    private LocalDateTime toStartOfWeek(LocalDateTime date) {

        if (date != null) {

//            if (date.with(DayOfWeek.MONDAY).getMonth() != date.getMonth()) {
//                return date.withDayOfMonth(1);
//            } else {
                return date.with(DayOfWeek.MONDAY);
//            }

        }

        return null;
    }

    private Date toUtcDate(LocalDateTime date) {
        return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }

}
