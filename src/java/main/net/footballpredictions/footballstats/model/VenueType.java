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
package net.footballpredictions.footballstats.model;

/**
 * Enumerated type used to filter the matches used to calculate the various stats.
 * Tables can be generated for home matches, away matches or all matches.
 * @author Daniel Dyer
 */
public enum VenueType
{
    HOME("venue_type.home"),
    AWAY("venue_type.away"),
    BOTH("venue_type.both");

    private final String description;

    private VenueType(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return description;
    }
}
