package fr.sciforma.apietnic.business.provider;

import com.sciforma.psnext.api.Project;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.business.model.SciformaField;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class ProjectFieldProvider implements FieldProvider<Project> {

    private List<SciformaField> fields;

    @PostConstruct
    public void init() {
        fields = new ArrayList<>();
        fields.add(SciformaField.builder().name("% Completed").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("%Ecart charge").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("%Ecart durée").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("%Ecart final").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("%Ecart prévision").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Active").type(FieldType.BOOLEAN).build());
        fields.add(SciformaField.builder().name("Actual Labor Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Analyse avancement").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Analyse budget").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Approved").type(FieldType.BOOLEAN).build());
        fields.add(SciformaField.builder().name("As of Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Avancement réel").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Baseline Duration").type(FieldType.DURATION).build());
        fields.add(SciformaField.builder().name("Baseline Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline Labor Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Baseline Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline1 Saved Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline10 Saved Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline11 Saved Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline12 Saved Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline13 Saved Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline14 Saved Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline2 Saved Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline3 Saved Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline4 Saved Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline5 Saved Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline6 Saved Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline7 Saved Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline8 Saved Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Baseline9 Saved Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Charge finale").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Charge réelle").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Charge restante").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Closed").type(FieldType.BOOLEAN).build());
        fields.add(SciformaField.builder().name("Create Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Current Baseline").type(FieldType.INTEGER).build());
        fields.add(SciformaField.builder().name("Début réel").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Description").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Duration").type(FieldType.DURATION).build());
        fields.add(SciformaField.builder().name("Ecart charge").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Ecart début").type(FieldType.DURATION).build());
        fields.add(SciformaField.builder().name("Ecart durée").type(FieldType.DURATION).build());
        fields.add(SciformaField.builder().name("Ecart fin").type(FieldType.DURATION).build());
        fields.add(SciformaField.builder().name("Ecart final").type(FieldType.COST).build());
        fields.add(SciformaField.builder().name("Ecart prévision").type(FieldType.COST).build());
        fields.add(SciformaField.builder().name("Finish").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Finish Constraint").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("ID").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Initiator").type(FieldType.USER).build());
        fields.add(SciformaField.builder().name("Internal ID").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Manager 1").type(FieldType.USER).build());
        fields.add(SciformaField.builder().name("Manager 2").type(FieldType.USER).build());
        fields.add(SciformaField.builder().name("Manager 3").type(FieldType.USER).build());
        fields.add(SciformaField.builder().name("Modified By").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Modified Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("N° ligne").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Name").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Portfolio Folder").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Published Date").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Ratio avancement").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Ratio ConsommationCharge Réelle/Finale").type(FieldType.DECIMAL).build());
        fields.add(SciformaField.builder().name("Start").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Start Constraint").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Total Labor Effort").type(FieldType.EFFORT).build());
        fields.add(SciformaField.builder().name("Version").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Version ID").type(FieldType.INTEGER).build());
        fields.add(SciformaField.builder().name("Y_Analyste_Backup_nom").type(FieldType.RESOURCE).build());
        fields.add(SciformaField.builder().name("Y_CatégorieAnalytique_projet").type(FieldType.INTEGER).build());
        fields.add(SciformaField.builder().name("Y_CodeAnalytique_projet").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Y_DansProjetsStratégiquesCOSTRADI").type(FieldType.BOOLEAN).build());
        fields.add(SciformaField.builder().name("Y_Date demande").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Y_Date enreg. réf.").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Y_Date fin").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Y_Date statut demande ou projet").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Y_Decision").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Y_Décision_Risque_Exogene").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Y_DemandeurResp1").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Y_DemandeurResp2").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Y_Demandeur_projet").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Y_Documentation").type(FieldType.URL).build());
        fields.add(SciformaField.builder().name("Y_EstDeTypeET").type(FieldType.BOOLEAN).build());
        fields.add(SciformaField.builder().name("Y_Etat demande ou projet").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Y_Evalutation_Risque").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Y_Fin réf. 1").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Y_Fin réf. 2").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Y_Fin réf. 3").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Y_Fin réf. 4").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Y_Libellé_projet").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Y_lot").type(FieldType.BOOLEAN).build());
        fields.add(SciformaField.builder().name("Y_MElissage").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Y_Nature demande ou projet").type(FieldType.STRING).build());
        fields.add(SciformaField.builder().name("Y_Objectif fin").type(FieldType.DATE).build());
        fields.add(SciformaField.builder().name("Y_ProgrammeListe_projet").type(FieldType.LIST).build());
    }

}
