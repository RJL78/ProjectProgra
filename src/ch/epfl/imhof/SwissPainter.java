package ch.epfl.imhof;

import static ch.epfl.imhof.painting.Color.gray;
import static ch.epfl.imhof.painting.Color.rgb;
import static ch.epfl.imhof.painting.Filters.tagged;
import static ch.epfl.imhof.painting.Painter.line;
import static ch.epfl.imhof.painting.Painter.outline;
import static ch.epfl.imhof.painting.Painter.polygon;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.LineStyle.JOINT;
import ch.epfl.imhof.painting.LineStyle.LINE_END;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.painting.RoadPainterGenerator;
import ch.epfl.imhof.painting.RoadPainterGenerator.RoadSpec;
 /**
  * Classe définisant au travers de la constante PAINTER ( retournée par la méthode painter() ) le Painter qui définit le peintre à utiliser pour dessiner la carte
  * 
  * @author: l'equipe éducative de CS108
  */
public final class SwissPainter {
    private static final Painter PAINTER;

    static {
        Color black = Color.BLACK;
        Color darkGray = gray(0.2);
        Color darkGreen = rgb(0.75, 0.85, 0.7);
        Color darkRed = rgb(0.7, 0.15, 0.15);
        Color darkBlue = rgb(0.45, 0.7, 0.8);
        Color lightGreen = rgb(0.85, 0.9, 0.85);
        Color lightGray = gray(0.9);
        Color orange = rgb(1, 0.75, 0.2);
        Color lightYellow = rgb(1, 1, 0.5);
        Color lightRed = rgb(0.95, 0.7, 0.6);
        Color lightBlue = rgb(0.8, 0.9, 0.95);
        Color white = Color.WHITE;
        float [] array = {1f, 2f};

        Painter roadPainter = RoadPainterGenerator.painterForRoads(
                new RoadSpec(tagged("highway", "motorway", "trunk"), 2, orange, 0.5f, black),
                new RoadSpec(tagged("highway", "primary"), 1.7f, lightRed, 0.35f, black),
                new RoadSpec(tagged("highway", "motorway_link", "trunk_link"), 1.7f, orange, 0.35f, black),
                new RoadSpec(tagged("highway", "secondary"), 1.7f, lightYellow, 0.35f, black),
                new RoadSpec(tagged("highway", "primary_link"), 1.7f, lightRed, 0.35f, black),
                new RoadSpec(tagged("highway", "tertiary"), 1.7f, white, 0.35f, black),
                new RoadSpec(tagged("highway", "secondary_link"), 1.7f, lightYellow, 0.35f, black),
                new RoadSpec(tagged("highway", "residential", "living_street", "unclassified"), 1.2f, white, 0.15f, black),
                new RoadSpec(tagged("highway", "service", "pedestrian"), 0.5f, white, 0.15f, black));

        Painter fgPainter =
                roadPainter
                .above(line(darkGray, 0.5f, LINE_END.ROUND, JOINT.MITER, array).when(tagged("highway", "footway", "steps", "path", "track", "cycleway")))
                .above(polygon(darkGray).when(tagged("building")))
                .above(polygon(lightBlue).when(tagged("leisure", "swimming_pool")))
                .above(line(darkRed,0.7f).when(tagged("railway", "rail", "turntable")))
                .above(line(darkRed, 0.5f).when(tagged("railway", "subway", "narrow_gauge", "light_rail")))
                .above(polygon(lightGreen).when(tagged("leisure", "pitch")))
                .above(line(darkGray,1).when(tagged("man_made", "pier")))
                .layered();

        Painter bgPainter =
                outline(darkBlue,1).above(polygon(lightBlue)).when(tagged("natural", "water").or(tagged("waterway", "riverbank")))
                .above(line(lightBlue,1).above(line(darkBlue,1.5f)).when(tagged("waterway", "river", "canal")))
                .above(line(darkBlue,1).when(tagged("waterway", "stream")))
                .above(polygon(darkGreen).when(tagged("natural", "wood").or(tagged("landuse", "forest"))))
                .above(polygon(lightGreen).when(tagged("landuse", "grass", "recreation_ground", "meadow", "cemetery").or(tagged("leisure", "park"))))
                .above(polygon(lightGray).when(tagged("landuse", "residential", "industrial")))
                .layered();

        PAINTER = fgPainter.above(bgPainter);
    }

    public static Painter painter() {
        return PAINTER;
    }
}
