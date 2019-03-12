package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.PortfolioFolder;
import fr.sciforma.apietnic.service.SciformaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PortfolioFolderProcessor extends AbstractSystemDataProcessor<PortfolioFolder> {

    @Value("${filename.portfolioFolders}")
    private String filename;

    @Override
    protected Optional<PortfolioFolder> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getPortfolioFolders();
    }

    @Override
    protected List<PortfolioFolder> getChildren(PortfolioFolder fieldAccessor) {
        return fieldAccessor.getChildren();
    }

    @Override
    protected String getFilename() {
        return filename;
    }
}
