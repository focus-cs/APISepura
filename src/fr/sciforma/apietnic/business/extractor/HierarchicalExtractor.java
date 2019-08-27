package fr.sciforma.apietnic.business.extractor;

import com.sciforma.psnext.api.FieldAccessor;
import com.sciforma.psnext.api.PSException;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HierarchicalExtractor<T extends FieldAccessor> implements Extractor<T, String> {

    @Override
    public Optional<String> extractAsString(T fieldAccessor, String fieldName) {

        return Optional.of(fieldAccessor.toString());

    }

    @Override
    public Optional<String> extract(T fieldAccessor, String fieldName) {
        return extractAsString(fieldAccessor, fieldName);
    }

}
