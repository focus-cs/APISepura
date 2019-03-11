package fr.sciforma.apietnic.service;

import com.sciforma.psnext.api.JobClassification;
import com.sciforma.psnext.api.LockException;
import com.sciforma.psnext.api.Organization;
import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.PortfolioFolder;
import com.sciforma.psnext.api.Project;
import com.sciforma.psnext.api.Resource;
import com.sciforma.psnext.api.Session;
import com.sciforma.psnext.api.Skill;
import com.sciforma.psnext.api.SystemData;
import com.sciforma.psnext.api.TaskOutlineList;
import com.sciforma.psnext.api.Timesheet;
import com.sciforma.psnext.api.User;
import lombok.Getter;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author mehdi
 */
@Getter
@Configuration
@PropertySource("file:${user.dir}/config/application.properties")
public class SciformaService {

    @Value("${sciforma.url}")
    private String url;
    @Value("${sciforma.context}")
    private String context;
    @Value("${sciforma.username}")
    private String username;
    @Value("${sciforma.password}")
    private String password;

    private Session session;

    public boolean createConnection() {

        try {

            Logger.info("Connection to " + url + "/" + context + " with username " + username);
            session = new Session(url + "/" + context);
            session.login(username, password.toCharArray());
            Logger.info("Connection successful");

            return true;

        } catch (PSException e) {
            Logger.error(e, "Failed to connect to sciforma : " + e.getMessage());
        }

        return false;

    }

    public void closeConnection() {

        try {

            if (session.isLoggedIn()) {
                session.logout();
                Logger.info("Logout successful");
            }

        } catch (PSException e) {
            Logger.error(e, "Failed to logout");
        }

    }

    public List<Project> getProjects() {

        if (session.isLoggedIn()) {

            try {
                return session.getProjectList(Project.VERSION_WORKING, Project.READWRITE_ACCESS);
            } catch (PSException e) {
                Logger.error(e, "Failed to retrieve project list");
            }

        } else {
            Logger.error("You must be logged in to retrieve project list");
        }

        return new ArrayList<>();

    }

    public Optional<TaskOutlineList> getTasksForProject(Project project) {

        try {

            project.open(true);

            TaskOutlineList taskOutlineList = project.getTaskOutlineList();

            return Optional.of(taskOutlineList);

        } catch (LockException e) {
            Logger.error("Project is locked by " + e.getLockingUser());
        } catch (PSException e) {
            Logger.error(e);
        } finally {

            try {
                project.close();
            } catch (PSException e) {
                Logger.error("Failed to close project");
            }

        }

        return Optional.empty();

    }

    public List<Resource> getPublishedResources() {

        if (session.isLoggedIn()) {

            try {
                return session.getPublishedResourceList();
            } catch (PSException e) {
                Logger.error(e, "Failed to retrieve published resource list");
            }

        } else {
            Logger.error("You must be logged in to retrieve published resource list");
        }

        return new ArrayList<>();

    }

    public List<Resource> getWorkingResources() {

        if (session.isLoggedIn()) {

            try {
                return session.getWorkingResourceList();
            } catch (PSException e) {
                Logger.error(e, "Failed to retrieve working resource list");
            }

        } else {
            Logger.error("You must be logged in to retrieve working resource list");
        }

        return new ArrayList<>();

    }

    public Optional<Timesheet> getTimesheet(Resource resource, Date date) {

        if (session.isLoggedIn()) {

            try {
                return Optional.of(session.getTimesheet(resource, date));
            } catch (PSException e) {
                Logger.error(e, "Failed to retrieve timesheet for resource at date " + date);
            }

        } else {
            Logger.error("You must be logged in to retrieve timesheet");
        }

        return Optional.empty();
    }

    public Optional<Timesheet> getTimesheet(Resource resource, Date from, Date to) {

        if (session.isLoggedIn()) {

            try {
                return Optional.of(session.getTimesheet(resource, from, to));
            } catch (PSException e) {
                Logger.error(e, "Failed to retrieve timesheet for resource from " + from + " to " + to);
            }

        } else {
            Logger.error("You must be logged in to retrieve timesheet");
        }

        return Optional.empty();
    }

    public List<User> getUsers() {

        if (session.isLoggedIn()) {

            try {
                return session.getUserList();
            } catch (PSException e) {
                Logger.error(e, "Failed to retrieve user list");
            }

        } else {
            Logger.error("You must be logged in to retrieve user list");
        }

        return new ArrayList<>();

    }

    public Optional<JobClassification> getJobClassifications() {

        try {
            SystemData systemData = session.getSystemData(SystemData.JOB_CLASSIFICATIONS);

            if (systemData instanceof JobClassification) {
                return Optional.of((JobClassification) systemData);
            }

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve job classifications");
        }

        return Optional.empty();
    }

    public Optional<Organization> getOrganizations() {

        try {
            SystemData systemData = session.getSystemData(SystemData.ORGANIZATIONS);

            if (systemData instanceof Organization) {
                return Optional.of((Organization) systemData);
            }

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve organizations");
        }

        return Optional.empty();
    }

    public Optional<PortfolioFolder> getPortfolioFolders() {

        try {
            SystemData systemData = session.getSystemData(SystemData.PORTFOLIO_FOLDERS);

            if (systemData instanceof PortfolioFolder) {
                return Optional.of((PortfolioFolder) systemData);
            }

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve portfolio folders");
        }

        return Optional.empty();
    }

    public Optional<Skill> getSkills() {
        try {

            SystemData systemData = session.getSystemData(SystemData.SKILLS);

            if (systemData instanceof Skill) {
                return Optional.of((Skill) systemData);
            }

        } catch (PSException e) {
            Logger.error(e, "Failed to retrieve skills");
        }

        return Optional.empty();
    }
}
