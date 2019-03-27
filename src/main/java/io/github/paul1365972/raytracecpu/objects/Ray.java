package io.github.paul1365972.raytracecpu.objects;

import org.joml.Vector3f;

public class Ray {
	
	Vector3f p, r;
	int level;
	
	private Ray(Vector3f p, Vector3f r, int level) {
		this.p = p;
		this.r = r;
		this.level = level;
	}
	
	public static Ray create(Vector3f p, Vector3f r, int level) {
		if (r.lengthSquared() < 0.999 * 0.999 || r.lengthSquared() > 1.001 * 1.001)
			throw new IllegalArgumentException("Unit Vector with length of " + r.length());
		return new Ray(new Vector3f(p), new Vector3f(r), level + 1);
	}
	
	public static Ray create(Vector3f p, Vector3f r, int level, float epsilon) {
		if (r.lengthSquared() < 0.999 * 0.999 || r.lengthSquared() > 1.001 * 1.001)
			throw new IllegalArgumentException("Unit Vector with length of " + r.length());
		return new Ray(new Vector3f(p).fma(epsilon, r), new Vector3f(r), level + 1);
	}
	
}
