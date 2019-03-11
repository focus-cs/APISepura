package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.*;
import fr.sciforma.apietnic.business.FieldType;
import fr.sciforma.apietnic.business.SciformaField;
import fr.sciforma.apietnic.business.extractor.Extractor;
import fr.sciforma.apietnic.business.factory.ResourceAssignementExtractorFactory;
import fr.sciforma.apietnic.business.factory.ProjectExtractorFactory;
import fr.sciforma.apietnic.business.factory.TaskExtractorFactory;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class ProjectProcessor extends AbstractProcessor<Project> {

    @Value("${filename.projects}")
    private String projectsFilename;
    @Value("${filename.tasks}")
    private String tasksFilename;
    @Value("${filename.resAssignements}")
    private String resourceAssignementFilename;

    @Autowired
    ProjectExtractorFactory projectExtractorFactory;
    @Autowired
    TaskExtractorFactory taskExtractorFactory;
    @Autowired
    ResourceAssignementExtractorFactory resourceAssignementExtractorFactory;

    private Map<FieldType, Extractor<Project>> projectExtractorMap = new EnumMap<>(FieldType.class);
    private Map<FieldType, Extractor<Task>> taskExtractorMap = new EnumMap<>(FieldType.class);
    private Map<FieldType, Extractor<ResAssignment>> assignmentExtractorMap = new EnumMap<>(FieldType.class);

    private List<String> extractedProjects;
    private List<String> extractedTasks;
    private List<String> extractedAssignements;

    private List<SciformaField> taskFieldsToExtract;
    private List<SciformaField> assignementFieldsToExtract;

    @PostConstruct
    public void postConstruct() {
        projectExtractorMap.putIfAbsent(FieldType.STRING, stringExtractor);
        projectExtractorMap.putIfAbsent(FieldType.DECIMAL, decimalExtractor);
        projectExtractorMap.putIfAbsent(FieldType.BOOLEAN, booleanExtractor);
        projectExtractorMap.putIfAbsent(FieldType.COST, decimalExtractor);
        projectExtractorMap.putIfAbsent(FieldType.EFFORT, decimalExtractor);
        projectExtractorMap.putIfAbsent(FieldType.DATE, dateExtractor);
        projectExtractorMap.putIfAbsent(FieldType.FORMULA, stringExtractor);
        projectExtractorMap.putIfAbsent(FieldType.DURATION, decimalExtractor);
        projectExtractorMap.putIfAbsent(FieldType.INTEGER, integerExtractor);
        projectExtractorMap.putIfAbsent(FieldType.USER, stringExtractor);
        projectExtractorMap.putIfAbsent(FieldType.RESOURCE, stringExtractor);
        projectExtractorMap.putIfAbsent(FieldType.URL, stringExtractor);
        projectExtractorMap.putIfAbsent(FieldType.CALENDAR, stringExtractor);
        projectExtractorMap.putIfAbsent(FieldType.EFFORT_RATE, stringExtractor);
        projectExtractorMap.putIfAbsent(FieldType.LIST, listExtractor);

        taskExtractorMap.putIfAbsent(FieldType.STRING, stringExtractor);
        taskExtractorMap.putIfAbsent(FieldType.DECIMAL, decimalExtractor);
        taskExtractorMap.putIfAbsent(FieldType.BOOLEAN, booleanExtractor);
        taskExtractorMap.putIfAbsent(FieldType.COST, decimalExtractor);
        taskExtractorMap.putIfAbsent(FieldType.EFFORT, decimalExtractor);
        taskExtractorMap.putIfAbsent(FieldType.DATE, dateExtractor);
        taskExtractorMap.putIfAbsent(FieldType.FORMULA, stringExtractor);
        taskExtractorMap.putIfAbsent(FieldType.DURATION, decimalExtractor);
        taskExtractorMap.putIfAbsent(FieldType.INTEGER, integerExtractor);
        taskExtractorMap.putIfAbsent(FieldType.USER, stringExtractor);
        taskExtractorMap.putIfAbsent(FieldType.RESOURCE, stringExtractor);
        taskExtractorMap.putIfAbsent(FieldType.URL, stringExtractor);
        taskExtractorMap.putIfAbsent(FieldType.CALENDAR, stringExtractor);
        taskExtractorMap.putIfAbsent(FieldType.EFFORT_RATE, stringExtractor);
        taskExtractorMap.putIfAbsent(FieldType.LIST, listExtractor);

        assignmentExtractorMap.putIfAbsent(FieldType.STRING, stringExtractor);
        assignmentExtractorMap.putIfAbsent(FieldType.DECIMAL, decimalExtractor);
        assignmentExtractorMap.putIfAbsent(FieldType.BOOLEAN, booleanExtractor);
        assignmentExtractorMap.putIfAbsent(FieldType.COST, decimalExtractor);
        assignmentExtractorMap.putIfAbsent(FieldType.EFFORT, decimalExtractor);
        assignmentExtractorMap.putIfAbsent(FieldType.DATE, dateExtractor);
        assignmentExtractorMap.putIfAbsent(FieldType.FORMULA, stringExtractor);
        assignmentExtractorMap.putIfAbsent(FieldType.DURATION, decimalExtractor);
        assignmentExtractorMap.putIfAbsent(FieldType.INTEGER, integerExtractor);
        assignmentExtractorMap.putIfAbsent(FieldType.USER, stringExtractor);
        assignmentExtractorMap.putIfAbsent(FieldType.RESOURCE, stringExtractor);
        assignmentExtractorMap.putIfAbsent(FieldType.URL, stringExtractor);
        assignmentExtractorMap.putIfAbsent(FieldType.CALENDAR, stringExtractor);
        assignmentExtractorMap.putIfAbsent(FieldType.EFFORT_RATE, stringExtractor);
        assignmentExtractorMap.putIfAbsent(FieldType.LIST, listExtractor);
    }

    @Override
    public void process(SciformaService sciformaService) {

        List<Project> projectList = sciformaService.getProjects();

        List<SciformaField> projectFieldsToExtract = projectExtractorFactory.getFields();
        taskFieldsToExtract = taskExtractorFactory.getFields();
        assignementFieldsToExtract = resourceAssignementExtractorFactory.getFields();


        extractedProjects = new ArrayList<>(projectList.size());
        extractedTasks = new ArrayList<>();
        extractedAssignements = new ArrayList<>();

        for (Project project : projectList) {

            try {

                project.open(true);

                Logger.info("Extracting data from project : " + projectExtractorMap.get(FieldType.STRING).extract(project, "Name"));

                StringJoiner csvLine = new StringJoiner(csvDelimiter);

                for (SciformaField sciformaField : projectFieldsToExtract) {

                    projectExtractorMap.get(sciformaField.getType()).extract(project, sciformaField.getName()).ifPresent(csvLine::add);

                }

                Logger.info(csvLine.toString());

                extractedProjects.add(csvLine.toString());

                extractTasks(project);

                //TODO: remove this (for testing purpose)
                break;

            } catch (LockException e) {
                Logger.error("Project is locked by " + e.getLockingUser());
            } catch (PSException e) {
                Logger.error(e);
            } finally {

                try {
                    project.close();
                } catch (PSException e) {
                    Logger.error("Failed to close project");
                }

            }

        }
    }

    @Override
    protected String getFilename() {
        return null;
    }

    private void extractTasks(Project project) {

        try {

            TaskOutlineList tasks = project.getTaskOutlineList();

            Iterator taskTterator = tasks.iterator();
            while (taskTterator.hasNext()) {
                Task task = (Task) taskTterator.next();

                parseTask(task.getSuccessorLinksList());
                parseTask(task.getPredecessorLinksList());
            }


        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve task outline for project " + projectExtractorMap.get(FieldType.STRING).extract(project, "Name"));
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

                for (SciformaField sciformaField : taskFieldsToExtract) {
                    taskExtractorMap.get(sciformaField.getType()).extract(task, sciformaField.getName()).ifPresent(csvLine::add);
                }

                extractedTasks.add(csvLine.toString());

                extractAssignements(task);

            } catch (PSException e) {
                Logger.error(e, "Failed to retrieve task");
            }

        }
    }

    private void extractAssignements(Task task) {

        try {

            List resAssignmentList = task.getResAssignmentList();

            Iterator assignementIterator = resAssignmentList.iterator();
            while (assignementIterator.hasNext()) {
                ResAssignment resAssignment = (ResAssignment) assignementIterator.next();

                StringJoiner csvLine = new StringJoiner(csvDelimiter);

                for (SciformaField sciformaField : assignementFieldsToExtract) {
                    assignmentExtractorMap.get(sciformaField.getType()).extract(resAssignment, sciformaField.getName()).ifPresent(csvLine::add);
                }

                Logger.info(csvLine.toString());
                extractedAssignements.add(csvLine.toString());

            }

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve resource assignement for task " + taskExtractorMap.get(FieldType.STRING).extract(task, "Name"));
        }

    }

}
