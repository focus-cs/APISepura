package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.User;
import fr.sciforma.apietnic.service.SciformaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserProcessor extends AbstractFieldAccessorProcessor<User> {

    @Value("${filename.users}")
    protected String filename;

    @Override
    protected String getFilename() {
        return filename;
    }

    @Override
    protected List<User> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getUsers();
    }

}
