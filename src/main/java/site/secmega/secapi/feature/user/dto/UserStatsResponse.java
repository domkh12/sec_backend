package site.secmega.secapi.feature.user.dto;

import lombok.Builder;

@Builder
public record UserStatsResponse(
        long totalUsers,
        long activeUsers,
        long inactiveUsers,
        long blockedUsers
) {
}
