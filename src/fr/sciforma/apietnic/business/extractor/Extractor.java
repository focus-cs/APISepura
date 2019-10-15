package fr.sciforma.apietnic.business.extractor;

import com.sciforma.psnext.api.FieldAccessor;

import java.util.Optional;

public interface Extractor<T> {
    Optional<String> extractAsString(FieldAccessor fieldAccessor, String fieldName);
    Optional<T> extract(FieldAccessor fieldAccessor, String fieldName);
}
