package kz.pryahin.bitlabInternship.main.exceptions;

public class BlankNameException extends RuntimeException {
  public BlankNameException() {
    super("Name must not be blank");
  }
}
