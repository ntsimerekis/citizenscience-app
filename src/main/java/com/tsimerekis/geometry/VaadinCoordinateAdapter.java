package com.tsimerekis.geometry;

import com.vaadin.flow.component.map.configuration.Coordinate;
import org.locationtech.jts.geom.Point;

public class   VaadinCoordinateAdapter extends Coordinate {

    public VaadinCoordinateAdapter(Point jtsPoint) {
        super(jtsPoint.getX(), jtsPoint.getY());
    }
}
