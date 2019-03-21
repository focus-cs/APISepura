package fr.sciforma.apietnic.business.csv;

import com.sciforma.psnext.api.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TaskCsvHelper extends AbstractCsvHelper<Task> {

    @Value("${filename.tasks}")
    private String filename;

    @Override
    protected String getFilename() {
        return filename;
    }

}
