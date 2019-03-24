package io.github.paul1365972.raytracecpu;

import io.github.paul1365972.raytracecpu.objects.Light;
import io.github.paul1365972.raytracecpu.objects.TraceObject;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class State {
	
	private List<TraceObject> objects = new ArrayList<>();
	private List<Light> lights = new ArrayList<>();
	
	private Vector3f pos;
	private float pitch, yaw;
	
	public State(float x, float y, float z, float pitch, float yaw) {
		this.pos = new Vector3f(x, y, z);
		this.pitch = pitch;
		this.yaw = yaw;
	}
	
	public List<TraceObject> getObjects() {
		return objects;
	}
	
	public void addObject(TraceObject obj) {
		objects.add(obj);
	}
	
	public List<Light> getLights() {
		return lights;
	}
	
	public void addLight(Light light) {
		lights.add(light);
	}
	
	public void addPos(float x, float y, float z) {
		this.pos.add(x, y, z);
	}
	
	public void addPitch(float pitch) {
		this.pitch += pitch;
	}
	
	public void addYaw(float yaw) {
		this.yaw += yaw;
	}
	
	public Vector3f getPos() {
		return pos;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getYaw() {
		return yaw;
	}
}
