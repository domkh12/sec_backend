package site.secmega.secapi.feature.receipt;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.receipt.dto.ReceiptFilterRequest;
import site.secmega.secapi.feature.receipt.dto.ReceiptRequest;
import site.secmega.secapi.feature.receipt.dto.ReceiptResponse;

public interface ReceiptService {
    Page<ReceiptResponse> findAll(ReceiptFilterRequest receiptFilterRequest);

    ReceiptResponse updateReceipt(String uuid, @Valid ReceiptRequest receiptRequest);

    void deleteReceipt(String uuid);

    ReceiptResponse createReceipt(ReceiptRequest receiptRequest);
}
