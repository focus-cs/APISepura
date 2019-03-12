package fr.sciforma.apietnic.business.extractor;

import com.sciforma.psnext.api.FieldAccessor;
import com.sciforma.psnext.api.PSException;
import org.pmw.tinylog.Logger;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Optional;

@Component
public class DecimalExtractor<T extends FieldAccessor> implements Extractor<T, Double> {

    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    @Override
    public Optional<String> extractAsString(T fieldAccessor, String fieldName) {

        try {

            return Optional.of(decimalFormat.format(fieldAccessor.getDoubleField(fieldName)));

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve decimal value from field " + fieldName);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Double> extract(T fieldAccessor, String fieldName) {

        try {

            return Optional.of(fieldAccessor.getDoubleField(fieldName));

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve decimal value from field " + fieldName);
        }

        return Optional.empty();

    }

}
