package io.github.paul1365972.raytracecpu.objects;

import io.github.paul1365972.raytracecpu.RayTracer;
import io.github.paul1365972.raytracecpu.objects.lights.Light;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Sphere extends TraceObject {
	
	protected float radius;
	protected Vector3f diffuse;
	protected float shininess, opacity, reflectivity;
	
	public Sphere(Vector3f pos, float radius, Vector3f diffuse, float shininess, float opacity, float reflectivity) {
		super(pos);
		this.radius = radius;
		this.diffuse = diffuse;
		this.shininess = shininess;
		this.opacity = opacity;
		this.reflectivity = reflectivity;
	}
	
	public Sphere(float x, float y, float z, float radius, Vector3f diffuse, float shininess, float opacity, float reflectivity) {
		super(x, y, z);
		this.radius = radius;
		this.diffuse = diffuse;
		this.shininess = shininess;
		this.opacity = opacity;
		this.reflectivity = reflectivity;
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
			
			Ray lightRay = Ray.create(hit, dirN, ray.level, RayTracer.EPSILON);
			if (!tracer.occluded(lightRay, dir.length())) {
				float strength = light.intensity(dir);
				
				float lambertian = Math.max(0, normal.dot(dirN));
				color.add(new Vector3f(light.getColor()).mul(diffuse).mul(strength * lambertian));
				if (lambertian > 0 && shininess >= 0) {
					Vector3f halfVector = new Vector3f(dirN).sub(ray.r).normalize();
					float specular = (float) Math.pow(Math.max(0, halfVector.dot(normal)), shininess);
					color.fma(strength * specular, light.getColor());
				}
			}
		}
		if (reflectivity > 0 && ray.level < 15) {
			Vector3f reflected = new Vector3f(ray.r).reflect(normal);
			Ray reflectedRay = Ray.create(hit, reflected, ray.level, RayTracer.EPSILON);
			Vector4f reflectedColor = tracer.computeRay(reflectedRay);
			color.lerp(new Vector3f(reflectedColor.x, reflectedColor.y, reflectedColor.z), reflectivity);
		}
		if (opacity < 1 && ray.level < 15) {
			Ray continuedRay = Ray.create(hit, ray.r, ray.level, RayTracer.EPSILON);
			Vector4f refractedColor = tracer.computeRay(continuedRay);
			return new Vector4f(color, 1).lerp(refractedColor, 1 - opacity);
		} else {
			return new Vector4f(color, 1);
		}
	}
	
}
