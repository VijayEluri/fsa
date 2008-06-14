package net.footballpredictions.footballstats.awt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.text.DecimalFormat;
import net.footballpredictions.footballstats.model.LeagueSeason;
import net.footballpredictions.footballstats.model.Result;
import net.footballpredictions.footballstats.model.Team;
import net.footballpredictions.footballstats.model.TeamRecord;

/**
 * Sub-panel of Head-to-Head display.
 * @author Daniel Dyer.
 */
final class TeamRecordPanel extends Panel
{
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.00");
    
    private LeagueSeason data;
    private Theme theme;
    
    private final Label nameLabel = new Label("", Label.CENTER);
    private final Label positionLabel = new Label("", Label.CENTER);
    private final Label playingRecordTitleLabel = new Label();
    private final Label playedLabel = new Label();
    private final Label wonLabel = new Label();
    private final Label wonPercentageLabel = new Label();
    private final Label drawnLabel = new Label();
    private final Label drawnPercentageLabel = new Label();
    private final Label lostLabel = new Label();
    private final Label lostPercentageLabel = new Label();
    private final Label gdLabel = new Label();
    private final Label gdDetailsLabel = new Label();
    private final Label pointsLabel = new Label();
    private final Label pointsAverageLabel = new Label();
    private final Label formLabel = new Label();
    private final Label bigWinTitleLabel = new Label();
    private final Label bigDefeatTitleLabel = new Label();
    private final Label mostRecentTitleLabel = new Label();
    private final Label bigWinLabel = new Label();
    private final Label bigDefeatLabel = new Label();
    private final Label mostRecentLabel = new Label();

    private final Label[] notesLabels = new Label[3];
        
