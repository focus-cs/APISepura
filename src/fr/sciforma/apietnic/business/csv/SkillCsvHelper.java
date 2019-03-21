package fr.sciforma.apietnic.business.csv;

import com.sciforma.psnext.api.Skill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SkillCsvHelper extends AbstractCsvHelper<Skill> {

    @Value("${filename.skills}")
    private String filename;

    @Override
    protected String getFilename() {
        return filename;
    }

}
