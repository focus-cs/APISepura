package fr.sciforma.apietnic.business.extractor.user;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.Project;
import com.sciforma.psnext.api.User;
import fr.sciforma.apietnic.business.extractor.Extractor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class UserDateExtractor implements Extractor<User> {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public String extract(User user, String fieldName) throws PSException {
        return sdf.format(user.getDateField(fieldName));
    }

}
