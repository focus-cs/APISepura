package fr.sciforma.apietnic.business.provider;

import com.sciforma.psnext.api.JobClassification;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.business.model.SciformaField;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class JobClassificationFieldProvider implements FieldProvider<JobClassification> {

    private List<SciformaField> fields;

    @PostConstruct
    public void init() {
        fields = new ArrayList<>();
        fields.add(SciformaField.builder().name("Description").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Internal ID").type(FieldType.INTEGER).build());
        fields.add(SciformaField.builder().name("Name").type(FieldType.HIERARCHICAL).build());
    }
}
