package site.secmega.secapi.feature.rack;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.Rack;
import site.secmega.secapi.domain.Warehouse;
import site.secmega.secapi.feature.qr.QRCodeService;
import site.secmega.secapi.feature.rack.dto.RackFilterRequest;
import site.secmega.secapi.feature.rack.dto.RackRequest;
import site.secmega.secapi.feature.rack.dto.RackResponse;
import site.secmega.secapi.feature.warehouse.WarehouseRepository;
import site.secmega.secapi.mapper.RackMapper;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RackServiceImpl implements RackService{

    private final RackRepository rackRepository;
    private final RackMapper rackMapper;
    private final WarehouseRepository warehouseRepository;
    private final QRCodeService qrCodeService;

    @Override
    public void deleteRack(String uuid) {
        Rack rack = rackRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rack not found!")
        );
        rack.setDeletedAt(LocalDateTime.now());
        rackRepository.save(rack);
    }

    @Override
    public RackResponse updateRack(String uuid, RackRequest rackRequest) {

        if (rackRepository.existsByCodeAndDeletedAtNullAndUuidNot(rackRequest.code(), uuid)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Code already exist!");
        }

        Rack rack = rackRepository.findByUuidAndDeletedAtNull(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rack not found")
        );

        rackMapper.updateFromRackRequest(rackRequest, rack);
        Rack savedRack = rackRepository.save(rack);

        return rackMapper.toRackResponse(savedRack);
    }

    @Override
    public RackResponse createRack(RackRequest rackRequest) {

        if (rackRepository.existsByCodeAndDeletedAtNull(rackRequest.code())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Code already exist!");
        }

        Warehouse warehouse = warehouseRepository.findByUuid(rackRequest.warehouseUuid()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Warehouse not found")
        );

        Rack rack = new Rack();
        rack.setUuid(UUID.randomUUID().toString());
        rack.setCode(rackRequest.code());
        rack.setQrCode(rackRequest.code());
        rack.setIsActive(rackRequest.isActive());
        rack.setWarehouse(warehouse);

        Rack savedRack = rackRepository.save(rack);

        return rackMapper.toRackResponse(savedRack);
    }

    @Override
    public Page<RackResponse> findAll(RackFilterRequest rackFilterRequest) {

        if (rackFilterRequest.pageNo() <= 0 || rackFilterRequest.pageSize() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no or Page size invalid!");
        }

        Specification<Rack> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (rackFilterRequest.search() != null) {
            String searchTerm = "%" + rackFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("code")), searchTerm));
        }

        PageRequest pageRequest = PageRequest.of(
                rackFilterRequest.pageNo() - 1,
                rackFilterRequest.pageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return rackRepository.findAll(spec, pageRequest).map(this::toResponseWithQr);
    }

    private RackResponse toResponseWithQr(Rack rack) {
        RackResponse base = rackMapper.toRackResponse(rack);
        String imageBase64 = generateQrBase64(rack);
        return new RackResponse(
                base.id(),
                base.uuid(),
                base.code(),
                base.isActive(),
                base.warehouse(),
                imageBase64
        );
    }

    private String generateQrBase64(Rack rack) {
        try {
            Map<String, Object> payload = Map.of(
                    "type", "RACK",
                    "code", rack.getCode()
            );
            ResponseEntity<byte[]> qr = qrCodeService.generateQRCode(payload);
            byte[] png = qr.getBody();
            if (png == null || png.length == 0) {
                return null;
            }
            return Base64.getEncoder().encodeToString(png);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate QR code");
        }
    }
}
