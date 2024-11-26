package kz.pryahin.bitlabInternship.security.dtos;

import lombok.Data;

@Data
public class UpdateUserDto {

	private String email;
	private String firstName;
	private String lastName;

}

