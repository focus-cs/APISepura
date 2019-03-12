package fr.sciforma.apietnic.business.factory;

import fr.sciforma.apietnic.business.model.SciformaField;

import java.util.List;

public interface ExtractorFactory<T> {
    List<SciformaField> getFields();
}
