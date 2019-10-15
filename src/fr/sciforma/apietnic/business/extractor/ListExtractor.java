package fr.sciforma.apietnic.business.extractor;

import com.sciforma.psnext.api.FieldAccessor;
import com.sciforma.psnext.api.PSException;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Component
public class ListExtractor implements Extractor<List> {

    @Value("${multivalue.delimiter}")
    protected String delimiter;

    @Override
    public Optional<String> extractAsString(FieldAccessor fieldAccessor, String fieldName) {

        try {
            List listField = fieldAccessor.getListField(fieldName);

            StringJoiner joinedValue = new StringJoiner(delimiter);
            for (Object o : listField) {
                if (o instanceof String) {
                    joinedValue.add((String) o);
                }
            }

            return Optional.of(joinedValue.toString());

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve list value from field " + fieldName);
        }

        return Optional.empty();

    }

    @Override
    public Optional<List> extract(FieldAccessor fieldAccessor, String fieldName) {

        try {

            return Optional.of(fieldAccessor.getListField(fieldName));

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve list value from field " + fieldName);
        }

        return Optional.empty();
    }

}
