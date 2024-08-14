package Reigners.demo.Repository;

import Reigners.demo.Models.Events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository   extends JpaRepository<Events, Long> {
}
