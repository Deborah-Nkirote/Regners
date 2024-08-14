package Reigners.demo.Repository;

import Reigners.demo.Models.AdminModel;
import Reigners.demo.Models.Sermon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<AdminModel, Long> {


    @Query("SELECT a.welcomeMessage FROM AdminModel a")
    String findSingleWelcomeMessage();




}
