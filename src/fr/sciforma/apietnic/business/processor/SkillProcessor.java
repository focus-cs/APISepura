package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Skill;
import fr.sciforma.apietnic.business.FieldType;
import fr.sciforma.apietnic.business.SciformaField;
import fr.sciforma.apietnic.business.extractor.Extractor;
import fr.sciforma.apietnic.business.factory.SkillExtractorFactory;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class SkillProcessor extends AbstractProcessor<Skill> {

    @Value("${filename.skills}")
    private String filename;

    @Override
    public void process(SciformaService sciformaService) {

        Logger.info("Processing file " + filename);

        fieldsToExtract = extractorFactory.getFields();

        Optional<Skill> skills = sciformaService.getSkills();

        csvLines = new ArrayList<>();

        skills.ifPresent(this::parseSkills);

        toCsv();

        Logger.info("File " + filename + " has been processed successfully");

    }

    @Override
    protected String getFilename() {
        return filename;
    }

    private void parseSkills(Skill root) {

        List<Skill> children = root.getChildren();

        if (children == null || children.isEmpty()) {

            StringJoiner csvLine = new StringJoiner(csvDelimiter);

            for (SciformaField sciformaField : fieldsToExtract) {

                extractorMap.get(sciformaField.getType()).extract(root, sciformaField.getName()).ifPresent(csvLine::add);

            }

            csvLines.add(csvLine.toString());

        } else {

            for (Skill child : children) {

                parseSkills(child);

            }
        }

    }
}
