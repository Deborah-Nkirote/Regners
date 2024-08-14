package Reigners.demo.Repository;


import Reigners.demo.Models.Sermon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SermonRepository  extends JpaRepository<Sermon, Long> {
    //void updateLiveStreamUrl(String url, String sermonId);
}
