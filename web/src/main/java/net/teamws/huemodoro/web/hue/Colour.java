package net.teamws.huemodoro.web.hue;

public enum Colour {
	RED(1),
	GREEN(23000);

	private int colour;

	Colour(int colour) {
		this.colour = colour;
	}

	int value() {
		return colour;
	}
}