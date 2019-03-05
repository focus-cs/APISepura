package fr.sciforma.apietnic.business;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SciformaField {
    private String name;
    private FieldType type;
}
