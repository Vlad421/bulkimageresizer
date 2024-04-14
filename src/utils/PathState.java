package utils;

public enum PathState {
	SINGLE_DIR, MULTI_DIR, OUT_SET, OUT_NOT_SET;

	public static PathState IN_PATH = SINGLE_DIR;
	public static PathState OUT_PATH = OUT_NOT_SET;

}
