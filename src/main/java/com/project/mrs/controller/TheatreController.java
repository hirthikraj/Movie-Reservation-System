package com.project.mrs.controller;

import com.project.mrs.dto.*;
import com.project.mrs.dto.theatre.TheatreRequestDTO;
import com.project.mrs.dto.user.TheatreAdminRequestDTO;
import com.project.mrs.entity.Theatre;
import com.project.mrs.entity.User;
import com.project.mrs.service.TheatreService;
import com.project.mrs.service.UserService;
import com.project.mrs.validation.UserRoleValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/theatres")
public class TheatreController {

    private final TheatreService theatreService;
    private final UserService userService;
    private final UserRoleValidationService userRoleValidationService;

    @Autowired
    TheatreController(TheatreService theatreService, UserService userService, UserRoleValidationService userRoleValidationService)
    {
        this.theatreService = theatreService;
        this.userService = userService;
        this.userRoleValidationService = userRoleValidationService;
    }

    @GetMapping("/all")
    public ResponseEntity<PagedAPIResponseDTO> getAllTheatres(
            @RequestParam int page,
            @RequestParam int pageSize
    )
    {
        Page<Theatre> theatres = theatreService.getAllTheatres(page,pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        PagedAPIResponseDTO
                                .builder()
                                .pageData(theatres.getContent())
                                .totalElements(theatres.getTotalElements())
                                .totalPages(theatres.getTotalPages())
                                .currentLimit(theatres.getNumberOfElements())
                                .build()
                );
    }

    @GetMapping("/theatre/{theatreId}")
    public  ResponseEntity<APIResponseDTO> getTheatreById(@PathVariable Long theatreId)
    {
        Theatre theatre = theatreService.getTheatreById(theatreId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        APIResponseDTO.builder()
                                .data(theatre)
                                .build()
                );
    }

    @Secured({"ROLE_SUPER_ADMIN"})
    @PostMapping("/theatre/create")
    public ResponseEntity<APIResponseDTO> createNewTheatre(@RequestBody TheatreRequestDTO theatreRequestDTO)
    {
        Theatre newTheatre = theatreService.createNewTheatre(theatreRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        APIResponseDTO.builder()
                                .message("New theatre created with the id: " + newTheatre.getTheatreId() + " and name: "+newTheatre.getTheatreName()+".")
                                .data(newTheatre)
                                .build()
                );
    }

    @PreAuthorize("@userRoleValidationService.isUserHavePermissionToPerformWriteOperationForTheatre(#theatreId)")
    @PutMapping("theatre/{theatreId}")
    public ResponseEntity<APIResponseDTO> updateTheatreById(@PathVariable Long theatreId,@RequestBody TheatreRequestDTO theatreRequestDTO)
    {
        Theatre updatedTheatre = theatreService.updateTheatreById(theatreId,theatreRequestDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponseDTO.builder()
                                .message("Updated the theatre with the id: " + updatedTheatre.getTheatreId() + " and name: "+updatedTheatre.getTheatreName()+".")
                                .data(updatedTheatre)
                                .build()
                );
    }

    @PreAuthorize("@userRoleValidationService.isUserHavePermissionToPerformWriteOperationForTheatre(#theatreId)")
    @DeleteMapping("theatre/{theatreId}")
    public ResponseEntity<APIResponseDTO> deleteTheatreById(@PathVariable Long userId)
    {
        Theatre deletedTheatre = theatreService.getTheatreById(userId);
        theatreService.deleteTheatreById(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponseDTO
                                .builder()
                                .message("Deleted the theatre with the id: "+deletedTheatre.getTheatreId()+" and name: "+deletedTheatre.getTheatreName()+".")
                                .build()
                );
    }

    @PreAuthorize("@userRoleValidationService.isUserHavePermissionToPerformWriteOperationForTheatre(#theatreAdminRequestDTO.theatreId)")
    @PostMapping("theatre/admin")
    public ResponseEntity<APIResponseDTO> addTheatreAdmin(@RequestBody TheatreAdminRequestDTO theatreAdminRequestDTO)
    {
        Theatre theatre = theatreService.addTheatreAdmin(theatreAdminRequestDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponseDTO
                                .builder()
                                .message("Added the theatre admin to theatre name: "+theatre.getTheatreName()+".")
                                .build()
                );
    }

    @PreAuthorize("@userRoleValidationService.isUserHavePermissionToPerformWriteOperationForTheatre(#theatreAdminRequestDTO.theatreId)")
    @DeleteMapping("theatre/admin")
    public ResponseEntity<APIResponseDTO> removeTheatreAdmin(@RequestBody TheatreAdminRequestDTO theatreAdminRequestDTO)
    {
        User user = userService.getUserById(theatreAdminRequestDTO.getUserId());
        Theatre theatre = theatreService.removeTheatreAdmin(theatreAdminRequestDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponseDTO
                                .builder()
                                .message("Removed the theatre admin to theatre name: "+theatre.getTheatreName()+".")
                                .build()
                );
    }
}
