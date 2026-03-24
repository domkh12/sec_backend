package site.secmega.secapi.feature.buyer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.Buyer;
import site.secmega.secapi.feature.buyer.dto.BuyerFilterRequest;
import site.secmega.secapi.feature.buyer.dto.BuyerRequest;
import site.secmega.secapi.feature.buyer.dto.BuyerResponse;
import site.secmega.secapi.feature.buyer.dto.BuyerStatsResponse;
import site.secmega.secapi.mapper.BuyerMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BuyerServiceImpl implements BuyerService{

    private final BuyerRepository buyerRepository;
    private final BuyerMapper buyerMapper;

    @Override
    public BuyerResponse uploadBuyerFile(Long id, BuyerRequest buyerRequest) {
        Buyer buyer = buyerRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Buyer not found")
        );
        buyerMapper.updateFromBuyerRequest(buyerRequest, buyer);
        buyer.setUpdatedAt(LocalDateTime.now());
        buyer.setFiles(buyerRequest.files());
        Buyer updatedBuyer = buyerRepository.save(buyer);
        return buyerMapper.toBuyerResponse(updatedBuyer);
    }

    @Override
    public void deleteBuyer(Long id) {
        Buyer buyer = buyerRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Buyer not found")
        );
        buyer.setDeletedAt(LocalDateTime.now());
        buyerRepository.save(buyer);
    }

    @Override
    public BuyerStatsResponse getBuyerStats() {
        Integer totalBuyer = buyerRepository.countFirstBy();
        Integer activeOrder = 0;
        Integer totalPcs = 0;
        return BuyerStatsResponse.builder()
                .totalBuyer(totalBuyer)
                .activeOrder(activeOrder)
                .totalPcs(totalPcs)
                .build();
    }

    @Override
    public BuyerResponse updateBuyer(Long id, BuyerRequest buyerRequest) {
        Buyer buyer = buyerRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Buyer not found")
        );

        if (buyerRepository.existsByNameIgnoreCaseAndDeletedAtNullAndIdNot(buyerRequest.name(), id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Buyer name already exist");
        }

        buyerMapper.updateFromBuyerRequest(buyerRequest, buyer);
        buyer.setUpdatedAt(LocalDateTime.now());
        Buyer updatedBuyer = buyerRepository.save(buyer);

        return buyerMapper.toBuyerResponse(updatedBuyer);
    }

    @Override
    public Page<BuyerResponse> findAll(BuyerFilterRequest buyerFilterRequest) {
        if (buyerFilterRequest.pageNo() <= 0 || buyerFilterRequest.pageSize() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");
        }

        Specification<Buyer> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (buyerFilterRequest.search() != null){
            String searchTerm = "%" + buyerFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), searchTerm)
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(buyerFilterRequest.pageNo() - 1, buyerFilterRequest.pageSize(), sort);
        Page<Buyer> buyers = buyerRepository.findAll(spec, pageRequest);

        return buyers.map(buyerMapper::toBuyerResponse);
    }

    @Override
    public BuyerResponse createBuyer(BuyerRequest buyerRequest) {
        if (buyerRepository.existsByNameIgnoreCaseAndDeletedAtNull(buyerRequest.name())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Buyer name already exist");
        }
        Buyer buyer = buyerMapper.fromBuyerRequest(buyerRequest);
        Buyer savedBuyer = buyerRepository.save(buyer);
        return buyerMapper.toBuyerResponse(savedBuyer);
    }
}
