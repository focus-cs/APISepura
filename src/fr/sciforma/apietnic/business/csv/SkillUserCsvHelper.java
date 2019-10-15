package fr.sciforma.apietnic.business.csv;

import fr.sciforma.apietnic.business.provider.SkillUserFieldProvider;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SkillUserCsvHelper extends AbstractCsvHelper {

    @Value("${filename.skillsUsers}")
    private String filename;

    @Autowired
    private SkillUserFieldProvider fieldProvider;

}
