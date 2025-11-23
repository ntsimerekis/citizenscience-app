package com.tsimerekis.science;

import com.tsimerekis.submission.repository.FilterCriteria;
import org.locationtech.jts.geom.Point;

import java.util.logging.Filter;

public class AveragingReportBuilder {

    private Point[] points = new Point[2];

    private FilterCriteria criteria = new FilterCriteria();

    private AveragingReportBuilder() {}

    public static AveragingReportBuilder create() {
        return new AveragingReportBuilder();
    }

    public void setFilterCriteria(FilterCriteria criteria) {
        this.criteria = criteria;
    }

    public void setPoint1(Point point) {
        points[0] = point;
    }

    public boolean point1Present() {
        return points[0] != null;
    }

    public void setPoint2(Point point) {
        points[1] = point;
    }

    public boolean point2Present() {
        return points[1] != null;
    }



}
