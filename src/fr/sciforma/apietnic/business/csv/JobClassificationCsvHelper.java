package fr.sciforma.apietnic.business.csv;

import com.sciforma.psnext.api.JobClassification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JobClassificationCsvHelper extends AbstractCsvHelper<JobClassification> {

    @Value("${filename.jobClassifications}")
    private String filename;

    @Override
    public String getFilename() {
        return filename;
    }

}
