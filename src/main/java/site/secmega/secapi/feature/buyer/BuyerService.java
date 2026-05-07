package site.secmega.secapi.feature.buyer;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.buyer.dto.*;

import java.util.List;

public interface BuyerService {
    List<BuyerLookupResponse> getBuyerLookup();

    BuyerResponse createBuyer(BuyerRequest buyerRequest);

    Page<BuyerResponse> findAll(BuyerFilterRequest buyerFilterRequest);

    BuyerResponse updateBuyer(Long id, @Valid BuyerRequest buyerRequest);

    void deleteBuyer(Long id);

    BuyerStatsResponse getBuyerStats();

    BuyerResponse uploadBuyerFile(Long id, BuyerRequest buyerRequest);

    BuyerFileResponse getBuyerFile(Long id);
}
