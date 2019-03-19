package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.*;
import fr.sciforma.apietnic.business.extractor.*;
import fr.sciforma.apietnic.business.model.FieldType;
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

    protected void process(List<TaskOutlineList> tasks) {

        try {

            Iterator taskTterator = tasks.iterator();
            while (taskTterator.hasNext()) {
                Task task = (Task) taskTterator.next();

                parseTask(task.getSuccessorLinksList());
                parseTask(task.getPredecessorLinksList());
            }


        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve task outline for project");
        }

    }

    private void parseTask(List<Task> tasks) {

        Iterator taskIterator = tasks.iterator();

        while (taskIterator.hasNext()) {
            TaskLink taskLink = (TaskLink) taskIterator.next();

            Task task;
            try {
                task = taskLink.getTask();

                StringJoiner csvLine = new StringJoiner(csvDelimiter);

                Optional<Date> taskStart = Optional.empty();
                Optional<Date> taskFinish = Optional.empty();

                for (SciformaField sciformaField : getFieldsToExtract()) {

                    extractorMap.get(sciformaField.getType()).extractAsString(task, sciformaField.getName()).ifPresent(csvLine::add);

                    if ("Start".equals(sciformaField.getName())) {
                        taskStart = (Optional<Date>) extractorMap.get(FieldType.DATE).extract(task, sciformaField.getName());
                    }

                    if ("Finish".equals(sciformaField.getName())) {
                        taskFinish = (Optional<Date>) extractorMap.get(FieldType.DATE).extract(task, sciformaField.getName());
                    }
                }

                csvLines.add(csvLine.toString());

                if (taskStart.isPresent() && taskFinish.isPresent()) {
                    resourceAssignementProcessor.process(task, taskStart.get(), taskFinish.get());
                }

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
