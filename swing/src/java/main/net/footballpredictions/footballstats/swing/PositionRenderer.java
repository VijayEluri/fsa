// ============================================================================
//   The Football Statistics Applet (http://fsa.footballpredictions.net)
//   © Copyright 2000-2010 Daniel W. Dyer
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
package net.footballpredictions.footballstats.swing;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import net.footballpredictions.footballstats.model.LeagueMetaData;

/**
 * Customised {@link TableRenderer} that formats position labels.
 * @author Daniel Dyer
 */
class PositionRenderer extends TableRenderer
{
    /**
     * @param metadata League metadata is used to determine row colours.
     * @param highlightZones Whether or not to render promotion and relegation zones
     * in different colours to other positions.
     */
    public PositionRenderer(LeagueMetaData metadata, boolean highlightZones)
    {
        super(metadata, highlightZones, false);
    }


    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column)
    {
        JLabel component = (JLabel) super.getTableCellRendererComponent(table,
                                                                        value,
                                                                        isSelected,
                                                                        hasFocus,
                                                                        row,
                                                                        column);
        component.setText(component.getText() + ". ");
        return component;
    }
}
