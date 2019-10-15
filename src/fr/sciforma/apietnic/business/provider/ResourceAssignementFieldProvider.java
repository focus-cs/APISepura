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
public class ResourceAssignementFieldProvider implements FieldProvider {

    private List<SciformaField> fields;

    @PostConstruct
    public void init() {
        fields = new ArrayList<>();
        fields.add(SciformaField.builder().name("Actual Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Baseline1 Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Baseline1 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline1 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline10 Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Baseline10 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline10 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline11 Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Baseline11 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline11 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline12 Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Baseline12 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline12 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline13 Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Baseline13 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline13 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline14 Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Baseline14 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline14 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline2 Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Baseline2 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline2 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline3 Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Baseline3 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline3 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline4 Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Baseline4 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline4 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline5 Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Baseline5 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline5 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline6 Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Baseline6 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline6 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline7 Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Baseline7 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline7 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline8 Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Baseline8 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline8 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline9 Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Baseline9 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline9 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Duration").type(FieldType.DURATION).build());
        fields.add(SciformaField.builder().name("Effort % Complete").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("ID").type(FieldType.STRING).build());
//        fields.add(SciformaField.builder().name("Internal ID").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Job Classification").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Name").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Physical % Complete").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Project IID").type(FieldType.DECIMAL_NO_PRECISION).build());
        fields.add(SciformaField.builder().name("Rate").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Remaining Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Resource IID").type(FieldType.DECIMAL_NO_PRECISION).build());
        fields.add(SciformaField.builder().name("Skills").type(FieldType.LIST).build());
        fields.add(SciformaField.builder().name("Task IID").type(FieldType.DECIMAL_NO_PRECISION).build());
        fields.add(SciformaField.builder().name("Version ID").type(FieldType.INTEGER).build());
    }
}
