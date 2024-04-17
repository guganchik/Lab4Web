package ru.gugan.model;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "points")
public class Point {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "x")
	private float x;


	@Column(name = "y")
	private float y;


	@Column(name = "r")
	private float r;

	@Column(name = "hit")
	private boolean hit;

	@Column(name = "ctime")
	private String currentTime;

	@Column(name = "wtime")
	private long workTime;

	public void validateHit() {
		this.hit = false;
		if (x < 0 && y > 0) {
			if (Math.pow(x, 2) + Math.pow(y, 2) <= Math.pow(r/2, 2)) {
				this.hit = true;
			}
		} else if (x > 0 && y < 0) {
			if (y >= 2 * x - r) {
				this.hit = true;
			}
		} else if (x > 0 && y > 0) {
			if (x <= r && y <= r) {
				this.hit = true;
			}
		} else if (x == 0) {
			if(y <= r && y >= -r) {
				this.hit = true;
			}
		} else if (y == 0) {
			if(x <= r && x >= -r/2) {
				this.hit = true;
			}
		}
	}

	public Point() {
	}

	public Point(float x, float y, float r) {
		Date date = new Date();
		long startTime = System.currentTimeMillis();
		this.x = x;
		this.y = y;
		this.r = r;
		this.validateHit();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.currentTime = df.format(date);
		this.workTime = System.currentTimeMillis() - startTime;
	}


	public long getId() {
		return id;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getR() {
		return r;
	}

	public void setR(float r) {
		this.r = r;
	}

	public boolean isHit() {
		return hit;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public long getWorkTime() {
		return workTime;
	}

	public void setWorkTime(long workTime) {
		this.workTime = workTime;
	}

	@Override
	public String toString() {
		return "Tutorial [id=" + id + ", x=" + x + ", y=" + y + ", r=" + r + "]";
	}

}
