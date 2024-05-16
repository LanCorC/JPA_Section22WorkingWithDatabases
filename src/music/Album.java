package music;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "albums")
public class Album implements Comparable<Album> {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="album_id")
    private List<Song> songs = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="album_id")
    private int albumId;

    @Column(name="album_name")
    private String albumName;

    public Album() {
    }

    public Album(String albumName) {
        this.albumName = albumName;
    }

    public Album(int albumId, String albumName) {
        this.albumId = albumId;
        this.albumName = albumName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void addSong(String songName) {
        songs.add(new Song(songName));
    }

    @Override
    public String toString() {
        songs.sort(Comparator.comparingInt(Song::getTrackNo));
        StringJoiner stringJoiner = new StringJoiner("\n");
        songs.forEach(a->stringJoiner.add(a.toString()));

        return "Album{" +
                "albumId=" + albumId +
                ", albumName='" + albumName + '\'' +
                ", songs=" + stringJoiner.toString() +
                '}';
    }

    @Override
    public int compareTo(Album o) {
        return this.albumName.compareTo(o.getAlbumName());
    }
}
