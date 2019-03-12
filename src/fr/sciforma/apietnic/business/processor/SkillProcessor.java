package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Skill;
import fr.sciforma.apietnic.service.SciformaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SkillProcessor extends AbstractSystemDataProcessor<Skill> {

    @Value("${filename.skills}")
    private String filename;

    @Override
    protected Optional<Skill> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getSkills();
    }

    @Override
    protected List<Skill> getChildren(Skill fieldAccessor) {
        return fieldAccessor.getChildren();
    }

    @Override
    protected String getFilename() {
        return filename;
    }

}
