package fr.sciforma.apietnic.business.csv;

import fr.sciforma.apietnic.business.model.SciformaField;
import fr.sciforma.apietnic.business.provider.FieldProvider;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public abstract class AbstractCsvHelper implements CsvHelper {

    final String START_HEADER = "**Start**";
    final String FINISH_HEADER = "**Finish**";

    private static final int BATCH_SIZE = 200;

    @Value("${csv.delimiter}")
    protected String delimiter;
    @Value("${csv.path}")
    protected String path;

    private List<String> lines;

    @PostConstruct
    protected void init() {

        try {
            Files.deleteIfExists(Paths.get(path + "/" + getFilename()));
        } catch (IOException e) {
            Logger.error("Failed to delete file " + getFilename());
        }

        lines = new ArrayList<>();
        lines.add(getHeaderAsString());
    }

    protected List<String> getHeader() {
        List<String> headerAsList = new ArrayList<>();

        for (SciformaField sciformaField : getFieldProvider().getFields()) {
            headerAsList.add(sciformaField.getName());
        }

        return headerAsList;
    }

    public void addLine(String line) {

        lines.add(line);

        if (lines.size() >= BATCH_SIZE) {
            writeToFile();
        }

    }

    @Override
    public void flush() {
        writeToFile();
    }

    @Override
    public List<String> getHeaderAsList() {
        return getHeader();
    }

    @Override
    public String getHeaderAsString() {
        StringJoiner headerAsString = new StringJoiner(delimiter);

        for (String header : getHeader()) {
            headerAsString.add(header);
        }

        return headerAsString.toString();
    }

    private void writeToFile() {

        String filePath = path + getFilename();

        try {

            Files.write(
                    Paths.get(filePath),
                    lines,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);

            lines = new ArrayList<>();

        } catch (IOException e) {
            Logger.error(e, "Failed to create file with path " + filePath);
        }
    }

    public abstract FieldProvider getFieldProvider();

}
