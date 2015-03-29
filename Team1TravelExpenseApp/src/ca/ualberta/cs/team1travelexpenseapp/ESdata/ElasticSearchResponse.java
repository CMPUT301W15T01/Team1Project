//from https://github.com/rayzhangcl/ESDemo March 27 2015
package ca.ualberta.cs.team1travelexpenseapp.ESdata;

public class ElasticSearchResponse<T> {
    String _index;
    String _type;
    String _id;
    int _version;
    boolean exists;
    T _source;
    double max_score;
    public T getSource() {
        return _source;
    }
}
