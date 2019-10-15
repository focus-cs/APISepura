package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.JobClassification;
import fr.sciforma.apietnic.business.csv.JobClassificationCsvHelper;
import fr.sciforma.apietnic.business.provider.JobClassificationFieldProvider;
import fr.sciforma.apietnic.service.SciformaService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Getter
public class JobClassificationProcessor extends AbstractSystemDataProcessor<JobClassification> {

    @Autowired
    private JobClassificationFieldProvider fieldProvider;
    @Autowired
    private JobClassificationCsvHelper csvHelper;

    @Override
    protected Optional<JobClassification> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getJobClassifications();
    }

    @Override
    protected List<JobClassification> getChildren(JobClassification fieldAccessor) {
        return fieldAccessor.getChildren();
    }

}
