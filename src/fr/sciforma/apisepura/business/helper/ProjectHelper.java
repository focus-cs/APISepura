package fr.sciforma.apisepura.business.helper;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Project;
import com.sciforma.psnext.api.Task;
import com.sciforma.psnext.api.TaskOutlineList;
import lombok.Builder;
import org.pmw.tinylog.Logger;

import java.util.List;
import java.util.Optional;

@Builder
public class ProjectHelper {

    private List<Project> projects;

    public Optional<Project> findProject(String projectId) {
        for (Project project : projects) {

            try {

                if (projectId.equalsIgnoreCase(project.getStringField("ID"))) {

                    return Optional.of(project);

                }

            } catch (PSException e) {

                Logger.error("Failed to retrieve project ID");

            }

        }

        return Optional.empty();
    }

    public Optional<Task> findTask(String taskName, Project project) {

        try {
            project.open(true);

            TaskOutlineList taskOutlineList = project.getTaskOutlineList();

            for (Object task : taskOutlineList) {

                if (task instanceof Task) {

                    String name = ((Task) task).getStringField("Name");
                    String id = ((Task) task).getStringField("ID");

                    if ((!taskName.isEmpty()) && (name.startsWith(taskName) || taskName.equalsIgnoreCase(name) || taskName.equalsIgnoreCase(id))) {
                        return Optional.of((Task) task);
                    }

                }

            }

            return Optional.empty();

        } catch (PSException e) {

            Logger.error("Failed to retrieve task " + taskName);

        } finally {

            try {
                project.close();
            } catch (PSException e) {
                Logger.error("Failed to close project " + e);
            }

        }

        return Optional.empty();

    }

}
