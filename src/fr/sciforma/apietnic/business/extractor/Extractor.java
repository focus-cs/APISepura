package fr.sciforma.apietnic.business.extractor;

import com.sciforma.psnext.api.FieldAccessor;

import java.util.Optional;

public interface Extractor<P extends FieldAccessor, T> {
    Optional<String> extractAsString(P fieldAccessor, String fieldName);
    Optional<T> extract(P fieldAccessor, String fieldName);
}
