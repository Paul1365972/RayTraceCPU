package io.github.paul1365972.raytracecpu.objects.lights;

import org.joml.Vector3f;

public class PointLight extends Light {
	
	protected Vector3f p;
	
	public PointLight(Vector3f pos, Vector3f color, float intensity) {
		super(color, intensity);
		this.p = pos;
	}
	
	public PointLight(float x, float y, float z, Vector3f color, float intensity) {
		super(color, intensity);
		this.p = new Vector3f(x, y, z);
	}
	
	public float intensity(Vector3f dir) {
		return intensity / dir.lengthSquared();
	}
	
	@Override
	public Vector3f toLight(Vector3f pos) {
		return new Vector3f(p).sub(pos);
	}
	
	public Vector3f getPosition() {
		return p;
	}
}
