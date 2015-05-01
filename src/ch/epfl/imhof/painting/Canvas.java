package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

public interface Canvas {
    
    public void drawPolyline(PolyLine polyline, LineStyle lineStyle);
    public void drawPolygon(Polygon polygon, Color color);

}
