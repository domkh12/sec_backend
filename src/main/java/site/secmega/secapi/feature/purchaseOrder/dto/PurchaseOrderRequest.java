package site.secmega.secapi.feature.purchaseOrder.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record PurchaseOrderRequest(
        @NotBlank(message = "PO is required!")
        String po,
        @NotNull(message = "Style is required!")
        Long styleId,
        @NotNull(message = "Buyer is required!")
        Long buyerId,
        @NotNull(message = "Qty is required!")
        @Positive(message = "Qty must be greater than 0!")
        Integer qty,
        @NotNull(message = "Shipment date is required!")
        @FutureOrPresent(message = "Shipment date cannot be in the past!")
        LocalDate shipmentDate
) {
}
