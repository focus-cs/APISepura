package fr.sciforma.apietnic.business.extractor;

import com.sciforma.psnext.api.FieldAccessor;
import com.sciforma.psnext.api.PSException;
import org.pmw.tinylog.Logger;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Component
public class DateExtractor<T extends FieldAccessor> implements Extractor<T, Date> {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public Optional<String> extractAsString(T fieldAccessor, String fieldName) {

        try {

            Date dateField = fieldAccessor.getDateField(fieldName);
            if (dateField != null) {
                return Optional.of(sdf.format(dateField));
            }

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve date value from field " + fieldName);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Date> extract(T fieldAccessor, String fieldName) {

        try {

            return Optional.of(fieldAccessor.getDateField(fieldName));

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve date value from field " + fieldName);
        }

        return Optional.empty();
    }

}
