package fr.sciforma.apietnic.business.csv;

import com.sciforma.psnext.api.Organization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrganizationCsvHelper extends AbstractCsvHelper<Organization> {

    @Value("${filename.organizations}")
    private String filename;

    @Override
    public String getFilename() {
        return filename;
    }

}
