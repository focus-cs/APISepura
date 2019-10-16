package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.LockException;
import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Project;
import fr.sciforma.apietnic.business.csv.ProjectCsvHelper;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.business.provider.ProjectFieldProvider;
import fr.sciforma.apietnic.service.SciformaService;
import lombok.Getter;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

@Component
@Getter
public class ProjectProcessor extends AbstractProcessor<Project> {

    @Value("${filename.projects}")
    private String filename;

    @Autowired
    private ProjectFieldProvider fieldProvider;
    @Autowired
    private ProjectCsvHelper csvHelper;

    public Map<Double, Project> process(SciformaService sciformaService, Map<String, String> usersByName, Map<String, String> portfoliosByName, Map<String, String> organizationByName) {

        Logger.info("Processing file " + csvHelper.getFilename());

        List<Project> projectList = sciformaService.getProjects();

        Map<Double, Project> projectsById = new HashMap<>();

        int cpt = 0;

        for (Project project : projectList) {

            try {

                project.open(true);

                Logger.info("Extracting data from project : " + extractorMap.get(FieldType.STRING).extractAsString(project, "Name"));

                csvHelper.addLine(buildCsvLine(project, usersByName, portfoliosByName, organizationByName));

                projectsById.putIfAbsent(project.getDoubleField("Internal Id"), project);

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

            if (cpt > 10) {
                break;
            }
            cpt++;

        }

        csvHelper.flush();

        Logger.info("File " + csvHelper.getFilename() + " has been processed successfully");

        return projectsById;

    }

    String buildCsvLine(Project fieldAccessor, Map<String, String> usersByName, Map<String, String> portfoliosByName, Map<String, String> organizationByName) {
        StringJoiner csvLine = new StringJoiner(csvDelimiter);

        for (SciformaField sciformaField : fieldProvider.getFields()) {

            Optional<String> value = extractorMap.get(sciformaField.getType()).extractAsString(fieldAccessor, sciformaField.getName());

            if (sciformaField.getName().equals("Manager 1")
                    || sciformaField.getName().equals("Manager 2")
                    || sciformaField.getName().equals("Manager 3")) {

                if (value.isPresent() && usersByName.containsKey(value.get())) {

                    value = Optional.of(usersByName.get(value.get()));

                }

            } else if (sciformaField.getName().equals("Portfolio Folder")) {

                if (value.isPresent() && portfoliosByName.containsKey(value.get())) {

                    value = Optional.of(portfoliosByName.get(value.get()));

                }

            } else if (sciformaField.getName().equals("Owning Organization")) {

                if (value.isPresent() && organizationByName.containsKey(value.get())) {

                    value = Optional.of(organizationByName.get(value.get()));

                }

            }

            csvLine.add(value.orElse(""));

        }
        return csvLine.toString();
    }

}
