package fr.sciforma.apietnic.business.extractor;

import com.sciforma.psnext.api.FieldAccessor;
import com.sciforma.psnext.api.PSException;
import org.pmw.tinylog.Logger;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StringExtractor<T extends FieldAccessor> implements Extractor<T> {

    @Override
    public Optional<String> extract(T fieldAccessor, String fieldName) {

        try {

            return Optional.of(fieldAccessor.getStringField(fieldName));

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve string value from field " + fieldName);
        }

        return Optional.empty();
    }

}
