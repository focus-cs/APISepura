package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Resource;
import com.sciforma.psnext.api.User;
import fr.sciforma.apietnic.business.csv.SkillUserCsvHelper;
import fr.sciforma.apietnic.business.csv.UserCsvHelper;
import fr.sciforma.apietnic.business.model.FieldType;
import fr.sciforma.apietnic.business.provider.UserFieldProvider;
import fr.sciforma.apietnic.service.SciformaService;
import lombok.Getter;
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
@Getter
public class UserProcessor extends AbstractFieldAccessorProcessor<User> {

    @Autowired
    private UserFieldProvider fieldProvider;
    @Autowired
    private UserCsvHelper csvHelper;

    @Value("${filename.resources}")
    protected String filename;

    @Autowired
    ResourceProcessor resourceProcessor;

    @Autowired
    SkillUserCsvHelper skillUSerCsvHelper;

    public Map<String, User> getUsersById(SciformaService sciformaService, Map<String, Resource> resourcesById) {

        Logger.info("Processing file " + csvHelper.getFilename());

        Map<String, User> usersById = new HashMap<>();

        StringJoiner csvLine;

        int cpt = 0;

        for (User fieldAccessor : getFieldAccessors(sciformaService)) {

            Optional<String> internalId = extractorMap.get(FieldType.DECIMAL_NO_PRECISION).extractAsString(fieldAccessor, "Internal ID");

            internalId.ifPresent(iid -> usersById.putIfAbsent(iid, fieldAccessor));

//            cpt++;
//            if (cpt > 5) {
//                break;
//            }

        }

        for (Map.Entry<String, User> entry : usersById.entrySet()) {

            csvLine = new StringJoiner(csvDelimiter);
            csvLine.add(buildCsvLine(entry.getValue()));

            if (resourcesById.containsKey(entry.getKey())) {
                csvLine.add(resourceProcessor.buildCsvLine(resourcesById.get(entry.getKey())));
            }

            csvHelper.addLine(csvLine.toString());

        }

        csvHelper.flush();

        Logger.info("File " + csvHelper.getFilename() + " has been processed successfully");

        return usersById;
    }

    @Override
    protected List<User> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getUsers();
    }

}
