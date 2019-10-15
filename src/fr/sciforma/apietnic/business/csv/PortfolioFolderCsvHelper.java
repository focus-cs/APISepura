package fr.sciforma.apietnic.business.csv;

import fr.sciforma.apietnic.business.provider.PortfolioFieldProvider;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PortfolioFolderCsvHelper extends AbstractCsvHelper {

    @Value("${filename.portfolioFolders}")
    private String filename;

    @Autowired
    private PortfolioFieldProvider fieldProvider;

}
