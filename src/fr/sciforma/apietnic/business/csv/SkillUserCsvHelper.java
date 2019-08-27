package fr.sciforma.apietnic.business.csv;

import fr.sciforma.apietnic.business.model.SkillUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SkillUserCsvHelper extends AbstractCsvHelper<SkillUser> {

    @Value("${filename.skillsUsers}")
    private String filename;

    @Override
    public String getFilename() {
        return filename;
    }

}
