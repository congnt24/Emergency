package com.congnt.reversegeocodecountry;

import java.util.List;

/**
 * Created by congnt24 on 02/11/2016.
 */

public class GeocodeCountry {
    public String id;
    public String name;
    protected Geometry geometry;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public class Geometry {
        public String type;
        protected Object coordinates;

        protected List<Object> polygon;
        protected List<List<Object>> multiPolygon;
    }
}
