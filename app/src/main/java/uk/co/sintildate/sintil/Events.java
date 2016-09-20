package uk.co.sintildate.sintil;

public class Events {

    private int _id;
    private String _eventname;
    private String _eventinfo;
    private int _direction;
    private int _evtime;
    private String _evstatus;
    private String _evtype;
    private int _incmin;
    private int _incsec;
    private int _dayyears;
    private String _bgimage;
    private String _timeunits;
    private int _bgcolor;

    // empty constructor
    public Events() {
    }

    // constructor
    public Events(int id, String eventname) {
        this._id = id;
        this._eventname = eventname;
    }
    // constructor
    public Events(int id, String eventname, String eventinfo, int direction, int evtime, String evstatus,
                  String evtype, int incmin, int incsec, int dayyears, String bgimage, String tunits, int bgcolor) {
        this._id = id;
        this._eventname = eventname;
        this._eventinfo = eventinfo;
        this._direction = direction;
        this._evtime = evtime;
        this._evstatus = evstatus;
        this._evtype = evtype;
        this._incmin = incmin;
        this._incsec = incsec;
        this._dayyears = dayyears;
        this._bgimage = bgimage;
        this._timeunits = tunits;
        this._bgcolor = bgcolor;
    }
    // constructor
    public Events(String eventname, String eventinfo, int direction, int evtime, String evstatus,
                  String  evtype, int incmin, int incsec, int dayyears, String bgimage, String tunits, int bgcolor) {
        this._eventname = eventname;
        this._eventinfo = eventinfo;
        this._direction = direction;
        this._evtime = evtime;
        this._evstatus = evstatus;
        this._evtype = evtype;
        this._incmin = incmin;
        this._incsec = incsec;
        this._dayyears = dayyears;
        this._bgimage = bgimage;
        this._timeunits = tunits;
        this._bgcolor = bgcolor;

    }

    public int get_id() {
        return _id;
    }
    public String get_eventname() {
        return _eventname;
    }
    public String get_eventinfo() {
        return _eventinfo;
    }
    public int get_direction() {
        return _direction;
    }
    public int get_dayyears() {
        return _dayyears;
    }
    public int get_evtime() {
        return _evtime;
    }
    public String get_evstatus() {
        return _evstatus;
    }
    public String get_type() {
        return _evtype;
    }
    public int get_incmin() {
        return _incmin;
    }
    public int get_incsec() {
        return _incsec;
    }
    public int get_bgcolor() {
        return _bgcolor;
    }
    public String get_bgimage() {
        return _bgimage;
    }
    public String get_timeunits() {
        return _timeunits;
    }
    public void set_eventname(String _eventname) {
        this._eventname = _eventname;
    }
    public void set_eventinfo(String _eventinfo) {
        this._eventinfo = _eventinfo;
    }
    public void set_timeunits(String _timeunits) {
        this._timeunits = _timeunits;
    }
    public void set_evtype(String _evtype) {
        this._evtype = _evtype;
    }
    public void set_bgimage(String _bgimage) {
        this._bgimage = _bgimage;
    }
    public void set_evstatus(String _evstatus) {
        this._evstatus = _evstatus;
    }
    public void set_evtime(int _evtime) {
        this._evtime = _evtime;
    }
    public void set_direction(int _direction) {
        this._direction = _direction;
    }
    public void set_bgcolor(int _bgcolor) {
        this._bgcolor = _bgcolor;
    }
}