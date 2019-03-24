package io.github.paul1365972.raytracecpu.objects;

import io.github.paul1365972.raytracecpu.RayTracer;
import org.joml.Vector3f;
import org.joml.Vector4f;

public abstract class TraceObject {
	
	protected Vector3f p;
	
	public TraceObject(Vector3f pos) {
		this.p = pos;
	}
	
	public TraceObject(float x, float y, float z) {
		p = new Vector3f(x, y, z);
	}
	
	public abstract float dist(Ray ray);
	
	public abstract Vector4f trace(Ray ray, float dist, RayTracer tracer);
	
}
