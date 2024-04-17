package ru.gugan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gugan.model.Point;
import ru.gugan.repository.PointRepository;

import java.util.ArrayList;
import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class PointController {

	@Autowired
	PointRepository pointRepository;

	@GetMapping("/points")
	public ResponseEntity<List<Point>> getAllResults() {
		try {
			List<Point> points = new ArrayList<Point>();
			pointRepository.findAll().forEach(points::add);
			return new ResponseEntity<>(points, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/points")
	public ResponseEntity<Point> createResult(@RequestBody Point point) {
		try {
			Point newPoint = pointRepository
					.save(new Point(point.getX(), point.getY(), point.getR()));
			return new ResponseEntity<>(newPoint, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/points")
	public ResponseEntity<HttpStatus> deleteAllPoints() {
		try {
			pointRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
