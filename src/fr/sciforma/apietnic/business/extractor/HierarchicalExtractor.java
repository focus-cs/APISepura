package fr.sciforma.apietnic.business.extractor;

import com.sciforma.psnext.api.FieldAccessor;
import com.sciforma.psnext.api.PSException;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HierarchicalExtractor implements Extractor<String> {

    @Override
    public Optional<String> extractAsString(FieldAccessor fieldAccessor, String fieldName) {

        return Optional.of(fieldAccessor.toString());

    }

    @Override
    public Optional<String> extract(FieldAccessor fieldAccessor, String fieldName) {
        return extractAsString(fieldAccessor, fieldName);
    }

}
