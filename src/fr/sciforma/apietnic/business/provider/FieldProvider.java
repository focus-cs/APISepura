package fr.sciforma.apietnic.business.provider;

import fr.sciforma.apietnic.business.model.SciformaField;

import java.util.List;

public interface FieldProvider<T> {
    List<SciformaField> getFields();
}
