package fr.sciforma.apisepura.business.helper;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Resource;
import lombok.Builder;
import org.pmw.tinylog.Logger;

import java.util.List;
import java.util.Optional;

@Builder
public class ResourceHelper {

    private List<Resource> resources;

    public Optional<Resource> findResource(String userName) {

        for (Resource resource : resources) {

            try {

                if (userName.equalsIgnoreCase(resource.getStringField("ID"))) {
                    return Optional.of(resource);
                }

            } catch (PSException e) {
                Logger.error("Failed to read resource");
            }
        }

        return Optional.empty();

    }

}
