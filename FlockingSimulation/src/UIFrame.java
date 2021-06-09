import drawing.Canvas;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class constructs the GUI panel for the flocking simulation. It consists of the the canvas and a
 * sidePanel.
 *<b/>
 * The canvas panel is used to simulate the boids whereas the sidePanel contains the controllers.
 * @author Y3879165
 *
 */
public class UIFrame extends JFrame {
    public final static int WINDOW_X_SIZE = 1500;
    public final static int WINDOW_Y_SIZE = 1000;

    public static final int DEFAULT_SLIDER_LENGTH = 10;
    public static final int SLIDER_STARTING_POSITION = DEFAULT_SLIDER_LENGTH / 2;
    public static final double SLIDER_WEIGHT = SLIDER_STARTING_POSITION * 1.0;
    private final Canvas canvas;
    private final Flock flock;

    private JPanel sidePanel = new JPanel();
    private BoxLayout box = new BoxLayout(sidePanel, BoxLayout.Y_AXIS);

    public UIFrame(Canvas canvas, Flock flock) throws HeadlessException {
        super();
        this.canvas = canvas;
        this.flock = flock;
        this.setTitle("Flocking Simulation");
        this.setSize(WINDOW_X_SIZE, WINDOW_Y_SIZE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(canvas);
        this.add(canvas, BorderLayout.CENTER);
        this.add(sidePanel, BorderLayout.EAST);
        sidePanel.setLayout(box);

        JSlider separationWeightSlide = createSlider();
        JLabel sepLabel = new JLabel("Separation");
        sidePanel.add(Box.createVerticalStrut(30));
        sidePanel.add(sepLabel);
        sidePanel.add(separationWeightSlide);
        separationWeightSlide.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                double separationWeight = (separationWeightSlide.getValue() / SLIDER_WEIGHT) * flock.INITIAL_SEPARATION_WEIGHT;
               flock.setSeparationWeight(separationWeight);
            }
        });

        JSlider cohesionWeightSlide = createSlider();
        JLabel cohLabel = new JLabel("Cohesion");
        sidePanel.add(Box.createVerticalStrut(30));
        sidePanel.add(cohLabel);
        sidePanel.add(cohesionWeightSlide);
        cohesionWeightSlide.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                double cohesionWeight = (cohesionWeightSlide.getValue() / SLIDER_WEIGHT) * flock.INITIAL_COHESION_WEIGHT;
                flock.setCohesionWeight(cohesionWeight);
            }
        });

        JSlider alignmentWeightSlide = createSlider();
        JLabel aliLabel = new JLabel("Alignment");
        sidePanel.add(Box.createVerticalStrut(30));
        sidePanel.add(aliLabel);
        sidePanel.add(alignmentWeightSlide);
        alignmentWeightSlide.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                System.out.println( "Alignment Slide : " + alignmentWeightSlide.getValue());
                double alignmentWeight = (alignmentWeightSlide.getValue() / SLIDER_WEIGHT) * flock.INITIAL_ALIGNMENT_WEIGHT;
                flock.setAlignmentWeight(alignmentWeight);
            }
        });

        sidePanel.add(Box.createVerticalStrut(30));
        JLabel speedLabel = new JLabel("Speed");
        JSlider speedSlide = createSlider();
        sidePanel.add(speedLabel);
        sidePanel.add(speedSlide);
        speedSlide.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                double newDeltaTime = (DEFAULT_SLIDER_LENGTH - speedSlide.getValue())/ SLIDER_WEIGHT * flock.INITIAL_DELTA_TIME;
                flock.setDeltaTime(newDeltaTime);
            }
        });

        sidePanel.add(Box.createVerticalStrut(30));
        JLabel orientationLabel = new JLabel("Orientation");
        JSlider orientationSlide = createSlider();
        sidePanel.add(orientationLabel);
        sidePanel.add(orientationSlide);
        orientationSlide.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                double orientationWeight = (orientationSlide.getValue())/ SLIDER_WEIGHT * flock.INITIAL_ORIENTATION_WEIGHT;
                flock.setOrientationWeight(orientationWeight);
            }
        });

        JButton resetSlidersButton = new JButton();
        sidePanel.add(Box.createVerticalStrut(20));
        resetSlidersButton.setText("Click to reset sliders!");
        sidePanel.add(resetSlidersButton);
        resetSlidersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                separationWeightSlide.setValue(SLIDER_STARTING_POSITION);
                cohesionWeightSlide.setValue(SLIDER_STARTING_POSITION);
                alignmentWeightSlide.setValue(SLIDER_STARTING_POSITION);
                speedSlide.setValue(SLIDER_STARTING_POSITION);
                orientationSlide.setValue(SLIDER_STARTING_POSITION);
            }
        });

        sidePanel.add(Box.createVerticalStrut(30));
        Box hBox = Box.createHorizontalBox();
        JTextField boidsSizeField = new JTextField();
        boidsSizeField.setText( Integer.toString(flock.getFlockSize()) );
        boidsSizeField.setHorizontalAlignment(JTextField.CENTER);
        boidsSizeField.setMinimumSize( new Dimension(50,20));
        boidsSizeField.setMaximumSize( new Dimension(150,40));
        boidsSizeField.setPreferredSize( new Dimension(100,30));
        hBox.add(boidsSizeField);

        JButton flockSizeButton = new JButton();
        flockSizeButton.setText("Set Flock Size");
        flockSizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sizeText = boidsSizeField.getText();
                try {
                    int size = Integer.parseInt(sizeText);
                    flock.setFlockSize(size);
                    flock.createBoids();
                } catch(NumberFormatException exception) {
                    boidsSizeField.setForeground(Color.RED);
                }
            }
        });

        hBox.add( flockSizeButton);
        sidePanel.add(hBox);

        sidePanel.add(Box.createVerticalStrut(30));

        this.setVisible(true);

    }

    private JSlider createSlider() {
        JSlider slider = new JSlider(SwingConstants.HORIZONTAL, 0, DEFAULT_SLIDER_LENGTH,
                SLIDER_STARTING_POSITION);
        slider.setMajorTickSpacing(5);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        return slider;
    }
}
