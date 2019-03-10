package fr.sciforma.apietnic.business.extractor;

import com.sciforma.psnext.api.FieldAccessor;
import com.sciforma.psnext.api.PSException;
import org.pmw.tinylog.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Component
public class ListExtractor<T extends FieldAccessor> implements Extractor<T> {

    @Override
    public Optional<String> extract(T fieldAccessor, String fieldName) {

        try {
            List listField = fieldAccessor.getListField(fieldName);

            StringJoiner joinedValue = new StringJoiner(",");
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

}
