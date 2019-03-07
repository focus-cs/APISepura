package fr.sciforma.apietnic.business.extractor;

import com.sciforma.psnext.api.PSException;

public interface Extractor<P> {
    String extract(P psObject, String fieldName) throws PSException;
}
