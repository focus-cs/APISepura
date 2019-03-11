package fr.sciforma.apietnic.business.factory;

import com.sciforma.psnext.api.Organization;
import com.sciforma.psnext.api.User;
import fr.sciforma.apietnic.business.FieldType;
import fr.sciforma.apietnic.business.SciformaField;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class OrganizationExtractorFactory implements ExtractorFactory<Organization> {

    private List<SciformaField> fields;

    @PostConstruct
    public void init() {
        fields = new ArrayList<>();
        fields.add(SciformaField.builder().name("Description").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Internal ID").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Manager 1").type(FieldType.USER).build());
        fields.add(SciformaField.builder().name("Manager 2").type(FieldType.USER).build());
        fields.add(SciformaField.builder().name("Manager 3").type(FieldType.USER).build());
        fields.add(SciformaField.builder().name("Name").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Organization Code").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Responsable 4").type(FieldType.USER).build());
    }
}
