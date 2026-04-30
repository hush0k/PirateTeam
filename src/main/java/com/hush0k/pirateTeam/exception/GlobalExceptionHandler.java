package com.hush0k.pirateTeam.exception;

import com.hush0k.pirateTeam.exception.fleet.FleetNotFoundException;
import com.hush0k.pirateTeam.exception.fleet.InsufficientDistanceToBattle;
import com.hush0k.pirateTeam.exception.fleet.InsufficientPirateSpaceException;
import com.hush0k.pirateTeam.exception.fleet.InsufficientProvisionException;
import com.hush0k.pirateTeam.exception.island.IslandNotFoundException;
import com.hush0k.pirateTeam.exception.pirate.*;
import com.hush0k.pirateTeam.exception.ship.*;
import com.hush0k.pirateTeam.exception.team.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PirateNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePirateNotFoundException(
            PirateNotFoundException e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTeamNotFoundException(
            TeamNotFoundException e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(TeamNotFoundByFleetException.class)
    public ResponseEntity<ErrorResponse> handleTeamNotFoundByFleetException(
            TeamNotFoundByFleetException e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(IslandNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleIslandNotFoundException(
            IslandNotFoundException e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(ShipNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleShipNotFoundException(
            ShipNotFoundException e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(ShipUnavailableToSale.class)
    public ResponseEntity<ErrorResponse> handleShipUnavailableToSale(
            ShipUnavailableToSale e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler(ShipNotOwnedByThisCapitan.class)
    public ResponseEntity<ErrorResponse> handleShipNotOwnedByThisCapitan(
            ShipNotOwnedByThisCapitan e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, e.getMessage(), request);
    }

    @ExceptionHandler(FleetNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFleetNotFoundException(
            FleetNotFoundException e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(InsufficientPirateSpaceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientPirateSpaceException(
            InsufficientPirateSpaceException e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler(InsufficientProvisionException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientProvisionException(
            InsufficientProvisionException e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler(InsufficientDistanceToBattle.class)
    public ResponseEntity<ErrorResponse> handleInsufficientDistanceToBattleException(
            InsufficientDistanceToBattle e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler(PirateInvalidRankException.class)
    public ResponseEntity<ErrorResponse> handlePirateInvalidRankException(
            PirateInvalidRankException e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, e.getMessage(), request);
    }

    @ExceptionHandler(PirateNotCaptainException.class)
    public ResponseEntity<ErrorResponse> handlePirateNotCaptainException(
            PirateNotCaptainException e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, e.getMessage(), request);
    }

    @ExceptionHandler(PirateIsPrisoner.class)
    public ResponseEntity<ErrorResponse> handlePirateIsPrisoner(
            PirateIsPrisoner e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, e.getMessage(), request);
    }

    @ExceptionHandler(InsufficientTreasuryException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientTreasuryException(
            InsufficientTreasuryException e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler(InsufficientPirateTreasuryException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientPirateTreasuryException(
            InsufficientPirateTreasuryException e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler(PirateNotInTeamException.class)
    public ResponseEntity<ErrorResponse> handlePirateNotInTeamException(
            PirateNotInTeamException e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler(PirateAlreadyInTeamException.class)
    public ResponseEntity<ErrorResponse> handlePirateAlreadyInTeamException(
            PirateAlreadyInTeamException e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid JSON request body", request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        if (message.isBlank()) {
            message = "Validation failed";
        }

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        status.value(),
                        status.getReasonPhrase(),
                        message,
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(CannotRemoveCaptainException.class)
    public ResponseEntity<ErrorResponse> handleCannotRemoveCaptainException(
            CannotRemoveCaptainException e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler(PirateNotEnoughTreasury.class)
    public ResponseEntity<ErrorResponse> handlePirateNotEnoughTreasury(
            PirateNotEnoughTreasury e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }
}
