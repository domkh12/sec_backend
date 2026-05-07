package site.secmega.secapi.feature.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import site.secmega.secapi.base.POStatus;
import site.secmega.secapi.feature.purchaseOrder.PurchaseOrderRepository;

import java.time.LocalDate;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class StatusScheduler {

    private final PurchaseOrderRepository purchaseOrderRepository;

    // Runs at 1:00 AM every day
    @Scheduled(cron = "0 0 1 * * *")
    public void checkDelayedOrders() {
        int updatedCount = purchaseOrderRepository.updateDelayedOrders(LocalDate.now(), POStatus.DELAYED);
        System.out.println("Updated " + updatedCount + " orders to DELAYED status.");
    }

}
