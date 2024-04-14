package utils;

public enum ResizeMode {
	PERCENT, PIXEL;

	private int sizeX, sizeY;
	private boolean isFast;

	public static ResizeMode MODE = PIXEL;

	public int getSizeX() {
		return sizeX;
	}

	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}

	public void setMode(boolean mode) {
		if (mode) {
			MODE = PERCENT;
		} else {
			MODE = PIXEL;
		}
	}

	public boolean isFast() {
		return isFast;
	}

	public void setFast(boolean isFast) {
		this.isFast = isFast;
	}
}
