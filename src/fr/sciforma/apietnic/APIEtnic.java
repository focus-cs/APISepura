package fr.sciforma.apietnic;

import com.sciforma.psnext.api.Project;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.List;

/**
 * Created on 22/02/19.
 */
@ComponentScan(basePackages = "fr.sciforma")
@Configuration
public class APIEtnic {

    @Autowired
    SciformaService sciformaService;

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(APIEtnic.class);
        APIEtnic api = context.getBean(APIEtnic .class);

        try {

            Logger.info("Start APIEtnic at " + new Date());
            api.start();

        } catch (Exception ex) {

            Logger.error("An error occured during execution", ex);

        }
    }

    private void start() throws Exception {

        if (sciformaService.createConnection()) {

            List<Project> projectList = sciformaService.getProjectList(Project.VERSION_WORKING, Project.READWRITE_ACCESS);
            for (Project project : projectList) {

            }

            sciformaService.closeConnection();

        }

    }

}
