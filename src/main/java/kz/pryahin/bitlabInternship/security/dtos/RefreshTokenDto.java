package kz.pryahin.bitlabInternship.security.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenDto {
	@NotBlank
	private String refreshToken;
}
