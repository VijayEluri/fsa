package net.footballpredictions.footballstats.model;

/**
 * Comparator for sorting a set of teams by a particular sequences statistic.
 * @author Daniel Dyer
 */
final class SequenceComparator implements TeamComparator
{
    private int when, where, sequence;
        
    public void configure(int when, int where, int sequence)
    {
        this.when = when;
        this.where = where;
        this.sequence = sequence;
    }
        
    public int compareTeams(Team team1, Team team2)
    {
        int compare = team2.getSequence(when, where, sequence) - team1.getSequence(when, where, sequence); // Swap teams for descending sort.
        if (compare == 0)
        {
            // If records are the same, sort on alphabetical order.
            compare = team1.getName().toLowerCase().compareTo(team2.getName().toLowerCase());
        }
        return compare;
    }
}