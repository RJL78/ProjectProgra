package ch.epfl.imhof.painting;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.imageio.ImageIO;

import org.xml.sax.SAXException;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.SwissPainter;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.projection.CH1903Projection;

public class Java2DCanvas implements Canvas {
    private final int height;
    private final int width;
    private final int resolution;
    private java.awt.Color colorBack;
    private Function<Point,Point> coordinateChange;
    private BufferedImage image;
    private Graphics2D ctx;
    
    public Java2DCanvas(Point pointLowerLeft, Point pointUpperRight, int height, int width, int resolution, Color colorBack) {   
        this.height=height;
        this.width=width;
        this.colorBack=colorBack.toAPIColor();
        this.resolution=resolution;
        
        coordinateChange= Point.alignedCoordinateChange(pointLowerLeft, new Point(0,height*72/resolution), pointUpperRight, new Point(width*72/resolution,0));
        image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        
        ctx = image.createGraphics();
        ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        ctx.setColor(this.colorBack);
        ctx.fillRect(0, 0, this.width, this.height);
        ctx.scale(this.resolution/72.0, this.resolution/72.0);
    }
    
    public BufferedImage image() {
        return image;
    }
    
   
    
    @Override
    public void drawPolyline(PolyLine polyline, LineStyle lineStyle) {
        
        Stroke s = new BasicStroke(lineStyle.getThickness(),lineStyle.getLineEnd().ordinal(),lineStyle.getJoint().ordinal(),(float)10.0,(lineStyle.lineSequence().length==0)? null: lineStyle.lineSequence(),0);
        Path2D path = makePath(polyline);
        if (polyline.isClosed()) path.closePath();
        ctx.setStroke(s);
        ctx.setColor(lineStyle.getColor().toAPIColor());
        ctx.draw(path);
    }
    

    
    private Path2D makePath(PolyLine polyline) {
        Path2D path = new Path2D.Double();
        Point firstPoint = coordinateChange.apply(polyline.firstPoint());
        path.moveTo(firstPoint.x(),firstPoint.y());
        for (Point point : polyline.points()) {
            point = coordinateChange.apply(point);
            path.lineTo(point.x(), point.y());
        }
        return path;
    }

    @Override
    public void drawPolygon(Polygon polygon, Color color) { 
        Path2D shell = makePath(polygon.shell());
        shell.closePath();
        Area shellArea = new Area(shell);
        for (PolyLine hole : polygon.holes()) {
            Path2D holePath = makePath(hole);
            holePath.closePath();
            shellArea.subtract(new Area(holePath));
        }
        // a confirmer comportement par defaut pour le contour des polygones? 
        ctx.setStroke(new BasicStroke (0,BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, (float)10.0 , null ,0));
        ctx.setColor(color.toAPIColor());
        ctx.draw(shellArea);
        ctx.fill(shellArea); 
    }
    
    
    public static void main(String[] args) throws IOException, SAXException {


            OSMToGeoTransformer transformer = new OSMToGeoTransformer(new CH1903Projection());
            Map map = transformer.transform(OSMMapReader.readOSMFile("/Users/Romain/Documents/Eclipse-workspace/github/ProjectProgra/src/ch/epfl/imhof/osm/lausanne.osm.gz", true));

            // La toile
            Point bl = new Point(532510, 150590);
            Point tr = new Point(539570, 155260);
            Java2DCanvas canvas =
                new Java2DCanvas(bl, tr, 1060, 1600, 150, Color.WHITE);

            // Dessin de la carte et stockage dans un fichier
            SwissPainter.painter().drawMap(canvas, map);
            ImageIO.write(canvas.image(), "png", new File("lozFINAL.png"));
    }
}
