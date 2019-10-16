package fr.sciforma.apietnic.business.csv;

import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.business.provider.ResourceAssignementFieldProvider;
import fr.sciforma.apietnic.business.provider.ResourceFieldProvider;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class ResourceAssignementCsvHelper extends AbstractCsvHelper {

    @Value("${filename.resAssignements}")
    private String filename;

    @Autowired
    private ResourceAssignementFieldProvider fieldProvider;

    @Override
    protected List<String> getHeader() {

        List<String> header = new ArrayList<>();

        header.add(START_HEADER);
        header.add(FINISH_HEADER);

        for (SciformaField sciformaField : fieldProvider.getFields()) {
            header.add(sciformaField.getName());
        }

        return header;
    }

}
