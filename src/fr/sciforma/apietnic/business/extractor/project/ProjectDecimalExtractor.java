package fr.sciforma.apietnic.business.extractor.project;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Project;
import fr.sciforma.apietnic.business.extractor.Extractor;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
public class ProjectDecimalExtractor implements Extractor<Project> {

    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    @Override
    public String extract(Project project, String fieldName) throws PSException {
        return decimalFormat.format(project.getDoubleField(fieldName));
    }

}
