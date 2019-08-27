package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.LockException;
import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Project;
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
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

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
    private DoubleDatedExtractor<Project> doubleDatedExtractor;
    @Autowired
    private StringDatedExtractor<Project> stringDatedExtractor;
    @Autowired
    private HierarchicalExtractor<Project> hierarchicalExtractor;

    @Autowired
    private TaskProcessor taskProcessor;

    public void process(SciformaService sciformaService) {

        Logger.info("Processing file " + csvHelper.getFilename());

        List<Project> projectList = sciformaService.getProjects();

        for (Project project : projectList) {

            try {

                project.open(true);

                Logger.info("Extracting data from project : " + extractorMap.get(FieldType.STRING).extractAsString(project, "Name"));

                csvHelper.addLine(buildCsvLine(project));

                taskProcessor.process(project);

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

        csvHelper.flush();

        Logger.info("File " + csvHelper.getFilename() + " has been processed successfully");

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

    @Override
    public DoubleDatedExtractor<Project> getDoubleDatedExtractor() {
        return doubleDatedExtractor;
    }

    @Override
    public StringDatedExtractor<Project> getStringDatedExtractor() {
        return stringDatedExtractor;
    }

    @Override
    public HierarchicalExtractor<Project> getHierarchicalExtractor() {
        return hierarchicalExtractor;
    }
}
