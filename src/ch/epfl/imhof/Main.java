package ch.epfl.imhof;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.xml.sax.SAXException;

import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

/**
 * Classe principale de notre projet: 
 * 
 * La fonction main prends les huit arguments ci-dessous, dans cet ordre la  ( en tant que tableau de string ) : 
 *  -le nom (chemin) d'un fichier OSM compressé avec gzip contenant les données de la carte à dessiner,
 *  -le nom (chemin) d'un fichier HGT couvrant la totalité de la zone de la carte à dessiner, zone tampon incluse,
 *  -la longitude du point bas-gauche de la carte, en degrés,
 *  -la latitude du point bas-gauche de la carte, en degrés,
 *  -la longitude du point haut-droite de la carte, en degrés,
 *  -la latitude du point haut-droite de la carte, en degrés,
 *  -la résolution de l'image à dessiner, en points par pouce (dpi),
 *  -le nom (chemin) du fichier PNG à générer.
 * 
 * 
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 * 
 *
 */


public class Main { 
  
   private final static double INCHES_PER_METER = 39.370;
   private final static double MILLIMETERS_PER_INCH = 25.4;
   private final static double ASPECT_RATIO = 25000;
   private final static Vector3 LIGHTING_VECTOR = new Vector3(-1,1,1);
   private final static Projection PROJECTION = new CH1903Projection();
   private final static double BLURR_RADIUS_MM = 1.7;
   
   /**
    * @throw Exception : si les fichiers utilisés ne sont pas lisibles, les arguments sont faux 
    */
    public static void main1(String [] args) throws Exception {
         
        PointGeo topRightGeo = new PointGeo(Math.toRadians(Double.parseDouble(args[4])), Math.toRadians(Double.parseDouble(args[5])));
        PointGeo bottomLeftGeo = new PointGeo(Math.toRadians(Double.parseDouble(args[2])), Math.toRadians(Double.parseDouble(args[3])));
        Point topRight = PROJECTION.project(topRightGeo);
        Point bottomLeft = PROJECTION.project(bottomLeftGeo);

        int height = (int)(Math.round(Double.parseDouble(args[6])*INCHES_PER_METER*Earth.RADIUS*(topRightGeo.latitude()-bottomLeftGeo.latitude())/ASPECT_RATIO));
        int width = (int)Math.round(height*(topRight.x()-bottomLeft.x())/(topRight.y()-bottomLeft.y()));
       
        ReliefShader shader = new ReliefShader(PROJECTION, new HGTDigitalElevationModel(new File(args[1])), LIGHTING_VECTOR);
        BufferedImage topo = shader.shadedRelief(bottomLeft, topRight, height, width, Integer.parseInt(args[6])*BLURR_RADIUS_MM/MILLIMETERS_PER_INCH);
        OSMToGeoTransformer transformer = new OSMToGeoTransformer(PROJECTION);
        Map map = transformer.transform(OSMMapReader.readOSMFile(args[0], true));
        Java2DCanvas canvas = new Java2DCanvas(bottomLeft, topRight,height,width,Integer.parseInt(args[6]), Color.WHITE);
        
        SwissPainter.painter().drawMap(canvas, map);
        BufferedImage mainImage = canvas.image();
        for (int x=0;x<width;x++){ // mélange des deux images 
            for (int y=0;y<height;y++) {
                mainImage.setRGB(x, y, Color.rgb(mainImage.getRGB(x,y)).multiply(Color.rgb(topo.getRGB(x,y))).toAPIColor().getRGB());
            }
        }
        ImageIO.write(mainImage, "png", new File(args[7]));
    }
    
    public static void main(String [] args) throws IOException,SAXException, Exception{

        String[] args1 = {"/Users/Raphael/Sites/ProjectSemester2/src/ch/epfl/imhof/osm/lausanne.osm.gz", "/Users/Raphael/Downloads/imhof-dems/N46E006.hgt", "6.5594", "46.5032", "6.6508", "46.5459",
                "300", "top.png"};

        main1(args1);
    }


}
