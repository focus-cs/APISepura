package fr.sciforma.apisepura.business.model;

import com.sciforma.psnext.api.DoubleDatedData;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Builder(toBuilder = true)
@Getter
@ToString
public class AggregatedTimesheet {

    private String parentSdcr;
    private String projectCode;
    private LocalDateTime startDate;
    private String userName;
    private List<DoubleDatedData> datedDataList;

    public void addDatedData(Date start, Date end, double time) {

        if (datedDataList == null) {
            datedDataList = new LinkedList<>();
        }

        boolean datedDataAlreadyExists = false;
        for (DoubleDatedData datedData : datedDataList) {
            if (datedData.getStart().equals(start) && datedData.getFinish().equals(end)) {
                datedData.setData(datedData.getData() + time);
                datedDataAlreadyExists = true;
                break;
            }
        }

        if (!datedDataAlreadyExists) {
            datedDataList.add(new DoubleDatedData(time, start, end));
        }

    }

    public void addDatedDatas(List<DoubleDatedData> newDatedDatas) {
        if (datedDataList == null) {
            datedDataList = new LinkedList<>();
        }

        for (DoubleDatedData newDatedData : newDatedDatas) {
            boolean datedDataAlreadyExists = false;
            for (DoubleDatedData datedData : datedDataList) {
                if (datedData.getStart().equals(newDatedData.getStart()) && datedData.getFinish().equals(newDatedData.getFinish())) {
                    datedData.setData(datedData.getData() + newDatedData.getData());
                    datedDataAlreadyExists = true;
                    break;
                }
            }

            if (!datedDataAlreadyExists) {
                datedDataList.add(newDatedData);
            }
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggregatedTimesheet that = (AggregatedTimesheet) o;
        return parentSdcr.equals(that.parentSdcr) &&
                projectCode.equals(that.projectCode) &&
                startDate.equals(that.startDate) &&
                userName.equals(that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentSdcr, projectCode, startDate, userName);
    }
}
