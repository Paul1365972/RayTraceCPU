package io.github.paul1365972.raytracecpu;

import io.github.paul1365972.raytracecpu.objects.Light;
import io.github.paul1365972.raytracecpu.objects.Sphere;
import org.joml.Vector3f;

import javax.swing.SwingUtilities;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

public class RayTraceCPU {
	
	public static void main(String[] args) {
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "16");
		System.out.println("Threads: " + ForkJoinPool.getCommonPoolParallelism());
		
		LinkedBlockingQueue<BufferedImage> unusedImages = new LinkedBlockingQueue<>();
		LinkedBlockingQueue<BufferedImage> renderedImages = new LinkedBlockingQueue<>(3);
		
		State state = new State(0, 0, 0, 0, 0);
		Frame frame = new Frame(state, unusedImages, renderedImages);
		unusedImages.addAll(frame.generateImages(10, 1600 / 4, 850 / 4));
		
		state.addLight(new Light(0, 10, 5, new Vector3f(1, 1, 1), 50));
		
		state.addObject(new Sphere(-3, 0, 5, 1, new Vector3f(1, 0, 0)));
		state.addObject(new Sphere(0, 0, 5, 1, new Vector3f(0, 1, 0)));
		state.addObject(new Sphere(3, 0, 5, 1, new Vector3f(0, 0, 1)));
		state.addObject(new Sphere(0, 5, 0, 1, new Vector3f(1, 1, 0)));
		
		Thread computeThread = new Thread(() -> {
			while (true) {
				frame.updateInputs();
				try {
					BufferedImage img = unusedImages.take();
					
					long start = System.nanoTime();
					int[] pixels = new RayTracer(state).renderPixels(img.getWidth(), img.getHeight());
					System.out.println("Frame Computed in " + (System.nanoTime() - start) / 1_000_000f + " ms");
					img.setRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
					
					renderedImages.put(img);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
	
}
