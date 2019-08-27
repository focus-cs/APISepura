package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.JobClassification;
import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Project;
import com.sciforma.psnext.api.ResAssignment;
import com.sciforma.psnext.api.Task;
import fr.sciforma.apietnic.business.extractor.BooleanExtractor;
import fr.sciforma.apietnic.business.extractor.CalendarExtractor;
import fr.sciforma.apietnic.business.extractor.DateExtractor;
import fr.sciforma.apietnic.business.extractor.DecimalExtractor;
import fr.sciforma.apietnic.business.extractor.DoubleDatedExtractor;
import fr.sciforma.apietnic.business.extractor.EffortExtractor;
import fr.sciforma.apietnic.business.extractor.IntegerExtractor;
import fr.sciforma.apietnic.business.extractor.ListExtractor;
import fr.sciforma.apietnic.business.extractor.StringDatedExtractor;
import fr.sciforma.apietnic.business.extractor.StringExtractor;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
public class ResourceAssignementProcessor extends AbstractProcessor<ResAssignment> {

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
    @Autowired
    private EffortExtractor<ResAssignment> effortExtractor;
    @Autowired
    private DoubleDatedExtractor<ResAssignment> doubleDatedExtractor;
    @Autowired
    private StringDatedExtractor<ResAssignment> stringDatedExtractor;

    protected void process(Task task, Date start, Date end) {

        String taskName = null;

        try {

            taskName = task.getStringField("Name");

            Logger.info("Processing resource assignement for task " + taskName);

            List<ResAssignment> resAssignmentList = task.getResAssignmentList();

            LocalDate startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            for (ResAssignment resAssignment : resAssignmentList) {

                for (LocalDate localDate = startDate; localDate.isBefore(endDate); localDate = localDate.plusDays(1)) {

                    buildTimeDistributedCsvLine(resAssignment, localDate).ifPresent(csvHelper::addLine);

                }

            }

            Logger.info("Resource assignement for task " + taskName + " have been processed successfully");

        } catch (PSException e) {

            Logger.error(e, "Failed to retrieve resource assignement for task " + taskName);

        } finally {
            csvHelper.flush();
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

    @Override
    public EffortExtractor<ResAssignment> getEffortExtractor() {
        return effortExtractor;
    }

    @Override
    public DoubleDatedExtractor<ResAssignment> getDoubleDatedExtractor() {
        return doubleDatedExtractor;
    }

    @Override
    public StringDatedExtractor<ResAssignment> getStringDatedExtractor() {
        return stringDatedExtractor;
    }
}
