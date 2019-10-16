package fr.sciforma.apietnic.business.csv;

import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.business.provider.TaskFieldProvider;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class TaskCsvHelper extends AbstractCsvHelper {

    private static final String PROJECT_NAME_HEADER = "Project Name";
    private static final String PROJECT_IID_HEADER = "Project IID";

    @Value("${filename.tasks}")
    private String filename;

    @Autowired
    private TaskFieldProvider fieldProvider;

    @Override
    protected List<String> getHeader() {

        List<String> header = new ArrayList<>();

        for (SciformaField sciformaField : fieldProvider.getFields()) {
            header.add(sciformaField.getName());
        }

        header.add(PROJECT_NAME_HEADER);
        header.add(PROJECT_IID_HEADER);

        return header;
    }

}
