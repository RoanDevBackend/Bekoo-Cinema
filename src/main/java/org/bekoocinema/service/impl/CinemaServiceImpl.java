package org.bekoocinema.service.impl;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bekoocinema.entity.Cinema;
import org.bekoocinema.entity.Image;
import org.bekoocinema.mapper.CinemaMapper;
import org.bekoocinema.repository.CinemaRepository;
import org.bekoocinema.repository.ImageRepository;
import org.bekoocinema.request.cinema.CreateCinemaRequest;
import org.bekoocinema.response.cinema.CinemaResponse;
import org.bekoocinema.service.CinemaService;
import org.springframework.stereotype.Service;
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
    public List<CinemaResponse> getAllCinemas() {
        List<Cinema> cinemas = cinemaRepository.findAll();
        List<CinemaResponse> responses = new ArrayList<>();
        for (Cinema cinema : cinemas) {
            CinemaResponse cinemaResponse = cinemaMapper.toCinemaResponse(cinema);
            List<String> urlsCinema = imageRepository.getUrlByTargetId("cinema", cinema.getId());
            cinemaResponse.setUrlImages(urlsCinema);
            responses.add(cinemaResponse);
        }
        return responses;
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
