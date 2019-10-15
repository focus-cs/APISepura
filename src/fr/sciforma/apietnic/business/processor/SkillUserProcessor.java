package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Resource;
import com.sciforma.psnext.api.Skill;
import com.sciforma.psnext.api.User;
import fr.sciforma.apietnic.business.csv.SkillUserCsvHelper;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Component
public class SkillUserProcessor {

    @Value("${csv.delimiter}")
    protected String csvDelimiter;

    @Autowired
    SkillUserCsvHelper skillUserCsvHelper;

    public void process(Map<String, Skill> skillsByName, Map<Double, User> usersById, Map<Double, Resource> resourcesById) {

        Logger.info("Processing file " + skillUserCsvHelper.getFilename());

        try {

            for (Map.Entry<Double, User> userEntry : usersById.entrySet()) {

                if (resourcesById.containsKey(userEntry.getKey())) {

                    List<String> userSkills = resourcesById.get(userEntry.getKey()).getListField("Skills");

                    if (userSkills != null) {

                        for (String userSkill : userSkills) {

                            if (skillsByName.containsKey(userSkill)) {

                                StringJoiner csvLine = new StringJoiner(csvDelimiter);
                                csvLine.add(String.valueOf(Double.valueOf(userEntry.getValue().getDoubleField("Internal ID")).intValue()));
                                csvLine.add(userEntry.getValue().getStringField("Name"));
                                csvLine.add(String.valueOf(Double.valueOf(skillsByName.get(userSkill).getDoubleField("Internal ID")).intValue()));
                                csvLine.add(skillsByName.get(userSkill).toString());

                                skillUserCsvHelper.addLine(csvLine.toString());
                            }

                        }

                    }
                }


            }

        } catch (PSException e) {

            Logger.error(e);

        }

        skillUserCsvHelper.flush();
    }
}
