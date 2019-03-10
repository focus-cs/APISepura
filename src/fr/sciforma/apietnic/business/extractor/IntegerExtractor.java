package fr.sciforma.apietnic.business.extractor;

import com.sciforma.psnext.api.FieldAccessor;
import com.sciforma.psnext.api.PSException;
import org.pmw.tinylog.Logger;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class IntegerExtractor<T extends FieldAccessor> implements Extractor<T> {

    @Override
    public Optional<String> extract(T fieldAccessor, String fieldName) {

        try {

            return Optional.of(String.valueOf(fieldAccessor.getIntField(fieldName)));

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve integer value from field " + fieldName);
        }

        return Optional.empty();

    }

}
