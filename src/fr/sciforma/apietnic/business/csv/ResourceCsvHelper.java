package fr.sciforma.apietnic.business.csv;

import com.sciforma.psnext.api.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ResourceCsvHelper extends AbstractCsvHelper<Resource> {

    @Value("${filename.resources}")
    private String filename;

    @Override
    protected String getFilename() {
        return filename;
    }

}
