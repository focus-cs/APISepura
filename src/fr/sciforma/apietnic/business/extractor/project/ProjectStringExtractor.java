package fr.sciforma.apietnic.business.extractor.project;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Project;
import fr.sciforma.apietnic.business.extractor.Extractor;
import org.springframework.stereotype.Component;

@Component
public class ProjectStringExtractor implements Extractor<Project> {

    @Override
    public String extract(Project project, String fieldName) throws PSException {
        return project.getStringField(fieldName);
    }

}
