package fr.sciforma.apietnic.business.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class OrganizationBo implements BaseBo {

    private String description;
    private int internalId;
    private List<String> managers;
    private String name;

}
