package fr.sciforma.apietnic.service;

import com.sciforma.psnext.api.JobClassification;
import com.sciforma.psnext.api.Organization;
import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.PortfolioFolder;
import com.sciforma.psnext.api.Project;
import com.sciforma.psnext.api.Resource;
import com.sciforma.psnext.api.Session;
import com.sciforma.psnext.api.Skill;
import com.sciforma.psnext.api.SystemData;
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

    @Value("${url}")
    private String url;
    @Value("${context}")
    private String context;
    @Value("${username}")
    private String username;
    @Value("${password}")
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
            Logger.error("Failed to connect to sciforma : " + e.getMessage(), e);
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
            Logger.error("Failed to logout", e);
        }

    }

    public List<Project> getProjects(int version, int access) {

        if (session.isLoggedIn()) {

            try {
                return session.getProjectList(version, access);
            } catch (PSException e) {
                Logger.error("Failed to retrieve project list", e);
            }

        } else {
            Logger.error("You must be logged in to retrieve project list");
        }

        return new ArrayList<>();

    }

    public List<Resource> getPublishedResourceInfos() {

        if (session.isLoggedIn()) {

            try {
                return session.getPublishedResourceList();
            } catch (PSException e) {
                Logger.error("Failed to retrieve published resource list", e);
            }

        } else {
            Logger.error("You must be logged in to retrieve published resource list");
        }

        return new ArrayList<>();

    }

    public Optional<Timesheet> getTimesheet(Resource resource, Date date) {

        if (session.isLoggedIn()) {

            try {
                session.getTimesheet(resource, date);
            } catch (PSException e) {
                Logger.error("Failed to retrieve timesheet for resource at date " + date, e);
            }

        } else {
            Logger.error("You must be logged in to retrieve timesheet");
        }

        return Optional.empty();
    }

    public Optional<Timesheet> getTimesheet(Resource resource, Date from, Date to) {

        if (session.isLoggedIn()) {

            try {
                session.getTimesheet(resource, from, to);
            } catch (PSException e) {
                Logger.error("Failed to retrieve timesheet for resource from " + from + " to " + to, e);
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
                Logger.error("Failed to retrieve user list", e);
            }

        } else {
            Logger.error("You must be logged in to retrieve user list");
        }

        return new ArrayList<>();

    }

    public Optional<JobClassification> getJobClassifications() throws PSException {
        SystemData systemData = session.getSystemData(SystemData.JOB_CLASSIFICATIONS);

        if (systemData instanceof JobClassification) {
            return Optional.of((JobClassification) systemData);
        }

        return Optional.empty();
    }

    public Optional<Organization> getOrganizations() throws PSException {
        SystemData systemData = session.getSystemData(SystemData.ORGANIZATIONS);

        if (systemData instanceof Organization) {
            return Optional.of((Organization) systemData);
        }

        return Optional.empty();
    }

    public Optional<PortfolioFolder> getPortfolioFolders() throws PSException {
        SystemData systemData = session.getSystemData(SystemData.PORTFOLIO_FOLDERS);

        if (systemData instanceof PortfolioFolder) {
            return Optional.of((PortfolioFolder) systemData);
        }

        return Optional.empty();
    }

    public Optional<Skill> getSkills() throws PSException {
        SystemData systemData = session.getSystemData(SystemData.SKILLS);

        if (systemData instanceof Skill) {
            return Optional.of((Skill) systemData);
        }

        return Optional.empty();
    }
}
