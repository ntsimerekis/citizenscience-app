package com.tsimerekis.geometry;

import com.vaadin.flow.component.map.configuration.Coordinate;
import org.locationtech.jts.geom.*;

public class GeometryHelper {
    private final static PrecisionModel pm = new PrecisionModel(PrecisionModel.FLOATING);

    public final static GeometryFactory gf = new GeometryFactory(pm, 4326);

    public static Point getJTSPoint(Coordinate coordinate) {
        return gf.createPoint(new org.locationtech.jts.geom.Coordinate(coordinate.getX(), coordinate.getY()));
    }

    public static Polygon rectangleFromPoints(Point topLeft, Point bottomRight) {
        double minX = Math.min(topLeft.getX(), bottomRight.getX());
        double maxX = Math.max(topLeft.getX(), bottomRight.getX());
        double minY = Math.min(topLeft.getY(), bottomRight.getY());
        double maxY = Math.max(topLeft.getY(), bottomRight.getY());

        org.locationtech.jts.geom.Coordinate[] coords = new org.locationtech.jts.geom.Coordinate[] {
                new org.locationtech.jts.geom.Coordinate(minX, maxY),
                new org.locationtech.jts.geom.Coordinate(maxX, maxY),
                new org.locationtech.jts.geom.Coordinate(maxX, minY),
                new org.locationtech.jts.geom.Coordinate(minX, minY),
                new org.locationtech.jts.geom.Coordinate(minX, maxY)
        };

        // Use the coordinate sequence factory explicitly
        CoordinateSequence seq = gf.getCoordinateSequenceFactory().create(coords);
        LinearRing shell = new LinearRing(seq, gf);
        return gf.createPolygon(shell, null);
    }
}
