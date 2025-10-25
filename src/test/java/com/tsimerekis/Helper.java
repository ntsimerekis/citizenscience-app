package com.tsimerekis;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class Helper {

    public static final GeometryFactory geometryFactory = new GeometryFactory();

    public static Point point(double lon, double lat) {
        return geometryFactory.createPoint(new Coordinate(lon, lat));
    }

    public static Point samplePoint() {return point(1.5,-1.5);}
}
