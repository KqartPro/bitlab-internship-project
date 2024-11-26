package kz.pryahin.bitlabInternship.security.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class UserRolesDto {
	@NotBlank
	private String username;

	@NotEmpty
	private List<String> roles;
}
