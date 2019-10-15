package fr.sciforma.apietnic.business.csv;

import fr.sciforma.apietnic.business.provider.ProjectFieldProvider;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ProjectCsvHelper extends AbstractCsvHelper {

    @Value("${filename.projects}")
    private String filename;

    @Autowired
    private ProjectFieldProvider fieldProvider;

}
