package io.github.monitool.autoclient;

import com.google.common.collect.Lists;
import io.github.monitool.autoclient.dto.LoginDTO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Bartosz Głowacki on 2015-05-06.
 */
public class ConfigParser {


    public List<LoginDTO> parse(){
        List<LoginDTO> monitors = Lists.newArrayList();
        File fXmlFile = new File("config.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("monitor");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String url = eElement.getElementsByTagName("url").item(0).getTextContent();
                    String email = eElement.getElementsByTagName("email").item(0).getTextContent();
                    String pass = eElement.getElementsByTagName("password").item(0).getTextContent();
                   monitors.add(new LoginDTO(url,email,pass));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return monitors;
    }
}
