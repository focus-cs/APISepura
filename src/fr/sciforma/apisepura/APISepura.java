package fr.sciforma.apisepura;

import com.sciforma.psnext.api.DataViewRow;
import com.sciforma.psnext.api.DatedData;
import com.sciforma.psnext.api.DoubleDatedData;
import com.sciforma.psnext.api.Global;
import com.sciforma.psnext.api.LockException;
import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Project;
import com.sciforma.psnext.api.Resource;
import com.sciforma.psnext.api.Task;
import com.sciforma.psnext.api.Timesheet;
import com.sciforma.psnext.api.TimesheetAssignment;
import com.sciforma.psnext.mywork.SampleTimesheet;
import fr.sciforma.apisepura.business.csv.CsvHelper;
import fr.sciforma.apisepura.business.enums.LogLevel;
import fr.sciforma.apisepura.business.exception.TimesheetLockException;
import fr.sciforma.apisepura.business.helper.ProjectHelper;
import fr.sciforma.apisepura.business.helper.ResourceHelper;
import fr.sciforma.apisepura.business.model.AggregatedTimesheet;
import fr.sciforma.apisepura.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;

@ComponentScan(basePackages = "fr.sciforma")
@Configuration
@PropertySource("file:${user.dir}/config/application.properties")
public class APISepura {

    public static final String ACTUAL_EFFORT = "Actual Effort";
    @Autowired
    SciformaService sciformaService;
    @Autowired
    CsvHelper csvHelper;

    @Value("${project.name.default}")
    private String defaultProjectName;
    @Value("${task.name.default}")
    private String defaultTaskName;

    private Global global;

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(APISepura.class);
        APISepura api = context.getBean(APISepura.class);

