import sun.tools.jconsole.inspector.XTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by apple on 24.04.2016.
 */
public class Frame extends JFrame {

    public static final int WIDTH=1000;
    public static final int HEIGHT=900;

    private Mesh mesh;

    private Component component;
    private JPanel settingsPanel;
    JButton buttonStart;
    JTextField xField;
    JTextField yField;


    public Frame(Mesh mesh){
        this.mesh = mesh;
        initializeComponent();
    }



    @Override
    public void repaint() {
        super.repaint();
        String name;
        if(mesh.isStarted())
            name="STOP";
        else
            name="START";

        buttonStart.setText(name);
    }

    private void initializeComponent(){

        setSize(WIDTH,HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setLayout(new BorderLayout());

        settingsPanel=new JPanel();




        xField = new JTextField(String.valueOf(mesh.getX()));
        yField = new JTextField(String.valueOf(mesh.getX()));
        settingsPanel.add(new JLabel("width:"));
        settingsPanel.add(xField);
        settingsPanel.add(new JLabel("height:"));
        settingsPanel.add(yField);


        JButton buttonClear=new JButton("clear");
        buttonClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame.this.mesh.clear();
                component.repaint();
            }
        });
        settingsPanel.add(buttonClear);


        String start;
        if(mesh.isStarted())
            start="STOP";
        else
        start="START";

        buttonStart=new JButton(start);

        buttonStart.addActionListener(new ButtonStartListener());
        settingsPanel.add(buttonStart);

        add(settingsPanel, BorderLayout.NORTH);

        component =new Component(mesh);

        add(component, BorderLayout.CENTER);


        JPanel genPanel=new JPanel();
        genPanel.add(new JLabel("generate nucleons:"));

        JButton rand=new JButton("random");
        rand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int ammount=Integer.parseInt(JOptionPane.showInputDialog("number of nucleons"));
                mesh.generateRand(ammount);
                component.repaint();
            }
        });


        genPanel.add(rand);
        genPanel.add(new JLabel("generate inclusions:"));

        JButton rand_inc=new JButton("random");
        rand_inc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int ammount=Integer.parseInt(JOptionPane.showInputDialog("number of inclusions"));
                mesh.generateRandInc(ammount);
                component.repaint();
            }
        });
        genPanel.add(rand_inc);

        settingsPanel.add(genPanel);

        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu micro = new JMenu("Microstructure");
        JMenuItem imp_bmp = new JMenuItem("Import from bmp");
        JMenuItem imp = new JMenuItem("Import");
        JMenuItem exp_bmp = new JMenuItem("Export to bmp");
        JMenuItem exp = new JMenuItem("Export");

        imp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    Mesh new_mesh = FileHandler.importMesh(file);
                    Frame.this.mesh = new_mesh;
                    component.setMesh(new_mesh);
                    component.repaint();
                }
            }
        });

        imp_bmp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    BufferedImage img = FileHandler.importMeshFromImage(file);
                    Graphics g = component.getGraphics();
                    Graphics2D graphics2d = (Graphics2D) g;
                    graphics2d.drawImage(img, 0, 0, null);
                }
            }
        });

        exp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    FileHandler.exportMesh(file, mesh.getTab(), mesh.getX(), mesh.getY());
                }
            }
        });

        exp_bmp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    BufferedImage im = new BufferedImage(component.getWidth()-200, component.getHeight()-10, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = im.createGraphics();
                    component.paint(g);
                    FileHandler.exportMeshToImage(file, im);
                }
            }
        });

        micro.add(imp);
        micro.add(imp_bmp);
        micro.add(exp);
        micro.add(exp_bmp);
        file.add(micro);
        menubar.add(file);
        setJMenuBar(menubar);

    }


    class ButtonStartListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e){
            JButton b=(JButton)e.getSource();
//            mesh.setX(Integer.parseInt(xField.getText()));
//            mesh.setY(Integer.parseInt(yField.getText()));

            if(Frame.this.mesh.isStarted()){
                b.setText("START");
                mesh.stop();
            }
            else
            {
                mesh.start();
                Thread t=new Thread(mesh, component, Frame.this);
                t.start();
                b.setText("STOP");
            }


        }
    }
}

