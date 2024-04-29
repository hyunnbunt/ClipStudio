package projects.seller.ClipStudio.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import projects.seller.ClipStudio.dto.WatchHistoryDto;
import projects.seller.ClipStudio.oauth2.User.entity.User;
import projects.seller.ClipStudio.oauth2.User.userRepository.UserRepository;
import projects.seller.ClipStudio.repository.VideoRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class WatchHistoryService {
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    public WatchHistoryDto updateWatchHistory(@PathVariable Long videoNumber,
                                                              @RequestBody WatchHistoryDto watchHistoryDto,
                                                              String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        log.info(String.valueOf(user.getNumber()));
        watchHistoryDto.setUserNumber(user.getNumber());
        return watchHistoryDto;
    }
}
