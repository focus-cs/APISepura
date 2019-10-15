package fr.sciforma.apietnic.business.csv;

import fr.sciforma.apietnic.business.provider.ResourceFieldProvider;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ResourceCsvHelper extends AbstractCsvHelper {

    @Value("${filename.resources}")
    private String filename;

    @Autowired
    private ResourceFieldProvider fieldProvider;

}
