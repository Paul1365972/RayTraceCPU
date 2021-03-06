package io.github.paul1365972.raytracecpu;

import io.github.paul1365972.raytracecpu.objects.TraceObject;
import io.github.paul1365972.raytracecpu.objects.lights.Light;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class State {
	
	private List<TraceObject> objects = new ArrayList<>();
	private List<Light> lights = new ArrayList<>();
	
	private Vector3f pos;
	private float pitch, yaw;
	private float fov;
	
	public State(float x, float y, float z, float pitch, float yaw, float fov) {
		this.pos = new Vector3f(x, y, z);
		this.pitch = pitch;
		this.yaw = yaw;
		this.fov = fov;
	}
	
	public void update(long frameNumber) {
		//((PointLight) lights.get(0)).getPosition().setComponent(1, 11 + 4 * (float) Math.sin(Math.toRadians(frameNumber * 10.0f)));
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
		this.pitch %= Math.PI * 2;
	}
	
	public void addYaw(float yaw) {
		this.yaw += yaw;
		this.yaw %= Math.PI * 2;
	}
	
	public void setFov(float fov) {
		this.fov = fov;
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
	
	public float getFov() {
		return fov;
	}
	
}
