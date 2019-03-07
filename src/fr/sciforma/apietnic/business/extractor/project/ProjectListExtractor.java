package fr.sciforma.apietnic.business.extractor.project;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Project;
import fr.sciforma.apietnic.business.extractor.Extractor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

@Component
public class ProjectListExtractor implements Extractor<Project> {

    @Override
    public String extract(Project project, String fieldName) throws PSException {

        List listField = project.getListField(fieldName);

        StringJoiner joinedValue = new StringJoiner(",");
        for (Object o : listField) {
            if(o instanceof String) {
                joinedValue.add((String) o);
            }
        }

        return joinedValue.toString();
    }

}
