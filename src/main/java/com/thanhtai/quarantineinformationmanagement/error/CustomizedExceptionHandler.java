package com.thanhtai.quarantineinformationmanagement.error;

import com.thanhtai.quarantineinformationmanagement.api.model.Fault;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {
    @Autowired
    private ModelMapper modelMapper;

    @ExceptionHandler(AlreadyExistedException.class)
    public final ResponseEntity<Fault> handleAlreadyExistedException(AlreadyExistedException ex, WebRequest request) {
        ErrorCode errorCode = new ErrorCode (new Date().toString(), 208, 1001
                ,ex.getMessage() + " already existed");
        Fault fault = modelMapper.map(errorCode, Fault.class);
        return new ResponseEntity<>(fault, HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(InvalidProvinceName.class)
    public final ResponseEntity<Fault> handleInvalidProvinceNameException(InvalidProvinceName ex, WebRequest request) {
        ErrorCode errorCode = new ErrorCode (new Date().toString(), 400, 1002
                ,ex.getMessage() + " Invalid originFrom/destination");
        Fault fault = modelMapper.map(errorCode, Fault.class);
        return new ResponseEntity<>(fault, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Fault> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorCode errorCode = new ErrorCode (new Date().toString(), 404, 1003
                ,ex.getMessage() + " not found");
        Fault fault = modelMapper.map(errorCode, Fault.class);
        return new ResponseEntity<>(fault, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateException.class)
    public final ResponseEntity<Fault> handleDuplicateException(DuplicateException ex, WebRequest request) {
        ErrorCode errorCode = new ErrorCode (new Date().toString(), 400, 1004
                ,ex.getMessage() + " Origin place must differ to destination");
        Fault fault = modelMapper.map(errorCode, Fault.class);
        return new ResponseEntity<>(fault, HttpStatus.BAD_REQUEST);
    }



}
