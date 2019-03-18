package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.*;
import fr.sciforma.apietnic.business.extractor.*;
import fr.sciforma.apietnic.business.model.SciformaField;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class TaskProcessor extends AbstractProcessor<Task> {

    @Value("${filename.tasks}")
    private String filename;

    @Autowired
    private StringExtractor<Task> stringExtractor;
    @Autowired
    private DecimalExtractor<Task> decimalExtractor;
    @Autowired
    private BooleanExtractor<Task> booleanExtractor;
    @Autowired
    private DateExtractor<Task> dateExtractor;
    @Autowired
    private IntegerExtractor<Task> integerExtractor;
    @Autowired
    private ListExtractor<Task> listExtractor;
    @Autowired
    private CalendarExtractor<Task> calendarExtractor;
    @Autowired
    private EffortExtractor<Task> effortExtractor;

    @Autowired
    private ResourceAssignementProcessor resourceAssignementProcessor;

    @PostConstruct
    public void postConstruct() {
        super.postConstruct();
        csvLines = new ArrayList<>();
    }

    @Override
    protected String getFilename() {
        return filename;
    }

    protected void process(List<TaskOutlineList> tasks, Date projectStart, Date projectEnd) {

        try {

            Iterator taskTterator = tasks.iterator();
            while (taskTterator.hasNext()) {
                Task task = (Task) taskTterator.next();

                parseTask(task.getSuccessorLinksList(), projectStart, projectEnd);
                parseTask(task.getPredecessorLinksList(), projectStart, projectEnd);
            }


        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve task outline for project");
        }

    }

    private void parseTask(List<Task> tasks, Date start, Date end) {

        Iterator taskIterator = tasks.iterator();

        while (taskIterator.hasNext()) {
            TaskLink taskLink = (TaskLink) taskIterator.next();

            Task task;
            try {
                task = taskLink.getTask();

                StringJoiner csvLine = new StringJoiner(csvDelimiter);

                Optional<Double> taskDuration = Optional.empty();

//                Optional<Date> start = (Optional<Date>) extractorMap.get(FieldType.DATE).extract(task, "Start");
//                Optional<Date> finish = (Optional<Date>) extractorMap.get(FieldType.DATE).extract(task, "Finish");
//
//                taskDuration = (Optional<Double>) extractorMap.get(FieldType.DURATION).extract(task, "Duration");
//
//                if(start.isPresent() && finish.isPresent() && taskDuration.isPresent()) {
//                    Logger.info("Start : " + start.get());
//                    Logger.info("Finish : " + finish.get());
//                    Logger.info("Duration : " + taskDuration.get());
//                }

                for (SciformaField sciformaField : getFieldsToExtract()) {

                    extractorMap.get(sciformaField.getType()).extractAsString(task, sciformaField.getName()).ifPresent(csvLine::add);

                    if ("Duration".equals(sciformaField.getName())) {
                        taskDuration = (Optional<Double>) extractorMap.get(sciformaField.getType()).extract(task, sciformaField.getName());
                    }
                }

                csvLines.add(csvLine.toString());

                taskDuration.ifPresent(duration -> resourceAssignementProcessor.process(task, start, end));

            } catch (PSException e) {
                Logger.error(e, "Failed to retrieve task");
            }

        }

    }

    @Override
    public StringExtractor<Task> getStringExtractor() {
        return stringExtractor;
    }

    @Override
    public DecimalExtractor<Task> getDecimalExtractor() {
        return decimalExtractor;
    }

    @Override
    public BooleanExtractor<Task> getBooleanExtractor() {
        return booleanExtractor;
    }

    @Override
    public DateExtractor<Task> getDateExtractor() {
        return dateExtractor;
    }

    @Override
    public IntegerExtractor<Task> getIntegerExtractor() {
        return integerExtractor;
    }

    @Override
    public ListExtractor<Task> getListExtractor() {
        return listExtractor;
    }

    @Override
    public CalendarExtractor<Task> getCalendarExtractor() {
        return calendarExtractor;
    }

    @Override
    public EffortExtractor<Task> getEffortExtractor() {
        return effortExtractor;
    }
}
