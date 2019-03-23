package fr.sciforma.apietnic.business.csv;

import com.sciforma.psnext.api.ResAssignment;
import fr.sciforma.apietnic.business.model.SciformaField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResourceAssignementCsvHelper extends AbstractCsvHelper<ResAssignment> {

    @Value("${filename.resAssignements}")
    private String filename;

    @Override
    public String getFilename() {
        return filename;
    }

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
