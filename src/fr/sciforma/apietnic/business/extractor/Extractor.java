package fr.sciforma.apietnic.business.extractor;

import com.sciforma.psnext.api.FieldAccessor;

import java.util.Optional;

public interface Extractor<P extends FieldAccessor> {
    Optional<String> extract(P fieldAccessor, String fieldName);
}
