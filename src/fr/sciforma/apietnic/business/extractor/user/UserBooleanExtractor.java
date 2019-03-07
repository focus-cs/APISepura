package fr.sciforma.apietnic.business.extractor.user;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.User;
import fr.sciforma.apietnic.business.extractor.Extractor;
import org.springframework.stereotype.Component;

@Component
public class UserBooleanExtractor implements Extractor<User> {

    @Override
    public String extract(User user, String fieldName) throws PSException {
        return String.valueOf(user.getBooleanField(fieldName));
    }

}
