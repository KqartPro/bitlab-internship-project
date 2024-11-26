package kz.pryahin.bitlabInternship.security.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginUserDto {
	@NotBlank
	private String username;

	@NotBlank
	private String password;
}
