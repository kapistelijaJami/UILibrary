package uilibrary.animation;

import java.awt.image.BufferedImage;
import java.util.List;

public class Animation {
	private int frameCount;						// Counts updates for changing the frame
	private double frameDelay;					// How many updates between each frame
	private int currentFrame;					// Animations current frame index
	private int animationDirection;				// Animation direction (i.e counting forward or backward), 1 or -1
	private boolean atLeastOnePlaybackDone;		// Just keeps track if at least one full playback is done
	
	private boolean slowDownFrameDelay = false;	// If true, the animation will gradually slow down
	private int slowedFrameDelay;				// At what point the frame delay have to cross in order for the animation to completely stop or stay there
	private boolean slowDownFully;				// If true, the animation will completely stop after frameDelay crosses slowedFrameDelay. If false, frameDelay will stay there.
	
	private final List<BufferedImage> frames;    // Arraylist of frames
	
	public Animation(List<BufferedImage> frames, int frameDelay) {
		this.frames = frames;
		this.frameDelay = frameDelay;

		this.frameCount = 0;
		this.currentFrame = 0;
		this.animationDirection = 1;
		atLeastOnePlaybackDone = false;
	}
	
	public void restart() {
        if (frames.isEmpty()) {
            return;
        }

        currentFrame = 0;
    }
	
	public int getCurrentFrame() {
		return currentFrame;
	}
	
	public int getFrameDelay() {
		return (int) frameDelay;
	}
	
	public void setCurrentFrame(int frame) {
		this.currentFrame = frame;
	}
	
	/**
	 * Activates automatic frame delay slow down.
	 * If slowDownFully is true, slowedFrameDelay will be the cap where frame delay will go to infinity.
	 * Otherwise slowedFrameDelay is the slowest limit.
	 * @param slowedFrameDelay 
	 * @param slowDownFully
	 */
	public void activateSlowDownFrameDelay(int slowedFrameDelay, boolean slowDownFully) {
		this.slowDownFrameDelay = true;
		this.slowDownFully = slowDownFully;
		this.slowedFrameDelay = slowedFrameDelay;
	}
	
	public void deactivateSlowDownFrameDelay() {
		slowDownFrameDelay = false;
	}
	
	public BufferedImage getCurrentFrameImage() {
        return frames.get(currentFrame);
    }
	
	public boolean getAtLeastOnePlaybackDone() {
		return atLeastOnePlaybackDone;
	}
	
	public void setAtLeastOnePlaybackDone(boolean done) {
		this.atLeastOnePlaybackDone = done;
	}
	
	public void update() {
		if (frameCount >= frameDelay) {
			changeFrame();
		}
		
		if (slowDownFrameDelay) {
			frameDelay += 0.1;
			
			if (slowDownFully) {
				if (frameDelay > slowedFrameDelay) {
					frameDelay = Double.MAX_VALUE;
				}
			} else if (frameDelay >= slowedFrameDelay) {
				frameDelay = slowedFrameDelay;
				//slowDownFrameDelay = false;
			}
		}
		
		frameCount++;
	}
	
	private void changeFrame() {
		frameCount = 0;
		currentFrame += animationDirection;

		if (currentFrame >= frames.size()) {
			currentFrame = 0;
			atLeastOnePlaybackDone = true;
		} else if (currentFrame < 0) {
			currentFrame = frames.size() - 1;
			atLeastOnePlaybackDone = true;
		}
	}
	
	public void matchAnimation(Animation other) {
		this.currentFrame = other.currentFrame;
		this.frameCount = other.frameCount;
	}
	
	public void reset() {
		setCurrentFrame(0);
		atLeastOnePlaybackDone = false;
	}
	
	public int getWidth() {
		if (!frames.isEmpty()) {
			return frames.get(0).getWidth();
		}
		return 0;
	}
	
	public int getHeight() {
		if (!frames.isEmpty()) {
			return frames.get(0).getHeight();
		}
		return 0;
	}
	
	public void setFrameDelay(int delay) {
		this.frameDelay = delay;
	}
	
	public void setAnimationDirection(boolean forward) {
		animationDirection = forward ? 1 : -1;
	}
}
