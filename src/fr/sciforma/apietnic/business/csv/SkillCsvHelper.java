package fr.sciforma.apietnic.business.csv;

import fr.sciforma.apietnic.business.provider.SkillFieldProvider;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SkillCsvHelper extends AbstractCsvHelper {

    @Value("${filename.skills}")
    private String filename;

    @Autowired
    private SkillFieldProvider fieldProvider;

}
