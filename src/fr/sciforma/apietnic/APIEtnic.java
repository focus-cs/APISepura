package fr.sciforma.apietnic;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Project;
import com.sciforma.psnext.api.Resource;
import com.sciforma.psnext.api.Skill;
import com.sciforma.psnext.api.User;
import fr.sciforma.apietnic.business.processor.JobClassificationProcessor;
import fr.sciforma.apietnic.business.processor.OrganizationProcessor;
import fr.sciforma.apietnic.business.processor.PortfolioFolderProcessor;
import fr.sciforma.apietnic.business.processor.ProjectProcessor;
import fr.sciforma.apietnic.business.processor.ResourceProcessor;
import fr.sciforma.apietnic.business.processor.SkillProcessor;
import fr.sciforma.apietnic.business.processor.SkillUserProcessor;
import fr.sciforma.apietnic.business.processor.TaskProcessor;
import fr.sciforma.apietnic.business.processor.TimesheetProcessor;
import fr.sciforma.apietnic.business.processor.UserProcessor;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ComponentScan(basePackages = "fr.sciforma")
@Configuration
public class APIEtnic {

    @Autowired
    SciformaService sciformaService;

    @Autowired
    ResourceProcessor resourceProcessor;
    @Autowired
    TimesheetProcessor timesheetProcessor;
    @Autowired
    ProjectProcessor projectProcessor;
    @Autowired
    TaskProcessor taskProcessor;
    @Autowired
    JobClassificationProcessor jobClassificationProcessor;
    @Autowired
    OrganizationProcessor organizationProcessor;
    @Autowired
    SkillProcessor skillProcessor;
    @Autowired
    SkillUserProcessor skillUserProcessor;
    @Autowired
    UserProcessor userProcessor;
    @Autowired
    PortfolioFolderProcessor portfolioFolderProcessor;


    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(APIEtnic.class);
        APIEtnic api = context.getBean(APIEtnic.class);

        try {

            Logger.info("Start APIEtnic at " + new Date());
            api.start();

        } catch (Exception ex) {

            Logger.error(ex, "An error occured during execution");

        }

    }

    private void start() {

        try {

            if (sciformaService.createConnection()) {

                StopWatch extractStopWatch = new StopWatch("API extraction process");

                extractStopWatch.start("Resources");
                Map<String, Resource> resourcesById = resourceProcessor.getResourcesById(sciformaService);

                Map<String, User> usersById = userProcessor.getUsersById(sciformaService, resourcesById);
                extractStopWatch.stop();

                Map<String, String> usersByName = getUsersByName(usersById);

                extractStopWatch.start("Organizations");
                Map<String, String> organizationByName = organizationProcessor.getOrganizationByName(sciformaService);
                extractStopWatch.stop();

                extractStopWatch.start("Portfolios");
                Map<String, String> portfoliosByName = portfolioFolderProcessor.getPortfoliosByName(sciformaService, usersByName);
                extractStopWatch.stop();

                extractStopWatch.start("Skills");
                Map<String, Skill> skillsByName = skillProcessor.getSkillsByName(sciformaService);
                extractStopWatch.stop();

                extractStopWatch.start("Skills/Users");
                skillUserProcessor.process(skillsByName, usersById, resourcesById);
                extractStopWatch.stop();

                extractStopWatch.start("Job Classifications");
                jobClassificationProcessor.process(sciformaService);
                extractStopWatch.stop();

                extractStopWatch.start("Timesheets");
                for (Map.Entry<String, Resource> entry : resourcesById.entrySet()) {
                    timesheetProcessor.process(sciformaService, entry.getValue());
                }
                extractStopWatch.stop();

                extractStopWatch.start("Projects");
                Map<Double, Project> projectsById = projectProcessor.process(sciformaService, usersByName, portfoliosByName, organizationByName);
                extractStopWatch.stop();

                extractStopWatch.start("Tasks");
                for (Map.Entry<Double, Project> entry : projectsById.entrySet()) {
                    taskProcessor.process(entry.getValue(), usersByName);
                }
                extractStopWatch.stop();

                Logger.info(extractStopWatch.prettyPrint());

            }

        } catch (Exception e) {

            Logger.error(e);

        } finally {

            sciformaService.closeConnection();

        }

    }

    private Map<String, String> getUsersByName(Map<String, User> usersById) {
        Map<String, String> usersByName = new HashMap<>();

        try {

            for (Map.Entry<String, User> entry : usersById.entrySet()) {

                usersByName.put(entry.getValue().getStringField("Name"), entry.getKey());

            }

        } catch (PSException e) {

            Logger.error(e);

        }

        return usersByName;
    }

}
