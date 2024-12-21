package com.project.mrs.controller;

import com.project.mrs.dto.APIResponseDTO;
import com.project.mrs.dto.PagedAPIResponseDTO;
import com.project.mrs.dto.movie.MovieRequestDTO;
import com.project.mrs.entity.Movie;
import com.project.mrs.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    MovieService movieService;

    @Autowired
    MovieController(MovieService movieService)
    {
        this.movieService = movieService;
    }

    @GetMapping("/all")
    public ResponseEntity<PagedAPIResponseDTO> getAllMovies(
            @RequestParam int page,
            @RequestParam int pageSize
    )
    {
        Page<Movie> movies = movieService.getAllMovies(page,pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        PagedAPIResponseDTO
                                .builder()
                                .pageData(movies.getContent())
                                .totalElements(movies.getTotalElements())
                                .totalPages(movies.getTotalPages())
                                .currentLimit(movies.getNumberOfElements())
                                .build()
                );
    }

    @GetMapping("/movie/{movieId}")
    public  ResponseEntity<APIResponseDTO> getMovieById(@PathVariable Long movieId)
    {
        Movie movie = movieService.getMovieById(movieId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        APIResponseDTO.builder()
                                .data(movie)
                                .build()
                );
    }

    @PostMapping("/movie/create")
    public ResponseEntity<APIResponseDTO> createNewMovie(@RequestBody MovieRequestDTO MovieRequestDTO)
    {
        Movie newMovie = movieService.createNewMovie(MovieRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        APIResponseDTO.builder()
                                .message("New Movie created with the id: " + newMovie.getMovieId() + " and name: "+newMovie.getMovieName()+".")
                                .data(newMovie)
                                .build()
                );
    }

    @PutMapping("movie/{movieId}")
    public ResponseEntity<APIResponseDTO> updateMovieById(@PathVariable Long movieId,@RequestBody MovieRequestDTO movieRequestDTO)
    {
        Movie updatedMovie = movieService.updateMovieById(movieId,movieRequestDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponseDTO.builder()
                                .message("Updated the movie with the id: " + updatedMovie.getMovieId() + " and name: "+updatedMovie.getMovieName()+".")
                                .data(updatedMovie)
                                .build()
                );
    }

    @DeleteMapping("movie/{movieId}")
    public ResponseEntity<APIResponseDTO> deleteMovieById(@PathVariable Long movieId)
    {
        Movie deletedMovie = movieService.getMovieById(movieId);
        movieService.deleteMovieById(movieId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponseDTO
                                .builder()
                                .message("Deleted the movie with the id: "+deletedMovie.getMovieId()+" and name: "+deletedMovie.getMovieName()+".")
                                .build()
                );
    }
}
