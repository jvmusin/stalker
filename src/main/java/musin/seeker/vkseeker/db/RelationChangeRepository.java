package musin.seeker.vkseeker.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelationChangeRepository extends JpaRepository<RelationChange, Integer> {
    List<RelationChange> findAllByOwnerOrderById(int owner);
}