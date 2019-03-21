package fr.sciforma.apietnic.business.provider;

import com.sciforma.psnext.api.TimesheetAssignment;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.business.model.SciformaField;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class TimesheetFieldProvider implements FieldProvider<TimesheetAssignment> {

    private List<SciformaField> fields;

    @PostConstruct
    public void init() {
        fields = new ArrayList<>();
        fields.add(SciformaField.builder().name("Actual Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Actual Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Actual Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Charge réelle (Approuvée)").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Charge réelle (Présentée)").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Charge réelle (Validée)").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Daily Notes").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Description").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Duration").type(FieldType.DURATION).build());
        fields.add(SciformaField.builder().name("ID").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Name").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Notes").type(FieldType.INTEGER).build());
        fields.add(SciformaField.builder().name("Project ID").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Project IID").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Project Name").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Remaining Effort").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Remaining Estimate").type(FieldType.DURATION).build());
        fields.add(SciformaField.builder().name("Resource").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Resource IID").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Resource Organization").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Task IID").type(FieldType.DECIMAL).build());
    }

}
