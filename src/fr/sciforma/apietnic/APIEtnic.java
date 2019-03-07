package fr.sciforma.apietnic;

import com.sciforma.psnext.api.LockException;
import com.sciforma.psnext.api.Project;
import fr.sciforma.apietnic.business.ExtractorFactory;
import fr.sciforma.apietnic.business.SciformaField;
import fr.sciforma.apietnic.service.SciformaService;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created on 22/02/19.
 */
@ComponentScan(basePackages = "fr.sciforma")
@Configuration
public class APIEtnic {

    @Autowired
    SciformaService sciformaService;
    @Autowired
    ExtractorFactory extractorFactory;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");

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

        try {

            if (sciformaService.createConnection()) {

                List<Project> projectList = sciformaService.getProjects(Project.VERSION_WORKING, Project.READWRITE_ACCESS);

                List<SciformaField> projectFieldsToExtract = extractorFactory.getProjectFields();

                for (Project project : projectList) {

                    try {

                        project.open(false);

                        StringJoiner csvLine = new StringJoiner("|");

                        for (SciformaField sciformaField : projectFieldsToExtract) {

                            switch (sciformaField.getType()) {
                                case DECIMAL:
                                    csvLine.add(decimalFormat.format(project.getDoubleField(sciformaField.getName())));
                                    break;
                                case BOOLEAN:
                                    csvLine.add(String.valueOf(project.getBooleanField(sciformaField.getName())));
                                    break;
                                case COST:
                                    csvLine.add(decimalFormat.format(project.getDoubleField(sciformaField.getName())));
                                    break;
                                case EFFORT:
                                    csvLine.add(decimalFormat.format(project.getDoubleField(sciformaField.getName())));
                                    break;
                                case DATE:
                                    csvLine.add(sdf.format(project.getDateField(sciformaField.getName())));
                                    break;
                                case FORMULA:
                                    csvLine.add(project.getStringField(sciformaField.getName()));
                                    break;
                                case DURATION:
                                    csvLine.add(decimalFormat.format(project.getDoubleField(sciformaField.getName())));
                                    break;
                                case INTEGER:
                                    csvLine.add(String.valueOf(project.getIntField(sciformaField.getName())));
                                    break;
                                case STRING:
                                    csvLine.add(project.getStringField(sciformaField.getName()));
                                    break;
                                case USER:
                                    csvLine.add(project.getStringField(sciformaField.getName()));
                                    break;
                                case RESOURCE:
                                    csvLine.add(project.getStringField(sciformaField.getName()));
                                    break;
                            }

                        }

                        System.out.println(csvLine.toString());

                        break;

                    } catch (LockException e) {
                        System.out.println("Project is locked by " + e.getLockingUser());
                    } finally {
                        project.close();
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            sciformaService.closeConnection();

        }

    }

}
