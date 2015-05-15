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

public class Main {

    public static void main1(String [] args) throws IOException,SAXException{
        CH1903Projection proj = new CH1903Projection();
        PointGeo topRightGeo = new PointGeo(Double.parseDouble(args[4])*Math.PI/180, Double.parseDouble(args[5])*Math.PI/180);
        PointGeo bottomLeftGeo = new PointGeo(Double.parseDouble(args[2])*Math.PI/180, Double.parseDouble(args[3])*Math.PI/180);
        Point topRight = proj.project(topRightGeo);
        Point bottomLeft = proj.project(bottomLeftGeo);
        int height = (int)(Math.round(Double.parseDouble(args[6])*39.370*39.370*Earth.RADIUS*(topRightGeo.latitude()-bottomLeftGeo.latitude())*Math.PI/(360*25000) ));
        int width = (int)Math.round(height*(topRight.x()-bottomLeft.x())/(topRight.y()-bottomLeft.y()));
        ReliefShader shader = new ReliefShader(new CH1903Projection(), new HGTDigitalElevationModel(new File(args[1])), new Vector3(-1,1,1) );
        BufferedImage topo = shader.shadedRelief(bottomLeft, topRight, height, width, Integer.parseInt(args[6])*1.7/25.4);
        OSMToGeoTransformer transformer = new OSMToGeoTransformer(new CH1903Projection());
        Map map = transformer.transform(OSMMapReader.readOSMFile(args[0], true));
        Java2DCanvas canvas =
                new Java2DCanvas(bottomLeft, topRight,height,width,Integer.parseInt(args[6]), Color.WHITE);
        
        SwissPainter.painter().drawMap(canvas, map);
        BufferedImage mainImage = canvas.image();
        BufferedImage finalImage = new BufferedImage(mainImage.getWidth(),mainImage.getHeight(),BufferedImage.TYPE_INT_RGB);
        for (int x=0;x<finalImage.getWidth();x++){
            for (int y=0;y<finalImage.getHeight();y++) {
                finalImage.setRGB(x, y, Color.rgb(mainImage.getRGB(x,y)).multiply(Color.rgb(topo.getRGB(x,y))).toAPIColor().getRGB());
            }
        }
            ImageIO.write(finalImage, "png", new File(args[7]));
    }
    
    public static void main(String [] args) throws IOException,SAXException{
        String[] args1 = {"/Users/Romain/Documents/Eclipse-workspace/github/ProjectProgra/src/ch/epfl/imhof/osm/lausanne.osm.gz", "/Users/Romain/Documents/Eclipse-workspace/github/ProjectProgra/src/ch/epfl/imhof/osm/N46E006.hgt", "6.5594", "46.5032", "6.6508", "46.5459",
                "300", "topo.png"};
        main1(args1);
    }


}
