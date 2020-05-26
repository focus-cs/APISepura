package fr.sciforma.apisepura.business.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
@ToString
public class SepuraTimesheet {

    private String parentSdcr;
    private String projectCode;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String userName;
    private BigDecimal timeSpent;

}
