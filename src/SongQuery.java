import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import music.Album;
import music.Artist;
import music.Song;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Stream;

public class SongQuery {
    public static void main(String[] args) {
        List<Artist> allCandidates = null;
        try (var emf = Persistence.createEntityManagerFactory("dev.lpa.music");
             EntityManager em = emf.createEntityManager()
        ) {
            var transaction = em.getTransaction();
//            allCandidates = getArtistsWithSongJPQLstreamlined(em, "%Soul%");
            allCandidates = getArtistsWithSongBuilder(em, "%Soul%");
//            allCandidates = getArtistsWithSongJPQL(em, "%Soul%");
            allCandidates.forEach(System.out::println);
            System.out.println("Output size: " + allCandidates.size());
            transaction.commit();
        } catch(Exception e) {
            System.err.println("Something went wrong!");
            e.printStackTrace();
        }
    }

    public static List<Artist> getArtistsWithSongJPQL(EntityManager em, String matchedValue) {
        String jpql = "SELECT DISTINCT a FROM Artist a JOIN albums album JOIN songs song WHERE song.songTitle LIKE ?1";
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
    public static List<Artist> getArtistsWithSongBuilder(EntityManager em, String matchedValue) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Artist> cq = cb.createQuery(Artist.class);
        Root<Artist> rootArt = cq.from(Artist.class);
        Root<Album> rootAlb = cq.from(Album.class);
        Root<Song> rootSon = cq.from(Song.class);
//        cq.multiselect(rootArt.get("artistName"),rootAlb.get("albumName"),rootSon.get("songTitle")).
//                where(cb.like(rootSon.get("songTitle"), matchedValue));
        cq.select(rootArt)
                .where(cb.like(rootSon.get("songTitle"), matchedValue));
        return em.createQuery(cq).getResultList();
    }

}
