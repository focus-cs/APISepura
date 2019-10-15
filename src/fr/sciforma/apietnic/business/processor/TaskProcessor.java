package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Project;
import com.sciforma.psnext.api.Task;
import com.sciforma.psnext.api.TaskLink;
import com.sciforma.psnext.api.TaskOutlineList;
import fr.sciforma.apietnic.business.csv.TaskCsvHelper;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.business.provider.TaskFieldProvider;
import lombok.Getter;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

@Component
@Getter
public class TaskProcessor extends AbstractProcessor<Task> {

    @Autowired
    private TaskFieldProvider fieldProvider;
    @Autowired
    private TaskCsvHelper csvHelper;

    @Autowired
    private ResourceAssignementProcessor resourceAssignementProcessor;

    public void process(Project project, Map<String, Integer> usersByName) {

        String projectName = null;

        try {

            projectName = project.getStringField("Name");

            Logger.info("Processing tasks for project " + projectName);

            TaskOutlineList tasks = project.getTaskOutlineList();

            Iterator taskTterator = tasks.iterator();
            while (taskTterator.hasNext()) {
                Task task = (Task) taskTterator.next();

                parseTask(task.getSuccessorLinksList(), usersByName);
                parseTask(task.getPredecessorLinksList(), usersByName);
            }

            Logger.info("Tasks for project " + projectName + " have been processed successfully");

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve task outline for project " + projectName);
        } finally {
            csvHelper.flush();
        }

    }

    private void parseTask(List<Task> tasks, Map<String, Integer> usersByName) {

        Iterator taskIterator = tasks.iterator();

        while (taskIterator.hasNext()) {
            TaskLink taskLink = (TaskLink) taskIterator.next();

            Task task;
            try {
                task = taskLink.getTask();

                Optional<Date> taskStart = (Optional<Date>) extractorMap.get(FieldType.DATE).extract(task, "Start");
                Optional<Date> taskFinish = (Optional<Date>) extractorMap.get(FieldType.DATE).extract(task, "Finish");

                csvHelper.addLine(buildCsvLine(task, usersByName));

                if (taskStart.isPresent() && taskFinish.isPresent()) {
                    resourceAssignementProcessor.process(task, taskStart.get(), taskFinish.get());
                }

            } catch (PSException e) {
                Logger.error(e, "Failed to retrieve task");
            }

        }

    }

    String buildCsvLine(Task fieldAccessor, Map<String, Integer> usersByName) {
        StringJoiner csvLine = new StringJoiner(csvDelimiter);

        for (SciformaField sciformaField : getFieldProvider().getFields()) {

            Optional<String> value = extractorMap.get(sciformaField.getType()).extractAsString(fieldAccessor, sciformaField.getName());

            if (sciformaField.getName().equals("Manager 1")
                    || sciformaField.getName().equals("Manager 2")
                    || sciformaField.getName().equals("Manager 3")) {

                if (value.isPresent() && usersByName.containsKey(value.get())) {

                    value = Optional.of(String.valueOf(usersByName.get(value.get())));

                }

            }

            if (value.isPresent()) {
                csvLine.add(value.get());
            } else {
                csvLine.add("");
            }

        }
        return csvLine.toString();
    }

}
