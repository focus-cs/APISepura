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
import java.util.HashMap;
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

    public void process(Project project, Map<String, String> usersByName) {

        String projectName = null;

        try {

            projectName = project.getStringField("Name");

            Logger.info("Processing tasks for project " + projectName);

            project.open(true);
            TaskOutlineList tasks = project.getTaskOutlineList();

            Map<Integer, Task> taskMap = new HashMap<>();

            for (Object taskItem : tasks) {
                Task task = (Task) taskItem;

                Optional<Integer> taskNumber = (Optional<Integer>) extractorMap.get(FieldType.INTEGER).extract(task, "#");
                taskNumber.ifPresent(integer -> taskMap.putIfAbsent(integer, task));

//                parseTask(task, usersByName, project);

                for (int i = 0; i < tasks.getChildCount(task); i++) {

                    Task childTask = tasks.getChild(task, i);

                    taskNumber = (Optional<Integer>) extractorMap.get(FieldType.INTEGER).extract(childTask, "#");
                    taskNumber.ifPresent(integer -> taskMap.putIfAbsent(integer, childTask));

//                    parseTask(tasks.getChild(task, i), usersByName, project);

                }
//                for (Object o : task.getSuccessorLinksList()) {
//                    TaskLink taskLink = (TaskLink) o;
//                    parseTask(taskLink.getTask(), usersByName, project);
//                }
//
//                for (Object o : task.getPredecessorLinksList()) {
//                    TaskLink taskLink = (TaskLink) o;
//                    parseTask(taskLink.getTask(), usersByName, project);
//                }

            }

            for (Map.Entry<Integer, Task> entry : taskMap.entrySet()) {
                parseTask(entry.getValue(), usersByName, project);
            }


            Logger.info("Tasks for project " + projectName + " have been processed successfully");

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve task outline for project " + projectName);
        } finally {
            try {
                project.close();
            } catch (PSException e) {
                Logger.error(e, "Failed to close project " + projectName);
            }
            csvHelper.flush();
        }

    }

    private void parseTask(Task task, Map<String, String> usersByName, Project project) {

        csvHelper.addLine(buildCsvLine(task, usersByName, project));

        Optional<Date> taskStart = (Optional<Date>) extractorMap.get(FieldType.DATE).extract(task, "Start");
        Optional<Date> taskFinish = (Optional<Date>) extractorMap.get(FieldType.DATE).extract(task, "Finish");

        if (taskStart.isPresent() && taskFinish.isPresent()) {
            resourceAssignementProcessor.process(task, taskStart.get(), taskFinish.get());
        }
    }

    private void parseTasks(List<Task> tasks, Map<String, String> usersByName, Project project) {

        Iterator taskIterator = tasks.iterator();

        while (taskIterator.hasNext()) {
            TaskLink taskLink = (TaskLink) taskIterator.next();

            Task task;
            try {
                task = taskLink.getTask();

                Optional<Date> taskStart = (Optional<Date>) extractorMap.get(FieldType.DATE).extract(task, "Start");
                Optional<Date> taskFinish = (Optional<Date>) extractorMap.get(FieldType.DATE).extract(task, "Finish");

                csvHelper.addLine(buildCsvLine(task, usersByName, project));

                if (taskStart.isPresent() && taskFinish.isPresent()) {
                    resourceAssignementProcessor.process(task, taskStart.get(), taskFinish.get());
                }

            } catch (PSException e) {
                Logger.error(e, "Failed to retrieve task");
            }

        }

    }

    String buildCsvLine(Task fieldAccessor, Map<String, String> usersByName, Project project) {
        StringJoiner csvLine = new StringJoiner(csvDelimiter);

        for (SciformaField sciformaField : getFieldProvider().getFields()) {

            Optional<String> value = extractorMap.get(sciformaField.getType()).extractAsString(fieldAccessor, sciformaField.getName());

            if (sciformaField.getName().equals("Manager 1")
                    || sciformaField.getName().equals("Manager 2")
                    || sciformaField.getName().equals("Manager 3")) {

                if (value.isPresent() && usersByName.containsKey(value.get())) {

                    value = Optional.of(usersByName.get(value.get()));

                }

            }

            csvLine.add(value.orElse(""));

        }

        csvLine.add(extractorMap.get(FieldType.STRING).extractAsString(project, "Name").orElse(""));
        csvLine.add(extractorMap.get(FieldType.DECIMAL_NO_PRECISION).extractAsString(project, "Internal ID").orElse(""));

        return csvLine.toString();
    }

}
