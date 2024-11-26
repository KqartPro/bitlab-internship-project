package kz.pryahin.bitlabInternship.security.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordDto {
	@NotBlank
	private String currentPassword;
	@NotBlank
	private String newPassword;
}
