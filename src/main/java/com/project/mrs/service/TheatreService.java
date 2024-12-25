package com.project.mrs.service;

import com.project.mrs.constants.ExceptionConstants;
import com.project.mrs.dto.screen.ScreenRequestDTO;
import com.project.mrs.dto.theatre.TheatreRequestDTO;
import com.project.mrs.dto.user.TheatreAdminRequestDTO;
import com.project.mrs.entity.Screen;
import com.project.mrs.entity.Theatre;
import com.project.mrs.entity.TheatreVsAdmin;
import com.project.mrs.entity.User;
import com.project.mrs.exception.TheatreNotFoundException;
import com.project.mrs.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class TheatreService {

    private final TheatreRepository theatreRepository;
    private final UserService userService;
    private final ScreenService screenService;

    @Autowired
    TheatreService(TheatreRepository theatreRepository, UserService userService, ScreenService screenService)
    {
        this.theatreRepository = theatreRepository;
        this.userService = userService;
        this.screenService = screenService;
    }

    public Page<Theatre> getAllTheatres(int page, int pageSize)
    {
        return theatreRepository.findAll(PageRequest.of(page,pageSize));
    }


    public Theatre getTheatreById(Long theatreId)
    {
        return theatreRepository
                .findById(theatreId)
                .orElseThrow(() -> new TheatreNotFoundException(ExceptionConstants.THEATRE_NOT_FOUND,HttpStatus.NOT_FOUND));
    }

    public Theatre createNewTheatre(TheatreRequestDTO theatreRequestDTO)
    {
        User theatreAdmin = userService.getUserById(theatreRequestDTO.getTheatreAdminId());

        Theatre theatre = Theatre
                .builder()
                .theatreName(theatreRequestDTO.getTheatreName())
                .theatreLocation(theatreRequestDTO.getTheatreName())
                .totalBookings(0)
                .totalRevenue(0D)
                .build();

        List<TheatreVsAdmin> theatreAdmins = new ArrayList<>();
        TheatreVsAdmin theatreVsAdmin = createTheatreVsAdmin(theatre,theatreAdmin);
        theatreAdmins.add(theatreVsAdmin);
        theatre.setTheatreAdmins(theatreAdmins);

        userService.promoteUser(theatreAdmin);

        List<Screen> screens = new ArrayList<>();
        for(ScreenRequestDTO screenRequestDTO : theatreRequestDTO.getScreens())
        {
            Screen screen = screenService.createNewScreen(theatre,screenRequestDTO);
            screens.add(screen);
        }
        theatre.setScreens(screens);
        theatre.setTotalScreens(screens.size());
        theatre.setTotalBookings(0);
        return theatreRepository.save(theatre);
    }

    public Theatre updateTheatreById(Long theatreId, TheatreRequestDTO theatreRequestDTO)
    {
        return  theatreRepository
                .findById(theatreId)
                .map(Theatre -> {
                    Theatre.setTheatreName(theatreRequestDTO.getTheatreName());
                    Theatre.setTheatreLocation(theatreRequestDTO.getTheatreLocation());
                    return theatreRepository.save(Theatre);
                })
                .orElseThrow(() -> new TheatreNotFoundException(ExceptionConstants.THEATRE_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public Theatre addTheatreAdmin(TheatreAdminRequestDTO theatreAdminRequestDTO)
    {
        User theatreAdmin = userService.getUserById(theatreAdminRequestDTO.getUserId());
        return  theatreRepository
                .findById(theatreAdminRequestDTO.getTheatreId())
                .map(Theatre -> {
                    List<TheatreVsAdmin> theatreAdmins = Theatre.getTheatreAdmins();
                    TheatreVsAdmin theatreVsAdmin = createTheatreVsAdmin(Theatre,theatreAdmin);
                    theatreAdmins.add(theatreVsAdmin);
                    Theatre.setTheatreAdmins(theatreAdmins);
                    userService.promoteUser(theatreAdmin);
                    return theatreRepository.save(Theatre);
                })
                .orElseThrow(() -> new TheatreNotFoundException(ExceptionConstants.THEATRE_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public Theatre removeTheatreAdmin(TheatreAdminRequestDTO theatreAdminRequestDTO)
    {
        User theatreAdmin = userService.getUserById(theatreAdminRequestDTO.getUserId());
        return  theatreRepository
                .findById(theatreAdminRequestDTO.getTheatreId())
                .map(Theatre -> {
                    List<TheatreVsAdmin> theatreAdmins = Theatre.getTheatreAdmins();
                    TheatreVsAdmin theatreVsAdmin = createTheatreVsAdmin(Theatre,theatreAdmin);
                    theatreAdmins.remove(theatreVsAdmin);
                    Theatre.setTheatreAdmins(theatreAdmins);
                    // Need to de promote the user if not admin of any other theatre
                    return theatreRepository.save(Theatre);
                })
                .orElseThrow(() -> new TheatreNotFoundException(ExceptionConstants.THEATRE_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public TheatreVsAdmin createTheatreVsAdmin(Theatre theatre,User theatreAdmin)
    {
        return TheatreVsAdmin
                .builder()
                .theatre(theatre)
                .user(theatreAdmin)
                .build();
    }

    public void deleteTheatreById(Long theatreId)
    {
        theatreRepository.deleteById(theatreId);
    }

    public void updateTheatre(Theatre theatre)
    {
        theatreRepository.save(theatre);
    }
}
