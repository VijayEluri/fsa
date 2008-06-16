// ============================================================================
//   The Football Statistics Applet (http://fsa.footballpredictions.net)
//   � Copyright 2000-2008 Daniel W. Dyer
//
//   This program is free software: you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as published by
//   the Free Software Foundation, either version 3 of the License, or
//   (at your option) any later version.
//
//   This program is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//   GNU General Public License for more details.
//
//   You should have received a copy of the GNU General Public License
//   along with this program.  If not, see <http://www.gnu.org/licenses/>.
// ============================================================================
package net.footballpredictions.footballstats.awt;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.SortedSet;
import net.footballpredictions.footballstats.model.LeagueSeason;
import net.footballpredictions.footballstats.model.Result;
import net.footballpredictions.footballstats.model.Team;

/**
 * AWT panel for displaying attendance tables. 
 * @author Daniel Dyer
 * @since 17/1/2004
 */
public class Attendances implements StatsPanel
{
    private static final String[] TITLES = new String[]{"Average Attendance By Team",
                                                        "Highest Attendance By Team",
                                                        "Lowest Attendance By Team",
                                                        "Aggregate Attendance By Team",
                                                        "Top 20 Highest Attendances",
                                                        "Top 20 Lowest Attendances"};
        
    private LeagueSeason data = null;
    private Theme theme = null;
    private String highlightedTeam = null;
    
    private final Choice typeChoice = new Choice();
    private final Label titleLabel = new Label();
    private final Panel positionsColumn = new Panel(new GridLayout(0, 1));
    private final Panel teamsColumn = new Panel(new GridLayout(0, 1));
    private final Panel attendanceColumn = new Panel(new GridLayout(0, 1));
    
    private Panel controls = null;
    private Panel view = null;
    
    public void setLeagueData(LeagueSeason data, String highlightedTeam)
    {
        this.data = data;
        this.highlightedTeam = highlightedTeam;
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
            Panel innerPanel = new Panel(new GridLayout(2, 1));
            
            innerPanel.add(new Label("Table Type:"));
            typeChoice.add("Team Average");
            typeChoice.add("Team Highest");
            typeChoice.add("Team Lowest");
            typeChoice.add("Team Aggregate");
            typeChoice.add("Highest Attendances");
            typeChoice.add("Lowest Attendances");
            innerPanel.add(typeChoice);
            
            typeChoice.addItemListener(new ItemListener()
            {
                public void itemStateChanged(ItemEvent ev)
                {
                    updateView();
                }
            });
            
            typeChoice.setForeground(Color.black);
            
            controls = Util.borderLayoutWrapper(innerPanel, BorderLayout.NORTH);
        }
        return controls;
    }
    
    
    public Component getView()
    {
        if (view == null)
        {
            view = new Panel(new BorderLayout());
            Panel tablePanel = new Panel(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.weightx = 1.0;
            constraints.weighty = 1.0;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.anchor = GridBagConstraints.NORTH;
            tablePanel.add(positionsColumn, constraints);
            constraints.gridwidth = GridBagConstraints.RELATIVE;
            tablePanel.add(teamsColumn, constraints);
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            tablePanel.add(attendanceColumn, constraints);
            titleLabel.setFont(theme.getTitleFont());
            titleLabel.setAlignment(Label.CENTER);            
            view.add(titleLabel, BorderLayout.NORTH);
            view.add(Util.wrapTable(tablePanel), BorderLayout.CENTER);
            updateView();
        }
        return view;
    }
    
    
    private void updateView()
    {
        if (data != null)
        {
            int type = Math.max(0, typeChoice.getSelectedIndex()); // A cheat really, we know that the indices correspond to the constant values.
            positionsColumn.removeAll();
            teamsColumn.removeAll();
            attendanceColumn.removeAll();
            titleLabel.setText(TITLES[type]);

            switch (type)
            {
                case 4: // Top 20 Highest
                {
                    updateView(data.getHighestAttendances());
                    break;
                }
                case 5: // Top 20 Lowest
                {
                    updateView(data.getLowestAttendances());
                    break;
                }
                default:
                {
                    updateView(data.getAttendanceTable(type), type);
                }
            }

            view.validate();
        }
    }


    private void updateView(SortedSet<Result> attendanceTable)
    {
        int index = 1;
        for (Result result : attendanceTable)
        {
            positionsColumn.add(new Label(String.valueOf(index), Label.CENTER));
            Label textLabel = new Label(result.getHomeTeam().getName() + " vs. " + result.getAwayTeam().getName() + " (" + theme.getShortDateFormat().format(
                    result.getDate()) + ")");
            if (result.getHomeTeam().getName().equals(highlightedTeam))
            {
                textLabel.setFont(theme.getBoldFont());
            }
            Label valueLabel = new Label(String.valueOf(result.getAttendance()), Label.CENTER);
            teamsColumn.add(textLabel);
            valueLabel.setFont(theme.getBoldFont());
            attendanceColumn.add(valueLabel);
            ++index;
        }
    }


    private void updateView(SortedSet<Team> attendanceTable, int type)
    {
        int index = 1;
        for (Team team : attendanceTable)
        {
            positionsColumn.add(new Label(String.valueOf(index), Label.CENTER));
            Label textLabel = new Label(team.getName());
            if (team.getName().equals(highlightedTeam))
            {
                textLabel.setFont(theme.getBoldFont());
            }
            Label valueLabel = new Label(String.valueOf(team.getAttendanceRecord(type)), Label.CENTER);
            teamsColumn.add(textLabel);
            valueLabel.setFont(theme.getBoldFont());
            attendanceColumn.add(valueLabel);
            ++index;
        }
    }
}