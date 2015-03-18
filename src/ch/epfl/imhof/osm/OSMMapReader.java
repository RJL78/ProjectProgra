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

public final class OSMMapReader {


    private static OSMMap.Builder mapBuilder;
    private static OSMEntity.Builder entityBuilder;    
    
    private OSMMapReader(){
    }
    
    public static OSMMap readOSMFile (String fileName, boolean unGZip) throws IOException, SAXException {
        
        mapBuilder = new OSMMap.Builder(); 
        InputStream i = new FileInputStream(fileName);
        if(unGZip){
            i = new GZIPInputStream(i);
        }
        XMLReader r = XMLReaderFactory.createXMLReader();
        r.setContentHandler(new DefaultHandler() {
            
                // que faire si une relation en inclut une autre, mais que le xml parser n'est pas encore passe par la relation fils?
                // exceptions a tester - que se passe-t-il si un element ne possede pas toutes les informations donnes?
            
            
               public void startElement(String uri,String lName, String qName, Attributes atts)throws SAXException{
                   switch(qName) {
                        case "node": 
                            PointGeo location = new PointGeo( Double.parseDouble(atts.getValue("lon")), Double.parseDouble(atts.getValue("lat")));
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
                                    member = mapBuilder.nodeForId(Long.parseLong(atts.getValue("id")));
                                    type = OSMRelation.Member.Type.NODE;
                                    break;
                                case "way":
                                    member = mapBuilder.wayForId(Long.parseLong(atts.getValue("id")));
                                    type = OSMRelation.Member.Type.WAY;
                                    break;
                                case "relation":
                                    member = mapBuilder.relationForId(Long.parseLong(atts.getValue("id")));
                                    type = OSMRelation.Member.Type.RELATION;
                                    break;
                                default :
                                    type = null;
                                    member = null;
                                    break;
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
                   if(!entityBuilder.isIncomplete()){    
                       switch(qName){
                           case "way":      
                               try {
                                   mapBuilder.addWay(((OSMWay.Builder)entityBuilder).build());
                               }
                               catch (IllegalArgumentException e) {}
                               break; 
                           case "relation":                             
                               mapBuilder.addRelation(((OSMRelation.Builder)entityBuilder).build());
                               break;
                           case "node":
                               mapBuilder.addNode(((OSMNode.Builder)entityBuilder).build());
                           default : break;
                       }                        
                   }
               }
      });
      r.parse(new InputSource(i));
      i.close();
      return mapBuilder.build();
  }
    
}
