package io.github.paul1365972.raytracecpu;

import io.github.paul1365972.raytracecpu.objects.Plane;
import io.github.paul1365972.raytracecpu.objects.Sphere;
import io.github.paul1365972.raytracecpu.objects.lights.DirectionalLight;
import io.github.paul1365972.raytracecpu.objects.lights.PointLight;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

public class RayTraceCPU {
	
	private State state;
	private Frame frame;
	private LinkedBlockingQueue<BufferedImage> unusedImages = new LinkedBlockingQueue<>();
	private LinkedBlockingQueue<BufferedImage> renderedImages = new LinkedBlockingQueue<>(3);
	
	public RayTraceCPU() {
		state = new State(-2.4f, 0.3f, 0.6f, 0, 0, 90f);
		
		state.addLight(new PointLight(0, 5, 5, new Vector3f(1, 1, 1), 30));
		/*for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				state.addLight(new PointLight(0 + 0.05f * i, 10, 5 + 0.05f * j, new Vector3f(1, 1, 1), 30 / 9f));
			}
		}*/
		
		state.addLight(new DirectionalLight(0, 1, 0, new Vector3f(1, 1, 1), 0.3f));
		/*for (int i = -2; i < 3; i++) {
			for (int j = -2; j < 3; j++) {
				state.addLight(new DirectionalLight(0.01f * i, 1, 0.01f * j, new Vector3f(1, 1, 1), 0.3f / 25f));
			}
		}*/
		
		state.addObject(new Sphere(0, 5, 15, 5, new Vector3f(1, 0, 0), -1f, 1f, 1f));
		state.addObject(new Sphere(-3, 0, 5, 1, new Vector3f(1, 0, 0), 8f, 1f, 0f));
		state.addObject(new Sphere(0, 0, 5, 1, new Vector3f(0, 1, 0), 32f, 1f, 0f));
		state.addObject(new Sphere(3, 0, 5, 1, new Vector3f(0, 0, 1), 128f, 1f, 0f));
		state.addObject(new Sphere(3, 0, 0, 1, new Vector3f(1, 1, 1), 16f, 0.3f, 0.6f));
		state.addObject(new Plane(0, -5, 0, new Vector3f(0, 1, 0), new Vector3f(1, 1, 1), 1f, 1f, 0.7f - 0.7f));
		//state.addObject(new Plane(0, -5.01f, 0, new Vector3f(0, -1, 0), new Vector3f(1, 1, 1), 1f, 1f, 0.7f - 0.7f));
		
		frame = new Frame(state, unusedImages, renderedImages);
		unusedImages.addAll(frame.generateImages(10, 1600 / 1, 850 / 1));
	}
	
	public void run() {
		Thread computeThread = new Thread(() -> {
			long frameNumber = 0;
			while (true) {
				frame.updateInputs();
				state.update(frameNumber);
				try {
					BufferedImage img = unusedImages.take();
					
					long start = System.nanoTime();
					int[] pixels = new RayTracer(state).renderPixels(img.getWidth(), img.getHeight());
					System.out.println("Frame " + frameNumber + " Computed in " + (System.nanoTime() - start) / 1_000_000f + " ms");
					img.setRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
					
					if (frame.isRecording()) {
						try {
							ImageIO.write(img, "png", new File("C:\\Users\\PC\\Desktop\\RayTracerOutput\\Frame" + frameNumber + ".png"));
							System.out.println("Recorded Frame " + frameNumber);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					renderedImages.put(img);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				frameNumber++;
			}
		}, "Compute Thread");
		computeThread.setDaemon(true);
		computeThread.setPriority(Thread.MAX_PRIORITY);
		computeThread.start();
		
		new Timer(1000 / 60, e -> frame.repaint()).start();
		
	}
	
	public static void main(String[] args) {
		ImageIO.setUseCache(false);
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "2");
		System.out.println("Threads: " + ForkJoinPool.getCommonPoolParallelism());
		
		new RayTraceCPU().run();
	}
	
}
