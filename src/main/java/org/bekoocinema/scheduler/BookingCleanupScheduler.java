package org.bekoocinema.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bekoocinema.entity.Booking;
import org.bekoocinema.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingCleanupScheduler {

    private final BookingRepository bookingRepo;
    private final ApplicationEventPublisher publisher;

    @Value("${payment.timeout}")
    private long PAYMENT_TIMEOUT_MINUTES;

    // chạy mỗi 1 phút (60000ms)
    @Scheduled(fixedRate = 10_000L, initialDelay = 10_000L)
    @Transactional
    public void cleanupStaleBookings() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(PAYMENT_TIMEOUT_MINUTES);
        List<Booking> stale = bookingRepo.getBookingNotPayment(cutoff);
        log.info("Cleaning up stale bookings: {}", stale.size());
        if (stale.isEmpty()) return;
        for (Booking b : stale) {
            if(b.getSeatIds() == null || b.getSeatIds().isEmpty()) continue;
            List<String> seatIdsSelected = Arrays.stream(b.getSeatIds().split(",")).map(String::trim).toList();
            publisher.publishEvent(
                    new PayloadApplicationEvent<>(
                            this,
                            Map.of(
                                    "showtimeId", b.getShowtimeId(),
                                    "seatIdsSelected", seatIdsSelected
                            )
                    )
            );
        }
        bookingRepo.deleteAll(stale);
    }
}
