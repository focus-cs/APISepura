package fr.sciforma.apietnic.business.provider;

import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.business.model.SciformaField;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class ResourceFieldProvider implements FieldProvider {

    private List<SciformaField> fields;

    @PostConstruct
    public void init() {
        fields = new ArrayList<>();
        fields.add(SciformaField.builder().name("End Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Job Classification").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Métier direct").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Start Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Status").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("User Role").type(FieldType.STRING).build());
    }
}
