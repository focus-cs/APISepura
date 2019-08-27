package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Resource;
import com.sciforma.psnext.api.Skill;
import com.sciforma.psnext.api.User;
import fr.sciforma.apietnic.business.csv.SkillUserCsvHelper;
import fr.sciforma.apietnic.business.extractor.BooleanExtractor;
import fr.sciforma.apietnic.business.extractor.CalendarExtractor;
import fr.sciforma.apietnic.business.extractor.DateExtractor;
import fr.sciforma.apietnic.business.extractor.DecimalExtractor;
import fr.sciforma.apietnic.business.extractor.DoubleDatedExtractor;
import fr.sciforma.apietnic.business.extractor.EffortExtractor;
import fr.sciforma.apietnic.business.extractor.IntegerExtractor;
import fr.sciforma.apietnic.business.extractor.ListExtractor;
import fr.sciforma.apietnic.business.extractor.StringDatedExtractor;
import fr.sciforma.apietnic.business.extractor.StringExtractor;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

@Component
public class UserProcessor extends AbstractFieldAccessorProcessor<User> {

    @Value("${filename.resources}")
    protected String filename;

    @Autowired
    private StringExtractor<User> stringExtractor;
    @Autowired
    private DecimalExtractor<User> decimalExtractor;
    @Autowired
    private BooleanExtractor<User> booleanExtractor;
    @Autowired
    private DateExtractor<User> dateExtractor;
    @Autowired
    private IntegerExtractor<User> integerExtractor;
    @Autowired
    private ListExtractor<User> listExtractor;
    @Autowired
    private CalendarExtractor<User> calendarExtractor;
    @Autowired
    private EffortExtractor<User> effortExtractor;
    @Autowired
    private DoubleDatedExtractor<User> doubleDatedExtractor;
    @Autowired
    private StringDatedExtractor<User> stringDatedExtractor;

    @Autowired
    ResourceProcessor resourceProcessor;
    @Autowired
    SkillProcessor skillProcessor;

    @Autowired
    SkillUserCsvHelper skillUSerCsvHelper;

    @Override
    public void process(SciformaService sciformaService) {

        Logger.info("Processing file " + csvHelper.getFilename());

        Map<Double, User> userById = new HashMap<>();

        StringJoiner csvLine;

        int cpt = 0;

        for (User fieldAccessor : getFieldAccessors(sciformaService)) {

            Optional<Double> internalId = (Optional<Double>) extractorMap.get(FieldType.DECIMAL).extract(fieldAccessor, "Internal ID");

            internalId.ifPresent(aDouble -> userById.putIfAbsent(aDouble, fieldAccessor));

            cpt++;
            if (cpt > 5) {
                break;
            }

        }

        Map<Double, Resource> resourcesById = resourceProcessor.getResourcesById(sciformaService);

        for (Map.Entry<Double, User> entry : userById.entrySet()) {

            csvLine = new StringJoiner(csvDelimiter);
            csvLine.add(buildCsvLine(entry.getValue()));

            if (resourcesById.containsKey(entry.getKey())) {
                csvLine.add(resourceProcessor.buildCsvLine(resourcesById.get(entry.getKey())));
            }

            csvHelper.addLine(csvLine.toString());

        }

        csvHelper.flush();

        Logger.info("File " + csvHelper.getFilename() + " has been processed successfully");

        Logger.info("Processing file " + skillUSerCsvHelper.getFilename());

        try {

            Map<String, Skill> skillsByName = skillProcessor.getSkillsByName(sciformaService);

            for (Map.Entry<Double, User> userEntry : userById.entrySet()) {

                if (resourcesById.containsKey(userEntry.getKey())) {

                    List<String> userSkills = resourcesById.get(userEntry.getKey()).getListField("Skills");

                    if (userSkills != null) {

                        for (String userSkill : userSkills) {

                            if (skillsByName.containsKey(userSkill)) {

                                csvLine = new StringJoiner(csvDelimiter);
                                csvLine.add(String.valueOf(userEntry.getValue().getDoubleField("Internal ID")));
                                csvLine.add(userEntry.getValue().getStringField("Name"));
                                csvLine.add(String.valueOf(skillsByName.get(userSkill).getDoubleField("Internal ID")));
                                csvLine.add(skillsByName.get(userSkill).getStringField("Name"));

                                skillUSerCsvHelper.addLine(csvLine.toString());
                            }

                        }

                    }
                }


            }

            skillUSerCsvHelper.flush();

        } catch (PSException e) {

            Logger.error(e);

        }
    }

    @Override
    protected List<User> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getUsers();
    }

    @Override
    public StringExtractor<User> getStringExtractor() {
        return stringExtractor;
    }

    @Override
    public DecimalExtractor<User> getDecimalExtractor() {
        return decimalExtractor;
    }

    @Override
    public BooleanExtractor<User> getBooleanExtractor() {
        return booleanExtractor;
    }

    @Override
    public DateExtractor<User> getDateExtractor() {
        return dateExtractor;
    }

    @Override
    public IntegerExtractor<User> getIntegerExtractor() {
        return integerExtractor;
    }

    @Override
    public ListExtractor<User> getListExtractor() {
        return listExtractor;
    }

    @Override
    public CalendarExtractor<User> getCalendarExtractor() {
        return calendarExtractor;
    }

    @Override
    public EffortExtractor<User> getEffortExtractor() {
        return effortExtractor;
    }

    @Override
    public DoubleDatedExtractor<User> getDoubleDatedExtractor() {
        return doubleDatedExtractor;
    }

    @Override
    public StringDatedExtractor<User> getStringDatedExtractor() {
        return stringDatedExtractor;
    }

}
