package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.LockException;
import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Project;
import fr.sciforma.apietnic.business.extractor.BooleanExtractor;
import fr.sciforma.apietnic.business.extractor.CalendarExtractor;
import fr.sciforma.apietnic.business.extractor.DateExtractor;
import fr.sciforma.apietnic.business.extractor.DecimalExtractor;
import fr.sciforma.apietnic.business.extractor.EffortExtractor;
import fr.sciforma.apietnic.business.extractor.IntegerExtractor;
import fr.sciforma.apietnic.business.extractor.ListExtractor;
import fr.sciforma.apietnic.business.extractor.StringExtractor;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Component
public class ProjectProcessor extends AbstractProcessor<Project> {

    @Value("${filename.projects}")
    private String filename;

    @Autowired
    private StringExtractor<Project> stringExtractor;
    @Autowired
    private DecimalExtractor<Project> decimalExtractor;
    @Autowired
    private BooleanExtractor<Project> booleanExtractor;
    @Autowired
    private DateExtractor<Project> dateExtractor;
    @Autowired
    private IntegerExtractor<Project> integerExtractor;
    @Autowired
    private ListExtractor<Project> listExtractor;
    @Autowired
    private CalendarExtractor<Project> calendarExtractor;
    @Autowired
    private EffortExtractor<Project> effortExtractor;

    @Autowired
    private TaskProcessor taskProcessor;
    @Autowired
    private ResourceAssignementProcessor resourceAssignementProcessor;

    public void process(SciformaService sciformaService) {

        List<Project> projectList = sciformaService.getProjects();

        int testCpt = 0;

        for (Project project : projectList) {

            try {

                project.open(true);

                Logger.info("Extracting data from project : " + extractorMap.get(FieldType.STRING).extractAsString(project, "Name"));

                StringJoiner csvLine = new StringJoiner(csvDelimiter);

                for (SciformaField sciformaField : getFieldsToExtract()) {

                    Optional<String> value = extractorMap.get(sciformaField.getType()).extractAsString(project, sciformaField.getName());
                    if (value.isPresent()) {
                        csvLine.add(value.get());
                    } else {
                        csvLine.add("");
                    }

                }

                csvHelper.addLine(csvLine.toString());

                taskProcessor.process(project.getTaskOutlineList());

                //TODO: remove this (for testing purpose)
                testCpt++;
                if (testCpt > 0) {
                    break;
                }

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
    public StringExtractor<Project> getStringExtractor() {
        return stringExtractor;
    }

    @Override
    public DecimalExtractor<Project> getDecimalExtractor() {
        return decimalExtractor;
    }

    @Override
    public BooleanExtractor<Project> getBooleanExtractor() {
        return booleanExtractor;
    }

    @Override
    public DateExtractor<Project> getDateExtractor() {
        return dateExtractor;
    }

    @Override
    public IntegerExtractor<Project> getIntegerExtractor() {
        return integerExtractor;
    }

    @Override
    public ListExtractor<Project> getListExtractor() {
        return listExtractor;
    }

    @Override
    public CalendarExtractor<Project> getCalendarExtractor() {
        return calendarExtractor;
    }

    @Override
    public EffortExtractor<Project> getEffortExtractor() {
        return effortExtractor;
    }
}
