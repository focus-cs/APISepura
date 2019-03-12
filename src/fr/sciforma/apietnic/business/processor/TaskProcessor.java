package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Project;
import com.sciforma.psnext.api.Task;
import com.sciforma.psnext.api.TaskOutlineList;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.business.extractor.Extractor;
import fr.sciforma.apietnic.business.factory.UserExtractorFactory;
import fr.sciforma.apietnic.service.SciformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class TaskProcessor extends AbstractProcessor<Task> {

    @Autowired
    UserExtractorFactory extractorFactory;

    private Map<FieldType, Extractor<Task>> extractorMap = new EnumMap<>(FieldType.class);

    @PostConstruct
    public void postConstruct() {
        extractorMap.putIfAbsent(FieldType.STRING, stringExtractor);
        extractorMap.putIfAbsent(FieldType.DECIMAL, decimalExtractor);
        extractorMap.putIfAbsent(FieldType.BOOLEAN, booleanExtractor);
        extractorMap.putIfAbsent(FieldType.COST, decimalExtractor);
        extractorMap.putIfAbsent(FieldType.EFFORT, decimalExtractor);
        extractorMap.putIfAbsent(FieldType.DATE, dateExtractor);
        extractorMap.putIfAbsent(FieldType.FORMULA, stringExtractor);
        extractorMap.putIfAbsent(FieldType.DURATION, decimalExtractor);
        extractorMap.putIfAbsent(FieldType.INTEGER, integerExtractor);
        extractorMap.putIfAbsent(FieldType.USER, stringExtractor);
        extractorMap.putIfAbsent(FieldType.RESOURCE, stringExtractor);
        extractorMap.putIfAbsent(FieldType.URL, stringExtractor);
        extractorMap.putIfAbsent(FieldType.CALENDAR, stringExtractor);
        extractorMap.putIfAbsent(FieldType.EFFORT_RATE, stringExtractor);
        extractorMap.putIfAbsent(FieldType.LIST, listExtractor);
    }

    @Override
    public void process(SciformaService sciformaService) {

        List<SciformaField> userFieldsToExtract = extractorFactory.getFields();

        for (Project project : sciformaService.getProjects()) {

            Optional<TaskOutlineList> tasksForProject = sciformaService.getTasksForProject(project);
            if (tasksForProject.isPresent()) {
                // TODO: parse recursively
                TaskOutlineList taskOutlineList = tasksForProject.get();
            }
        }

    }

    @Override
    protected String getFilename() {
        return null;
    }
}
