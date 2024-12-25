package com.project.mrs.validation;

import com.project.mrs.entity.Reservation;
import com.project.mrs.entity.Theatre;
import com.project.mrs.entity.TheatreVsAdmin;
import com.project.mrs.entity.User;
import com.project.mrs.enums.UserRole;
import com.project.mrs.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserRoleValidationService {

    private final UserService userService;
    private final TheatreService theatreService;
    private final ScreenService screenService;
    private final ShowService showService;
    private final ReservationService reservationService;

    public UserRoleValidationService(UserService userService, TheatreService theatreService, ScreenService screenService, ShowService showService, ReservationService reservationService) {
        this.userService = userService;
        this.theatreService = theatreService;
        this.screenService = screenService;
        this.showService = showService;
        this.reservationService = reservationService;
    }

    public boolean isSuperAdmin()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals(UserRole.ROLE_SUPER_ADMIN.name())) {
                return true;
            }
        }
        return false;
    }

    public boolean isTheatreAdmin()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals(UserRole.ROLE_THEATRE_ADMIN.name())) {
                return true;
            }
        }
        return false;
    }

    public boolean isUserHavePermissionToPerformWriteOperationForTheatre(Long theatreId) {
        if(isSuperAdmin() || isTheatreAdmin())
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            User user = userService.getUserByUserName(currentUsername);
            Theatre theatre = theatreService.getTheatreById(theatreId);

            for(TheatreVsAdmin theatreAdmin : theatre.getTheatreAdmins())
            {
                if(theatreAdmin.getUser().getUserId().equals(user.getUserId()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isUserHavePermissionToPerformWriteOperationForShow(Long showId) {
        if(isSuperAdmin() || isTheatreAdmin())
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            User user = userService.getUserByUserName(currentUsername);
            Theatre theatre = showService.getShowById(showId).getTheatre();

            for(TheatreVsAdmin theatreAdmin : theatre.getTheatreAdmins())
            {
                if(theatreAdmin.getUser().getUserId().equals(user.getUserId()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isUserHavePermissionToPerformWriteOperationForScreen(Long screenId) {
        if(isSuperAdmin() || isTheatreAdmin())
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            User user = userService.getUserByUserName(currentUsername);
            Theatre theatre = screenService.getScreenById(screenId).getTheatre();

            for(TheatreVsAdmin theatreAdmin : theatre.getTheatreAdmins())
            {
                if(theatreAdmin.getUser().getUserId().equals(user.getUserId()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isUserHavePermissionToCancelReservation(Long reservationId)
    {
        Reservation reservation = reservationService.getReservationById(reservationId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userService.getUserByUserName(currentUsername);
        return user.getUserId().equals(reservation.getUser().getUserId());
    }
}
