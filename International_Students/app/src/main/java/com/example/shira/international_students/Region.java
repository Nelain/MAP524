package com.example.shira.international_students;

/**
 * Created by Kelan Bo Li on 2015-08-11.
 * data model for each country of origin or destination province
 * Since country and province have same members, they share the same class
 */
public class Region {

    private String _name;
    private String _iso;
    private int _2013_q1;
    private int _2013_q2;
    private int _2013_q3;
    private int _2013_q4;
    private int _2013_total;
    private int _2014_q1;
    private int _2014_q2;
    private int _2014_q3;
    private int _2014_q4;
    private int _2014_total;
    private int _2013_rank;
    private int _2014_rank;

    public Region (String name, String iso) {
        _name = name;
        _iso = iso;
    }

    public Region (String name, String iso,
                    int _2013_q1, int _2013_q2, int _2013_q3, int _2013_q4, int _2013_total,
                    int _2014_q1, int _2014_q2, int _2014_q3, int _2014_q4, int _2014_total,
                    int _2013_rank, int _2014_rank) {
        _name = name;
        _iso = iso;
        this._2013_q1 = _2013_q1;
        this._2013_q2 = _2013_q2;
        this._2013_q3 = _2013_q3;
        this._2013_q4 = _2013_q4;
        this._2013_total = _2013_total;
        this._2014_q1 = _2014_q1;
        this._2014_q2 = _2014_q2;
        this._2014_q3 = _2014_q3;
        this._2014_q4 = _2014_q4;
        this._2014_total = _2014_total;
        this._2013_rank = _2013_rank;
        this._2014_rank = _2014_rank;
    }

    public String get_name() { return _name; };
    public String get_iso() { return _iso; };
    public int get_2013_q1() { return _2013_q1; };
    public int get_2013_q2() { return _2013_q2; };
    public int get_2013_q3() { return _2013_q3; };
    public int get_2013_q4() { return _2013_q4; };
    public int get_2013_total() { return _2013_total; };
    public int get_2014_q1() { return _2014_q1; };
    public int get_2014_q2() { return _2014_q2; };
    public int get_2014_q3() { return _2014_q3; };
    public int get_2014_q4() { return _2014_q4; };
    public int get_2014_total() { return _2014_total; };
    public int get_2013_rank() { return _2013_rank; };
    public int get_2014_rank() { return _2014_rank; };

}
