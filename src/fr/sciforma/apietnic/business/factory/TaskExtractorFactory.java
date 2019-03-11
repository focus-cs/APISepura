package fr.sciforma.apietnic.business.factory;

import com.sciforma.psnext.api.Task;
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
public class TaskExtractorFactory implements ExtractorFactory<Task> {

    private List<SciformaField> fields;

    @PostConstruct
    public void init() {
        fields = new ArrayList<>();
        fields.add(SciformaField.builder().name("#").type(FieldType.INTEGER).build());
        fields.add(SciformaField.builder().name("% Completed").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("%Ecart charge").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("%Ecart durée").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("%Ecart final").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("%Ecart prévision").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Y_Act__externe").type(FieldType.BOOLEAN).build());
        fields.add(SciformaField.builder().name("Closed").type(FieldType.BOOLEAN).build());
        fields.add(SciformaField.builder().name("Analyse avancement").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Analyse budget").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Avancement réel").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Avancement planifié").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Avt basé étapes").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("% Labor Effort Complete").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Avt Acquis").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("%Avt (Manuel)").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Avt théorique activité").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Baseline Duration").type(FieldType.DURATION).build());
        fields.add(SciformaField.builder().name("Baseline Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline1 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline1 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline10 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline10 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline11 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline11 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline12 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline12 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline13 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline13 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline14 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline14 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline2 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline2 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline3 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline3 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline4 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline4 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline5 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline5 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline6 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline6 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline7 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline7 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline8 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline8 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline9 Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline9 Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Charge activité").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Charge finale Ress Sélectionnée").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Actual Labor Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Baseline Labor Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Remaining Labor Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Charge restante Ress  Sélectionnée").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Description").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Duration").type(FieldType.DURATION).build());
        fields.add(SciformaField.builder().name("Ecart").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("YY_Ecart (Etnic)").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Ecart charge").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Ecart début").type(FieldType.DURATION).build());
        fields.add(SciformaField.builder().name("Ecart durée").type(FieldType.DURATION).build());
        fields.add(SciformaField.builder().name("Ecart fin").type(FieldType.DURATION).build());
        fields.add(SciformaField.builder().name("Fin année en cours").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Fin mois en cours").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Fin semaine en cours").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Fin semestre en cours").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Fin trimestre en cours").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("ID").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Index performance (TCPI)").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Index prévision (SPI)").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Internal ID").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Is Parent").type(FieldType.BOOLEAN).build());
        fields.add(SciformaField.builder().name("Manager 1").type(FieldType.USER).build());
        fields.add(SciformaField.builder().name("Manager 2").type(FieldType.USER).build());
        fields.add(SciformaField.builder().name("Manager 3").type(FieldType.USER).build());
        fields.add(SciformaField.builder().name("Name").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Performance").type(FieldType.DECIMAL).build());
//        fields.add(SciformaField.builder().name("Predecessor #s").type(FieldType.null).build());
        fields.add(SciformaField.builder().name("Ratio avancement").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Required Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Required Labor").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Resource #s").type(FieldType.LIST).build());
        fields.add(SciformaField.builder().name("Resource Names").type(FieldType.LIST).build());
        fields.add(SciformaField.builder().name("Ratio budget").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Start Delay").type(FieldType.DURATION).build());
//        fields.add(SciformaField.builder().name("Successor #s").type(FieldType.null).build());
        fields.add(SciformaField.builder().name("Total Float").type(FieldType.DURATION).build());
        fields.add(SciformaField.builder().name("E_TypeActivite").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Unités produites").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Version ID").type(FieldType.INTEGER).build());
        fields.add(SciformaField.builder().name("WBS").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Work Package ID").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Y_Activité_est_un  lot").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Y_activité_lot").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Y_CodeAnalytique_lot").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Y_Demandeur hérité/surchargé").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Y_Demandeur_lot").type(FieldType.STRING).build());
    }
}
