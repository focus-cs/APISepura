package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.LockException;
import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Project;
import fr.sciforma.apietnic.business.FieldType;
import fr.sciforma.apietnic.business.SciformaField;
import fr.sciforma.apietnic.business.extractor.Extractor;
import fr.sciforma.apietnic.business.extractor.project.*;
import fr.sciforma.apietnic.business.factory.ProjectExtractorFactory;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Component
public class ProjectProcessor extends AbstractProcessor {

    @Autowired
    ProjectExtractorFactory projectExtractorFactory;
    @Autowired
    ProjectStringExtractor stringExtractor;
    @Autowired
    ProjectDecimalExtractor decimalExtractor;
    @Autowired
    ProjectBooleanExtractor booleanExtractor;
    @Autowired
    ProjectDateExtractor dateExtractor;
    @Autowired
    ProjectIntegerExtractor integerExtractor;
    @Autowired
    ProjectListExtractor listExtractor;

    private Map<FieldType, Extractor<Project>> extractorMap = new EnumMap<>(FieldType.class);

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
        List<Project> projectList = sciformaService.getProjects();

        List<SciformaField> projectFieldsToExtract = projectExtractorFactory.getFields();

        for (Project project : projectList) {

            try {

                project.open(false);

                Logger.info("Extracting data from project : " + project.getStringField("Name"));

                StringJoiner csvLine = new StringJoiner(csvDelimiter);

                for (SciformaField sciformaField : projectFieldsToExtract) {

                    csvLine.add(extractorMap.get(sciformaField.getType()).extract(project, sciformaField.getName()));

                }

                Logger.info(csvLine.toString());

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
}
