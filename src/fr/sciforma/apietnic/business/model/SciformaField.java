package fr.sciforma.apietnic.business.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SciformaField {
    private String name;
    private FieldType type;
}
