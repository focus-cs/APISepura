package fr.sciforma.apietnic.business.extractor;

import com.sciforma.psnext.api.FieldAccessor;
import com.sciforma.psnext.api.PSException;
import org.pmw.tinylog.Logger;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Optional;

@Component
public class CalendarExtractor implements Extractor<Calendar> {

    @Override
    public Optional<String> extractAsString(FieldAccessor fieldAccessor, String fieldName) {

        try {

            Object value = fieldAccessor.getValue(fieldName);
            Logger.info(value.toString());

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve string value from field " + fieldName);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Calendar> extract(FieldAccessor fieldAccessor, String fieldName) {
        return Optional.empty();
    }


}
