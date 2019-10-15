package fr.sciforma.apietnic.business.csv;

import fr.sciforma.apietnic.business.provider.OrganizationFieldProvider;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class OrganizationCsvHelper extends AbstractCsvHelper {

    @Value("${filename.organizations}")
    private String filename;

    @Autowired
    private OrganizationFieldProvider fieldProvider;

}
