package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.JobClassification;
import fr.sciforma.apietnic.service.SciformaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JobClassificationProcessor extends AbstractSystemDataProcessor<JobClassification> {

    @Value("${filename.jobClassifications}")
    private String filename;

    @Override
    protected Optional<JobClassification> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getJobClassifications();
    }

    @Override
    protected List<JobClassification> getChildren(JobClassification fieldAccessor) {
        return fieldAccessor.getChildren();
    }

    @Override
    protected String getFilename() {
        return filename;
    }
}
