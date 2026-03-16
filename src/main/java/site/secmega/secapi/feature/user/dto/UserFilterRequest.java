package site.secmega.secapi.feature.user.dto;

import site.secmega.secapi.base.UserStatus;

public record UserFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search,
        Integer roleId,
        Long departmentId,
        UserStatus status
) {
}
