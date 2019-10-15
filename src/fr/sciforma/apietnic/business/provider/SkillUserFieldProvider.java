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
public class SkillUserFieldProvider implements FieldProvider {

    private List<SciformaField> fields;

    @PostConstruct
    public void init() {
        fields = new ArrayList<>();
        fields.add(SciformaField.builder().name("Ressource IID").type(FieldType.DECIMAL_NO_PRECISION).build());
        fields.add(SciformaField.builder().name("Ressource Name").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Skill IID").type(FieldType.DECIMAL_NO_PRECISION).build());
        fields.add(SciformaField.builder().name("Skill Name").type(FieldType.STRING).build());
    }
}
