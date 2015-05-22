package ch.epfl.imhof.painting;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Function;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/** 
 * Classe immuable d'une toile concrète, élaborée à partir de Java2D dans la bibliothèque Java
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 */
public class Java2DCanvas implements Canvas {
    
    private final Function<Point,Point> coordinateChange;
    private final BufferedImage image;
    private final Graphics2D ctx;
    
    /**
     * Constructeur de Java2DCanvas
     * 
     * @param pointLowerLeft : le point en coordonnées du plan correspondant au coin en bas à gauche de la toile
     * @param pointUpperRight : le point en coordonnées du plan correspondant au coin en haut à droite de la toile
     * @param height : hauteur de l'image de la toile, en pixels
     * @param width : largeur de l'image de la toile, en pixels
     * @param resolution : resolution de l'image de la toile, en points par pouce
     * @param colorBack : couleur de fond de la toile
     */
    public Java2DCanvas(Point pointLowerLeft, Point pointUpperRight, int height, int width, int resolution, Color colorBack) {   
       
        double scale = resolution/72d;
        Point pointLowerLeftImage = new Point(0,height/scale);
        Point pointUpperRightImage = new Point(width/scale,0);
       
        coordinateChange= Point.alignedCoordinateChange(pointLowerLeft, pointLowerLeftImage, pointUpperRight, pointUpperRightImage);
        image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
  
        ctx = image.createGraphics();
        ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        ctx.setColor(colorBack.toAPIColor());
        ctx.fillRect(0, 0, width, height);
        ctx.scale(scale, scale);
        
    }
    
    /**
     * Permet d'obtenir l'image de la toile
     * 
     * @return image de la toile
     */
    //!!! ASK TA !!! Immuabilité? 
    public BufferedImage image() {
        return image;
    }
    
    /**
     * Override
     * Permet de dessiner sur la toile une polyligne donnée avec un style de ligne donné
     * 
     * @param polyline : la polyligne à dessiner
     * @param lineStyle : le style de ligne à utiliser
     */
    @Override
    public void drawPolyline(PolyLine polyline, LineStyle lineStyle) {
        
        Stroke s = new BasicStroke(
                lineStyle.getThickness(),
                lineStyle.getLineEnd().ordinal(),
                lineStyle.getJoint().ordinal(),
                (float)10.0,
                (lineStyle.lineSequence().length == 0)? null: lineStyle.lineSequence(),0);
                    // Passe null au constructeur de BasicStroke si l'instance de LineStyle mis en paramètre a pour attribut "lineSequence", lineStyle.DEFAULT_LINE_SEQUENCE
        ctx.setStroke(s);
        ctx.setColor(lineStyle.getColor().toAPIColor());
        Path2D path = makePath(polyline);
        ctx.draw(path);
    }
 
    /**
     * Override
     * Permet de dessiner sur la toile un polygone donnée avec une couleur donnée
     * 
     * @param polygon : le polygone à dessiner
     * @param color : la couleur à utiliser
     */
    @Override
    public void drawPolygon(Polygon polygon, Color color) { 
        
        Path2D shell = makePath(polygon.shell());
        Area shellArea = new Area(shell);
        
        for (PolyLine hole : polygon.holes()) {
            Path2D holePath = makePath(hole);
            shellArea.subtract(new Area(holePath));
        }
        
        ctx.setStroke(new BasicStroke (0,BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 10.0f, null, 0));
        ctx.setColor(color.toAPIColor());
        ctx.fill(shellArea); 
    }
    
    private Path2D makePath(PolyLine polyline) {
        
        Path2D path = new Path2D.Double();
        Point firstPoint = coordinateChange.apply(polyline.firstPoint());
        path.moveTo(firstPoint.x(),firstPoint.y());
        List<Point> remainingPoints = polyline.points().subList(1,polyline.points().size());
        
      //!!! ASK TA !!! Confirmer efficacite
        for (Point point: remainingPoints) {
            point = coordinateChange.apply(point);
            path.lineTo(point.x(), point.y());
        }
        if (polyline.isClosed()) path.closePath();
        // La liste de points de closedPolyLine ne contient pas de points en double ( du fait de l'utilisation de OSMWay.nonRepeatingNodes() dans OSMtoGeoTransformer)
      
        return path;
    }
}
