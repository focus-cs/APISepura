package fr.sciforma.apietnic.business.extractor;

import com.sciforma.psnext.api.FieldAccessor;
import com.sciforma.psnext.api.PSException;
import org.pmw.tinylog.Logger;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BooleanExtractor<T extends FieldAccessor> implements Extractor<T, Boolean> {

    @Override
    public Optional<String> extractAsString(T fieldAccessor, String fieldName) {

        try {

            return Optional.of(String.valueOf(fieldAccessor.getBooleanField(fieldName)));

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve boolean value from field " + fieldName);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Boolean> extract(T fieldAccessor, String fieldName) {
        try {

            return Optional.of(fieldAccessor.getBooleanField(fieldName));

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve boolean value from field " + fieldName);
        }

        return Optional.empty();
    }

}
