package music;

import jakarta.persistence.*;

@Entity
@Table(name = "songs")
public class Song {
//        Comparable<Song> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="song_id")
    private int songId;

    @Column(name="track_number")
    private int trackNo;
    @Column(name="song_title")

    private String songTitle;

    public Song() {}

    public Song(String songTitle) {
        this.songTitle = songTitle;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public int getTrackNo() {
        return trackNo;
    }

    public void setTrackNo(int trackNo) {
        this.trackNo = trackNo;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songId="+songId+
                ", trackNo="+trackNo+
                ", songTitle='"+songTitle+
                "'}";
    }
}
