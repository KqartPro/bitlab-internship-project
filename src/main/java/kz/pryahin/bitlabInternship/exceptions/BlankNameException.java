package kz.pryahin.bitlabInternship.exceptions;

public class BlankNameException extends RuntimeException {
	public BlankNameException() {
		super("Name must not be blank");
	}
}
