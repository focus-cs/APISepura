package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.LockException;
import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Project;
import com.sciforma.psnext.api.ResAssignment;
import com.sciforma.psnext.api.Task;
import com.sciforma.psnext.api.TaskOutlineList;
import fr.sciforma.apietnic.business.FieldType;
import fr.sciforma.apietnic.business.SciformaField;
import fr.sciforma.apietnic.business.extractor.Extractor;
import fr.sciforma.apietnic.business.factory.ProjectExtractorFactory;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Component
public class ProjectProcessor extends AbstractProcessor {

    @Autowired
    ProjectExtractorFactory projectExtractorFactory;

    private Map<FieldType, Extractor<Project>> projectExtractorMap = new EnumMap<>(FieldType.class);
    private Map<FieldType, Extractor<Task>> taskExtractorMap = new EnumMap<>(FieldType.class);
    private Map<FieldType, Extractor<ResAssignment>> assignmentExtractorMap = new EnumMap<>(FieldType.class);

    List<String> extractedProjects;
    List<String> extractedTasks;
    List<String> extractedAssignements;

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
    }

    @Override
    public void process(SciformaService sciformaService) {
        List<Project> projectList = sciformaService.getProjects();

        List<SciformaField> projectFieldsToExtract = projectExtractorFactory.getFields();

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

    private void extractTasks(Project project) {

        try {

            TaskOutlineList tasksForProject = project.getTaskOutlineList();

            Task root = tasksForProject.getRoot();


        } catch (PSException e) {
            e.printStackTrace();
        }


    }

}
