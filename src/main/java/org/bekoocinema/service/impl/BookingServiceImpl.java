package org.bekoocinema.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bekoocinema.config.VNPayConfig;
import org.bekoocinema.entity.*;
import org.bekoocinema.exception.AppException;
import org.bekoocinema.exception.ErrorDetail;
import org.bekoocinema.mapper.BookingMapper;
import org.bekoocinema.repository.*;
import org.bekoocinema.request.booking.BookingRequest;
import org.bekoocinema.response.booking.BookingResponse;
import org.bekoocinema.service.BookingService;
import org.bekoocinema.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    final ShowtimeRepository showtimeRepository;
    final SeatRepository seatRepository;
    final BookingRepository bookingRepository;
    final BookingMapper bookingMapper;
    final RedisRepository redisRepository;
    final ObjectMapper objectMapper;
    final UserRepository userRepository;
    final MailService mailService;
    final SpringTemplateEngine templateEngine;
    @Value("${vnpay.return.url}")
    String vnp_ReturnUrl;
    @Value("${qr.url}")
    String qr_url;
    final String PAYMENT_STATUS_PENDING = "Chờ thanh toán";

    @Override
    @Transactional
    @SneakyThrows
    public String booking(BookingRequest bookingRequest, User user, HttpServletRequest request) {
        Showtime showtime = showtimeRepository.findById(bookingRequest.getShowtimeId())
                .orElseThrow(
                        () -> new RuntimeException("Xuất chiếu không tồn tại")
                );
        List<String> tempSeatIdBooked = bookingRepository.getSeatBooked(showtime.getId());
        List<String> seatIdsSelected = tempSeatIdBooked.stream()
                .flatMap(s -> Arrays.stream(s.split(",")))
                .map(String::trim)
                .filter(id -> !id.isEmpty())
                .toList();
        long totalPrice = 0;
        StringBuilder seatNames = new StringBuilder();
        StringBuilder seatIds = new StringBuilder();
        List<Seat> seatReqBooking = seatRepository.getSeatInId(bookingRequest.getSeatIds());
        for(Seat seat : seatReqBooking) {
            if(seatIdsSelected.contains(seat.getId())) {
                throw new RuntimeException("Ghế đã được đặt");
            }
            totalPrice =  totalPrice + seat.getPrice();
            if(seatNames.toString().equals("")) {
                seatNames = new StringBuilder(seat.getSeatName());
                seatIds = new StringBuilder(seat.getId());
            }else {
                seatNames.append(", ").append(seat.getSeatName());
                seatIds.append(", ").append(seat.getId());
            }
        }
        Booking booking = new Booking();
        booking.setShowtimeId(bookingRequest.getShowtimeId());
        booking.setBookingDate(LocalDateTime.now());
        booking.setMovieId(showtime.getMovie().getId());
        booking.setMovieName(showtime.getMovie().getName());
        Set<Genre> genres = showtime.getMovie().getGenres();
        StringBuilder genreName = new StringBuilder();
        for (Genre genre : genres) {
            if (genreName.equals("")) {
                genreName.append(genre.getName());
            } else {
                genreName.append(",").append(genre.getName());
            }
        }
        booking.setGenreName(genreName.toString());
        booking.setSeatNames(seatNames.toString());
        booking.setSeatIds(seatIds.toString());
        booking.setRoomName(showtime.getRoom().getName());

        booking.setCinemaName(showtime.getRoom().getCinema().getName());
        booking.setCinemaAddress(showtime.getRoom().getCinema().getAddress());
        booking.setUserId(user.getId());
        booking.setFullName(user.getFullName());
        booking.setEmail(user.getEmail());
        booking.setPhone(user.getPhone());

        booking.setTotalPrice(totalPrice);
        booking.setStartTime(showtime.getStartTime());
        booking.setEndTime(showtime.getEndTime());
        booking.setPosterUrl(showtime.getMovie().getPosterUrl());

        booking.setPaymentStatus(PAYMENT_STATUS_PENDING);
        bookingRepository.save(booking);

        String urlPayment = this.getUrlPayment(booking, request);
        return urlPayment;
    }

    @Override
    @SneakyThrows
    public List<BookingResponse> getBookings(User user, String bookingId){
        User fullUser = userRepository.findByEmail(user.getUsername());
        if(fullUser == null) {
            throw new RuntimeException("User không tồn tại");
        }
        List<Booking> bookings = bookingRepository.findAllByUserIdAndId(fullUser.getId(), bookingId);
        return bookings.stream().map(bookingMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    @Transactional
    public String executePaymentResult(Map<String, String> params, HttpServletRequest request){
        String value = "";
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        Map<String, String> hashData = new HashMap<>();
        for(String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                fieldName = URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString());
                fieldValue = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString());
                hashData.put(fieldName, fieldValue);
            }
        }
        hashData.remove("vnp_SecureHashType");
        String receivedHash = hashData.remove("vnp_SecureHash");
        String signValue = VNPayConfig.hashAllFields(hashData);
        if (signValue.equals(receivedHash)) {
            String bookingJson = redisRepository.get(params.get("vnp_TxnRef")).toString();
            if(bookingJson == null) {
                throw new AppException(ErrorDetail.ERR_BOOKING_NOT_EXISTED);
            }
            Booking booking = objectMapper.readValue(bookingJson, Booking.class);
            LocalDateTime createdDate = booking.getBookingDate();
            LocalDateTime timeout = createdDate.plusMinutes(10); // Huỷ thanh toán sau khi quá 15p
            if(LocalDateTime.now().isAfter(timeout)) {
                throw new AppException(ErrorDetail.ERR_ORDER_TIME_VALID);
            }
            String vnp_ResponseCode = params.get("vnp_ResponseCode");
            if(vnp_ResponseCode.equals("00")){
                booking.setPaymentDate(LocalDateTime.now());
                booking.setPaymentStatus("Đã thanh toán thành công");
                bookingRepository.save(booking);
                value = "Thanh toán thành công!";
                sendBookingSuccessEmail(booking);
            }else {
                bookingRepository.delete(booking);
                if (vnp_ResponseCode.equals("11"))
                    value = "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.";
                if (vnp_ResponseCode.equals("12"))
                    value = "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.";
                if (vnp_ResponseCode.equals("13"))
                    value = "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch.";
                if (vnp_ResponseCode.equals("24"))
                    value = "Giao dịch không thành công do: Khách hàng hủy giao dịch";
                if (vnp_ResponseCode.equals("51"))
                    value = "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.";
                if (vnp_ResponseCode.equals("65"))
                    value = "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.";
                if (vnp_ResponseCode.equals("75"))
                    value = "Ngân hàng thanh toán đang bảo trì.";
                if (vnp_ResponseCode.equals("79"))
                    value = "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch";
            }
        }else{
            value = "Giao dịch không hợp lệ, vui lòng kết nối để biết thêm chi tiết";
        }
        return value;
    }

    @SneakyThrows
    public void sendBookingSuccessEmail(Booking booking){
        byte[] qrBytes;
        try {
            qrBytes = this.generateQrCode(qr_url);
        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo mã QR");
        }
        String qrBase64 = Base64.getEncoder().encodeToString(qrBytes);

        // 2) Format date
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter fullFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        // 3) Build Thymeleaf context
        Context context = new Context();
        context.setVariable("fullName", booking.getFullName());
        context.setVariable("movieName", booking.getMovieName());
        context.setVariable("genreName", booking.getGenreName());
        context.setVariable("posterUrl", booking.getPosterUrl());

        context.setVariable("cinemaName", booking.getCinemaName());
        context.setVariable("cinemaAddress", booking.getCinemaAddress());

        context.setVariable("roomName", booking.getRoomName());
        context.setVariable("showDate", booking.getStartTime().format(dateFmt));
        context.setVariable("showTime", booking.getStartTime().format(timeFmt));

        context.setVariable("seatNames", booking.getSeatNames());
        context.setVariable("ticketId", booking.getId());

        context.setVariable("bookingDate", booking.getBookingDate().format(fullFmt));
        context.setVariable("paymentDate", booking.getPaymentDate().format(fullFmt));

        context.setVariable("totalPrice",
                NumberFormat.getCurrencyInstance(new Locale("vi", "VN"))
                        .format(booking.getTotalPrice()));

        context.setVariable("qrBase64", qrBase64);

        // 4) Render HTML từ template
        String html = templateEngine.process("booking-success", context);

        // 5) Send email
        mailService.sendMail(
                "🎟️ Vé xem phim của bạn – " + booking.getMovieName(),
                booking.getEmail(),
                html,
                true
        );
    }

    public byte[] generateQrCode(String text) throws Exception {
        int width = 300;
        int height = 300;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }

    @SneakyThrows
    private String getUrlPayment(Booking booking, HttpServletRequest req) {
        String orderType = "other";
        long totalAmount = booking.getTotalPrice() * 100;

        String vnp_TxnRef = booking.getId() + LocalDateTime.now();// Khi cần thanh toán lại thì cần tạo mã thanh toán mới
        this.redisRepository.set(vnp_TxnRef, objectMapper.writeValueAsString(booking));
        this.redisRepository.setTimeToLive(vnp_TxnRef, 10 * 1000L * 60);
        String vnp_IpAddr = VNPayConfig.getIpAddress(req);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(totalAmount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toán đơn hàng " + booking.getId());
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_OrderType", orderType);


        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 10);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return VNPayConfig.vnp_PayUrl + "?" + queryUrl;
    }
}
