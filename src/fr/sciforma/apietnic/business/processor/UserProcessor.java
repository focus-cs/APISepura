package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.User;
import fr.sciforma.apietnic.business.FieldType;
import fr.sciforma.apietnic.business.SciformaField;
import fr.sciforma.apietnic.business.extractor.Extractor;
import fr.sciforma.apietnic.business.extractor.user.*;
import fr.sciforma.apietnic.business.factory.UserExtractorFactory;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Component
public class UserProcessor extends AbstractProcessor {

    @Autowired
    UserExtractorFactory extractorFactory;
    @Autowired
    UserStringExtractor stringExtractor;
    @Autowired
    UserDecimalExtractor decimalExtractor;
    @Autowired
    UserBooleanExtractor booleanExtractor;
    @Autowired
    UserDateExtractor dateExtractor;
    @Autowired
    UserIntegerExtractor integerExtractor;
    @Autowired
    UserListExtractor listExtractor;

    private Map<FieldType, Extractor<User>> extractorMap = new EnumMap<>(FieldType.class);

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

        List<SciformaField> userFieldsToExtract = extractorFactory.getFields();

        try {

            for (User user : sciformaService.getUsers()) {

                StringJoiner csvLine = new StringJoiner(csvDelimiter);

                for (SciformaField sciformaField : userFieldsToExtract) {

                    csvLine.add(extractorMap.get(sciformaField.getType()).extract(user, sciformaField.getName()));

                }

                Logger.info(csvLine.toString());
            }

        } catch (PSException e) {
            Logger.error(e);
        }

    }
}
