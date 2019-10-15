package fr.sciforma.apietnic.business.csv;

import fr.sciforma.apietnic.business.provider.TaskFieldProvider;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TaskCsvHelper extends AbstractCsvHelper {

    @Value("${filename.tasks}")
    private String filename;

    @Autowired
    private TaskFieldProvider fieldProvider;

}
