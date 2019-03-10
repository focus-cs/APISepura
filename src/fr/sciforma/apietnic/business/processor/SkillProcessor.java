package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Skill;
import fr.sciforma.apietnic.business.FieldType;
import fr.sciforma.apietnic.business.SciformaField;
import fr.sciforma.apietnic.business.extractor.Extractor;
import fr.sciforma.apietnic.business.factory.PortfolioExtractorFactory;
import fr.sciforma.apietnic.business.factory.SkillExtractorFactory;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class SkillProcessor extends AbstractProcessor {

    @Autowired
    SkillExtractorFactory extractorFactory;

    private Map<FieldType, Extractor<Skill>> extractorMap = new EnumMap<>(FieldType.class);
    private List<SciformaField> userFieldsToExtract;

    @PostConstruct
    public void postConstruct() {
        extractorMap.putIfAbsent(FieldType.STRING, stringExtractor);
        extractorMap.putIfAbsent(FieldType.DECIMAL, decimalExtractor);
        extractorMap.putIfAbsent(FieldType.BOOLEAN, booleanExtractor);
        extractorMap.putIfAbsent(FieldType.COST, decimalExtractor);
        extractorMap.putIfAbsent(FieldType.EFFORT, decimalExtractor);
        extractorMap.putIfAbsent(FieldType.DATE, dateExtractor);
        extractorMap.putIfAbsent(FieldType.FORMULA, stringExtractor);
        extractorMap.putIfAbsent(FieldType.DURATION, decimalExtractor);
        extractorMap.putIfAbsent(FieldType.INTEGER, integerExtractor);
        extractorMap.putIfAbsent(FieldType.USER, stringExtractor);
        extractorMap.putIfAbsent(FieldType.RESOURCE, stringExtractor);
        extractorMap.putIfAbsent(FieldType.URL, stringExtractor);
        extractorMap.putIfAbsent(FieldType.CALENDAR, stringExtractor);
        extractorMap.putIfAbsent(FieldType.EFFORT_RATE, stringExtractor);
        extractorMap.putIfAbsent(FieldType.LIST, listExtractor);
    }

    @Override
    public void process(SciformaService sciformaService) {

        userFieldsToExtract = extractorFactory.getFields();

        Optional<Skill> skills = sciformaService.getSkills();

        skills.ifPresent(this::parseSkills);

    }

    private void parseSkills(Skill root) {

        List<Skill> children = root.getChildren();

        if (children == null || children.isEmpty()) {

            StringJoiner csvLine = new StringJoiner(csvDelimiter);

            for (SciformaField sciformaField : userFieldsToExtract) {

                extractorMap.get(sciformaField.getType()).extract(root, sciformaField.getName()).ifPresent(csvLine::add);

            }

            Logger.info(csvLine.toString());

        } else {

            for (Skill child : children) {

                parseSkills(child);

            }
        }

    }
}
