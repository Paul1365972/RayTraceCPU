package io.github.paul1365972.raytracecpu;

import io.github.paul1365972.raytracecpu.objects.lights.DirectionalLight;
import io.github.paul1365972.raytracecpu.objects.lights.PointLight;
import io.github.paul1365972.raytracecpu.objects.Sphere;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

public class RayTraceCPU {
	
	private State state;
	private Frame frame;
	private LinkedBlockingQueue<BufferedImage> unusedImages = new LinkedBlockingQueue<>();
	private LinkedBlockingQueue<BufferedImage> renderedImages = new LinkedBlockingQueue<>(3);
	
	public RayTraceCPU() {
		state = new State(0, 0, 0, 0, 0);
		
		state.addLight(new PointLight(0, 10, 5, new Vector3f(1, 1, 1), 30));
		state.addLight(new DirectionalLight(0, 1, 0, new Vector3f(1, 1, 1), 0.3f));
		
		state.addObject(new Sphere(-3, 0, 5, 1, new Vector3f(1, 0, 0)));
		state.addObject(new Sphere(0, 0, 5, 1, new Vector3f(0, 1, 0)));
		state.addObject(new Sphere(3, 0, 5, 1, new Vector3f(0, 0, 1)));
		state.addObject(new Sphere(0, 5, 0, 1, new Vector3f(1, 1, 0)));
		
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
					
					/*if (frameNumber > 300) {
						try {
							ImageIO.write(img, "png", new File("C:\\Users\\PC\\Desktop\\RayTracerOutput\\Frame" + frameNumber + ".png"));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}*/
					
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
		
		while (true) {
			try {
				SwingUtilities.invokeAndWait(frame::repaint);
				Thread.sleep(10);
			} catch (InterruptedException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "16");
		System.out.println("Threads: " + ForkJoinPool.getCommonPoolParallelism());
		
		new RayTraceCPU().run();
	}
	
}
