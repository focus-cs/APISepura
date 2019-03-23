package fr.sciforma.apietnic.business.csv;

import com.sciforma.psnext.api.User;
import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.business.provider.ResourceFieldProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserCsvHelper extends AbstractCsvHelper<User> {

    @Value("${filename.resources}")
    private String filename;

    @Autowired
    private ResourceFieldProvider resourceFieldProvider;

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    protected List<String> getHeader() {

        List<String> header = new ArrayList<>();

        for (SciformaField sciformaField : fieldProvider.getFields()) {
            header.add(sciformaField.getName());
        }

        for (SciformaField sciformaField : resourceFieldProvider.getFields()) {
            header.add(sciformaField.getName());
        }

        return header;
    }

}
