package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Resource;
import com.sciforma.psnext.api.Skill;
import com.sciforma.psnext.api.User;
import fr.sciforma.apietnic.business.csv.SkillUserCsvHelper;
import fr.sciforma.apietnic.business.extractor.DecimalNoPrecisionExtractor;
import fr.sciforma.apietnic.business.extractor.ListExtractor;
import fr.sciforma.apietnic.business.extractor.StringExtractor;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

@Component
public class SkillUserProcessor {

    @Value("${csv.delimiter}")
    protected String csvDelimiter;

    @Autowired
    private StringExtractor stringExtractor;
    @Autowired
    private DecimalNoPrecisionExtractor decimalNoPrecisionExtractor;
    @Autowired
    private ListExtractor listExtractor;

    @Autowired
    SkillUserCsvHelper skillUserCsvHelper;

    public void process(Map<String, Skill> skillsByName, Map<String, User> usersById, Map<String, Resource> resourcesById) {

        Logger.info("Processing file " + skillUserCsvHelper.getFilename());

        for (Map.Entry<String, User> userEntry : usersById.entrySet()) {

            if (resourcesById.containsKey(userEntry.getKey())) {

                Optional<List> userSkills = listExtractor.extract(resourcesById.get(userEntry.getKey()), "Skills");

                if (userSkills.isPresent()) {

                    List<String> userSkillList = userSkills.get();

                    for (String userSkill : userSkillList) {

                        if (skillsByName.containsKey(userSkill)) {

                            StringJoiner csvLine = new StringJoiner(csvDelimiter);

                            csvLine.add(decimalNoPrecisionExtractor.extractAsString(userEntry.getValue(), "Internal ID").orElse(""));
                            csvLine.add(stringExtractor.extractAsString(userEntry.getValue(), "Name").orElse(""));
                            csvLine.add(decimalNoPrecisionExtractor.extractAsString(skillsByName.get(userSkill), "Internal ID").orElse(""));
                            csvLine.add(skillsByName.get(userSkill).toString());

                            skillUserCsvHelper.addLine(csvLine.toString());
                        }

                    }

                }
            }


        }

        skillUserCsvHelper.flush();
    }
}
