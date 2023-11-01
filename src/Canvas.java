import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JPanel {
    private final static int STROKE_SIZE = 8;
    private List<List<ColorPoint>> allPaths;
    private List<ColorPoint> currentPath;
    private Color color;
    private int canvasWidth, canvasHeight;
    private int x, y;

    public Canvas(int targetWidth, int targetHeight){
        super();
        setPreferredSize(new Dimension(targetWidth, targetHeight));
        setOpaque(true);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        allPaths = new ArrayList<>(25);
        canvasWidth = targetWidth;
        canvasHeight = targetHeight;

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                Graphics g = getGraphics();
                g.setColor(color);
                g.fillRect(x, y, STROKE_SIZE, STROKE_SIZE);
                g.dispose();
                currentPath = new ArrayList<>(25);
                currentPath.add(new ColorPoint(x, y, color));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                allPaths.add(currentPath);
                currentPath = null;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                Graphics2D g2d = (Graphics2D) getGraphics();
                g2d.setColor(color);
                if(!currentPath.isEmpty()){
                    ColorPoint prevPoint = currentPath.get(currentPath.size() - 1);
                    g2d.setStroke(new BasicStroke(STROKE_SIZE));
                    g2d.drawLine(prevPoint.getX(), prevPoint.getY(), x, y);
                }
                g2d.dispose();
                ColorPoint nextPoint = new ColorPoint(e.getX(), e.getY(), color);
                currentPath.add(nextPoint);            }
        };

        addMouseListener(ma);
        addMouseMotionListener(ma);
    }
    public void setColor(Color color){
        this.color = color;
    }
    public void resetCanvas(){
        Graphics g = getGraphics();
        g.clearRect(0, 0, canvasWidth, canvasHeight);
        g.dispose();
        allPaths = new ArrayList<>(25);
        currentPath = null;

        repaint();
        revalidate();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for(List<ColorPoint> path : allPaths){
            ColorPoint from = null;
            for(ColorPoint point : path){
                g2d.setColor(point.getColor());
                if(path.size() == 1){
                    g2d.fillRect(point.getX(), point.getY(), STROKE_SIZE, STROKE_SIZE);
                }
                if(from != null){
                    g2d.setStroke(new BasicStroke(STROKE_SIZE));
                    g2d.drawLine(from.getX(), from.getY(), point.getX(), point.getY());
                }
                from = point;
            }
        }
    }
}