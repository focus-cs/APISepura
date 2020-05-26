package fr.sciforma.apisepura.service;

import com.sciforma.psnext.api.DataViewRow;
import com.sciforma.psnext.api.FieldAccessor;
import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Project;
import com.sciforma.psnext.api.Resource;
import com.sciforma.psnext.api.Session;
import com.sciforma.psnext.api.Timesheet;
import lombok.Getter;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author mehdi
 */
@Getter
@Configuration
public class SciformaService {

    @Value("${sciforma.address}")
    private String url;
    @Value("${sciforma.username}")
    private String username;
    @Value("${sciforma.password}")
    private String password;

    private Session session;

    public boolean createConnection() {

        try {

            Logger.info("Login to Sciforma");
            Logger.info("URL        : [" + url + "]");
            Logger.info("Username   : [" + username + "]");

            session = new Session(url);
            session.login(username, password.toCharArray());
            Logger.info("Connection successful");

            return true;

        } catch (PSException e) {
            Logger.error(e, "Failed to connect to sciforma");

            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));

            Logger.error(errors);
        }

        return false;

    }

    public void closeConnection() {

        try {

            if (session != null && session.isLoggedIn()) {
                session.logout();
                Logger.info("Logout successful");
            }

        } catch (PSException e) {
            Logger.error(e, "Failed to logout");
        }

    }

    public List<Project> getProjects() {

        List<Project> projects = new ArrayList<>();

        if (session.isLoggedIn()) {

            try {
                projects = session.getProjectList(Project.VERSION_PUBLISHED, Project.READWRITE_ACCESS);
                projects.add(Project.getNonProject());
                return projects;
            } catch (PSException e) {
                Logger.error(e, "Failed to retrieve project list");
            }

        } else {
            Logger.error("You must be logged in to retrieve project list");
        }

        return projects;

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

    public List<DataViewRow> getDataViewRows(String name, FieldAccessor fieldAccessor) {

        if (session.isLoggedIn()) {
            try {
                return session.getDataViewRowList(name, fieldAccessor);
            } catch (PSException e) {
                Logger.error(e, "Failed to retrieve data view rows with name " + name);
            }
        } else {
            Logger.error("You must be logged in to retrieve data view rows");
        }

        return null;

    }

}
