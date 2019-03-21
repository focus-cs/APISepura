package fr.sciforma.apietnic.business.csv;

import com.sciforma.psnext.api.PortfolioFolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PortfolioFolderCsvHelper extends AbstractCsvHelper<PortfolioFolder> {

    @Value("${filename.portfolioFolders}")
    private String filename;

    @Override
    protected String getFilename() {
        return filename;
    }

}
