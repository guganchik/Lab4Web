package ru.gugan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gugan.model.Point;

public interface PointRepository extends JpaRepository<Point, Long> {

}
