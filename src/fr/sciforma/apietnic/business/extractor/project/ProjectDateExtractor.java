package fr.sciforma.apietnic.business.extractor.project;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.PSObject;
import com.sciforma.psnext.api.Project;
import com.sciforma.psnext.api.User;
import fr.sciforma.apietnic.business.extractor.Extractor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class ProjectDateExtractor implements Extractor<Project> {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public String extract(Project project, String fieldName) throws PSException {
        return sdf.format(project.getDateField(fieldName));
    }

}