        try {

            Logger.info("Start APISepura at " + new Date());
            api.start();

        } catch (Exception ex) {

            Logger.error(ex, "An error occured during execution");

        }

    }

    private void start() {

        StopWatch extractStopWatch = new StopWatch("API timesheet");

        extractStopWatch.start("Read CSV input files");
        SortedMap<String, SortedMap<LocalDateTime, List<AggregatedTimesheet>>> sepuraTimesheets = csvHelper.parseFiles();
        extractStopWatch.stop();

        if (!sepuraTimesheets.isEmpty()) {

            try {

                if (sciformaService.createConnection()) {

                    createAndLockGlobal();

                    extractStopWatch.start("Retrieve resources");
                    ResourceHelper resourceHelper = ResourceHelper.builder()
                            .resources(sciformaService.getWorkingResources())
                            .build();
                    extractStopWatch.stop();

                    extractStopWatch.start("Retrieve projects");
                    ProjectHelper projectHelper = ProjectHelper.builder()
                            .projects(sciformaService.getProjects())
                            .build();
                    extractStopWatch.stop();

                    extractStopWatch.start("Retrieve default project/task");
                    // Looking for default project
                    Optional<Project> defaultProject = projectHelper.findProject(defaultProjectName);
                    if (!defaultProject.isPresent()) {
                        log("ER1 - Default project [" + defaultProjectName + "] not found or not published", LogLevel.ERROR);
                        return;
                    }

                    // Looking for default task
                    Optional<Task> defaultTask = projectHelper.findTask(defaultTaskName, defaultProject.get());
                    if (!defaultTask.isPresent()) {
                        log("ER2 - Default task [" + defaultTaskName + "] not found for project [" + defaultProjectName + ']', LogLevel.ERROR);
                        return;
                    }
                    extractStopWatch.stop();

                    extractStopWatch.start("Processing timesheets");
                    for (Map.Entry<String, SortedMap<LocalDateTime, List<AggregatedTimesheet>>> userNameEntry : sepuraTimesheets.entrySet()) {

                        Optional<Resource> resource = resourceHelper.findResource(userNameEntry.getKey());
                        if (resource.isPresent()) {

                            String resourceName = resource.get().getStringField("Name");

                            for (Map.Entry<LocalDateTime, List<AggregatedTimesheet>> dateEntry : userNameEntry.getValue().entrySet()) {

                                Optional<Timesheet> sciformaTimesheet = findTimesheet(resource.get(), dateEntry.getKey());
                                if (sciformaTimesheet.isPresent()) {

                                    if (sciformaTimesheet.get().getStatus() != Timesheet.STATUS_SUBMITTED
                                            && sciformaTimesheet.get().getStatus() != Timesheet.STATUS_REVIEWED
                                            && sciformaTimesheet.get().getStatus() != Timesheet.STATUS_APPROVED
                                            && sciformaTimesheet.get().getStatus() != Timesheet.STATUS_LOCKED
//                                                && sciformaTimesheet.get().getStatus() != Timesheet.STATUS_REWORK
                                    ) {

                                        Logger.info("Clearing timesheet for user: [" + resource.get().getStringField("Name") + "] and date " + dateEntry.getKey());
                                        sciformaTimesheet = clearTimesheet(sciformaTimesheet.get(), resource.get(), dateEntry.getKey());

                                        AggregatedTimesheet defaultTimesheet = AggregatedTimesheet.builder()
                                                .parentSdcr(defaultProjectName)
                                                .projectCode(defaultProjectName)
                                                .startDate(dateEntry.getKey())
                                                .userName(resourceName)
                                                .datedDataList(new ArrayList<>())
                                                .build();

                                        for (AggregatedTimesheet aggregatedTimesheet : dateEntry.getValue()) {

                                            Optional<Project> project = projectHelper.findProject(aggregatedTimesheet.getProjectCode());
                                            if (project.isPresent()) {

                                                String projectName = project.get().getStringField("Name");

                                                Optional<Task> task = projectHelper.findTask(aggregatedTimesheet.getParentSdcr(), project.get());
                                                if (task.isPresent()) {

                                                    if (task.get().getBooleanField("Closed")) {
                                                        log("ER2 - Task [" + aggregatedTimesheet.getParentSdcr() + "] is closed in project [" + projectName + "] - using default task [" + defaultTaskName + "]", LogLevel.ERROR);
                                                        defaultTimesheet.addDatedDatas(aggregatedTimesheet.getDatedDataList());
                                                    } else {
                                                        processTimesheet(aggregatedTimesheet, sciformaTimesheet.get(), task.get(), resourceName, projectName);
                                                    }

                                                } else {
                                                    log("ER2 - Task [" + aggregatedTimesheet.getParentSdcr() + "] not found in project [" + projectName + "] - using default task [" + defaultTaskName + "]", LogLevel.ERROR);
                                                    defaultTimesheet.addDatedDatas(aggregatedTimesheet.getDatedDataList());
                                                }

                                            } else {
                                                log("ER1 - Project [" + aggregatedTimesheet.getProjectCode() + "] not found or not published - using default project [" + defaultProjectName + "]", LogLevel.ERROR);
                                                defaultTimesheet.addDatedDatas(aggregatedTimesheet.getDatedDataList());
                                            }

                                        }

                                        processDefaultedTimesheets(sciformaTimesheet.get(), defaultTask.get(), resourceName, defaultTimesheet);

                                        sciformaTimesheet.get().save();

                                    } else {
                                        log("ER4 - Timesheet is submitted, reviewed, approved or locked", LogLevel.ERROR);
                                    }

                                } else {
                                    log("ER4 - Timesheet not found", LogLevel.ERROR);
                                }
                            }

                        } else {
                            log("ER3 - Resource [" + userNameEntry.getKey() + "] not found", LogLevel.ERROR);
                        }
                    }

                    csvHelper.renameFiles();

                    extractStopWatch.stop();

                }

            } catch (PSException e) {

                Logger.error(e);

            } finally {

                saveAndUnlockGlobal();

                sciformaService.closeConnection();

                Logger.info(extractStopWatch.prettyPrint());

            }

        } else {

            Logger.info("No file to process");

        }

    }

    private void processDefaultedTimesheets(Timesheet sciformaTimesheet, Task defaultTask, String resourceName, AggregatedTimesheet defaultTimesheets) {

        if (!defaultTimesheets.getDatedDataList().isEmpty()) {
            log("Processing default timesheet", LogLevel.INFO);
            processTimesheet(defaultTimesheets, sciformaTimesheet, defaultTask, resourceName, defaultProjectName);
        }

    }

    private void saveAndUnlockGlobal() {
        try {

            if (global != null) {
                Logger.info("Saving global");
                global.save(false);
                Logger.info("Global saved");
            }

        } catch (PSException e) {
            Logger.error("Failed to save global");
        } finally {

            if (global != null) {
                Logger.info("Unlocking global");
                try {
                    global.unlock();
                } catch (LockException e) {
                    Logger.error("Global locked by " + e.getLockingUser());
                } catch (PSException e) {
                    Logger.error("Failed to unlock global");
                }
                Logger.info("Global unlocked");
            }
        }
    }

    private void createAndLockGlobal() {
        global = new Global();

        try {
            Logger.info("Locking global");
            global.lock();
            Logger.info("Global locked");
        } catch (LockException e) {
            Logger.error("Global locked by " + e.getLockingUser());
        } catch (PSException e) {
            Logger.error("Failed to lock global", e);
        }
    }

    private Optional<Timesheet> clearTimesheet(Timesheet sciformaTimesheet, Resource resource, LocalDateTime dateTime) {
        try {

            List<TimesheetAssignment> timesheetAssignmentList = sciformaTimesheet.getTimesheetAssignmentList();

            if (!timesheetAssignmentList.isEmpty()) {

                for (TimesheetAssignment timesheetAssignment : timesheetAssignmentList) {

                    List<DatedData> actualEffort = timesheetAssignment.getDatedData(ACTUAL_EFFORT);

                    for (DatedData datedData : actualEffort) {

                        try {
                            // trying to add a dummy assignment to check if the timesheet is locked or not. Couldn't rely on timesheet assignment status as it would be anything but locked
                            timesheetAssignment.updateDatedData(ACTUAL_EFFORT, Collections.singletonList(new DoubleDatedData(0, datedData.getStart(), datedData.getFinish())));
                            timesheetAssignment.clearDatedData(ACTUAL_EFFORT, datedData.getStart(), datedData.getFinish());
                        } catch (PSException e) {
                            if (e.getCause() instanceof SampleTimesheet.LockException) {
                                log("Timesheet is locked for date " + datedData.getStart(), LogLevel.INFO);
                            }
                        }

                    }

                }

                sciformaTimesheet.save();

                return findTimesheet(resource, dateTime);

            }

        } catch (PSException e) {
            Logger.error("Failed to clear timesheet");
            Logger.error(e);
        }

        return Optional.of(sciformaTimesheet);
    }

    private void processTimesheet(AggregatedTimesheet aggregatedTimesheet, Timesheet sciformaTimesheet, Task task, String resourceName, String projectName) {

        try {
            String taskName = task.getStringField("Name");

            for (DoubleDatedData doubleDatedData : aggregatedTimesheet.getDatedDataList()) {
                log("Applying timesheet - Date: " + doubleDatedData.getStart() + " - Time: " + doubleDatedData.getData() + " hours - User: " + resourceName + " - Task: " + taskName + " - Project: " + projectName, LogLevel.INFO);

                try {
                    TimesheetAssignment timesheetAssignment = sciformaTimesheet.addAssignment(task);
                    timesheetAssignment.updateDatedData(ACTUAL_EFFORT, aggregatedTimesheet.getDatedDataList());
                } catch (PSException e) {
                    if (e.getCause() instanceof SampleTimesheet.LockException) {
                        log("ER4 - Timesheet is locked for date " + doubleDatedData.getStart(), LogLevel.ERROR);
                    } else {
                        Logger.error(e);
                    }
                }
            }

        } catch (PSException e) {

            Logger.error(e);

        }

    }

    private Optional<Timesheet> findTimesheet(Resource resource, LocalDateTime startDate) {

        return sciformaService.getTimesheet(resource, toUtcDate(startDate));

    }

    private Optional<Timesheet> findTimesheet(Resource resource, LocalDateTime startDate, LocalDateTime endDate) {

        return sciformaService.getTimesheet(resource, toUtcDate(startDate), toUtcDate(endDate));

    }

    private void log(String message, LogLevel logLevel) {

        switch (logLevel) {
            case INFO:
                Logger.info(message);
                break;
            case DEBUG:
                Logger.debug(message);
                break;
            case WARNING:
                Logger.warn(message);
                break;
            case ERROR:
                Logger.error(message);
                break;
        }

        try {
            DataViewRow dataViewRow = new DataViewRow("APISepura_Logs", global);
            dataViewRow.setDateField("date", Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            dataViewRow.setStringField("message", message);
        } catch (PSException e) {
            Logger.error("Failed to create data view", e);
        }
    }

    private Date toUtcDate(LocalDateTime date) {
        return Date.from(date.atZone(ZoneId.of("UTC")).toInstant());
    }

}
