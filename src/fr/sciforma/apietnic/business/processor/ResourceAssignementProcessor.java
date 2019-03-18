package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.ResAssignment;
import com.sciforma.psnext.api.Task;
import fr.sciforma.apietnic.business.extractor.BooleanExtractor;
import fr.sciforma.apietnic.business.extractor.CalendarExtractor;
import fr.sciforma.apietnic.business.extractor.DateExtractor;
import fr.sciforma.apietnic.business.extractor.DecimalExtractor;
import fr.sciforma.apietnic.business.extractor.IntegerExtractor;
import fr.sciforma.apietnic.business.extractor.ListExtractor;
import fr.sciforma.apietnic.business.extractor.StringExtractor;
import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

@Component
public class ResourceAssignementProcessor extends AbstractProcessor<ResAssignment> {

    @Value("${filename.resAssignements}")
    private String filename;

    @Autowired
    private StringExtractor<ResAssignment> stringExtractor;
    @Autowired
    private DecimalExtractor<ResAssignment> decimalExtractor;
    @Autowired
    private BooleanExtractor<ResAssignment> booleanExtractor;
    @Autowired
    private DateExtractor<ResAssignment> dateExtractor;
    @Autowired
    private IntegerExtractor<ResAssignment> integerExtractor;
    @Autowired
    private ListExtractor<ResAssignment> listExtractor;
    @Autowired
    private CalendarExtractor<ResAssignment> calendarExtractor;

    @PostConstruct
    public void postConstruct() {
        super.postConstruct();
        csvLines = new ArrayList<>();
    }

    @Override
    protected String getFilename() {
        return filename;
    }

    @Override
    protected void process(SciformaService sciformaService) {
        throw new UnsupportedOperationException();
    }

    protected void process(Task task, Double s) {

        try {

            List resAssignmentList = task.getResAssignmentList();

            Iterator assignementIterator = resAssignmentList.iterator();
            while (assignementIterator.hasNext()) {
                ResAssignment resAssignment = (ResAssignment) assignementIterator.next();

                StringJoiner csvLine = new StringJoiner(csvDelimiter);

                for (SciformaField sciformaField : getFieldsToExtract()) {
                    extractorMap.get(sciformaField.getType()).extractAsString(resAssignment, sciformaField.getName()).ifPresent(csvLine::add);
                }

                csvLines.add(csvLine.toString());

            }

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve resource assignement for task");
        }

    }

    @Override
    public StringExtractor<ResAssignment> getStringExtractor() {
        return stringExtractor;
    }

    @Override
    public DecimalExtractor<ResAssignment> getDecimalExtractor() {
        return decimalExtractor;
    }

    @Override
    public BooleanExtractor<ResAssignment> getBooleanExtractor() {
        return booleanExtractor;
    }

    @Override
    public DateExtractor<ResAssignment> getDateExtractor() {
        return dateExtractor;
    }

    @Override
    public IntegerExtractor<ResAssignment> getIntegerExtractor() {
        return integerExtractor;
    }

    @Override
    public ListExtractor<ResAssignment> getListExtractor() {
        return listExtractor;
    }

    @Override
    public CalendarExtractor<ResAssignment> getCalendarExtractor() {
        return calendarExtractor;
    }
}
