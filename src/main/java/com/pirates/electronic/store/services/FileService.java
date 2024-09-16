package com.pirates.electronic.store.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    public String uploadImage(MultipartFile file, String path);

}
