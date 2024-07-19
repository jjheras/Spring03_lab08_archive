package com.example.CarRegistry.controller;


import com.example.CarRegistry.service.impl.FileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;


@Slf4j
@RestController
public class FileController {
    @Autowired
    private FileServiceImpl fileService;

    //descargar la información de los coches en la BBDD con formato CSV
    @GetMapping("/downloadCarsCSV")
    @PreAuthorize("hasAnyRole('VENDOR','CLIENT')")
    public ResponseEntity<?> downloadCarsCSV() throws IOException {
        try {
            // Configurar los encabezados HTTP para la respuesta
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "cars.csv");
            // Obtener los bytes del archivo CSV
            byte[] csvByte = fileService.carsCSV().getBytes();
            // Devolver la respuesta en archivo CSV
            return new ResponseEntity<>(csvByte,headers,HttpStatus.OK);
        }catch (Exception e){
            // Devolver una respuesta de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al descargar el archivo CSV.");

        }


    }
    //la dirección para poder  subir una imagen a un usuario ya existente.
    @PostMapping("/userImage/{id}/add")
    @PreAuthorize("hasAnyRole('VENDOR','CLIENT')")
    public ResponseEntity<String> addUserImg(@PathVariable Long id, @RequestParam("imageFile") MultipartFile imageFile){

        try{
            // Obtener nombre del archivo y convertirlo a minúsculas
            String originalFileName = imageFile.getOriginalFilename().toLowerCase();
            // Validar el archivo que tenga la extensión .png
            if(!(originalFileName.endsWith(".png"))){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El archivo debe ser una imagen con extensión .png");
            }
            // Añadir la imagen del usuario
            fileService.addUserImage(id,imageFile);
            return ResponseEntity.ok("Imagen grabada");
        }catch(Exception e){
            // Devolver una respuesta de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la imagen.");
        }
    }
    //obtener una imagen de un usuario a partir del ID
    @GetMapping("/userImage/{id}")
    @PreAuthorize("hasAnyRole('VENDOR','CLIENT')")
    public ResponseEntity<byte[]> getUserImage(@PathVariable Long id){
        try {
            // Obtener los bytes de la imagen
            byte[] imageBytes = fileService.getUserImage(id);
            // Configurar los encabezados HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            // Devolver la imagen del usuario
            return new ResponseEntity<>(imageBytes,headers,HttpStatus.OK);
        }catch (RuntimeException e){
            // Devolver una respuesta de error de no encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage().getBytes());
        } catch (Exception e){
            // Devolver una respuesta de error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    //subir un numero indeterminado de coches a partir del un CSV.
    @PostMapping( value ="/uploadCarsCSV",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<String> uploadCarsCSV (@RequestParam(value = "file") MultipartFile file) {
        if(file.isEmpty()){
            log.error("Archivo vacio");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(file.getOriginalFilename().contains(".csv")){
            log.info("El nombre del archivo es {}.", file.getOriginalFilename());
            try {
                // Procesar el archivo CSV
                fileService.uploadCarsCSV(file);
                return ResponseEntity.ok("Archivo subido correctamente.");
            }catch (Exception e){
                log.error("Error al procesar el archivo CSV", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el archivo CSV");
            }
        }
        log.error("El archivo no es un CSV");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El archivo no es un CSV");

    }
}


