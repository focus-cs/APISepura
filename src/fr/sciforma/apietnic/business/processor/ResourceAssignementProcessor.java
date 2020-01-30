package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.ResAssignment;
import com.sciforma.psnext.api.Task;
import fr.sciforma.apietnic.business.csv.ResourceAssignementCsvHelper;
import fr.sciforma.apietnic.business.provider.ResourceAssignementFieldProvider;
import lombok.Getter;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
@Getter
public class ResourceAssignementProcessor extends AbstractProcessor<ResAssignment> {

    @Autowired
    private ResourceAssignementFieldProvider fieldProvider;
    @Autowired
    private ResourceAssignementCsvHelper csvHelper;

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

                    buildTimeDistributedCsvLine(resAssignment, localDate, false).ifPresent(csvHelper::addLine);

                }

            }

            Logger.info("Resource assignement for task " + taskName + " have been processed successfully");

        } catch (PSException e) {

            Logger.error(e, "Failed to retrieve resource assignement for task " + taskName);

        } finally {
            csvHelper.flush();
        }

    }

}
