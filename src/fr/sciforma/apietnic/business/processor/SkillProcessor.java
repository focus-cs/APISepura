package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Skill;
import fr.sciforma.apietnic.business.csv.SkillCsvHelper;
import fr.sciforma.apietnic.business.provider.SkillFieldProvider;
import fr.sciforma.apietnic.service.SciformaService;
import lombok.Getter;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Getter
public class SkillProcessor extends AbstractSystemDataProcessor<Skill> {

    @Autowired
    private SkillFieldProvider fieldProvider;
    @Autowired
    private SkillCsvHelper csvHelper;

    private Map<String, Skill> skillsByName;

    public Map<String, Skill> getSkillsByName(SciformaService sciformaService) throws PSException {

        Logger.info("Processing file " + csvHelper.getFilename());

        skillsByName = new HashMap<>();

        Optional<Skill> rootSkill = getFieldAccessors(sciformaService);

        if(rootSkill.isPresent()) {
            parse(rootSkill.get());
        }

        csvHelper.flush();

        Logger.info("File " + csvHelper.getFilename() + " has been processed successfully");

        return skillsByName;
    }

    private void parse(Skill rootSkill) throws PSException {

        List<Skill> children = getChildren(rootSkill);

        if (children == null || children.isEmpty()) {

            csvHelper.addLine(buildCsvLine(rootSkill));

            skillsByName.putIfAbsent(rootSkill.toString(), rootSkill);

        } else {

            for (Skill child : children) {

                parse(child);

            }
        }

    }

    @Override
    protected Optional<Skill> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getSkills();
    }

    @Override
    protected List<Skill> getChildren(Skill fieldAccessor) {
        return fieldAccessor.getChildren();
    }

}
