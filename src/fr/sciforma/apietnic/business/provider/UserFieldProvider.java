package fr.sciforma.apietnic.business.provider;

import com.sciforma.psnext.api.User;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.business.model.SciformaField;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class UserFieldProvider implements FieldProvider<User> {

    private List<SciformaField> fields;

    @PostConstruct
    public void init() {
        fields = new ArrayList<>();
        fields.add(SciformaField.builder().name("Email Address 1").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("First Name").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("ID").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Internal ID").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Last Name").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Login ID").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Middle Name").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Name").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Organization").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Tokens Used").type(FieldType.INTEGER).build());
    }
}