    public TeamRecordPanel()
    {
        super(new BorderLayout());

        // Header.
        Panel header = new Panel(new BorderLayout());
        header.add(nameLabel, BorderLayout.NORTH);
        header.add(positionLabel, BorderLayout.CENTER);
        header.add(playingRecordTitleLabel, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);
            
        // Footer.
        Panel notesPanel = new Panel(new GridLayout(3, 1));
        for (int i = 0; i < notesLabels.length; i++)
        {
            notesLabels[i] = new Label("");
            notesPanel.add(notesLabels[i]);
        }
        add(notesPanel, BorderLayout.SOUTH);
            
        // Main stats.
        Panel mainPanel = new Panel(new GridLayout(0, 2));
        mainPanel.add(new Label("Played:"));
        mainPanel.add(playedLabel);
        mainPanel.add(new Label("Won:"));
        mainPanel.add(Util.wrapLabelPair(wonLabel, wonPercentageLabel));
        mainPanel.add(new Label("Drawn:"));
        mainPanel.add(Util.wrapLabelPair(drawnLabel, drawnPercentageLabel));
        mainPanel.add(new Label("Lost:"));
        mainPanel.add(Util.wrapLabelPair(lostLabel, lostPercentageLabel));
        mainPanel.add(new Label("Goal Difference:"));
        mainPanel.add(Util.wrapLabelPair(gdLabel, gdLabel));
        mainPanel.add(new Label("Points:"));
        mainPanel.add(Util.wrapLabelPair(pointsLabel, pointsAverageLabel));
        mainPanel.add(new Label("Form:"));
        mainPanel.add(formLabel);

        // Key results.
        Panel resultsPanel = new Panel(new GridLayout(0, 1));
        resultsPanel.add(bigWinTitleLabel);
        resultsPanel.add(bigWinLabel);
        resultsPanel.add(bigDefeatTitleLabel);
        resultsPanel.add(bigDefeatLabel);
        resultsPanel.add(mostRecentTitleLabel);
        resultsPanel.add(mostRecentLabel);
            
        Panel statsWrapper = new Panel(new GridLayout(2, 1));
        statsWrapper.add(mainPanel);
        statsWrapper.add(resultsPanel);
        add(statsWrapper, BorderLayout.CENTER);
    }
    
    
    public void setLeagueData(LeagueSeason data)
    {
        this.data = data;
    }
    
    
    public void setTheme(Theme theme)
    {
        this.theme = theme;
        nameLabel.setFont(theme.getTitleFont());
        positionLabel.setFont(theme.getSmallFont());
        playingRecordTitleLabel.setFont(theme.getBoldFont());
        pointsLabel.setFont(theme.getBoldFont());
        formLabel.setFont(theme.getFixedWidthFont());
        bigWinTitleLabel.setFont(theme.getBoldFont());
        bigDefeatTitleLabel.setFont(theme.getBoldFont());
        mostRecentTitleLabel.setFont(theme.getBoldFont());
        for (Label notesLabel : notesLabels)
        {
            notesLabel.setForeground(theme.getNoteColour());
        }
    }

        
    public void setTeam(Team team, int where)
    {
        nameLabel.setText(team.getName());
        int pos = team.getLastLeaguePosition();
        positionLabel.setText("Current league position: " + pos + getSuffix(pos));
        String venueText = getVenueText(where);
        playingRecordTitleLabel.setText(venueText + " Playing Record");
        if (where == TeamRecord.BOTH)
        {
            bigWinTitleLabel.setText("Biggest Win");
            bigDefeatTitleLabel.setText("Biggest Defeat");
            mostRecentTitleLabel.setText("Most Recent Result");
        }
        else
        {
            bigWinTitleLabel.setText("Biggest " + venueText + " Win");
            bigDefeatTitleLabel.setText("Biggest " + venueText + " Defeat");
            mostRecentTitleLabel.setText("Most Recent " + venueText + " Result");
        }
            
        int played = team.getAggregate(where, TeamRecord.AGGREGATE_PLAYED);
        playedLabel.setText(String.valueOf(played));
        wonLabel.setText(String.valueOf(team.getAggregate(where, TeamRecord.AGGREGATE_WON)));
        wonPercentageLabel.setText("(" + DECIMAL_FORMAT.format(getPercentage(team.getAggregate(where, TeamRecord.AGGREGATE_WON), played)) + "%)");
        drawnLabel.setText(String.valueOf(team.getAggregate(where, TeamRecord.AGGREGATE_DRAWN)));
        drawnPercentageLabel.setText("(" + DECIMAL_FORMAT.format(getPercentage(team.getAggregate(where, TeamRecord.AGGREGATE_DRAWN), played)) + "%)");
        lostLabel.setText(String.valueOf(team.getAggregate(where, TeamRecord.AGGREGATE_LOST)));
        lostPercentageLabel.setText("(" + DECIMAL_FORMAT.format(getPercentage(team.getAggregate(where, TeamRecord.AGGREGATE_LOST), played)) + "%)");
        int gd = team.getRecord(where).getGoalDifference();
        gdLabel.setText(gd > 0 ? "+" + gd : String.valueOf(gd));
        gdLabel.setForeground(theme.getGoalDifferenceColour(gd));
        gdDetailsLabel.setText("(F" + team.getAggregate(where, TeamRecord.AGGREGATE_SCORED) + ", A" + team.getAggregate(where, TeamRecord.AGGREGATE_CONCEDED) + ")");
        int points = data.getPoints(where, team.getRecord(where));
        pointsLabel.setText(String.valueOf(points));
        pointsAverageLabel.setText("(Av. " + DECIMAL_FORMAT.format(((double) points) / played) + ")");
        formLabel.setText(team.getForm(where));
            
        Result bigWin = team.getKeyResult(where, Team.BIGGEST_WIN);
        Result bigDefeat = team.getKeyResult(where, Team.BIGGEST_DEFEAT);
        Result mostRecent = team.getKeyResult(where, Team.LAST_RESULT);
        bigWinLabel.setText(resultAsString(team, bigWin));
        bigWinLabel.setForeground(getResultColour(team, bigWin));
        bigDefeatLabel.setText(resultAsString(team, bigDefeat));
        bigDefeatLabel.setForeground(getResultColour(team, bigDefeat));
        mostRecentLabel.setText(resultAsString(team, mostRecent));
        mostRecentLabel.setForeground(getResultColour(team, mostRecent));
            
        for (int i = 0; i < notesLabels.length; i++)
        {
            String[] notes = team.getNotes(where);
            notesLabels[i].setText(i < notes.length ? notes[i] : "");
        }
    }
        
        
    private String getVenueText(int venue)
    {
        if (venue == TeamRecord.HOME)
        {
            return "Home";
        }
        else if (venue == TeamRecord.AWAY)
        {
            return "Away";
        }
        return "Overall";
    }
            
            
    private String resultAsString(Team team, Result result)
    {
        if (result == null)
        {
            return "N/A";
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(result.getGoalsFor(team));
        buffer.append('-');
        buffer.append(result.getGoalsAgainst(team));
        if (result.getHomeTeam().equals(team))
        {
            buffer.append(" v ");
            buffer.append(result.getAwayTeam().getName());
        }
        else
        {
            buffer.append(" at ");
            buffer.append(result.getHomeTeam().getName());
        }
        return buffer.toString();
    }
        
        
    private Color getResultColour(Team team, Result result)
    {
        if (result == null)
        {
            return theme.getMainViewTextColour();
        }
        else if (result.isWin(team))
        {
            return theme.getWinColour();
        }
        else if (result.isDefeat(team))
        {
            return theme.getDefeatColour();
        }
        return theme.getDrawColour();
    }
        
        
    private String getSuffix(int pos)
    {
        String suffix = "th";
        if (pos < 10 || pos > 20)
        {
            switch (pos % 10)
            {
                case 1:
                {
                    suffix = "st";
                    break;
                }
                case 2:
                {
                    suffix = "nd";
                    break;
                }
                case 3:
                {
                    suffix = "rd";
                    break;
                }
                default: suffix = "th";
            }
        }
        return suffix;
    }

        
    private double getPercentage(int numerator, int denominator)
    {
        return denominator == 0 ? 0 : (((double) numerator) / denominator) * 100;
    }
}
