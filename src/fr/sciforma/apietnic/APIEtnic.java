package fr.sciforma.apietnic;

import fr.sciforma.apietnic.business.processor.JobClassificationProcessor;
import fr.sciforma.apietnic.business.processor.OrganizationProcessor;
import fr.sciforma.apietnic.business.processor.PortfolioFolderProcessor;
import fr.sciforma.apietnic.business.processor.ProjectProcessor;
import fr.sciforma.apietnic.business.processor.SkillProcessor;
import fr.sciforma.apietnic.business.processor.UserProcessor;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@ComponentScan(basePackages = "fr.sciforma")
@Configuration
public class APIEtnic {

    @Autowired
    SciformaService sciformaService;
    @Autowired
    ProjectProcessor projectProcessor;
    @Autowired
    UserProcessor userProcessor;
    @Autowired
    JobClassificationProcessor jobClassificationProcessor;
    @Autowired
    OrganizationProcessor organizationProcessor;
    @Autowired
    PortfolioFolderProcessor portfolioFolderProcessor;
    @Autowired
    SkillProcessor skillProcessor;


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

//                projectProcessor.process(sciformaService);
//                userProcessor.process(sciformaService);
//                jobClassificationProcessor.process(sciformaService);
                organizationProcessor.process(sciformaService);
//                portfolioFolderProcessor.process(sciformaService);
//                skillProcessor.process(sciformaService);

            }

        } catch (Exception e) {

            Logger.error(e);

        } finally {

            sciformaService.closeConnection();

        }

    }

}
