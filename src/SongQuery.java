import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import music.Album;
import music.Artist;
import music.Song;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Stream;

public class SongQuery {
    public static void main(String[] args) {
//        List<Artist> allCandidates = null;
        try (var emf = Persistence.createEntityManagerFactory(
                "dev.lpa.music");
             EntityManager em = emf.createEntityManager()
        ) {
            var transaction = em.getTransaction();
            transaction.begin();
            String dashedString = "-".repeat(19);
            String word = "Soul";
//            allCandidates = getArtistsWithSongJPQLstreamlined(em, "%Soul%");
//            var allCandidates = getArtistsWithSongBuilder(em, "%"+word+"%");
            var allCandidates = getArtistsWithSongJPQL(em, "%"+word+"%");
//            allCandidates.forEach(System.out::println);
            System.out.printf("%-30s %-65s %s%n", "Artist", "Album", "Song Title");
            System.out.printf("%1$-30s %1$-65s %1$s%n", dashedString);


            allCandidates.forEach(a -> {
                String artistName = a.getArtistName();
                a.getAlbums().forEach(b-> {
                    String albumName = b.getAlbumName();
                    b.getSongs().forEach(s->{
                        String songTitle = s.getSongTitle();
                        if(songTitle.contains(word)){
                            System.out.printf("%-30s %-65s %s%n",
                                    artistName, albumName, songTitle);
                        }
                    });
                });
            });


            System.out.printf("%-30s %-65s %s%n", "Artist", "Album", "Song Title");
            System.out.printf("%1$-30s %1$-65s %1$s%n", dashedString);
            var aBllCandidates = getArtistsWithSongBuilder(em, "%"+word+"%");
            aBllCandidates.forEach(m->
                    System.out.printf("%-30s %-65s %s%n",
                            (String) m[0], (String) m[1], (String) m[2]));
            System.out.println("Output size: " + aBllCandidates.size());
            transaction.commit();
        } catch(Exception e) {
            System.err.println("Something went wrong!");
            e.printStackTrace();
        }
    }

    public static List<Artist> getArtistsWithSongJPQL(EntityManager em, String matchedValue) {
        String jpql = "SELECT a FROM Artist a JOIN albums album JOIN songs song WHERE song.songTitle LIKE ?1";
//        String jpql = "SELECT a.artistName, album.albumName, song.songTitle FROM Artist a JOIN albums album JOIN songs song WHERE song.songTitle LIKE ?1";
        Query qr = em.createQuery(jpql, Artist.class);
        qr.setParameter(1, matchedValue);
        return qr.getResultList();
    }

    public static List<Tuple> getArtistsWithSongJPQLstreamlined(EntityManager em, String matchedValue) {
        String jpql = "SELECT a.artistName, album.albumName, song.songTitle FROM Artist a JOIN albums album JOIN songs song WHERE song.songTitle LIKE ?1";
        Query qr = em.createQuery(jpql, Tuple.class);
        qr.setParameter(1, matchedValue);
        return qr.getResultList();
    }

    //Bonus challenge:
    public static List<Object[]> getArtistsWithSongBuilder(EntityManager em, String matchedValue) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

        Root<Artist> root = cq.from(Artist.class);
        Join<Artist, Album> albumJoin = root.
                join("albums", JoinType.INNER);
        Join<Album, Song> songJoin = albumJoin.
                join("songs", JoinType.INNER);
        cq.multiselect(
                root.get("artistName"),
                albumJoin.get("albumName"),
                songJoin.get("songTitle")
                        )
                .where(cb.like(songJoin.get("songTitle"), matchedValue))
                .orderBy(cb.asc(root.get("artistName")));
        return em.createQuery(cq).getResultList();
    }

}
