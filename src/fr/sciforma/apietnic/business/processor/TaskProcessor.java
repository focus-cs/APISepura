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
import fr.sciforma.apietnic.business.extractor.DoubleDatedExtractor;
import fr.sciforma.apietnic.business.extractor.EffortExtractor;
import fr.sciforma.apietnic.business.extractor.HierarchicalExtractor;
import fr.sciforma.apietnic.business.extractor.IntegerExtractor;
import fr.sciforma.apietnic.business.extractor.ListExtractor;
import fr.sciforma.apietnic.business.extractor.StringDatedExtractor;
import fr.sciforma.apietnic.business.extractor.StringExtractor;
import fr.sciforma.apietnic.business.model.FieldType;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component
public class TaskProcessor extends AbstractProcessor<Task> {

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
    private DoubleDatedExtractor<Task> doubleDatedExtractor;
    @Autowired
    private StringDatedExtractor<Task> stringDatedExtractor;
    @Autowired
    private HierarchicalExtractor<Task> hierarchicalExtractor;

    @Autowired
    private ResourceAssignementProcessor resourceAssignementProcessor;

    protected void process(Project project) {

        String projectName = null;

        try {

            projectName = project.getStringField("Name");

            Logger.info("Processing tasks for project " + projectName);

            TaskOutlineList tasks = project.getTaskOutlineList();

            Iterator taskTterator = tasks.iterator();
            while (taskTterator.hasNext()) {
                Task task = (Task) taskTterator.next();

                parseTask(task.getSuccessorLinksList());
                parseTask(task.getPredecessorLinksList());
            }

            Logger.info("Tasks for project " + projectName + " have been processed successfully");

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve task outline for project " + projectName);
        } finally {
            csvHelper.flush();
        }

    }

    private void parseTask(List<Task> tasks) {

        Iterator taskIterator = tasks.iterator();

        while (taskIterator.hasNext()) {
            TaskLink taskLink = (TaskLink) taskIterator.next();

            Task task;
            try {
                task = taskLink.getTask();

                Optional<Date> taskStart = (Optional<Date>) extractorMap.get(FieldType.DATE).extract(task, "Start");
                Optional<Date> taskFinish = (Optional<Date>) extractorMap.get(FieldType.DATE).extract(task, "Finish");

                csvHelper.addLine(buildCsvLine(task));

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

    @Override
    public DoubleDatedExtractor<Task> getDoubleDatedExtractor() {
        return doubleDatedExtractor;
    }

    @Override
    public StringDatedExtractor<Task> getStringDatedExtractor() {
        return stringDatedExtractor;
    }

    @Override
    public HierarchicalExtractor<Task> getHierarchicalExtractor() {
        return hierarchicalExtractor;
    }
}
