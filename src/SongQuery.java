import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import music.Artist;

import java.util.List;
import java.util.stream.Stream;

public class SongQuery {
    public static void main(String[] args) {
        List<Tuple> allCandidates = null;
        try (var emf = Persistence.createEntityManagerFactory("dev.lpa.music");
             EntityManager em = emf.createEntityManager()
        ) {
            var transaction = em.getTransaction();
            allCandidates = getArtistsWithSongJPQLstreamlined(em, "%Soul%");
            allCandidates.forEach(System.out::println);
            transaction.commit();
        } catch(Exception e) {
            System.err.println("Something went wrong!");
            e.printStackTrace();
        }
    }

    public static List<Artist> getArtistsWithSongJPQL(EntityManager em, String matchedValue) {
        String jpql = "SELECT a FROM Artist a JOIN albums album JOIN songs song WHERE song.songTitle LIKE ?1";
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




}
