package io.github.paul1365972.raytracecpu.objects;

import org.joml.Vector3f;

public class Light {
	
	protected Vector3f p;
	protected Vector3f color;
	protected float intensity;
	
	public Light(Vector3f p, Vector3f color, float intensity) {
		this.p = p;
		this.color = color;
		this.intensity = intensity;
	}
	
	public Light(float x, float y, float z, Vector3f color, float intensity) {
		this.p = new Vector3f(x, y, z);
		this.color = color;
		this.intensity = intensity;
	}
	
}
