//from https://github.com/rayzhangcl/ESDemo March 27 2015
package ca.ualberta.cs.team1travelexpenseapp.ESdata;

import java.util.Collection;


public class Hits<T> {
    int total;
    double max_score;
    Collection<ElasticSearchResponse<T>> hits;
    public Collection<ElasticSearchResponse<T>> getHits() {
        return hits;
    }
    public String toString() {
        return (super.toString()+","+total+","+max_score+","+hits);
    }
}