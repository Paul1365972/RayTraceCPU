package io.github.paul1365972.raytracecpu.objects;

import io.github.paul1365972.raytracecpu.RayTracer;
import io.github.paul1365972.raytracecpu.objects.lights.Light;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Sphere extends TraceObject {
	
	protected float radius;
	protected Vector3f diffuse;
	
	public Sphere(Vector3f pos, float radius, Vector3f diffuse) {
		super(pos);
		this.radius = radius;
		this.diffuse = diffuse;
	}
	
	public Sphere(float x, float y, float z, float radius, Vector3f diffuse) {
		super(x, y, z);
		this.radius = radius;
		this.diffuse = diffuse;
	}
	
	@Override
	public float dist(Ray ray) {
		Vector3f v = new Vector3f(ray.p).sub(p);
		float vdd = v.dot(ray.r);
		float tmp = vdd * vdd - v.lengthSquared() + radius * radius;
		if (tmp < 0)
			return Float.NEGATIVE_INFINITY;
		tmp = (float) Math.sqrt(tmp);
		float a = -vdd + tmp;
		float b = -vdd - tmp;
		if (a > 0) {
			if (b > 0) {
				return Math.min(a, b);
			} else {
				return a;
			}
		} else {
			if (b > 0) {
				return b;
			} else {
				return Float.NEGATIVE_INFINITY;
			}
		}
	}
	
	@Override
	public Vector4f trace(Ray ray, float dist, RayTracer tracer) {
		Vector3f hit = new Vector3f(ray.p).fma(dist, ray.r);
		Vector3f normal = new Vector3f(hit).sub(p).normalize();
		Vector3f color = new Vector3f(diffuse).mul(0.15f);
		
		for (Light light : tracer.getLights()) {
			Vector3f dir = light.toLight(hit);
			Vector3f dirN = dir.normalize(new Vector3f());
			Ray lightRay = new Ray(hit, dirN, ray.level++, ray.weight);
			if (!tracer.occluded(lightRay, this)) {
				float strength = light.intensity(dir);
				
				float angle = Math.max(0, normal.dot(dirN));
				color.add(new Vector3f(light.getColor()).mul(diffuse).mul(strength * angle));
				
				Vector3f reflected = new Vector3f(ray.r).reflect(normal);
				float specular = (float) Math.pow(Math.max(0, reflected.dot(dirN)), 16f);
				color.fma(strength * specular, light.getColor());
			}
		}
		return new Vector4f(color, 1);
	}
	
}
