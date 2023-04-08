package com.iternova.ecommerce.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadFileService {

    private String folder ="images//"; //ubicación del proyecto donde se guardan las imagenes
    //en la BD no se guarda la imagen, solo el nombre de la imagen

    //método para subir imagen
    public String saveImage(MultipartFile file) throws IOException {
        if (!file.isEmpty()){ // entre a este if siempre que carguemos la foto, porque podemos elegir no subir nada
            //la convertimos a bites para poder trasadarla
          byte[] bytes = file.getBytes(); // pasamos la imagen a bytes, para pasar del cliente al servidor
            Path path = Paths.get(folder+file.getOriginalFilename()); //ponemos la ubicación donde queremos que se guarde la imagen
            Files.write(path, bytes); // parametros: la ruta y los bytes de la imagen, se graban
            return file.getOriginalFilename(); //retorna el string del nombre de la imagen
        }
        //si el usuario cuando carga elproducto no carga la imagen, se carga una imagen default
        return "default.jpg";
    }

    public void deleteImage(String nombre){
        String ruta = "images//";
        File file = new File(ruta+nombre);
        file.delete();
    }
}
