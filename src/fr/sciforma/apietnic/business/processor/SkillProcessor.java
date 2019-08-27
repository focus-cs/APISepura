package fr.sciforma.apietnic.business.processor;

import com.sciforma.psnext.api.Resource;
import com.sciforma.psnext.api.Skill;
import fr.sciforma.apietnic.business.extractor.BooleanExtractor;
import fr.sciforma.apietnic.business.extractor.CalendarExtractor;
import fr.sciforma.apietnic.business.extractor.DateExtractor;
import fr.sciforma.apietnic.business.extractor.DecimalExtractor;
import fr.sciforma.apietnic.business.extractor.DoubleDatedExtractor;
import fr.sciforma.apietnic.business.extractor.EffortExtractor;
import fr.sciforma.apietnic.business.extractor.IntegerExtractor;
import fr.sciforma.apietnic.business.extractor.ListExtractor;
import fr.sciforma.apietnic.business.extractor.StringDatedExtractor;
import fr.sciforma.apietnic.business.extractor.StringExtractor;
import fr.sciforma.apietnic.service.SciformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SkillProcessor extends AbstractSystemDataProcessor<Skill> {

    @Autowired
    private StringExtractor<Skill> stringExtractor;
    @Autowired
    private DecimalExtractor<Skill> decimalExtractor;
    @Autowired
    private BooleanExtractor<Skill> booleanExtractor;
    @Autowired
    private DateExtractor<Skill> dateExtractor;
    @Autowired
    private IntegerExtractor<Skill> integerExtractor;
    @Autowired
    private ListExtractor<Skill> listExtractor;
    @Autowired
    private CalendarExtractor<Skill> calendarExtractor;
    @Autowired
    private EffortExtractor<Skill> effortExtractor;
    @Autowired
    private DoubleDatedExtractor<Skill> doubleDatedExtractor;
    @Autowired
    private StringDatedExtractor<Skill> stringDatedExtractor;

    @Override
    protected Optional<Skill> getFieldAccessors(SciformaService sciformaService) {
        return sciformaService.getSkills();
    }

    @Override
    protected List<Skill> getChildren(Skill fieldAccessor) {
        return fieldAccessor.getChildren();
    }

    @Override
    public StringExtractor<Skill> getStringExtractor() {
        return stringExtractor;
    }

    @Override
    public DecimalExtractor<Skill> getDecimalExtractor() {
        return decimalExtractor;
    }

    @Override
    public BooleanExtractor<Skill> getBooleanExtractor() {
        return booleanExtractor;
    }

    @Override
    public DateExtractor<Skill> getDateExtractor() {
        return dateExtractor;
    }

    @Override
    public IntegerExtractor<Skill> getIntegerExtractor() {
        return integerExtractor;
    }

    @Override
    public ListExtractor<Skill> getListExtractor() {
        return listExtractor;
    }

    @Override
    public CalendarExtractor<Skill> getCalendarExtractor() {
        return calendarExtractor;
    }

    @Override
    public EffortExtractor<Skill> getEffortExtractor() {
        return effortExtractor;
    }

    @Override
    public DoubleDatedExtractor<Skill> getDoubleDatedExtractor() {
        return doubleDatedExtractor;
    }

    @Override
    public StringDatedExtractor<Skill> getStringDatedExtractor() {
        return stringDatedExtractor;
    }

}
