package site.secmega.secapi.feature.receipt;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import site.secmega.secapi.feature.receipt.dto.ReceiptFilterRequest;
import site.secmega.secapi.feature.receipt.dto.ReceiptRequest;
import site.secmega.secapi.feature.receipt.dto.ReceiptResponse;

@Service
public class ReceiptServiceImpl implements ReceiptService{

    @Override
    public Page<ReceiptResponse> findAll(ReceiptFilterRequest receiptFilterRequest) {

        return null;
    }

    @Override
    public ReceiptResponse updateReceipt(String uuid, ReceiptRequest receiptRequest) {
        return null;
    }

    @Override
    public void deleteReceipt(String uuid) {

    }

    @Override
    public ReceiptResponse createReceipt(ReceiptRequest receiptRequest) {
        return null;
    }

}
