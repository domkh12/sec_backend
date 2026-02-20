package site.secmega.secapi.feature.message.dto;


import lombok.Builder;

@Builder
public record MessageRequest(
        String message,
        Boolean isUpdate
) {
}
