package sg.edu.tp.musicstream;

//Song class holds all song related info
public class Song {
    private String id;
    private String title;
    private String artist;
    private String fileLink;
    private double songLength;
    private String coverArt;
    private boolean filtered = false;

    public Song(String _id, String _title, String _artist, String _fileLink, double _songLength, String _coverArt)
    {
        this.id = _id;
        this.title = _title;
        this.artist = _artist;
        this.fileLink = _fileLink;
        this.songLength = _songLength;
        this.coverArt = _coverArt;
    }

    public void setFiltered(boolean filter)
    {
        this.filtered = filter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public double getSongLength() {
        return songLength;
    }

    public void setSongLength(double songLength) {
        this.songLength = songLength;
    }

    public String getCoverArt() {
        return coverArt;
    }

    public void setCoverArt(String coverArt) {
        this.coverArt = coverArt;
    }
}
