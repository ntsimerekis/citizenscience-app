package com.tsimerekis.geometry;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryHelper {
    private final static PrecisionModel pm = new PrecisionModel(PrecisionModel.FLOATING);

    public final static GeometryFactory gf = new GeometryFactory(pm, 4326);
}
