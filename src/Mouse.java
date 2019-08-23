import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

public class Mouse extends JLayeredPane {
    public static final int WIDTH = 700;
    public static final int HEIGHT = 700;
    private static final int GRID_ROWS = 15;
    private static final int GRID_COLS = 15;
    private static final int GAP = 1;
    private static final Dimension LAYERED_PANE_SIZE = new Dimension(WIDTH, HEIGHT);
    private static final Dimension LABEL_SIZE = new Dimension(45, 45);
    private GridLayout gridlayout = new GridLayout(GRID_ROWS, GRID_COLS, GAP, GAP);

    private JPanel backingPanel = new JPanel(gridlayout);
    private JPanel rackBacking = new JPanel(new GridLayout(1, 7, 1, 1));

    private JPanel[][] panelGrid = new JPanel[GRID_ROWS][GRID_COLS];
    private JPanel[][] rackGrid = new JPanel[1][7];
    private JLabel block1 = new JLabel("A", SwingConstants.CENTER);
    private JLabel block2 = new JLabel("B", SwingConstants.CENTER);

    public Mouse() {
        backingPanel.setSize(LAYERED_PANE_SIZE);
        backingPanel.setLocation(0, 0);
        backingPanel.setBackground(Color.black);
        rackBacking.setSize(new Dimension(350, 47));
        rackBacking.setLocation(50, 750);
        rackBacking.setBackground(Color.black);


        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                panelGrid[row][col] = new JPanel(new GridBagLayout());
                backingPanel.add(panelGrid[row][col]);
            }
        }
        for (int col = 0; col < 7; col++) {
                rackGrid[0][col] = new JPanel(new GridBagLayout());
                rackBacking.add(rackGrid[0][col]);
        }

        block1.setOpaque(true);
        block1.setBackground(Color.gray);
        block1.setPreferredSize(LABEL_SIZE);
        rackGrid[0][0].add(block1);

        block2.setOpaque(true);
        block2.setBackground(Color.gray);
        block2.setPreferredSize(LABEL_SIZE);
        panelGrid[0][1].add(block2);

        backingPanel.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        rackBacking.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        setPreferredSize(new Dimension(900, 900));
        add(backingPanel, JLayeredPane.DEFAULT_LAYER);
        add(rackBacking, JLayeredPane.DEFAULT_LAYER);
        MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
        addMouseListener(myMouseAdapter);
        addMouseMotionListener(myMouseAdapter);
    }

    private class MyMouseAdapter extends MouseAdapter {
        private JLabel dragLabel = null;
        private int dragLabelWidthDiv2;
        private int dragLabelHeightDiv2;
        private JPanel clickedPanel = null;
        private JPanel clickedRack = null;

        public void mousePressed(MouseEvent e) {
            clickedPanel = (JPanel) backingPanel.getComponentAt(e.getPoint());
            clickedRack = (JPanel) rackBacking.getComponentAt(e.getPoint());
            System.out.println(rackBacking.getComponentAt(e.getPoint()));
            Component[] components1 = clickedPanel.getComponents();
            Component[] components2 = clickedPanel.getComponents();
            if (components1.length == 0) {
                return;
            }
            if (components2.length == 0) {
                return;
            }
            // if we click on jpanel that holds a jlabel
            if (components1[0] instanceof JLabel) {

                // remove label from panel
                dragLabel = (JLabel) components1[0];
                clickedPanel.remove(dragLabel);
                clickedPanel.revalidate();
                clickedPanel.repaint();

                dragLabelWidthDiv2 = dragLabel.getWidth() / 2;
                dragLabelHeightDiv2 = dragLabel.getHeight() / 2;

                int x = e.getPoint().x - dragLabelWidthDiv2;
                int y = e.getPoint().y - dragLabelHeightDiv2;
                dragLabel.setLocation(x, y);
                add(dragLabel, JLayeredPane.DRAG_LAYER);
                repaint();
            }
        }

        public void mouseDragged(MouseEvent e) {
            if (dragLabel == null) {
                return;
            }
            int x = e.getPoint().x - dragLabelWidthDiv2;
            int y = e.getPoint().y - dragLabelHeightDiv2;
            dragLabel.setLocation(x, y);
            repaint();
        }

        public void mouseReleased(MouseEvent e) {
            if (dragLabel == null) {
                return;
            }
            remove(dragLabel); // remove dragLabel for drag layer of JLayeredPane
            JPanel droppedPanel = (JPanel) backingPanel.getComponentAt(e.getPoint());
            JPanel droppedRack = (JPanel) rackBacking.getComponentAt(e.getPoint());
            if (droppedPanel == null) {
                // if off the grid, return label to home
                clickedPanel.add(dragLabel);
                clickedPanel.revalidate();
            } else {
                int r = -1;
                int c = -1;
                searchPanelGrid: for (int row = 0; row < panelGrid.length; row++) {
                    for (int col = 0; col < panelGrid[row].length; col++) {
                        if (panelGrid[row][col] == droppedPanel) {
                            r = row;
                            c = col;
                            break searchPanelGrid;
                        }
                    }
                }

                if (r < 0 || c < 0) {
                    // if off the grid, return label to home
                    clickedPanel.add(dragLabel);
                    clickedPanel.revalidate();
                } else {
                    droppedPanel.add(dragLabel);
                    droppedPanel.revalidate();
                }
            }
            /*
            if (droppedRack == null) {
                // if off the grid, return label to home
                clickedPanel.add(dragLabel);
                clickedPanel.revalidate();
            } else {
                int r = -1;
                int c = -1;
                searchRackGrid: for (int row = 0; row < rackGrid.length; row++) {
                    for (int col = 0; col < rackGrid[row].length; col++) {
                        if (rackGrid[row][col] == droppedRack) {
                            r = row;
                            c = col;
                            break searchRackGrid;
                        }
                    }
                }

                if (r < 0 || c < 0) {
                    // if off the grid, return label to home
                    clickedPanel.add(dragLabel);
                    clickedPanel.revalidate();
                } else {
                    droppedPanel.add(dragLabel);
                    droppedPanel.revalidate();
                }
            }
*/
            repaint();
            dragLabel = null;
        }

    }

    private static void createAndShowUI() {
        JFrame frame = new JFrame("Scrabble");
        frame.getContentPane().add(new Mouse());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        createAndShowUI();
        /*
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                createAndShowUI();
            }
        });
        */
    }
}