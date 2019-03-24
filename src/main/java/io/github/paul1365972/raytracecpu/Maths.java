package io.github.paul1365972.raytracecpu;

import org.joml.Vector4f;

public class Maths {
	
	public static void clamp(Vector4f vec, float min, float max) {
		vec.x = vec.x < min ? min : vec.x > max ? max : vec.x;
		vec.y = vec.y < min ? min : vec.y > max ? max : vec.y;
		vec.z = vec.z < min ? min : vec.z > max ? max : vec.z;
		vec.w = vec.w < min ? min : vec.w > max ? max : vec.w;
	}
	
	public static void gamma(Vector4f vec, float gamma) {
		vec.x = (float) Math.pow(vec.x, gamma);
		vec.y = (float) Math.pow(vec.y, gamma);
		vec.z = (float) Math.pow(vec.z, gamma);
		vec.w = (float) Math.pow(vec.w, gamma);
	}
}
