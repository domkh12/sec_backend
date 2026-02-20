package site.secmega.secapi.feature.tv.dto;

public record HourData(
    String date,
    Integer dTarg,
    Integer h8,
    Integer h9,
    Integer h10,
    Integer h11,
    Integer h13,
    Integer h14,
    Integer h15,
    Integer h16,
    Integer h17,
    Integer h18,
    Boolean isToday
) {
}
