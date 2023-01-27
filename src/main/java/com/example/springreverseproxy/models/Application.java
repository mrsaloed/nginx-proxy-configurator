package com.example.springreverseproxy.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(ApplicationPK.class)
public class Application {
    @Id
    @Pattern(regexp = "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$", message = "Must be a valid IP")
    private String host;
    @Id
    @Min(value = 1, message = "Must be in range 1-65535")
    @Max(value = 65535, message = "Must be in range 1-65535")
    private int port;
    @NotEmpty
    @Size(min = 5, max = 250, message = "Length must be between 5 and 250")
    private String applicationName;
}
