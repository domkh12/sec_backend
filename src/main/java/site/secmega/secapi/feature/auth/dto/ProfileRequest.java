package site.secmega.secapi.feature.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record ProfileRequest(
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate dateOfBirth,
        String avatar
) {
}
