package HW1;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * A JDailog GUI for choosing a GeoSegemnt and adding it to the route shown
 * by RoutDirectionGUI.
 * <p>
 * A figure showing this GUI can be found in homework assignment #1.
 */
public class GeoSegmentsDialog extends JDialog implements ActionListener
{

    private static final long serialVersionUID = 1L;

    // the RouteDirectionsGUI that this JDialog was opened from
    private RouteFormatterGUI parent;
    private Route parentRoute;

    // a control contained in this
    private JList<GeoSegment> lstSegments;

    // the label
    private JLabel lblSegments;
    // the scroll panel
    private JScrollPane scrlSegments;

    // the buttons
    private JButton cancelButton;
    private JButton addSegmentButton;

    /**
     * Creates a new GeoSegmentsDialog JDialog.
     *
     * @effects Creates a new GeoSegmentsDialog JDialog with owner-frame
     * owner and parent pnlParent
     */
    public GeoSegmentsDialog(Frame owner, RouteFormatterGUI pnlParent)
    {
        // create a modal JDialog with the an owner Frame (a modal window
        // in one that doesn't allow other windows to be active at the
        // same time).
        super(owner, "Please choose a GeoSegment", true);

        this.parent = pnlParent;

        // create components
        lstSegments = new JList<GeoSegment>(ExampleGeoSegments.segments);
        lstSegments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrlSegments = new JScrollPane(lstSegments);
        scrlSegments.setPreferredSize(new Dimension(450, 100));

        lblSegments = new JLabel("GeoSegments:");
        lblSegments.setLabelFor(lstSegments);

        addSegmentButton = new JButton("Add");
        addSegmentButton.addActionListener(this);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        // arrange components on grid
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(gridbag);

        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.insets = new Insets(0, 0, 0, 0);
        gridbag.setConstraints(lblSegments, c);
        this.add(lblSegments);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 5;
        c.gridheight = 5;
        c.insets = new Insets(0, 0, 0, 0);
        gridbag.setConstraints(scrlSegments, c);
        this.add(scrlSegments);

        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.insets = new Insets(0, 20, 0, 0);
        c.anchor = GridBagConstraints.SOUTH;
        gridbag.setConstraints(addSegmentButton, c);
        this.add(addSegmentButton);

        c.gridx = 3;
        c.gridy = 6;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.insets = new Insets(0, 280, 0, 0);
        c.anchor = GridBagConstraints.SOUTH;
        gridbag.setConstraints(cancelButton, c);
        this.add(cancelButton);

    }

    /**
     * A semantic event which indicates that a component-defined action occurred.
     * This high-level event is generated by a component (such as a
     * <code>Button</code>) when the component-specific action occurs (such as being pressed).
     * The event is passed to every <code>ActionListener</code> object
     * that registered to receive such events using the component's.
     *
     * @modifies lstSegments
     * @effects actionPerformed is making the changes of the event as the dialog requests it
     */
    public void actionPerformed(ActionEvent e)
    {
        assert e != null && e.getSource() instanceof JButton;

        JButton button = (JButton) (e.getSource());

        if (button.equals(this.addSegmentButton))
        {
            GeoSegment selectedGS = this.lstSegments.getSelectedValue();
            this.parentRoute = this.parent.getRoute();

            if (this.parentRoute != null)
            {
                // Check if the GeoSegment is can be added to the last segment
                if (!this.parentRoute.getEnd().equals(selectedGS.getP1()))
                {
                    // Do nothing as the segment can't be added.
                    return;
                }
            }

            this.lstSegments.clearSelection();
            this.setVisible(false);
            this.parent.addSegment(selectedGS);
        }
        else // Cancel button.
        {
            this.lstSegments.clearSelection();
            this.setVisible(false);
        }
    }
}
