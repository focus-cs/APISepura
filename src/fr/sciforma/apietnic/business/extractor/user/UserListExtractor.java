package fr.sciforma.apietnic.business.extractor.user;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.User;
import fr.sciforma.apietnic.business.extractor.Extractor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

@Component
public class UserListExtractor implements Extractor<User> {

    @Override
    public String extract(User user, String fieldName) throws PSException {

        List listField = user.getListField(fieldName);

        StringJoiner joinedValue = new StringJoiner(",");
        for (Object o : listField) {
            if (o instanceof String) {
                joinedValue.add((String) o);
            }
        }

        return joinedValue.toString();
    }

}
