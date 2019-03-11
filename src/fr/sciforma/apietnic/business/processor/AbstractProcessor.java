package fr.sciforma.apietnic.business.processor;

import fr.sciforma.apietnic.business.extractor.*;
import fr.sciforma.apietnic.service.SciformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractProcessor {

    @Value("${csv.delimiter}")
    protected String csvDelimiter;

    @Autowired
    protected StringExtractor stringExtractor;
    @Autowired
    protected DecimalExtractor decimalExtractor;
    @Autowired
    protected BooleanExtractor booleanExtractor;
    @Autowired
    protected DateExtractor dateExtractor;
    @Autowired
    protected IntegerExtractor integerExtractor;
    @Autowired
    protected ListExtractor listExtractor;

    public abstract void process(SciformaService sciformaService);

//    protected abstract void toCsv();
}
