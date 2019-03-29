package io.github.paul1365972.raytracecpu;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Frame implements KeyListener {
	
	private static final float SPEED = 0.3f;
	private static final float ROT = (float) Math.toRadians(3f);
	
	private State state;
	
	private JFrame jFrame;
	private JPanel jPanel;
	
	private boolean W, A, S, D, SPACE, SHIFT, UP, DOWN, RIGHT, LEFT, PLUS, MINUS;
	private boolean record;
	
	public Frame(State state, LinkedBlockingQueue<BufferedImage> unused, LinkedBlockingQueue<BufferedImage> rendered) {
		this.state = state;
		try {
			SwingUtilities.invokeAndWait(() -> {
				jFrame = new JFrame("RayTraceCPU");
				jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				jPanel = new JPanel(true) {
					@Override
					protected void paintComponent(Graphics g) {
						BufferedImage img = rendered.size() > 1 ? rendered.poll() : rendered.peek();
						if (img != null) {
							g.drawImage(img.getScaledInstance(g.getClipBounds().width, g.getClipBounds().height, Image.SCALE_FAST), 0, 0, null);
							
							unused.add(img);
						}
					}
				};
				
				jFrame.addKeyListener(this);
				
				jFrame.getContentPane().add(jPanel);
				jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				jFrame.pack();
				jFrame.setVisible(true);
				jFrame.setSize(600, 450);
				jFrame.setLocationRelativeTo(null);
			});
		} catch (InterruptedException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public List<BufferedImage> generateImages(int amount, int width, int height) {
		GraphicsConfiguration gc = jPanel.getGraphicsConfiguration();
		ArrayList<BufferedImage> list = new ArrayList<>(amount);
		for (int i = 0; i < amount; i++) {
			list.add(gc.createCompatibleImage(width, height));
		}
		return list;
	}
	
	public void repaint() {
		jFrame.repaint();
	}
	
	public void updateInputs() {
		if (W)
			state.addPos(SPEED * (float) Math.sin(state.getYaw()), 0, SPEED * (float) Math.cos(state.getYaw()));
		if (S)
			state.addPos(-SPEED * (float) Math.sin(state.getYaw()), 0, -SPEED * (float) Math.cos(state.getYaw()));
		if (A)
			state.addPos(-SPEED * (float) Math.cos(state.getYaw()), 0, SPEED * (float) Math.sin(state.getYaw()));
		if (D)
			state.addPos(SPEED * (float) Math.cos(state.getYaw()), 0, -SPEED * (float) Math.sin(state.getYaw()));
		if (SPACE)
			state.addPos(0, SPEED, 0);
		if (SHIFT)
			state.addPos(0, - SPEED, 0);
		if (UP)
			state.addPitch(ROT * state.getFov() / 90);
		if (DOWN)
			state.addPitch(-ROT * state.getFov() / 90);
		if (LEFT)
			state.addYaw(-ROT * state.getFov() / 90);
		if (RIGHT)
			state.addYaw(ROT * state.getFov() / 90);
		if (PLUS)
			state.setFov(state.getFov() / 1.1f);
		if (MINUS)
			state.setFov(state.getFov() * 1.1f);
	}
	
	public boolean isRecording() {
		return record;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				W = true;
				break;
			case KeyEvent.VK_S:
				S = true;
				break;
			case KeyEvent.VK_A:
				A = true;
				break;
			case KeyEvent.VK_D:
				D = true;
				break;
			case KeyEvent.VK_SPACE:
				SPACE = true;
				break;
			case KeyEvent.VK_SHIFT:
				SHIFT = true;
				break;
			case KeyEvent.VK_UP:
				UP = true;
				break;
			case KeyEvent.VK_DOWN:
				DOWN = true;
				break;
			case KeyEvent.VK_RIGHT:
				RIGHT = true;
				break;
			case KeyEvent.VK_LEFT:
				LEFT = true;
				break;
			case KeyEvent.VK_R:
				record = true;
				break;
			case KeyEvent.VK_PLUS:
				PLUS = true;
				break;
			case KeyEvent.VK_MINUS:
				MINUS = true;
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				W = false;
				break;
			case KeyEvent.VK_S:
				S = false;
				break;
			case KeyEvent.VK_A:
				A = false;
				break;
			case KeyEvent.VK_D:
				D = false;
				break;
			case KeyEvent.VK_SPACE:
				SPACE = false;
				break;
			case KeyEvent.VK_SHIFT:
				SHIFT = false;
				break;
			case KeyEvent.VK_UP:
				UP = false;
				break;
			case KeyEvent.VK_DOWN:
				DOWN = false;
				break;
			case KeyEvent.VK_RIGHT:
				RIGHT = false;
				break;
			case KeyEvent.VK_R:
				record = false;
				break;
			case KeyEvent.VK_LEFT:
				LEFT = false;
				break;
			case KeyEvent.VK_PLUS:
				PLUS = false;
				break;
			case KeyEvent.VK_MINUS:
				MINUS = false;
				break;
		}
	}
}
