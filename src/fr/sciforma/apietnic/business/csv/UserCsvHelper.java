package fr.sciforma.apietnic.business.csv;

import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.business.provider.ResourceFieldProvider;
import fr.sciforma.apietnic.business.provider.UserFieldProvider;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class UserCsvHelper extends AbstractCsvHelper {

    @Value("${filename.resources}")
    private String filename;

    @Autowired
    private ResourceFieldProvider fieldProvider;
    @Autowired
    private UserFieldProvider userFieldProvider;

    @Override
    protected List<String> getHeader() {

        List<String> header = new ArrayList<>();

        for (SciformaField sciformaField : userFieldProvider.getFields()) {
            header.add(sciformaField.getName());
        }

        for (SciformaField sciformaField : fieldProvider.getFields()) {
            header.add(sciformaField.getName());
        }

        return header;
    }

}
