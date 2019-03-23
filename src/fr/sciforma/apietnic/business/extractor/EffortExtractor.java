package fr.sciforma.apietnic.business.extractor;

import com.sciforma.psnext.api.DatedData;
import com.sciforma.psnext.api.DoubleDatedData;
import com.sciforma.psnext.api.FieldAccessor;
import com.sciforma.psnext.api.PSException;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Component
public class EffortExtractor<T extends FieldAccessor> implements Extractor<T, List<DoubleDatedData>> {

    @Value("${multivalue.delimiter}")
    protected String delimiter;

    @Override
    public Optional<String> extractAsString(T fieldAccessor, String fieldName) {

        Optional<List<DoubleDatedData>> extract = extract(fieldAccessor, fieldName, DatedData.NONE);

        if (extract.isPresent()) {
            StringJoiner stringJoiner = new StringJoiner(delimiter);
            Iterator<DoubleDatedData> iterator = extract.get().iterator();
            while (iterator.hasNext()) {
                DoubleDatedData next = iterator.next();
                stringJoiner.add(String.valueOf(next.getData()));
            }

            return Optional.of(stringJoiner.toString());

        }

        return Optional.empty();
    }

    @Override
    public Optional<List<DoubleDatedData>> extract(T fieldAccessor, String fieldName) {

        return extract(fieldAccessor, fieldName, DatedData.NONE);

    }

    private Optional<List<DoubleDatedData>> extract(T fieldAccessor, String fieldName, int granularity) {

        try {
            return Optional.of(fieldAccessor.getDatedData(fieldName, granularity));
        } catch (PSException e) {
            Logger.error("Failed to retrieve dated data from field " + fieldName + " with granularity " + granularity);
        }

        return Optional.empty();
    }

}
