package ch.epfl.imhof.osm;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.epfl.imhof.PointGeo;

/**
 *  Classe immuable et non instanciable permettant de de construire une carte 
 *  OpenStreetMap à partir de données stockées dans un fichier au format OSM
 *  
 * @author Raphael Laporte (251209) / Romain Leteurtre (238162)
 *
 */
public final class OSMMapReader {

    private static OSMEntity.Builder entityBuilder;

    private OSMMapReader(){
    }

    /**
     * méthode qui lit la carte OSM contenue dans le fichier de nom donné, en le décompressant 
     * avec gzip si et seulement si le second argument est vrai.
     * 
     * @param fileName : nom du fichier à lire
     * @param unGiz : boolean comfirmant si le fichier est un GZip file ou pas
     * 
     * @return la carte OSMMap construite à partir du fichier
     * 
     * @throws SAXException : en cas d'erreur dans le format du fichier XML contenant la carte
     * @throws IOException : en cas d'autre erreur d'entrée/sortie
     */
    public static OSMMap readOSMFile (String fileName, boolean unGZip) throws IOException, SAXException {

        final OSMMap.Builder mapBuilder = new OSMMap.Builder(); 
        InputStream i = new FileInputStream(fileName);
        if(unGZip){
            i = new GZIPInputStream(i);
        }
        XMLReader r = XMLReaderFactory.createXMLReader();
        r.setContentHandler(new DefaultHandler() {

            public void startElement(String uri,String lName, String qName, Attributes atts)throws SAXException{
                switch(qName) {
                case "node": 
                    PointGeo location = new PointGeo( Math.toRadians(Double.parseDouble(atts.getValue("lon"))), Math.toRadians(Double.parseDouble(atts.getValue("lat"))));
                    entityBuilder = new OSMNode.Builder (Long.parseLong(atts.getValue("id")), location );
                    break;
                case "way":
                    entityBuilder = new OSMWay.Builder(Long.parseLong(atts.getValue("id")));
                    break;
                case "nd":
                    if(mapBuilder.nodeForId(Long.parseLong(atts.getValue("ref")))==null){
                        entityBuilder.setIncomplete();
                    }
                    ((OSMWay.Builder)entityBuilder).addNode(mapBuilder.nodeForId(Long.parseLong(atts.getValue("ref"))));
                    break;
                case "tag":
                    entityBuilder.setAttribute(atts.getValue("k"), atts.getValue("v"));
                    break;
                case "relation": 
                    entityBuilder = new OSMRelation.Builder(Long.parseLong(atts.getValue("id")));
                    break;
                case "member":
                    OSMRelation.Member.Type type;
                    OSMEntity member;
                    switch(atts.getValue("type")){
                    case "node":
                        member = mapBuilder.nodeForId(Long.parseLong(atts.getValue("ref")));
                        type = OSMRelation.Member.Type.NODE;
                        break;
                    case "way":
                        member = mapBuilder.wayForId(Long.parseLong(atts.getValue("ref")));
                        type = OSMRelation.Member.Type.WAY;                          
                        break;
                    case "relation":
                        member = mapBuilder.relationForId(Long.parseLong(atts.getValue("ref")));
                        type = OSMRelation.Member.Type.RELATION;
                        break;
                    default :
                        type = null;
                        member = null;
                        throw new SAXException();
                    }                           
                    if (member==null){
                        entityBuilder.setIncomplete();
                    }
                    else {
                        ((OSMRelation.Builder)entityBuilder).addMember(type, atts.getValue("role"), member);
                    } 
                default : break;
                }   
            }

            public void endElement (String uri,String lName, String qName){ 
                try {
                    switch(qName){
                    case "way":
                        mapBuilder.addWay(((OSMWay.Builder)entityBuilder).build());
                        break; 
                    case "relation":                             
                        mapBuilder.addRelation(((OSMRelation.Builder)entityBuilder).build());
                        break;
                    case "node":
                        mapBuilder.addNode(((OSMNode.Builder)entityBuilder).build());
                    default : break;
                    }                        
                }
                catch (IllegalArgumentException | IllegalStateException e ){}
            }
        });
        r.parse(new InputSource(i));
        i.close();
        return mapBuilder.build();
    }
}
