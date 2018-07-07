import javafx.scene.layout.Pane;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

public class DisplayModule extends Applet implements ActionListener {

    class GraphicFrame extends Frame{
        Pane leftPane;
        int width,height;
        int x,y;
        BufferedImage image;
        String fileExtension;
        float[] floatArray;
        GraphicFrame(String title) {
            super(title);
            width = 400;
            height = 400;
            x = 200;
            y = 200;
            leftPane = new Pane();
            setLayout(new GridLayout());


            MyWindowAdapter myWindowAdapter = new MyWindowAdapter(this);
            addWindowListener(myWindowAdapter);
            setTitle("Graph");
        }

        public void setImgResolution(int width, int height){
            this.width = width;
            this.height = height;
        }

        public void setGraphXY(int x, int y){
            this.x = x;
            this.y = y;
        }

        public void processData(String fileDirectory, String fileName){
            if(fileDirectory != null && fileName != null){
                switch (fileName.substring(fileName.indexOf("."))){
                    case ".jpg" :
                        fileExtension = ".jpg";
                        BufferedImage img;
                        try {
                            img = ReadWriteModule.processJPG(DisplayModule.fileDirectory + DisplayModule.fileName);
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                        if(img != null) {
                            makeImgGray(img);
                            image = img;
                        }
                        break;
                    case ".dat" :
                        fileExtension = ".dat";
                        float [] arr;
                        try {
                            arr = ReadWriteModule.processDAT(DisplayModule.fileDirectory + DisplayModule.fileName);
                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                        if(arr != null){
                            floatArray = arr;
                        }
                        break;
                    case ".xcr" :
                        fileExtension = ".xcr";
                        BufferedImage img1;
                        try {
                            img1 = ReadWriteModule.processXCR(DisplayModule.fileDirectory + DisplayModule.fileName, fileName);
                        } catch (IOException e) {
                            e.printStackTrace();
                            img1 = null;
                        }
                        if(img1 != null){
                            //makeImgGray(img1);
                            image = img1;
                        }
                        break;

                }
            }
        }

        public void makeImgGray(BufferedImage img){
            for (int x = 0; x < img.getWidth(); ++x)
                for (int y = 0; y < img.getHeight(); ++y)
                {
                    int rgb = img.getRGB(x, y);
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = (rgb & 0xFF);

                    int grayLevel = (r + g + b) / 3;
                    int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                    img.setRGB(x, y, gray);
                }
        }

        @Override
        public void paint(Graphics g) {
            if(fileExtension != null && (fileExtension.equals(".jpg") || fileExtension.equals(".xcr")) && image != null){
                g.drawImage(image,0,0,width,height,this);
            } else if(fileExtension != null && fileExtension.equals(".dat") && floatArray != null){
                // ось x
                g.drawLine(10,height/2,width-10,height/2);
                int temp = (width - 20)/20;
                int t = x / 10;
                for(int i = 0; i<= 20;i++){
                    if(i%4 == 0){
                        g.drawLine(10 + temp * i, height / 2 + 4, 10 + temp * i, height / 2 - 4);
                    } else {
                        g.drawLine(10 + temp * i, height / 2 + 2, 10 + temp * i, height / 2 - 2);
                    }
                    if(i%4==0) {
                        g.drawString(Integer.toString(-x + t * i), temp * i, height / 2 + 20);
                    }
                }
                // ось y
                g.drawLine(width/2,10,width/2,height-10);
                temp = (height - 20)/20;
                t = y / 10;
                for(int i = 0; i<= 20;i++){
                    if(i%4 == 0){
                        g.drawLine(width/2 - 4,10+ temp*i,width/2+4, 10+temp*i);
                    } else {
                        g.drawLine(width/2 - 2,10+ temp*i,width/2+2, 10+temp*i);
                    }
                    if(i%4==0) {
                        g.drawString(Integer.toString(y - t * i), width/2 - 30, 15+temp*i);
                    }
                }
                //вывод
            }
        }

    }

    class MyWindowAdapter extends WindowAdapter{
        Frame frame;
        public MyWindowAdapter(Frame gf){
            frame = gf;
        }

        @Override
        public void windowClosing(WindowEvent e) {
            setNanPanel();
            frame.setVisible(false);
        }
    }
    String fileExt;
    String message;
    CardLayout cardLO;
    Panel panels,graphSettings,imgSettings;
    TextField imgHeight,imgWidth;
    TextField graphX,graphY;
    GraphicFrame graph;
    Button fileDialog, saveSettings, closePanel, close2,save2;
    public static String fileDirectory;
    public static String fileName;

    void setNanPanel(){
        cardLO.show(panels,"openFile");
    }

    @Override
    public void init() {
        cardLO = new CardLayout();
        panels = new Panel();
        panels.setLayout(cardLO);
        imgHeight = new TextField("image_height");
        imgWidth = new TextField("image_width");
        graphX = new TextField("graphX");
        graphY = new TextField("graphY");
        saveSettings = new Button("save settings");
        closePanel = new Button("close");
        save2 = new Button("save settings");
        close2 = new Button("close");
        graphSettings = new Panel();
        graphSettings.add(graphX);
        graphSettings.add(graphY);
        graphSettings.add(save2);
        graphSettings.add(close2);
        imgSettings = new Panel();
        imgSettings.add(imgWidth);
        imgSettings.add(imgHeight);
        imgSettings.add(saveSettings);
        imgSettings.add(closePanel);
        Panel openFile = new Panel();
        fileDialog = new Button("open file");
        openFile.add(fileDialog);
        panels.add(imgSettings,"imgSettings");
        panels.add(openFile,"openFile");
        panels.add(graphSettings, "graphSettings");
        add(panels);
        fileDialog.addActionListener(this);
        imgWidth.addActionListener(this);
        imgHeight.addActionListener(this);
        graphY.addActionListener(this);
        graphX.addActionListener(this);
        saveSettings.addActionListener(this);
        closePanel.addActionListener(this);
        save2.addActionListener(this);
        close2.addActionListener(this);
        graph = new GraphicFrame("Graph");

        graph.setSize(400,400);

    }

    @Override
    public void start() {
        cardLO.show(panels,"openFile");
    }

    @Override
    public void stop() {
        graph.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        if(str.equals("open file")){
            FileDialog fd = new FileDialog(this.graph,"File Dialog");
            fd.setVisible(true);
            fileDirectory = fd.getDirectory();
            fileName = fd.getFile();
            if(fileName != null && fileDirectory != null){
                String ext = fileName.substring(fileName.indexOf("."));
                if(!(ext.equals(".jpg") || ext.equals(".dat") || ext.equals(".xcr"))){
                    fileName = null;
                    fileDirectory = "wrong";
                } else{
                    fileExt = ext;
                    graph.processData(fileDirectory,fileName);
                    if(ext.equals(".dat")){
                        cardLO.show(panels,"graphSettings");
                    } else {
                        cardLO.show(panels, "imgSettings");
                    }
                    graph.repaint();
                    graph.setVisible(true);
                }
            }
        } else if(str.equals("save settings")){
            if(fileExt != null && (fileExt.equals(".jpg") || fileExt.equals(".xcr"))) {
                try {
                    int width = Integer.parseInt(imgWidth.getText());
                    int height = Integer.parseInt(imgHeight.getText());
                    graph.setImgResolution(width, height);
                    graph.repaint();
                } catch (Exception e1) {
                    message = "please enter right settings";
                    repaint();
                }
            } else if(fileExt != null && fileExt.equals(".dat")){
                try {
                    int x = Integer.parseInt(graphX.getText());
                    int y = Integer.parseInt(graphY.getText());
                    graph.setGraphXY(x,y);
                    graph.repaint();
                } catch (Exception e1) {
                    message = "please enter right settings";
                    repaint();
                }
            }
        } else if(str.equals("close")){
            fileExt = null;
            fileName = null;
            fileDirectory = null;
            graph.setImgResolution(400,300);
            graph.setVisible(false);
            cardLO.show(panels,"openFile");
            message = "";
        }

    }

    @Override
    public void paint(Graphics g) {
        if(message!=null){
            g.drawString(message,10,250);
            message = "";
        }
    }
}
