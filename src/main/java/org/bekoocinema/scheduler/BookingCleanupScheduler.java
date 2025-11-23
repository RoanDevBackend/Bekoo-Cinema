package org.bekoocinema.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bekoocinema.entity.Booking;
import org.bekoocinema.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingCleanupScheduler {

    private final BookingRepository bookingRepo;

    @Value("${payment.timeout}")
    private long PAYMENT_TIMEOUT_MINUTES;

    // chạy mỗi 1 phút (60000ms)
    @Scheduled(fixedRate = 60_000L, initialDelay = 10_000L)
    @Transactional
    public void cleanupStaleBookings() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(PAYMENT_TIMEOUT_MINUTES);
        List<Booking> stale = bookingRepo.getBookingNotPayment(cutoff);
        log.info("Cleaning up stale bookings: {}", stale.size());
        if (stale.isEmpty()) return;
        bookingRepo.deleteAll(stale);
    }
}
