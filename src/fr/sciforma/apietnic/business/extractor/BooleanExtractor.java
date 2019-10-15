package fr.sciforma.apietnic.business.extractor;

import com.sciforma.psnext.api.FieldAccessor;
import com.sciforma.psnext.api.PSException;
import org.pmw.tinylog.Logger;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BooleanExtractor implements Extractor<Boolean> {

    @Override
    public Optional<String> extractAsString(FieldAccessor fieldAccessor, String fieldName) {

        try {

            return Optional.of(String.valueOf(fieldAccessor.getBooleanField(fieldName)));

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve boolean value from field " + fieldName);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Boolean> extract(FieldAccessor fieldAccessor, String fieldName) {
        try {

            return Optional.of(fieldAccessor.getBooleanField(fieldName));

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve boolean value from field " + fieldName);
        }

        return Optional.empty();
    }

}
