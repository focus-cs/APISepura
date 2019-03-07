package fr.sciforma.apietnic.business.extractor.user;

import com.sciforma.psnext.api.PSException;
import com.sciforma.psnext.api.User;
import fr.sciforma.apietnic.business.extractor.Extractor;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
public class UserDecimalExtractor implements Extractor<User> {

    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    @Override
    public String extract(User user, String fieldName) throws PSException {
        return decimalFormat.format(user.getDoubleField(fieldName));
    }

}
