package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Project;
import com.sciforma.psnext.api.Task;
import com.sciforma.psnext.api.TaskLink;
import com.sciforma.psnext.api.TaskOutlineList;
import fr.sciforma.apietnic.business.extractor.BooleanExtractor;
import fr.sciforma.apietnic.business.extractor.CalendarExtractor;
import fr.sciforma.apietnic.business.extractor.DateExtractor;
import fr.sciforma.apietnic.business.extractor.DecimalExtractor;
import fr.sciforma.apietnic.business.extractor.IntegerExtractor;
import fr.sciforma.apietnic.business.extractor.ListExtractor;
import fr.sciforma.apietnic.business.extractor.StringExtractor;
import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

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

    @Override
    protected void process(SciformaService sciformaService) {
        throw new UnsupportedOperationException();
    }

    protected void process(Project project) {

        try {

            TaskOutlineList tasks = project.getTaskOutlineList();

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

                Optional<Double> duration = Optional.empty();

                for (SciformaField sciformaField : getFieldsToExtract()) {

                    extractorMap.get(sciformaField.getType()).extractAsString(task, sciformaField.getName()).ifPresent(csvLine::add);

                    Logger.info("PASSED");

                    if ("Duration".equals(sciformaField.getName())) {
                        duration = (Optional<Double>) extractorMap.get(sciformaField.getType()).extract(task, sciformaField.getName());
                    }
                }

                csvLines.add(csvLine.toString());

                duration.ifPresent(s -> resourceAssignementProcessor.process(task, s));

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
}
