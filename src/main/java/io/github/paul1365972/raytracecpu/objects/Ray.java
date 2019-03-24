package io.github.paul1365972.raytracecpu.objects;

import org.joml.Vector3f;

public class Ray {
	
	Vector3f p, r;
	int level;
	float weight;
	
	public Ray(Vector3f p, Vector3f r, int level, float weight) {
		this.p = p;
		this.r = r;
		this.level = level;
		this.weight = weight;
	}
	
}
