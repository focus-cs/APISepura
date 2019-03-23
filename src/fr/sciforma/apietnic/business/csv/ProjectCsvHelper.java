package fr.sciforma.apietnic.business.csv;

import com.sciforma.psnext.api.Project;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProjectCsvHelper extends AbstractCsvHelper<Project> {

    @Value("${filename.projects}")
    private String filename;

    @Override
    public String getFilename() {
        return filename;
    }

}
