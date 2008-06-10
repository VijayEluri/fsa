// $Header: $
package net.footballpredictions.footballstats.awt;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;
import net.footballpredictions.footballstats.model.LeagueSeason;
import net.footballpredictions.footballstats.model.Team;

/**
 * @author Daniel Dyer
 * @since 18/1/2004
 * @version $Revision: $
 */
public class Graphs implements StatsPanel
{
    private LeagueSeason data = null;
    private Theme theme = null;

    private final Label titleLabel = new Label("", Label.CENTER);
    private final Choice typeChoice = new Choice();
    private final Choice[] teamChoices = new Choice[]{new Choice(), new Choice(), new Choice()};
    private final Checkbox[] teamCheckboxes = new Checkbox[]{new Checkbox("Team 1:"), new Checkbox("Team 2:"), new Checkbox("Team 3:")};
    
    private Panel controls = null;
    private Panel view = null;

    public void setLeagueData(LeagueSeason data, String highlightedTeam)
    {
        this.data = data;

        for (int i = 0; i < teamChoices.length; i++)
        {
            teamChoices[i].removeAll();
            teamCheckboxes[i].setState(i == 0);
            teamChoices[i].setEnabled(i == 0);
        }
        String[] teamNames = data.getTeamNames();
        for (int i = 0; i < teamNames.length; i++)
        {
            teamChoices[0].add(teamNames[i]);
            teamChoices[1].add(teamNames[i]);
            teamChoices[2].add(teamNames[i]);
        }
        if (highlightedTeam != null)
        {
            teamChoices[0].select(highlightedTeam);
        }
        
        int index = -1;
        do 
        {
            index++;
        }
        while (teamChoices[1].getItem(index).equals(teamChoices[0].getSelectedItem()));
        teamChoices[1].select(index);
        
        index = -1;
        do 
        {
            index++;
        }
        while (teamChoices[2].getItem(index).equals(teamChoices[0].getSelectedItem()) || teamChoices[2].getItem(index).equals(teamChoices[1].getSelectedItem()));
        teamChoices[2].select(index);
        
        
        if (view != null)
        {
            updateView();
        }
    }
    
    
    public void setTheme(Theme theme)
    {
        this.theme = theme;
    }
    
    
    public Component getControls()
    {
        if (controls == null)
        {
            ItemListener itemListener = new ItemListener()
            {
                public void itemStateChanged(ItemEvent ev)
                {
                    for (int i = 0; i < teamChoices.length; i++)
                    {
                        teamChoices[i].setEnabled(teamCheckboxes[i].getState());
                    }
                    updateView();
                }
            };

            Panel innerPanel = new Panel(new GridLayout(9, 1));
            innerPanel.add(new Label("Graph Type"));
            typeChoice.add("League Position");
            typeChoice.add("Total Points");
            innerPanel.add(typeChoice);
            innerPanel.add(new Label()); // Blank Row
            for (int i = 0; i < teamChoices.length; i++)
            {
                innerPanel.add(teamCheckboxes[i]);
                innerPanel.add(teamChoices[i]);
                teamChoices[i].addItemListener(itemListener);
                teamChoices[i].setForeground(Color.black);
                teamCheckboxes[i].addItemListener(itemListener);
                teamCheckboxes[i].setForeground(theme.getGraphColour(i));
            }
            controls = Util.borderLayoutWrapper(innerPanel, BorderLayout.NORTH);
            typeChoice.addItemListener(itemListener);
            typeChoice.setForeground(Color.black);
        }
        return controls;
    }
    
    
    public Component getView()
    {
        if (view == null)
        {
            view = new Panel(new BorderLayout());
            titleLabel.setFont(theme.getTitleFont());
            view.add(titleLabel, BorderLayout.NORTH);
            updateView();
        }
        return view;
    }
    
    
    private void updateView()
    {
        if (data != null)
        {
            if (view.getComponentCount() > 1)
            {
                view.remove(1);
            }
            
            int maxX = 0;
            int maxY = 0;
            Vector plots = new Vector(teamChoices.length);
            for (int i = 0; i < teamChoices.length; i++)
            {
                if (teamChoices[i].isEnabled())
                {
                    String teamName = teamChoices[i].getSelectedItem();
                    if (typeChoice.getSelectedIndex() <= 0)
                    {
                        Team.LeaguePosition[] positions = data.getTeam(teamName).getLeaguePositions();
                        int[][] points = new int[positions.length][2];
                        for (int j = 0; j < positions.length; j++)
                        {
                            points[j][0] = j;
                            points[j][1] = positions[j].position;
                        }
                        maxX = Math.max(maxX, points.length - 1);
                        maxY = Math.max(maxY, data.getTeamNames().length);
                        plots.addElement(points);
                    }
                    else if (typeChoice.getSelectedIndex() == 1)
                    {
                        int[][] points = data.getTeam(teamName).getPointsData(data.getPointsForWin(),
                                                                              data.getPointsForDraw());
                        maxX = Math.max(maxX, points.length - 1);
                        maxY = Math.max(maxY, data.getHighestPointsTotal());
                        plots.addElement(points);
                    }
                }
                else
                {
                    plots.addElement(null);
                }
            }
            
            Graph graph = null;
            if (typeChoice.getSelectedIndex() <= 0)
            {
                graph = new Graph(maxX, maxY, true);
                graph.setYLabels("1", String.valueOf(maxY));
                titleLabel.setText("League Positions By Date");
            }
            else if (typeChoice.getSelectedIndex() == 1)
            {
                graph = new Graph(maxX, maxY, false);
                graph.setYLabels("0", String.valueOf(maxY));
                titleLabel.setText("Total Points After Each Match");
            }
            for (int i = 0; i < plots.size(); i++)
            {
                int[][] plot = (int[][]) plots.elementAt(i);
                if (plot != null)
                {
                    graph.addSeries(plot, theme.getGraphColour(i));
                }
            }
            graph.setFont(theme.getSmallFont());
            graph.setForeground(theme.getMainViewTextColour());
            view.add(graph, BorderLayout.CENTER);
            view.validate();
        }
    }
    
    
    /**
     * Graph component class.
     */
    private static final class Graph extends Canvas
    {
        private final Insets insets = new Insets(20, 20, 10, 10);
        private final Vector plots = new Vector();
        private final Vector colours = new Vector();
        private final boolean inverted;
        private int maxX, maxY;
        private double scaleX, scaleY;
        
