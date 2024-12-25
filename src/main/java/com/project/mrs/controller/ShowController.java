package com.project.mrs.controller;

import com.project.mrs.dto.APIResponseDTO;
import com.project.mrs.dto.PagedAPIResponseDTO;
import com.project.mrs.dto.show.ShowRequestDTO;
import com.project.mrs.entity.Show;
import com.project.mrs.service.ShowService;
import com.project.mrs.validation.UserRoleValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final ShowService showService;
    private final UserRoleValidationService userRoleValidationService;

    @Autowired
    ShowController(ShowService showService, UserRoleValidationService userRoleValidationService)
    {
        this.showService = showService;
        this.userRoleValidationService = userRoleValidationService;
    }

    @GetMapping("/all")
    public ResponseEntity<PagedAPIResponseDTO> getAllShows(
            @RequestParam int page,
            @RequestParam int pageSize
    )
    {
        Page<Show> shows = showService.getAllShows(page,pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        PagedAPIResponseDTO
                                .builder()
                                .pageData(shows.getContent())
                                .totalElements(shows.getTotalElements())
                                .totalPages(shows.getTotalPages())
                                .currentLimit(shows.getNumberOfElements())
                                .build()
                );
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<PagedAPIResponseDTO> getAllShowsByMovie(
            @PathVariable Long movieId,
            @RequestParam int page,
            @RequestParam int pageSize
    )
    {
        Page<Show> shows = showService.getShowsByMovieId(movieId,page,pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        PagedAPIResponseDTO
                                .builder()
                                .pageData(shows.getContent())
                                .totalElements(shows.getTotalElements())
                                .totalPages(shows.getTotalPages())
                                .currentLimit(shows.getNumberOfElements())
                                .build()
                );
    }

    @GetMapping("/screen/{screenId}")
    public ResponseEntity<PagedAPIResponseDTO> getAllShowsByScreen(
            @PathVariable Long screenId,
            @RequestParam int page,
            @RequestParam int pageSize
    )
    {
        Page<Show> shows = showService.getShowsByScreenId(screenId,page,pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        PagedAPIResponseDTO
                                .builder()
                                .pageData(shows.getContent())
                                .totalElements(shows.getTotalElements())
                                .totalPages(shows.getTotalPages())
                                .currentLimit(shows.getNumberOfElements())
                                .build()
                );
    }

    @GetMapping("/theatre/{theatreId}")
    public ResponseEntity<PagedAPIResponseDTO> getAllShowsByTheatre(
            @PathVariable Long theatreId,
            @RequestParam int page,
            @RequestParam int pageSize
    )
    {
        Page<Show> shows = showService.getShowsByTheatreId(theatreId,page,pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        PagedAPIResponseDTO
                                .builder()
                                .pageData(shows.getContent())
                                .totalElements(shows.getTotalElements())
                                .totalPages(shows.getTotalPages())
                                .currentLimit(shows.getNumberOfElements())
                                .build()
                );
    }

    @GetMapping("/show/{showId}")
    public  ResponseEntity<APIResponseDTO> getShowById(@PathVariable Long showId)
    {
        Show show = showService.getShowById(showId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        APIResponseDTO.builder()
                                .data(show)
                                .build()
                );
    }

    @PreAuthorize("@userRoleValidationService.isUserHavePermissionToPerformWriteOperationForScreen(#showRequestDTO.screenId)")
    @PostMapping("/show/create")
    public ResponseEntity<APIResponseDTO> createNewShow(@RequestBody ShowRequestDTO showRequestDTO)
    {
        Show show = showService.createNewShow(showRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        APIResponseDTO.builder()
                                .message("New show created with the id: " + show.getShowId() + " and movie name: "+show.getMovie().getMovieName()+".")
                                .data(show)
                                .build()
                );
    }

    @PreAuthorize("@userRoleValidationService.isUserHavePermissionToPerformWriteOperationForShow(#showId)")
    @PutMapping("/show/{showId}")
    public ResponseEntity<APIResponseDTO> updateShowById(@PathVariable Long showId,@RequestBody ShowRequestDTO showRequestDTO)
    {
        Show updatedShow = showService.updateShowById(showId,showRequestDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponseDTO.builder()
                                .message("Updated the show with the id: " + updatedShow.getShowId() + " and movie name: "+updatedShow.getMovie().getMovieName()+".")
                                .data(updatedShow)
                                .build()
                );
    }

    @PreAuthorize("@userRoleValidationService.isUserHavePermissionToPerformWriteOperationForShow(#showId)")
    @DeleteMapping("/show/{showId}")
    public ResponseEntity<APIResponseDTO> deleteShowById(@PathVariable Long showId)
    {
        Show deletedShow = showService.getShowById(showId);
        showService.deleteShowById(showId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponseDTO
                                .builder()
                                .message("Deleted the show with the id: "+deletedShow.getShowId()+" and movie name: "+deletedShow.getMovie().getMovieName()+".")
                                .build()
                );
    }
}
