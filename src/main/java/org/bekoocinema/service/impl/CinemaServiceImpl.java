package org.bekoocinema.service.impl;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bekoocinema.entity.Cinema;
import org.bekoocinema.entity.Image;
import org.bekoocinema.entity.Room;
import org.bekoocinema.exception.AppException;
import org.bekoocinema.exception.ErrorDetail;
import org.bekoocinema.mapper.CinemaMapper;
import org.bekoocinema.repository.CinemaRepository;
import org.bekoocinema.repository.ImageRepository;
import org.bekoocinema.repository.RoomRepository;
import org.bekoocinema.request.cinema.CreateCinemaRequest;
import org.bekoocinema.request.cinema.UpdateCinemaRequest;
import org.bekoocinema.response.PageResponse;
import org.bekoocinema.response.cinema.CinemaResponse;
import org.bekoocinema.service.CinemaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CinemaServiceImpl implements CinemaService {

    final Cloudinary cloudinary;
    final CinemaRepository cinemaRepository;
    final ImageRepository imageRepository;
    final CinemaMapper cinemaMapper;
    final RoomRepository roomRepository;

    @Override
    @SneakyThrows
    public void addCinema(CreateCinemaRequest createCinemaRequest) {
        Cinema cinema = cinemaMapper.toCinema(createCinemaRequest);
        cinemaRepository.save(cinema);
        List<Image> imageCinemas = new ArrayList<>();
        for(MultipartFile file: createCinemaRequest.getFiles()) {
            String urlCinemaImage = this.getFileUrl(file);
            Image image = new Image();
            image.setImageType("cinema");
            image.setUrl(urlCinemaImage);
            image.setTargetId(cinema.getId());
            imageCinemas.add(image);
        }
        imageRepository.saveAll(imageCinemas);
    }

    @Override
    public List<CinemaResponse> getCinema(String keyWord) {
        List<Cinema> cinemas = cinemaRepository.getAllByKey(keyWord);
        List<CinemaResponse> cinemaResponses = new ArrayList<>();
        for (Cinema cinema : cinemas) {
            CinemaResponse cinemaResponse = cinemaMapper.toCinemaResponse(cinema);
            List<String> urlsCinema = imageRepository.getUrlByTargetId("cinema", cinema.getId());
            cinemaResponse.setUrlImages(urlsCinema);
            cinemaResponses.add(cinemaResponse);
        }
        return cinemaResponses;
    }

    @Override
    public CinemaResponse getCinemaById(String id) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cinema not found"));
        
        CinemaResponse cinemaResponse = cinemaMapper.toCinemaResponse(cinema);
        List<String> urlsCinema = imageRepository.getUrlByTargetId("cinema", cinema.getId());
        cinemaResponse.setUrlImages(urlsCinema);
        
        return cinemaResponse;
    }

    @Override
    public PageResponse<?> getAllCinemas(int pageIndex, int pageSize) {
        this.validPage(pageIndex, pageSize);
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Cinema> cinemaPage = cinemaRepository.findAllCinemas(pageable);
        
        List<CinemaResponse> responses = new ArrayList<>();
        for (Cinema cinema : cinemaPage.getContent()) {
            CinemaResponse cinemaResponse = cinemaMapper.toCinemaResponse(cinema);
            List<String> urlsCinema = imageRepository.getUrlByTargetId("cinema", cinema.getId());
            cinemaResponse.setUrlImages(urlsCinema);
            responses.add(cinemaResponse);
        }
        
        return PageResponse.<CinemaResponse>builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .sortBy(new PageResponse.SortBy("name", "asc"))
                .content(responses)
                .totalElements(cinemaPage.getTotalElements())
                .totalPages(cinemaPage.getTotalPages())
                .build();
    }

    private void validPage(int pageIndex, int pageSize){
        if(pageIndex < 1 || pageSize < 1){
            throw new IllegalArgumentException("Số trang và số phần tử trong một trang cần lớn hơn 1");
        }
    }

    @Override
    @SneakyThrows
    @Transactional
    public void updateCinema(String id, UpdateCinemaRequest updateCinemaRequest) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorDetail.ERR_CINEMA_NOT_EXISTED));

        if(updateCinemaRequest.getName() != null && !updateCinemaRequest.getName().isBlank()) {
            cinema.setName(updateCinemaRequest.getName());
        }
        if(updateCinemaRequest.getProvince() != null && !updateCinemaRequest.getProvince().isBlank()) {
            cinema.setProvince(updateCinemaRequest.getProvince());
        }
        if(updateCinemaRequest.getDistrict() != null) {
            cinema.setDistrict(updateCinemaRequest.getDistrict());
        }
        if(updateCinemaRequest.getCommune() != null && !updateCinemaRequest.getCommune().isBlank()) {
            cinema.setCommune(updateCinemaRequest.getCommune());
        }
        if(updateCinemaRequest.getDetailAddress() != null) {
            cinema.setDetailAddress(updateCinemaRequest.getDetailAddress());
        }
        if(updateCinemaRequest.getPhone() != null && !updateCinemaRequest.getPhone().isBlank()) {
            cinema.setPhone(updateCinemaRequest.getPhone());
        }
        if(updateCinemaRequest.getDescription() != null) {
            cinema.setDescription(updateCinemaRequest.getDescription());
        }
        if(updateCinemaRequest.getStatus() != null) {
            cinema.setStatus(updateCinemaRequest.getStatus());
        }

        cinemaRepository.save(cinema);

        if(updateCinemaRequest.getFiles() != null && !updateCinemaRequest.getFiles().isEmpty()) {
            Boolean replaceImages = updateCinemaRequest.getReplaceImages();

            if(replaceImages != null && replaceImages) {
                List<Image> oldImages = imageRepository.findAll()
                        .stream()
                        .filter(img -> "cinema".equals(img.getImageType()) && id.equals(img.getTargetId()))
                        .toList();
                if(!oldImages.isEmpty()) {
                    imageRepository.deleteAll(oldImages);
                }
            }

            List<Image> imageCinemas = new ArrayList<>();
            for(MultipartFile file : updateCinemaRequest.getFiles()) {
                String urlCinemaImage = this.getFileUrl(file);
                Image image = new Image();
                image.setImageType("cinema");
                image.setUrl(urlCinemaImage);
                image.setTargetId(cinema.getId());
                imageCinemas.add(image);
            }
            imageRepository.saveAll(imageCinemas);
        }
    }

    @Override
    @SneakyThrows
    @Transactional
    public void deleteCinema(String id) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorDetail.ERR_CINEMA_NOT_EXISTED));

        List<Room> rooms = roomRepository.getRoomByCinema(id);
        if(!rooms.isEmpty()) {
            throw new RuntimeException("Không thể xóa rạp vì còn phòng chiếu " + rooms.size() + " đang hoạt động");
        }

        List<Image> images = imageRepository.findAll()
                .stream()
                .filter(img -> "cinema".equals(img.getImageType()) && id.equals(img.getTargetId()))
                .toList();
        if (!images.isEmpty()) {
            imageRepository.deleteAll(images);
        }

        cinemaRepository.delete(cinema);
    }


    private String getFileUrl(MultipartFile file) throws IOException {
        try {
            if(file == null || file.isEmpty()){
                return null;
            }
            var response = cloudinary.uploader().upload(file.getBytes(), Map.of());
            return response.get("url").toString();
        }catch (Exception e){
            log.error(e.getMessage());
            throw new IOException("File không hợp lệ");
        }
    }
}
