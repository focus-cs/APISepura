package fr.sciforma.apietnic.business.extractor.user;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Project;
import com.sciforma.psnext.api.User;
import fr.sciforma.apietnic.business.extractor.Extractor;
import org.springframework.stereotype.Component;

@Component
public class UserStringExtractor implements Extractor<User> {

    @Override
    public String extract(User user, String fieldName) throws PSException {
        return user.getStringField(fieldName);
    }

}
