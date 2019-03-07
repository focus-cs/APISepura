package fr.sciforma.apietnic.business.processor;

import fr.sciforma.apietnic.service.SciformaService;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractProcessor {

    @Value("${csv.delimiter}")
    String csvDelimiter;

    public abstract void process(SciformaService sciformaService);
}
