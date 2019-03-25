package io.github.paul1365972.raytracecpu.objects.lights;

import org.joml.Vector3f;

public abstract class Light {
	
	protected Vector3f color;
	protected float intensity;
	
	public Light(Vector3f color, float intensity) {
		this.color = color;
		this.intensity = intensity;
	}
	
	public abstract float intensity(Vector3f dir);
	
	public abstract Vector3f toLight(Vector3f pos);
	
	public Vector3f getColor() {
		return color;
	}
}