        private String minXLabel, maxXLabel, minYLabel, maxYLabel;
        
        public Graph(int maxX, int maxY, boolean inverted)
        {
            this.maxX = maxX;
            this.maxY = maxY;
            if (inverted)
            {
                this.maxY--;
            }
            this.inverted = inverted;
        }
        
        public void addSeries(int[][] points, Color colour)
        {
            plots.addElement(points);
            colours.addElement(colour);
        }
        
        
        public void setXLabels(String min, String max)
        {
            this.minXLabel = min;
            this.maxXLabel = max;
        }
        
        
        public void setYLabels(String min, String max)
        {
            this.minYLabel = min;
            this.maxYLabel = max;
        }
                
        public void paint(Graphics graphics)
        {
            if (plots.size() > 0)
            {
                Dimension size = getSize();
                scaleX = (double) (size.width - insets.left - insets.right) / maxX;
                scaleY = (double) (size.height - insets.top - insets.bottom) / maxY;

                // Draw plots.
                for (int i = 0; i < plots.size(); i++)
                {
                    int[][] plot = (int[][]) plots.elementAt(i);
                    graphics.setColor((Color) colours.elementAt(i));
                    Point lastPoint = mapToGraphCoordinates(plot[0][0], plot[0][1], inverted);
                    for (int j = 1; j < plot.length; j++)
                    {
                        Point point = mapToGraphCoordinates(plot[j][0], plot[j][1], inverted);
                        graphics.drawLine(lastPoint.x, lastPoint.y, point.x, point.y);
                        lastPoint = point;
                    }
                }
                
                graphics.setColor(getForeground());
                // Draw axes (after plots so that they are on top).
                Point origin = mapToGraphCoordinates(0, 0, false);
                Point maxXPoint = mapToGraphCoordinates(maxX, 0, false);
                Point maxYPoint = mapToGraphCoordinates(0, maxY, false);
                graphics.drawLine(origin.x, origin.y, maxXPoint.x, maxXPoint.y);
                graphics.drawLine(origin.x, origin.y, maxYPoint.x, maxYPoint.y);
            
                FontMetrics metrics = graphics.getFontMetrics();
                // Draw labels.
                if (maxYLabel != null)
                {
                    Point labelPoint = mapToGraphCoordinates(0, inverted ? maxY + 1 : maxY, inverted);
                    graphics.drawString(maxYLabel, insets.left - metrics.stringWidth(maxYLabel) - 1, labelPoint.y + (getFont().getSize() / 2));
                }
                if (minYLabel != null)
                {
                    Point labelPoint = mapToGraphCoordinates(0, inverted ? 1 : 0, inverted);
                    graphics.drawString(minYLabel, insets.left - metrics.stringWidth(minYLabel) - 1, labelPoint.y + (getFont().getSize() / 2));
                }
            }
        }

        
        private Point mapToGraphCoordinates(int x, int y, boolean inverted)
        {
            int graphX = (int) Math.round(scaleX * x + insets.left);
            int yValue = inverted ? y - 1 : maxY - y;
            int graphY = (int) Math.round(scaleY * yValue + insets.bottom);
            return new Point(graphX, graphY);
        }
    }
}