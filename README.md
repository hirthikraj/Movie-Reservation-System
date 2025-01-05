# Movie-Reservation-System

## Project Overview

The Movie Reservation System is a Spring Boot application designed to manage movie reservations, theatres, screens, and users. It provides a comprehensive set of features to handle the administration and booking processes.

Tools & Technologies used :
- Spring Boot
- Spring Security
- Spring Data JPA
- Spring Web
- h2 database (In-memory Database
- lombok
- hibernate
- JSON Web Token

Project Link : https://roadmap.sh/projects/movie-reservation-system

## Entities

### Movie
- **Attributes**: `movieId`, `movieName`, `movieGenre`, `movieDirector`, `movieReleaseDate`, `movieDescription`, `movieDuration`, `totalBookings`
- **Relationships**:
  - One-to-Many with `Show`

### Theatre
- **Attributes**: `theatreId`, `theatreName`, `theatreLocation`, `totalBookings`, `totalRevenue`, `totalScreens`
- **Relationships**:
    - One-to-Many with `Screen`
    - Many-to-Many with `User` through `TheatreVsAdmin`

### Screen
- **Attributes**: `screenId`, `screenName`
- **Relationships**:
    - Many-to-One with `Theatre`
    - One-to-Many with `Seat`
  
### Screen
- **Attributes**: `seatId`, `rowId`, `seatNumber`, `seatType`, `seatPrice`
- **Relationships**:
  - Many-to-One with `Screen`

### Show
- **Attributes**: `showId`, `startTime`, `endTime`
- **Relationships**:
  - Many-to-One with `Movie`
  - Many-to-One with `Theatre`
  - Many-to-One with `Screen`
  - One-to-Many with `ShowSeat`
  
### ShowSeat
- **Attributes**: `showSeatId`, `seatStatus`, `endTime`
- **Relationships**:
  - Many-to-One with `Seat`
  - Many-to-One with `Show`

### Reservation
- **Attributes**: `reservationId`, `reservationTime`, `updatedTime`, `totalAmount`, `reservationStatus`
- **Relationships**:
  - Many-to-One with `Show`
  - Many-to-One with `User`

### User
- **Attributes**: `userId`, `userName`, `password`, `firstName`, `lastName`, `userEmail`, `userStatus`, `userCreatedAt`, `userUpdatedAt`, `userRole`

### TheatreVsAdmin
- **Attributes**: `id`
- **Relationships**:
    - Many-to-One with `Theatre`
    - Many-to-One with `User`

## Features

- Authentication and authorization
- CRUD - users (Permission - All)
- CRUD - movies (Permission - Super Admin)
- CRUD - theatres (Permission - Super Admin)
- CRUD - shows (Permission - Super Admin,Theatre Admin)
- Create, get and cancel reservations (Permission - All)
- Implemented ReentrantLock to avoid over bookings. 


## API Endpoints

### Authentication APIs

- **Signup**
    - **URL**: `/auth/signup`
    - **Method**: `Post`
    - **Description**: Signup with user

- **Login**
    - **URL**: `/auth/login`
    - **Method**: `Post`
    - **Description**: Retrieve a theatre by its ID.


### Theatre APIs

- **Get All Theatres**
    - **URL**: `/api/theatres/all`
    - **Method**: `GET`
    - **Description**: Retrieve a paginated list of all theatres.

- **Get Theatre by ID**
    - **URL**: `/api/theatres/theatre/{theatreId}`
    - **Method**: `GET`
    - **Description**: Retrieve a theatre by its ID.

- **Create New Theatre**
    - **URL**: `/api/theatres/theatre/create`
    - **Method**: `POST`
    - **Description**: Create a new theatre.
    - **Role**: Super Admin

- **Update Theatre by ID**
    - **URL**: `/api/theatres/theatre/{theatreId}`
    - **Method**: `PUT`
    - **Description**: Update an existing theatre by its ID.
    - **Role**: Super Admin, Theatre Admin

- **Delete Theatre by ID**
    - **URL**: `/api/theatres/theatre/{theatreId}`
    - **Method**: `DELETE`
    - **Description**: Delete a theatre by its ID.
    - **Role**: Super Admin, Theatre Admin

- **Add Theatre Admin**
    - **URL**: `/api/theatres/theatre/admin`
    - **Method**: `POST`
    - **Description**: Add a theatre admin
    - **Role**: Super Admin
  
- **Remove Theatre Admin**
    - **URL**: `/api/theatres/theatre/admin`
    - **Method**: `DELETE`
    - **Description**: Remove a theatre admin
    - **Role**: Super Admin

### Movie APIs

- **Get All Movies**
    - **URL**: `/api/movies/all`
    - **Method**: `GET`
    - **Description**: Retrieve a list of all movies.

- **Get Movie by ID**
    - **URL**: `/api/movies/movie/{movieId}`
    - **Method**: `GET`
    - **Description**: Retrieve a movie by its ID.

- **Create New Movie**
    - **URL**: `/api/movies/movie/create`
    - **Method**: `POST`
    - **Description**: Create a new movie.
    - **Role**: Super Admin

- **Update Movie by ID**
    - **URL**: `/api/movies/movie/{movieId}`
    - **Method**: `PUT`
    - **Description**: Update an existing movie by its ID.
    - **Role**: Super Admin

- **Delete Movie by ID**
    - **URL**: `/api/movies/movie/{movieId}`
    - **Method**: `DELETE`
    - **Description**: Delete a movie by its ID.
    - **Role**: Super Admin
  
  ### Show APIs
  
- **Get All Shows**
    - **URL**: `/api/shows/all`
    - **Method**: `GET`
    - **Description**: Retrieve a list of all shows.

- **Get all shows by Movie ID**
    - **URL**: `/api/shows/movie/{movieId}`
    - **Method**: `GET`
    - **Description**: Retrieve all shows by movie ID.

- **Get all shows by Screen ID**
    - **URL**: `/api/shows/screen/{screenId}`
    - **Method**: `GET`
    - **Description**: Retrieve all shows by screen ID.
    
- **Get all shows by Theatre ID**
    - **URL**: `/api/shows/theatre/{theatreId}`
    - **Method**: `GET`
    - **Description**: Retrieve all shows by theatre ID.
    
- **Get Show by ID**
    - **URL**: `/api/shows/show/{showId}`
    - **Method**: `GET`
    - **Description**: Retrieve a show by its ID.

- **Create New Show**
    - **URL**: `/api/shows/show/{showId}`
    - **Method**: `POST`
    - **Description**: Create a new show.
    - **Role**: Super Admin,Theatre Admin

- **Update Show by ID**
    - **URL**: `/api/shows/show/{showId}`
    - **Method**: `PUT`
    - **Description**: Update an existing show by its ID.
    - **Role**:  Super Admin,Theatre Admin

- **Delete Show by ID**
    - **URL**: `/api/shows/show/{showId}`
    - **Method**: `DELETE`
    - **Description**: Delete a show by its ID.
    - **Role**:  Super Admin,Theatre Admin

### Reservation APIs

- **Get All Reservations by User Id**
    - **URL**: `/api/reservations/user/{userId}/all`
    - **Method**: `GET`
    - **Description**: Retrieve a list of all reservations for specific user.
    
- **Create New Reservation**
    - **URL**: `/api/reservations/reserve`
    - **Method**: `POST`
    - **Description**: Create a new reservation.

- **Cancel Reservation by ID**
    - **URL**: `/api/reservations/cancel/{reservationId}`
    - **Method**: `PUT`
    - **Description**: Cancel a reservation by Id

### User APIs

- **Get All Users**
    - **URL**: `/api/users/all`
    - **Method**: `GET`
    - **Description**: Retrieve a list of all users.

- **Create a new user**
    - **URL**: `/api/users/user/create`
    - **Method**: `GET`
    - **Description**: Create a new user

- **Get User by ID**
    - **URL**: `/api/users/user/{userId}`
    - **Method**: `GET`
    - **Description**: Retrieve a user by its ID.

- **Update User by ID**
    - **URL**: `/api/users/user/{userId}`
    - **Method**: `PUT`
    - **Description**: Update an existing user by its ID.
    - **Role**: Super Admin

- **Delete User by ID**
    - **URL**: `/api/users/user/{userId}`
    - **Method**: `DELETE`
    - **Description**: Delete a existing user by its ID.
    - **Role**: Super Admin
