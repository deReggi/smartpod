/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartpod;

import com.janezfeldin.Display.ImageWindow;
import com.janezfeldin.Math.Point;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.wrapper.AgentController;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Janez Feldin
 */
public class EnvironmentAgent extends UISAgent
{

    private ImageWindow window = new ImageWindow();
    private BufferedImage image;
//    private Properties prop = new Properties();
    private int mapWidth = 500;
    private int mapHeight = 500;
    private String roadBelongingType = "inbound";
    //deklaracija spremenljivk za izdelavo seznama vseh agentov
    private ArrayList<PodAgent> podsList = new ArrayList<PodAgent>();
    private ArrayList<StationAgent> stationsList = new ArrayList<StationAgent>();
    private ArrayList<JunctionAgent> junctionList = new ArrayList<JunctionAgent>();
    private ArrayList<RoadAgent> roadsList = new ArrayList<RoadAgent>();

    @Override
    protected void setup()
    {
        //odpre okno animacije
        window.setVisible(true);

        //nalaganje zemljevida in zagon agentov
        try
        {
            File xmlFile = new File("conf.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            //nastavljanje agenta okolja
            Element temp;
            NodeList tempList;

            temp = (Element) doc.getElementsByTagName("Environment").item(0);
            mapWidth = Integer.parseInt(temp.getElementsByTagName("width").item(0).getTextContent());
            mapHeight = Integer.parseInt(temp.getElementsByTagName("height").item(0).getTextContent());
            roadBelongingType = temp.getElementsByTagName("roadBelongingType").item(0).getTextContent();

            //nastavljanje križišč
            tempList = doc.getElementsByTagName("Junction");
            for (int i = 0;
                    i < tempList.getLength();
                    i++)
            {
                temp = (Element) tempList.item(i);
                Point tempPoint = new Point(Integer.parseInt(temp.getElementsByTagName("x").item(0).getTextContent()), Integer.parseInt(temp.getElementsByTagName("y").item(0).getTextContent()));
                JunctionAgent tempAgent = new JunctionAgent(tempPoint);
                ((AgentController) getContainerController().acceptNewAgent(temp.getElementsByTagName("name").item(0).getTextContent(), tempAgent)).start();
                junctionList.add(tempAgent);
            }
            //nastavljanje postaj
            tempList = doc.getElementsByTagName("Station");
            for (int i = 0;
                    i < tempList.getLength();
                    i++)
            {
                temp = (Element) tempList.item(i);
                Point tempPoint = new Point(Integer.parseInt(temp.getElementsByTagName("x").item(0).getTextContent()), Integer.parseInt(temp.getElementsByTagName("y").item(0).getTextContent()));
                StationAgent tempAgent = new StationAgent(tempPoint, Integer.parseInt(temp.getElementsByTagName("podCapacity").item(0).getTextContent()), Integer.parseInt(temp.getElementsByTagName("peopleCapacity").item(0).getTextContent()));
                ((AgentController) getContainerController().acceptNewAgent(temp.getElementsByTagName("name").item(0).getTextContent(), tempAgent)).start();
                stationsList.add(tempAgent);
            }
            //nastavljanje poti
            tempList = doc.getElementsByTagName("Road");
            for (int i = 0; i < tempList.getLength(); i++)
            {
                temp = (Element) tempList.item(i);
                String tempStart = temp.getElementsByTagName("start").item(0).getTextContent();
                String tempEnd = temp.getElementsByTagName("end").item(0).getTextContent();
                RoadAgent tempAgent = new RoadAgent(tempStart, tempEnd, getNodesPosition(tempStart), getNodesPosition(tempEnd), roadBelongingType);
                ((AgentController) getContainerController().acceptNewAgent(temp.getElementsByTagName("name").item(0).getTextContent(), tempAgent)).start();
                roadsList.add(tempAgent);
            }

            //nastavljanje podov
            temp = (Element) doc.getElementsByTagName("Pods").item(0);
            String[] tempPodsCapacity = temp.getElementsByTagName("capacity").item(0).getTextContent().split(",");
            int numPods = Integer.parseInt(temp.getElementsByTagName("numPods").item(0).getTextContent());
            int j = 0;

            for (int i = 0; i < numPods; i++)
            {
                if (j >= tempPodsCapacity.length)
                {
                    j--;
                }

                PodAgent tempAgent = new PodAgent(getNodesPosition(temp.getElementsByTagName("startPosition").item(0).getTextContent()), Integer.parseInt(tempPodsCapacity[j]), 0);

                ((AgentController) getContainerController().acceptNewAgent("Pod" + (i + 1), tempAgent)).start();
                podsList.add(tempAgent);

                j++;
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.toString());
        }


        //nastavitev slika "animacije"
        image = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_RGB);


        //dodajanje obnašanja k agentu okolja
        addBehaviour(new EnvironmentAgentBehaviour(this));


    }

    private Point getNodesPosition(String name)
    {
        for (int i = 0; i < stationsList.size(); i++)
        {
            if (stationsList.get(i).getLocalName().equals(name))
            {
                return stationsList.get(i).getPosition();
            }
        }

        for (int i = 0; i < junctionList.size(); i++)
        {
            if (junctionList.get(i).getLocalName().equals(name))
            {
                return junctionList.get(i).getPosition();
            }
        }

        return null;
    }

    public class EnvironmentAgentBehaviour extends CyclicBehaviour
    {

        public EnvironmentAgentBehaviour(Agent a)
        {
            super(a);
        }

        @Override
        public void action()
        {
            //izris ozadja
            drawBackground();





            //izris Agentov
            Graphics g = image.getGraphics();
            //podi
            g.setColor(Color.blue);
            for(int i=0;i<podsList.size();i++)
            {
                Point tempPosition = podsList.get(i).getPosition();
                g.fillRoundRect((int)tempPosition.x-2, (int)tempPosition.y-2, 3, 3, 3, 3);
            }
            //postaje
            g.setColor(Color.black);
            for(int i=0;i<stationsList.size();i++)
            {
                Point tempPosition = stationsList.get(i).getPosition();
                g.fillRect((int)tempPosition.x-8, (int)tempPosition.y-8, 15, 15);
            }
            //križišča
            g.setColor(Color.gray);
            for(int i=0;i<junctionList.size();i++)
            {
                Point tempPosition = junctionList.get(i).getPosition();
                g.fillRect((int)tempPosition.x-4, (int)tempPosition.y-4, 7, 7);
            }
            //križišča
            g.setColor(Color.green);
            for(int i=0;i<roadsList.size();i++)
            {
                Point p1 = roadsList.get(i).getStartPosition();
                Point p2 = roadsList.get(i).getEndPosition();
                g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
            }




            //prikaz slike in zakasnitev
            window.showImage(image);


//            sleep();
        }

        private void drawBackground()
        {
            //izris ozadja
            Graphics g = image.getGraphics();
            g.setColor(Color.white);
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
        }

        private void sleep()
        {
            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e)
            {
                System.out.println(e.toString());
            }
        }
    }
}
