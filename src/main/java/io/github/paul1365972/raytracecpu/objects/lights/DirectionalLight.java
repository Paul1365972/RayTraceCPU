package io.github.paul1365972.raytracecpu.objects.lights;

import org.joml.Vector3f;

public class DirectionalLight extends Light {
	
	private Vector3f dir;
	
	public DirectionalLight(Vector3f dir, Vector3f color, float intensity) {
		super(color, intensity);
		this.dir = dir.normalize();
	}
	
	public DirectionalLight(float x, float y, float z, Vector3f color, float intensity) {
		super(color, intensity);
		this.dir = new Vector3f(x, y, z).normalize();
	}
	
	@Override
	public float intensity(Vector3f dir) {
		return intensity;
	}
	
	@Override
	public Vector3f toLight(Vector3f pos) {
		return new Vector3f(dir).mul(10_000);
	}
}
