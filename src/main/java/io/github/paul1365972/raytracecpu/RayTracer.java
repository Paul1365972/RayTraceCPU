package io.github.paul1365972.raytracecpu;

import io.github.paul1365972.raytracecpu.objects.Light;
import io.github.paul1365972.raytracecpu.objects.Ray;
import io.github.paul1365972.raytracecpu.objects.TraceObject;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;
import java.util.stream.IntStream;

public class RayTracer {
	
	public static final Vector3f UP = new Vector3f(0, 1, 0);
	
	private static final Vector4f NULL_VECTOR = new Vector4f(0);
	private static final Vector4f BG_COLOR = new Vector4f(0, 0, 0, 1);
	private static final float FOV = 90;
	
	private State state;
	
	public RayTracer(State state) {
		this.state = state;
	}
	
	public int[] renderPixels(int width, int height) {
		Vector3f pos = new Vector3f(state.getPos());
		float pitch = state.getPitch();
		float yaw = state.getYaw();
		
		Vector3f E = pos;
		Vector3f t = new Vector3f((float) (Math.sin(yaw) * Math.cos(pitch)), (float) Math.sin(pitch), (float) (Math.cos(yaw) * Math.cos(pitch)));
		
		Vector3f b = UP.cross(t, new Vector3f()).normalize();
		Vector3f v = t.cross(b, new Vector3f()).normalize(); //Unnecessary maybe?
		
		float gx = (float) (1 * Math.tan(Math.toRadians(FOV / 2)));
		float gy = gx * (height / (float) width);
		
		Vector3f qx = new Vector3f(b).mul(2 * gx / (width - 1)); //To Minus 1 or not to minus 1 hmm
		Vector3f qy = new Vector3f(v).mul(2 * gy / (height - 1));
		
		Vector3f p1m = new Vector3f(t).fma(-gx, b).fma(-gy, v);
		
		return IntStream.range(0, width * height).parallel().map(k -> {
			int y = k / width;
			int x = k - y * width;
			y = height - 1 - y;
			
			Vector3f r = new Vector3f(p1m).fma(x, qx).fma(y, qy).normalize();
			
			Vector4f color = computeRay(new Ray(E, r, 0, 1));
			color.lerp(BG_COLOR, 1 - color.w);
			Maths.clamp(color, 0, 1);
			Maths.gamma(color, 1.4f);
			
			return ((int) (color.x * 255 + 0.5f) << 16) | ((int) (color.y * 255 + 0.5f) << 8)  | ((int) (color.z * 255 + 0.5f));
		}).toArray();
		
		/*IntStream.range(0, width * height).parallel().forEach(k -> {
			int i = k / width;
			int j = k - i * width;
			
			Vector3f tmp2 = new Vector3f();
			Vector3f r = new Vector3f(p1m).add(qx.mul(j, tmp2)).add(qy.mul(i, tmp2)).normalize();
			
			Vector4f color = computeRay(new Ray(E, r, 0, 1));
			color.lerp(BG_COLOR, 1 - color.w);
			Maths.clamp(color, 0, 1);
			int value = ((int) (color.x * 255 + 0.5f) << 16) | ((int) (color.y * 255 + 0.5f) << 8)  | ((int) (color.z * 255 + 0.5f));
			pixels[j + (height - 1 - i) * height] = value;
		});
		return pixels;*/
	}
	
	public Vector4f computeRay(Ray ray) {
		float minimum = Float.MAX_VALUE;
		TraceObject nearest = null;
		for (TraceObject obj : state.getObjects()) {
			float dist = obj.dist(ray);
			if (dist > 0 && dist < minimum) {
				minimum = dist;
				nearest = obj;
			}
		}
		return nearest != null ? nearest.trace(ray, minimum, this) : NULL_VECTOR;
	}
	
	public List<Light> getLights() {
		return state.getLights();
	}
	
	public boolean occluded(Ray ray, TraceObject ignore) {
		for (TraceObject obj : state.getObjects()) {
			if (obj == ignore)
				continue;
			float dist = obj.dist(ray);
			if (dist > 0)
				return true;
		}
		return false;
	}
	
}
