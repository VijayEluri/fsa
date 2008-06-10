// $Header: $
package net.footballpredictions.footballstats.awt;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Vector;

/**
 * Class for combining together all customisable GUI data such as colours and fonts.
 * @author Daniel Dyer
 * @since 30/12/2003
 * @version $Revision: $
 */
public class Theme
{
    private final Color buttonColour;
    private final Color buttonTextColour;
    private final Color selectedButtonColour;
    private final Color topBarColour;
    private final Color topBarTextColour;
    private final Color mainViewColour;
    private final Color mainViewTextColour;
    private final Color controlPanelColour;
    private final Color controlPanelTextColour;

    private final Color[] prizeColours;
    private final Color[] relegationColours;

    private final Color winColour;
    private final Color drawColour;
    private final Color defeatColour;
    
    private final Color positiveGDColour;
    private final Color negativeGDColour;
    
    private final Color noteColour;
    
    private final Color[] graphColours;

    // Fonts are not configurable at present (not sure it's a good idea to let people change them).
    private final Font titleFont = new Font("SansSerif", Font.BOLD, 14);
    private final Font boldFont = new Font("SansSerif", Font.BOLD, 12);
    private final Font plainFont = new Font("SansSerif", Font.PLAIN, 12);
    private final Font smallFont = new Font("SansSerif", Font.PLAIN, 10);
    private final Font smallBoldFont = new Font("SansSerif", Font.BOLD, 10);
    private final Font fixedWidthFont = new Font("Monospaced", Font.PLAIN, 12);

    // Date and number formats are also not presently configurable.
    private final SimpleDateFormat longDateFormat = new SimpleDateFormat("EEE d MMM yyyy");
    private final SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    
    public Theme(URL themeURL) throws IOException
    {
        // Load theme file.
        Properties themeProperties = new Properties();
        themeProperties.load(themeURL.openStream());
        
        // Configure control colours.
        buttonColour = convertToColour(themeProperties.getProperty("colour.button"));
        buttonTextColour = convertToColour(themeProperties.getProperty("colour.button.text"));
        selectedButtonColour = convertToColour(themeProperties.getProperty("colour.button.selected"));
        controlPanelColour = convertToColour(themeProperties.getProperty("colour.controlpanel"));
        controlPanelTextColour = convertToColour(themeProperties.getProperty("colour.controlpanel.text"));
        topBarColour = convertToColour(themeProperties.getProperty("colour.topbar"));
        topBarTextColour = convertToColour(themeProperties.getProperty("colour.topbar.text"));
        mainViewColour = convertToColour(themeProperties.getProperty("colour.mainview"));
        mainViewTextColour = convertToColour(themeProperties.getProperty("colour.mainview.text"));
        
        // Configure result/goal difference colours.
        defeatColour = convertToColour(themeProperties.getProperty("colour.defeat"));
        drawColour = convertToColour(themeProperties.getProperty("colour.draw"));
        winColour = convertToColour(themeProperties.getProperty("colour.win"));
        negativeGDColour = convertToColour(themeProperties.getProperty("colour.goaldiff.negative"));
        positiveGDColour = convertToColour(themeProperties.getProperty("colour.goaldiff.positive"));
        
        // Configure prize colours.
        Vector colours = new Vector();
        int index = 1;
        String colourString = themeProperties.getProperty("colour.prize" + index);
        while (colourString != null)
        {
            colours.addElement(convertToColour(colourString));
            index++;
            colourString = themeProperties.getProperty("colour.prize" + index);
        }
        prizeColours = new Color[colours.size()];
        colours.copyInto(prizeColours);
        
        // Configure relegation colours.
        colours.setSize(0);
        index = 1;
        colourString = themeProperties.getProperty("colour.relegation" + index);
        while (colourString != null)
        {
            colours.addElement(convertToColour(colourString));
            index++;
            colourString = themeProperties.getProperty("colour.relegation" + index);
        }
        relegationColours = new Color[colours.size()];
        colours.copyInto(relegationColours);
        
        // Notes colour.
        noteColour = convertToColour(themeProperties.getProperty("colour.notes"));
        
        // Configure graph colours.
        graphColours = new Color[]{convertToColour(themeProperties.getProperty("colour.graph1")),
                                   convertToColour(themeProperties.getProperty("colour.graph2")),
                                   convertToColour(themeProperties.getProperty("colour.graph3"))};
    }
    
    
    private Color convertToColour(String hexString)
    {
        int red = Integer.parseInt(hexString.substring(0, 2), 16);
        int green = Integer.parseInt(hexString.substring(2, 4), 16);
        int blue = Integer.parseInt(hexString.substring(4), 16);
        return new Color(red, green, blue);
    }


    public Color getButtonColour()
    {
        return buttonColour;
    }
    
    
    public Color getSelectedButtonColour()
    {
        return selectedButtonColour;
    }
    
    
    public Color getButtonTextColour()
    {
        return buttonTextColour;
    }
    
    
    public Color getTopBarColour()
    {
        return topBarColour;
    }
    
    
    public Color getTopBarTextColour()
    {
        return topBarTextColour;
    }
    
    
    public Color getMainViewColour()
    {
        return mainViewColour;
    }
    
    
    public Color getMainViewTextColour()
    {
        return mainViewTextColour;
    }
    
    
    public Color getControlPanelColour()
    {
        return controlPanelColour;
    }
    
    
    public Color getControlPanelTextColour()
    {
        return controlPanelTextColour;
    }
    
    
    public Color getWinColour()
    {
        return winColour;
    }
    
    
    public Color getDrawColour()
    {
        return drawColour;
    }
    
    
    public Color getDefeatColour()
    {
        return defeatColour;
    }
    
    
    public Color getGoalDifferenceColour(int goalDifference)
    {
        if (goalDifference > 0)
        {
            return positiveGDColour;
        }
        else if (goalDifference < 0)
        {
            return negativeGDColour;
        }
        return mainViewTextColour;
    }
    
    
    public Color getNoteColour()
    {
        return noteColour;
    }
    
    
    public Color getZoneColour(int zoneID)
    {
        if (zoneID == 0)
        {
            return null;
        }
        else if (zoneID > 0)
        {
            return prizeColours[--zoneID];
        }
        else
        {
            zoneID = Math.abs(zoneID);
            return relegationColours[--zoneID];
        }
    }
    
    
    public Color getGraphColour(int index)
    {
        return graphColours[index];
    }
    
    
    public Font getTitleFont()
    {
        return titleFont;
    }
    
    
    public Font getBoldFont()
    {
        return boldFont;
    }
    
    
    public Font getPlainFont()
    {
        return plainFont;
    }
    
    
    public Font getSmallFont()
    {
        return smallFont;
    }
    
    
    public Font getSmallBoldFont()
    {
        return smallBoldFont;
    }

    
    public Font getFixedWidthFont()
    {
        return fixedWidthFont;
    }

    
    public DateFormat getLongDateFormat()
    {
        return longDateFormat;
    }
    
    
    public DateFormat getShortDateFormat()
    {
        return shortDateFormat;
    }
    
    
    public DecimalFormat getDecimalFormat()
    {
        return decimalFormat;
    }
}